# -*- coding: utf-8 -*-
"""Main Controller"""

from tg import expose, flash, require, url, lurl, request, redirect, tmpl_context
from tg.i18n import ugettext as _, lazy_ugettext as l_
from tg import predicates
from gameon import model
from gameon.controllers.secure import SecureController
from gameon.model import DBSession, metadata, Event
from tgext.admin.tgadminconfig import TGAdminConfig
from tgext.admin.controller import AdminController
from tgext.crud import EasyCrudRestController

from gameon.lib.base import BaseController
from gameon.controllers.error import ErrorController

# Dojo stuff that doesn't work with my setup for some reason.
#from sprox.dojo.formbase import DojoAddRecordForm, DojoEditableForm
#from sprox.dojo.tablebase import DojoTableBase as TableBase
#from sprox.dojo.fillerbase import DojoTableFiller as TableFiller

from sprox.formbase import AddRecordForm, EditableForm
from sprox.tablebase import TableBase
from sprox.fillerbase import TableFiller

__all__ = ['RootController']

# Sprox class definitions

# Omitted fields
# Note: actions are removed... this should only be for an admin or for someone who manages an event...
# which I haven't done yet.
eventOmittedFields = ['event_id', 'created', 'is_invite_only', 'is_private', 'is_guestlist_private', '__actions__']
eventHeadersDict = {
                    'date':'Date',
                    'title':'Title', 
                    'description':'Description',
                    'location':'Location',
                    'is_invite_only':'Invitation only?',
                    'is_private':'Private?',
                    'is_guestlist_private':'Guestlist private?'
                    }

class EventAddForm(AddRecordForm):
    __model__ = Event
    __require_fields__ = ['title', 'location', 'date', 'is_invite_only', 'is_private', 'is_guestlist_private']
    __omit_fields__ = eventOmittedFields
     
    # Left as examples:
    #__field_order__ = ['user_name', 'email_address', 'display_name', 'password', 'verify_password']
    #__base_validator__ = form_validator
    #email_address          = TextField
    #display_name           = TextField
    #verify_password        = PasswordField('verify_password')
    
event_add_form = EventAddForm(DBSession)

class EventEditForm(EditableForm):
    __model__ = Event
    __require_fields__ = ['title', 'location','date', 'is_invite_only', 'is_private', 'is_guestlist_private']
    __omit_fields__ = eventOmittedFields
event_edit_form = EventEditForm(DBSession)

class EventTable(TableBase):
    __model__ = Event
    __omit_fields__ = eventOmittedFields
    __headers__ = eventHeadersDict
event_table = EventTable(DBSession)

class EventTableFiller(TableFiller):
    __model__ = Event
event_table_filler = EventTableFiller(DBSession)

class EventController(EasyCrudRestController):
    model = Event
    table = event_table
    table_filler = event_table_filler
    new_form = event_add_form
    edit_form = event_edit_form

class RootController(BaseController):
    """
    The root controller for the GameOn application.

    All the other controllers and WSGI applications should be mounted on this
    controller. For example::

        panel = ControlPanelController()
        another_app = AnotherWSGIApplication()

    Keep in mind that WSGI applications shouldn't be mounted directly: They
    must be wrapped around with :class:`tg.controllers.WSGIAppController`.

    """
    secc = SecureController()
    admin = AdminController(model, DBSession, config_type=TGAdminConfig)

    events = EventController(DBSession)
    error = ErrorController()

    def _before(self, *args, **kw):
        tmpl_context.project_name = "gameon"

    @expose('gameon.templates.index')
    def index(self):
        """Handle the front-page."""
        #This is terrible and will have to be modified because its really inefficient.
        #Only public events should be shown too....
        tmpl_context.widget = event_table
        
        #Yeah... filter somehow
        currentEvents = event_table_filler.get_value()
        
        #events = DBSession.query(Event).order_by(Event.event_id.desc()).all()
        return dict(page='index', currentEvents=currentEvents)

    @expose('gameon.templates.about')
    def about(self):
        """Handle the 'about' page."""
        return dict(page='about')

    @expose('gameon.templates.environ')
    def environ(self):
        """This method showcases TG's access to the wsgi environment."""
        return dict(page='environ', environment=request.environ)

    @expose('gameon.templates.data')
    @expose('json')
    def data(self, **kw):
        """This method showcases how you can use the same controller for a data page and a display page"""
        return dict(page='data', params=kw)

    @expose('gameon.templates.index')
    @require(predicates.has_permission('manage', msg=l_('Only for managers')))
    def manage_permission_only(self, **kw):
        """Illustrate how a page for managers only works."""
        return dict(page='managers stuff')

    @expose('gameon.templates.index')
    @require(predicates.is_user('editor', msg=l_('Only for the editor')))
    def editor_user_only(self, **kw):
        """Illustrate how a page exclusive for the editor works."""
        return dict(page='editor stuff')

    @expose('example.templates.testjquerymobile')
    def index2(self):
        """Handle the 'test jquery mobile' page."""
        drawerOptions = ['Organize', 'Friends', 'Communities', 'Near Me', 'Favorites', 'Competition']
        events = ['Friday Night Fights', 'Board Game Nites', 'Boardgameageddon']
        return dict(drawerOptions=drawerOptions, events=events)
    @expose('gameon.templates.login')
    def login(self, came_from=lurl('/')):
        """Start the user login."""
        login_counter = request.environ.get('repoze.who.logins', 0)
        if login_counter > 0:
            flash(_('Wrong credentials'), 'warning')
        return dict(page='login', login_counter=str(login_counter),
                    came_from=came_from)

    @expose()
    def post_login(self, came_from=lurl('/')):
        """
        Redirect the user to the initially requested page on successful
        authentication or redirect her back to the login page if login failed.

        """
        if not request.identity:
            login_counter = request.environ.get('repoze.who.logins', 0) + 1
            redirect('/login',
                params=dict(came_from=came_from, __logins=login_counter))
        userid = request.identity['repoze.who.userid']
        flash(_('Welcome back, %s!') % userid)
        redirect(came_from)

    @expose()
    def post_logout(self, came_from=lurl('/')):
        """
        Redirect the user to the initially requested page on logout and say
        goodbye as well.

        """
        flash(_('We hope to see you soon!'))
        redirect(came_from)

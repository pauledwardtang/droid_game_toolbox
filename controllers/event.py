import logging

from pylons import request, response, session, tmpl_context as c, url
from pylons.controllers.util import abort, redirect
from gameon.model import DBSession, metadata, Event
#from gameon.lib.base import BaseController, render

from tgext.crud import EasyCrudRestController

# Dojo stuff that doesn't work with my setup for some reason.
#from sprox.dojo.formbase import DojoAddRecordForm, DojoEditableForm
#from sprox.dojo.tablebase import DojoTableBase as TableBase
#from sprox.dojo.fillerbase import DojoTableFiller as TableFiller

from sprox.formbase import AddRecordForm, EditableForm
from sprox.tablebase import TableBase
from sprox.fillerbase import TableFiller

log = logging.getLogger(__name__)

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

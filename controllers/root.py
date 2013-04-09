# -*- coding: utf-8 -*-
"""Main Controller"""

from tg import expose, flash, require, url, lurl, request, redirect, tmpl_context
from tg.i18n import ugettext as _, lazy_ugettext as l_
from tgext.crud import EasyCrudRestController
from example.model import DBSession, metadata, Movie

from example.lib.base import BaseController
from example.controllers.error import ErrorController

from sprox.formbase import AddRecordForm, EditableForm
from sprox.tablebase import TableBase
from sprox.fillerbase import TableFiller

__all__ = ['RootController']

# Sprox class definitions
class MovieAddForm(AddRecordForm):
    __model__ = Movie
    __omit_fields__ = ['genre_id', 'movie_id']
movie_add_form = MovieAddForm(DBSession)

class MovieEditForm(EditableForm):
    __model__ = Movie
    __omit_fields__ = ['genre_id', 'movie_id']
movie_edit_form = MovieEditForm(DBSession)

class MovieTable(TableBase):
    __model__ = Movie
    __omit_fields__ = "genre_id"
movie_table = MovieTable(DBSession)

class MovieTableFiller(TableFiller):
    __model__ = Movie
movie_table_filler = MovieTableFiller(DBSession)

class MovieController(EasyCrudRestController):
    model = Movie
    table = movie_table
    table_filler = movie_table_filler
    new_form = movie_add_form
    edit_form = movie_edit_form

class RootController(BaseController):
    """
    The root controller for the example application.

    All the other controllers and WSGI applications should be mounted on this
    controller. For example::

        panel = ControlPanelController()
        another_app = AnotherWSGIApplication()

    Keep in mind that WSGI applications shouldn't be mounted directly: They
    must be wrapped around with :class:`tg.controllers.WSGIAppController`.

    """
    movies = MovieController(DBSession)
    error = ErrorController()

    def _before(self, *args, **kw):
        tmpl_context.project_name = "example"

    @expose('example.templates.index')
    def index(self):
        """Handle the front-page."""
        drawerOptions = ['Organize', 'Friends', 'Communities', 'Near Me', 'Favorites', 'Competition']
        events = ['Friday Night Fights', 'Board Game Nites', 'Boardgameageddon']
        return dict(page='index', drawerOptions=drawerOptions, events=events)

    @expose('example.templates.about')
    def about(self):
        """Handle the 'about' page."""
        return dict(page='about')

    @expose('example.templates.environ')
    def environ(self):
        """This method showcases TG's access to the wsgi environment."""
        return dict(page='environ', environment=request.environ)

    @expose('example.templates.data')
    @expose('json')
    def data(self, **kw):
        """This method showcases how you can use the same controller for a data page and a display page"""
        return dict(page='data', params=kw)

    @expose('example.templates.test')
    def test(self):
        """Handle the 'test' page."""
        return dict(page='test')
    
    @expose('example.templates.testjquerymobile')
    def index2(self):
        """Handle the 'test jquery mobile' page."""
        drawerOptions = ['Organize', 'Friends', 'Communities', 'Near Me', 'Favorites', 'Competition']
        events = ['Friday Night Fights', 'Board Game Nites', 'Boardgameageddon']
        return dict(drawerOptions=drawerOptions, events=events)

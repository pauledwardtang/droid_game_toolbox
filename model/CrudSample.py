from sqlalchemy import Column, Integer, String, Date, Text, ForeignKey
from sqlalchemy.orm import relation
from sprox.tablebase import TableBase

from base import DeclarativeBase

__all__ = [ 'Movie' ]

class Genre(DeclarativeBase):
    __tablename__ = "genres"
    genre_id = Column(Integer, primary_key=True)
    name = Column(String(100))

class Movie(DeclarativeBase):
    __tablename__ = "movies"
    movie_id = Column(Integer, primary_key=True)
    title = Column(String(100), nullable=False)
    description = Column(Text, nullable=True)
    genre_id = Column(Integer, ForeignKey('genres.genre_id'))
    genre = relation('Genre', foreign_keys=genre_id, backref='movies')
    release_date = Column(Date, nullable=True)
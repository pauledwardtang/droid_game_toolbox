import datetime

from sqlalchemy import Column, Integer, String, Date, Text, ForeignKey, Enum, DateTime
from sqlalchemy.orm import relation
from sprox.tablebase import TableBase

from base import DeclarativeBase

### Constants ###
# - TODO find a better "best" practice for defining consts
TABLE_EVENT =  'Event'
TABLE_LOCATION   =  'Location'
TABLE_EVENTPRIVACY = 'EventPrivacyTypes'
### End Consts ###

# Specifying class name, not table name...not sure what this is for actually
__all__ = [ 'Event' ]


    # Notification settings - these are really USER settings
    #push_enabled = Column(Integer, default=False)
    #digest_enabled = Column(Integer, default=True)
    #digest_interval_minutes = Column(Integer, default=60)

# Deprecated...
#class Location(DeclarativeBase):
    #"""Place-holder table for geolocation information"""
    #__tablename__ = TABLE_LOCATION
    #location_id = Column(Integer, primary_key=True)

# TODO Is this really going to capture all of the possible combinations of privacy settings?
#class EventPrivacySettings:
#    """Privacy settings for events. Essentially an enum describing different privacy settings.
#       Ex. PUBLIC, PRIVATE, INVITE_ONLY, USER_LIST_PUBLIC"""
#    __tablename__ = TABLE_EVENTPRIVACY
#    event_privacy_id = Column(Integer, primary_key=True)
#    type = Column(Text, nullable=False)

# TODO create a new quickstart project with AUTH enabled so this table can
# have keys into some relations table.
# Need many-to-many for nested events and users
class Event(DeclarativeBase):
    """User created event"""
    __tablename__ = TABLE_EVENT
    event_id = Column(Integer, primary_key=True)
    #creator_id = Column(Integer, ForeignKey('genres.genre_id'))
    #creator = relation(TABLE_LOCATION, foreign_keys=location_id, backref=TABLE_EVENT)
    #location_id = Column(Integer, ForeignKey(TABLE_LOCATION + '.' + 'location_id'))
    #location_info = relation(TABLE_LOCATION, foreign_keys=location_id, backref=TABLE_EVENT)
    title = Column(String(100), nullable=False)
    description = Column(Text, nullable=True)
    created = Column(DateTime, default=datetime.datetime.utcnow())
    location = Column(Text, nullable=True)
    #Event scheduling settings
    is_invite_only = Column(Integer, default=True)
 
    #Privacy settings
    is_private = Column(Integer, default=True)
    is_guestlist_private = Column(Integer, default=False) # only handles/usernames should be revealed by the model.NOT email addrs or full names

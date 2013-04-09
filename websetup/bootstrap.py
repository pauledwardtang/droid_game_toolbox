# -*- coding: utf-8 -*-
"""Setup the example application"""

import logging
from tg import config
from example import model
import transaction

def bootstrap(command, conf, vars):
    """Place any commands to setup example here"""

    # <websetup.bootstrap.before.auth

    # <websetup.bootstrap.after.auth>

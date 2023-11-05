from django.contrib.auth.models import BaseUserManager
from django.utils import timezone
from datetime import datetime

import logging
logger = logging.getLogger(__name__)


class UserManager(BaseUserManager):
    """
    Django requires that custom users define their own Manager class. By
    inheriting from 'BaseUserManager', we get a lot of the same code used by
    Django to create a 'User'.

    All we have to do is override the 'create_user' function which we will use
    to create 'User' objects.
    """
    def create_user(self, username, password):
        """Create and return a 'user' with and email, username and password"""
        if username is None:
            raise TypeError('Users must have a username')

        if password is None:
            raise TypeError('Users must have a password')

        user = self.model(username=username,
                          created_datetime=datetime.now(timezone.get_current_timezone()),
                          last_login=datetime.now(timezone.get_current_timezone()))
        user.set_password(password)
        user.save()

        user.save()

        return user

# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import calendar
import jwt
from datetime import datetime, timedelta

from django.conf import settings
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.db import models
from user_authentication.ModelManagers import UserManager
# Create your models here.


class User(AbstractBaseUser, PermissionsMixin):

    # Each 'User' needs a human-readable unique identifier that we can use to
    # represent the 'User' in the UI. We want to index this column in the
    # database to improve lookup performance.
    username = models.CharField(db_index=True, max_length=30, unique=True)

    # When a user no longer wishes to use our platform, they may try to delete
    # their account. That's a problem for us because the data we collect is
    # valuable to us and we don't want to delete it. We
    # will simply offer users a way to deactivate their account instead of
    # letting them delete it. That way they won't show up on the site anymore,
    # but we can still analyze the data.
    is_active = models.BooleanField(default=True)

    # A timestamp representing when this object was created.
    created_datetime = models.DateTimeField()

    # The 'USERNAME_FIELD' property tells us which field we will use to log in.
    # In this case we want it to be the username field.
    USERNAME_FIELD = 'username'

    # Tells Django that the UserManager class defined above should manage
    # objects of this type.
    objects = UserManager.UserManager()

    def __str__(self):
        """
        Returns a string representation of this 'User'.

        This string is used when a 'User' is printed in the console.
        """
        return self.username

    @property
    def token(self):
        """
        Allows us to get a user's token by calling 'user.token' instead of
        user.generate_jwt_token().

        The '@property' decorator above makes this possible. 'token' is called
        a "dynamic property".
        """
        return self._generate_jwt_token()

    def _generate_jwt_token(self):
        """
        Generates a JSON Web Token that stores this user's ID and has an expiry
        date set to 60 days into the future.
        """
        dt = datetime.now() + timedelta(days=60)

        token = jwt.encode({
            'id': self.pk,
            'exp': calendar.timegm(dt.timetuple())
        }, settings.SECRET_KEY, 'HS256')

        return token.decode('utf-8')

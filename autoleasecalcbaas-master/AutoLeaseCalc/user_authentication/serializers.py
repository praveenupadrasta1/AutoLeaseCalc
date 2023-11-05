from datetime import datetime
from django.utils import timezone

from django.contrib.auth import authenticate
from rest_framework import serializers

from AutoLeaseCalc.config import PASSWORD_KEY, USERNAME_KEY, TOKEN_KEY, INVALID_CREDENTIALS, USER_DEACTIVATED, \
    PASSWORD_MIN_LENGTH
from user_authentication.models import User


class RegistrationSerializer(serializers.ModelSerializer):
    """Serializers registration requests and creates a new user"""

    # Ensure passwords are at least 8 characters long, no longer than 128
    # characters, and can not be read by the client.

    password = serializers.CharField(
            max_length=128,
            min_length=PASSWORD_MIN_LENGTH,
            write_only=True
        )

    # The client should not be able to send a token along with a registration
    # request. Making 'token' read-only handles that for us.
    token = serializers.CharField(max_length=255, read_only=True)

    class Meta:
        model = User
        # List all of the fields that could possibly be included in a request
        # or response, including fields specified explicitly above.
        fields = ['username', 'password', 'token']

    def create(self, validated_data):
        # Use the 'create_user' method we write earlier to create a new user.
        user = User.objects.create_user(**validated_data)
        return user


class LoginSerializer(serializers.Serializer):

    username = serializers.CharField(max_length=30)
    password = serializers.CharField(max_length=128, write_only=True)
    token = serializers.CharField(max_length=255, read_only=True)

    def validate(self, data):
        # The 'validate' method is where we make sure that the current
        # instance of 'LoginSerializer' has "valid". In the case of logging a
        # user in, this means validating that they've provided an username
        # and password and that this combination matches one of the users in
        # our database.
        data = dict(data)
        user = None
        password = data.get(PASSWORD_KEY, None)

        # Raise an exception if a
        # password is not provided.
        if password is None:
            raise serializers.ValidationError(INVALID_CREDENTIALS)

        # The 'authenticate' method is provided by Django and handles checking
        # for a user that matches this username/password combination.
        if data.has_key(USERNAME_KEY):
            username = data.get(USERNAME_KEY, None)
            user = authenticate(username=username, password=password)

        # If no user was found matching this username/password combination then
        # 'authenticate' will return 'None'. Raise an exception in this case.
        if user is None:
            raise serializers.ValidationError(
                INVALID_CREDENTIALS
            )

        # Django provides a flag on our 'User' model called 'is_active'. The
        # purpose of this flag is to tell us whether the user has been banned
        # or deactivated. This will almost never be the case, but
        # it is worth checking. Raise an exception in this case.
        if not user.is_active:
            raise serializers.ValidationError(
                USER_DEACTIVATED
            )

        user.last_login = datetime.now(timezone.get_current_timezone())
        user.save()
        # The 'validate' method should return a dictionary of validated data.
        return {
            USERNAME_KEY: user.username,
            TOKEN_KEY: user.token
        }

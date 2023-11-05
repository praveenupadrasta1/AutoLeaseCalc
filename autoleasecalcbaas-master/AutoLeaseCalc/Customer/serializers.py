import re

from rest_framework import serializers

from AutoLeaseCalc.config import INVALID_PHONE_NUMBER_1, INVALID_PHONE_NUMBER_2, INVALID_EMAIL, INVALID_COUNTRY, \
    INVALID_FIRST_NAME, INVALID_LAST_NAME, INVALID_CITY, INVALID_STATE
from Customer.utils.country_list import country_list
from .models import Customer


class CustomerProfileSerializer(serializers.Serializer):

    TITLES_CHOICE = (('Mr.', 'Mr.'),
                     ('Miss', 'Miss'),
                     ('Mrs.', 'Mrs.'),)
    PHONE_TYPE    = ((0, 'Mobile'),
                     (1, 'Residence'),
                     (2, 'Office'),)

    title         = serializers.ChoiceField(choices=TITLES_CHOICE, allow_blank=False)
    first_name    = serializers.CharField(max_length=20, required=True, allow_blank=False)
    last_name     = serializers.CharField(max_length=20, required=True, allow_blank=False)
    address_st_1  = serializers.CharField(max_length=20, required=False, allow_blank=True)
    address_st_2  = serializers.CharField(max_length=30, required=False, allow_blank=True)
    city          = serializers.CharField(max_length=15, required=False, allow_blank=True)
    state         = serializers.CharField(max_length=25, required=False, allow_blank=True)
    zip           = serializers.CharField(max_length=10, required=False, allow_blank=True)
    country       = serializers.CharField(max_length=20, required=False, allow_blank=True)
    phone_1       = serializers.CharField(max_length=15, required=True, allow_blank=False)
    phone_1_type  = serializers.ChoiceField(choices=PHONE_TYPE, required=False, allow_blank=True)
    phone_2       = serializers.CharField(max_length=15, required=False, allow_blank=True)
    phone_2_type  = serializers.ChoiceField(choices=PHONE_TYPE, required=False, allow_blank=True)
    email         = serializers.CharField(max_length=40, required=True, allow_blank=False)

    # class Meta:
    #     model = Customer
    #     # List all of the fields that could possibly be included in a request
    #     # or response, including fields specified explicitly above.
    #     fields = ['title', 'first_name', 'last_name', 'address_st_1', 'address_st_2', 'city', 'state', 'zip', 'country',
    #               'phone_1', 'phone_1_type', 'phone_2', 'phone_2_type', 'email']

    def validate_phone_1(self, phone_1):
        # str(phone_1)[1:] will trim the '+' sign in the beginning of the mobile number
        # and later checks whether its a digit
        if str(phone_1)[1:].isdigit():
            return phone_1
        else:
            raise serializers.ValidationError(INVALID_PHONE_NUMBER_1)

    def validate_phone_2(self, phone_2):
        if str(phone_2)[1:].isdigit() or phone_2 == "":
            return phone_2
        else:
            raise serializers.ValidationError(INVALID_PHONE_NUMBER_2)

    def validate_email(self, email):
        # The following regex matches following kind of emails
        # asmith@mactec.com | foo12@foo.edu | bob.smith@foo.tv
        if re.match("^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$",
                    email):
            return email
        else:
            raise serializers.ValidationError(INVALID_EMAIL)

    # def validate_country(self, country):
    #     if country == "" :
    #         return country
    #     else:
    #         raise serializers.ValidationError(INVALID_COUNTRY)

    def validate_first_name(self, first_name):
        if str(first_name).isalpha():
            return first_name
        else:
            raise serializers.ValidationError(INVALID_FIRST_NAME)

    def validate_last_name(self, last_name):
        if str(last_name).isalpha():
            return last_name
        else:
            raise serializers.ValidationError(INVALID_LAST_NAME)

    def validate_city(self, city):
        city = city.replace(' ', '').isalpha()
        if str(city).isalpha() or city == "":
            return city
        else:
            raise serializers.ValidationError(INVALID_CITY)

    def validate_state(self, state):
        if str(state).isalpha() or state=="":
            return state
        else:
            raise serializers.ValidationError(INVALID_STATE)

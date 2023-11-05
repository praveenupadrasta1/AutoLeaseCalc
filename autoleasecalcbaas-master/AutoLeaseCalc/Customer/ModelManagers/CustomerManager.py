from django.db import models
import logging
logger = logging.getLogger(__name__)

from AutoLeaseCalc.config import TITLE_KEY, FIRST_NAME_KEY, LAST_NAME_KEY, ADD_ST_1_KEY, ADD_ST_2_KEY, CITY_KEY, STATE_KEY, \
    ZIP_KEY, COUNTRY_KEY, PHONE_1_KEY, PHONE_1_TYPE_KEY, PHONE_2_KEY, PHONE_2_TYPE_KEY, EMAIL_KEY


class CustomerManager(models.Manager):

    def create(self, data):
        # This medthod Creates Customer by parsing the request data.
        try:
            customer_profile = self.model(title      = data.get(TITLE_KEY),
                                        first_name   = data.get(FIRST_NAME_KEY),
                                        last_name    = data.get(LAST_NAME_KEY),
                                        address_st_1 = data.get(ADD_ST_1_KEY, None),
                                        address_st_2 = data.get(ADD_ST_2_KEY, None),
                                        city         = data.get(CITY_KEY, None),
                                        state        = data.get(STATE_KEY, None),
                                        zip          = data.get(ZIP_KEY, None),
                                        country      = data.get(COUNTRY_KEY, None),
                                        phone_1      = data.get(PHONE_1_KEY),
                                        phone_1_type = data.get(PHONE_1_TYPE_KEY, 0),
                                        phone_2      = data.get(PHONE_2_KEY, None),
                                        phone_2_type = data.get(PHONE_2_TYPE_KEY, None),
                                        email        = data.get(EMAIL_KEY))
            customer_profile.save()
            return customer_profile
        except Exception as e:
            logger.error(str(e))
            raise Exception(e)

    def update(self, data, customer_record):
        # This medthod updates Customer by parsing the request data.
        try:
            customer_record.title = data.get(TITLE_KEY)
            customer_record.first_name = data.get(FIRST_NAME_KEY)
            customer_record.last_name = data.get(LAST_NAME_KEY)
            customer_record.address_st_1 = data.get(ADD_ST_1_KEY, None)
            customer_record.address_st_2 = data.get(ADD_ST_2_KEY, None)
            customer_record.city = data.get(CITY_KEY, None)
            customer_record.state = data.get(STATE_KEY, None)
            customer_record.zip = data.get(ZIP_KEY, None)
            customer_record.country = data.get(COUNTRY_KEY, None)
            customer_record.phone_1 = data.get(PHONE_1_KEY)
            customer_record.phone_1_type = data.get(PHONE_1_TYPE_KEY, 0)
            customer_record.phone_2 = data.get(PHONE_2_KEY, None)
            if data.get(PHONE_2_KEY) != "":
                customer_record.phone_2_type = data.get(PHONE_2_TYPE_KEY, None)
            customer_record.email = data.get(EMAIL_KEY)
            customer_record.save()
            return customer_record
        except Exception as e:
            logger.error(str(e))
            raise Exception(e)

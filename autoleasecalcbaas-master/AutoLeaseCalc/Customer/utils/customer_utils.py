from Customer.models import Customer

from AutoLeaseCalc.config import TITLE_KEY, FIRST_NAME_KEY, LAST_NAME_KEY, ADD_ST_2_KEY, ADD_ST_1_KEY, CITY_KEY, STATE_KEY,\
    ZIP_KEY, COUNTRY_KEY, PHONE_2_KEY, PHONE_1_KEY, PHONE_1_TYPE_KEY, PHONE_2_TYPE_KEY, EMAIL_KEY


def get_customer_record_by_email(unique_key):
    try:
        return Customer.objects.get(email=unique_key)
    except Exception as e:
        return None


def get_customer_record_by_id(unique_key):
    try:
        return Customer.objects.get(pk=unique_key)
    except Exception as e:
        return None


def frame_customer_data(customer_record):
    customer = dict()
    customer[TITLE_KEY] = customer_record.title
    customer[FIRST_NAME_KEY] = customer_record.first_name
    customer[LAST_NAME_KEY] = customer_record.last_name
    customer[ADD_ST_1_KEY] = customer_record.address_st_1
    customer[ADD_ST_2_KEY] = customer_record.address_st_2
    customer[CITY_KEY] = customer_record.city
    customer[STATE_KEY] = customer_record.state
    customer[ZIP_KEY] = customer_record.zip
    customer[COUNTRY_KEY] = customer_record.country
    customer[PHONE_1_KEY] = customer_record.phone_1

    if customer_record.phone_1_type == '0':
        customer[PHONE_1_TYPE_KEY] = 'Mobile'
    elif customer_record.phone_1_type == '1':
        customer[PHONE_1_TYPE_KEY] = 'Residence'
    elif customer_record.phone_1_type == '2':
        customer[PHONE_1_TYPE_KEY] = 'Office'
    else:
        customer[PHONE_1_TYPE_KEY] = None

    customer[PHONE_2_KEY] = customer_record.phone_2
    if customer_record.phone_2_type == '0':
        customer[PHONE_2_TYPE_KEY] = 'Mobile'
    elif customer_record.phone_2_type == '1':
        customer[PHONE_2_TYPE_KEY] = 'Residence'
    elif customer_record.phone_2_type == '2':
        customer[PHONE_2_TYPE_KEY] = 'Office'
    else:
        customer[PHONE_2_TYPE_KEY] = None

    customer[EMAIL_KEY] = customer_record.email
    return customer
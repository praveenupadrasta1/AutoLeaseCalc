# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from Customer.ModelManagers.CustomerManager import CustomerManager
# Create your models here.


class Customer(models.Model):

    TITLES_CHOICE = (('Mr.', 'Mr.'),
                    ('Miss', 'Miss'),
                    ('Mrs.', 'Mrs.'),)
    PHONE_TYPE    = ((0, 'Mobile'),
                    (1, 'Residence'),
                    (2, 'Office'),)

    title        = models.CharField(choices=TITLES_CHOICE, max_length=4, blank=False)
    first_name   = models.CharField(max_length=20, blank=False)
    last_name    = models.CharField(max_length=20, blank=False)
    address_st_1 = models.CharField(max_length=20, blank=True)
    address_st_2 = models.CharField(max_length=30, blank=True)
    city         = models.CharField(max_length=15, blank=True)
    state        = models.CharField(max_length=25, blank=True)
    zip          = models.CharField(max_length=10, blank=True)
    country      = models.CharField(max_length=20, blank=True)
    phone_1      = models.CharField(max_length=15, blank=False)
    phone_1_type = models.CharField(choices=PHONE_TYPE, max_length=1, blank=True)
    phone_2      = models.CharField(max_length=15, blank=True)
    phone_2_type = models.CharField(choices=PHONE_TYPE, max_length=1, blank=True)
    email        = models.EmailField(db_index=True, unique=True)

    object       = CustomerManager()
    objects      = models.Manager()

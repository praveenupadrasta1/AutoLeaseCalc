# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from Customer.models import Customer
from .ModelManagers.LeaseManager import LeaseManager

# Create your models here.


class Lease(models.Model):
    customer         = models.ForeignKey(Customer, on_delete=models.CASCADE)
    sticker_price    = models.PositiveIntegerField()
    residual_percent = models.FloatField()
    neg_sales_price  = models.PositiveIntegerField()
    down_payment     = models.PositiveIntegerField()
    lease_acq_fee    = models.PositiveIntegerField()
    doc_tire_fee     = models.PositiveIntegerField()
    money_factor     = models.FloatField()
    lease_months     = models.SmallIntegerField()
    tax_rate         = models.FloatField()
    reg_fee          = models.PositiveIntegerField()
    tot_lease_with_tax = models.FloatField()

    object = LeaseManager()
    objects = models.Manager()
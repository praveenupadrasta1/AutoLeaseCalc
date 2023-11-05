# -*- coding: utf-8 -*-
# Generated by Django 1.11.16 on 2018-10-16 16:36
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('Customer', '0002_auto_20181016_0103'),
    ]

    operations = [
        migrations.CreateModel(
            name='Lease',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('sticker_price', models.PositiveIntegerField()),
                ('residual_percent', models.FloatField()),
                ('neg_sales_price', models.PositiveIntegerField()),
                ('down_payment', models.PositiveIntegerField()),
                ('lease_acq_fee', models.PositiveIntegerField()),
                ('doc_tire_fee', models.PositiveIntegerField()),
                ('money_factor', models.FloatField()),
                ('lease_months', models.SmallIntegerField()),
                ('tax_rate', models.FloatField()),
                ('reg_fee', models.PositiveIntegerField()),
                ('tot_lease_with_tax', models.FloatField()),
                ('customer', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Customer.Customer')),
            ],
        ),
    ]
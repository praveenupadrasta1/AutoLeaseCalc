# -*- coding: utf-8 -*-
# Generated by Django 1.11.16 on 2018-10-15 16:56
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.manager


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Customer',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(choices=[('Mr.', 'Mr.'), ('Miss', 'Miss'), ('Mrs.', 'Mrs.')], max_length=4)),
                ('first_name', models.CharField(max_length=20)),
                ('last_name', models.CharField(max_length=20)),
                ('address_st_1', models.CharField(blank=True, max_length=20)),
                ('address_st_2', models.CharField(blank=True, max_length=30)),
                ('city', models.CharField(blank=True, max_length=15)),
                ('state', models.CharField(blank=True, max_length=25)),
                ('zip', models.CharField(blank=True, max_length=10)),
                ('country', models.CharField(blank=True, max_length=20)),
                ('phone_1', models.CharField(max_length=15)),
                ('phone_1_type', models.CharField(blank=True, choices=[(0, 'Mobile'), (1, 'Residence'), (2, 'Office')], max_length=1)),
                ('phone_2', models.CharField(blank=True, max_length=15)),
                ('phone_2_type', models.CharField(blank=True, choices=[(0, 'Mobile'), (1, 'Residence'), (2, 'Office')], max_length=1)),
                ('email', models.EmailField(db_index=True, max_length=254)),
            ],
            managers=[
                ('object', django.db.models.manager.Manager()),
            ],
        ),
    ]

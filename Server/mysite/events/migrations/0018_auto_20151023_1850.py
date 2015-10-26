# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0017_auto_20151023_1840'),
    ]

    operations = [
        migrations.RenameField(
            model_name='event',
            old_name='adress',
            new_name='address',
        ),
        migrations.AddField(
            model_name='event',
            name='creator',
            field=models.CharField(default=b'Ben!', max_length=200),
        ),
        migrations.AlterField(
            model_name='event',
            name='date',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 23, 16, 50, 1, 77000, tzinfo=utc), verbose_name=b'date'),
        ),
    ]

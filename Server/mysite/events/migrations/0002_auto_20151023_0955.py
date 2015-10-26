# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0001_initial'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='event',
            options={'ordering': ('Event_name',)},
        ),
        migrations.RemoveField(
            model_name='event',
            name='question_text',
        ),
        migrations.AddField(
            model_name='event',
            name='Event_name',
            field=models.CharField(default=b'No Name!', max_length=200),
        ),
        migrations.AddField(
            model_name='event',
            name='adress',
            field=models.CharField(default=b'Sidney!', max_length=200),
        ),
        migrations.AddField(
            model_name='event',
            name='date',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 23, 7, 55, 26, 816000, tzinfo=utc), verbose_name=b'date'),
        ),
        migrations.AddField(
            model_name='event',
            name='description',
            field=models.TextField(default=b'No Description!'),
        ),
        migrations.AddField(
            model_name='event',
            name='duration',
            field=models.DurationField(default=datetime.timedelta(0), verbose_name=b'duration'),
        ),
        migrations.AddField(
            model_name='event',
            name='latitude',
            field=models.FloatField(default=0.0),
        ),
        migrations.AddField(
            model_name='event',
            name='longitude',
            field=models.FloatField(default=0.0),
        ),
        migrations.AddField(
            model_name='event',
            name='tags',
            field=models.CharField(default=b'Foot!', max_length=200),
        ),
    ]

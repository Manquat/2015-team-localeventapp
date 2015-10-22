# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


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
            name='description',
            field=models.TextField(default=b'No Description!'),
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

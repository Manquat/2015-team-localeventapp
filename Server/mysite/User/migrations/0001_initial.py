# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(default=b'No Name!', max_length=200)),
                ('token', models.CharField(default=b'00', max_length=200)),
            ],
            options={
                'ordering': ('name',),
            },
        ),
    ]

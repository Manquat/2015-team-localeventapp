from django.db import models
from rest_framework import serializers
from django.utils import timezone
from datetime import timedelta
from User.models import participant


class Event(models.Model):
    Event_name = models.CharField(max_length=200, default='No Name!')
    creator = models.IntegerField(default='1')
    description = models.TextField(default='No Description!')
    latitude = models.FloatField(default=0.00000)
    longitude = models.FloatField(default=0.00000)
    address = models.CharField(max_length=200, default='Sidney!')
    date = models.DateTimeField("date", default=timezone.now())
    duration = models.DurationField("duration", default=timedelta())
    tags = models.CharField(max_length=200, default='Foot!')
    image = models.TextField(default='No Image')
    participants = models.ManyToManyField(participant)
    def __unicode__(self):
        return self.Event_name

    class Meta:
        ordering = ('Event_name',)


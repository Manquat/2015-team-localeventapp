from django.db import models
from rest_framework import serializers
from django.utils import timezone
# Create your models here.


class Event(models.Model):
    Event_name = models.CharField(max_length=200, default='No Name!')
    description = models.TextField(default='No Description!')
    latitude = models.FloatField(default=0.00000)
    longitude = models.FloatField(default=0.00000)
    adress = models.CharField(max_length=200, default='Sidney!')
    #date = models.DateTimeField("date", default=timezone.now())
    #duration = models.DateTimeField("duration", default=timezone.now())
    tags = models.CharField(max_length=200, default='Foot!')
    def __unicode__(self):
        return self.Event_name

    class Meta:
        ordering = ('Event_name',)
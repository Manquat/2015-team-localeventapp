__author__ = 'cerschae'

from rest_framework import serializers
from models import Event
from django.utils import timezone


class EventSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    Event_name = serializers.CharField(default='No Name!', max_length=100)
    description = serializers.CharField(default='No Description!')
    latitude = serializers.FloatField(default=0.00000)
    longitude = serializers.FloatField(default=0.00000)
    adress = serializers.CharField(default='Sidney!', max_length=100)
    #date = serializers.DateTimeField("date", default=timezone.now())
    #duration = serializers.DateTimeField("duration", default=timezone.now())
    tags = serializers.CharField(max_length=200, default='Foot!')

    def create(self, validated_data):
        """
        Create and return a new `Snippet` instance, given the validated data.
        """
        return Snippet.objects.create(**validated_data)

    def update(self, instance, validated_data):
        """
        Update and return an existing `Snippet` instance, given the validated data.
        """
        instance.Event_name = validated_data.get('Event_name', instance.Event_name)
        instance.description = validated_data.get('description', instance.description)
        instance.latitude = validated_data.get('latitude', instance.latitude)
        instance.longitude = validated_data.get('longitude', instance.longitude)
        instance.adress = validated_data.get('adress', instance.adress)
        #instance.date = validated_data.get('date', instance.date)
        #instance.duration = validated_data.get('duration', instance.duration)
        instance.tags = validated_data.get('tags', instance.tags)
        instance.save()
        return instance
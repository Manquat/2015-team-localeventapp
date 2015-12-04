__author__ = 'cerschae'

from rest_framework import serializers
from models import Event, Comment
from django.utils import timezone


class EventSerializer(serializers.ModelSerializer):
    class Meta:
        model = Event
        fields = ('id', 'Event_name', 'creator', 'description', 'latitude', 'longitude', 'address', 'date', 'duration', 'tags', 'image', 'participants', 'comments')

class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ('body', 'creator', 'event')

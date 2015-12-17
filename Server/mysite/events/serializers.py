__author__ = 'cerschae'

from rest_framework import serializers
from models import Event, Comment
from django.utils import timezone


class EventSerializer(serializers.ModelSerializer):
    comments = serializers.StringRelatedField(required=False)
    class Meta:
        model = Event
<<<<<<< HEAD
<<<<<<< HEAD
        fields = ('id', 'Event_name', 'owner', 'description', 'latitude', 'longitude', 'address', 'date', 'duration', 'tags', 'image', 'participants', 'comments')
=======
        fields = ('id', 'Event_name', 'creator', 'description', 'latitude', 'longitude', 'address', 'date', 'duration', 'tags', 'image', 'participants', 'comments')
>>>>>>> 18a31a380003d9e2c4e0cbfd50d56ec43216c8eb
=======
        fields = ('id', 'Event_name', 'owner', 'description', 'latitude', 'longitude', 'address', 'date', 'duration', 'tags', 'image', 'participants', 'comments')
>>>>>>> refreshEventActivity

class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ('id','body','creator_name', 'creator', 'event')

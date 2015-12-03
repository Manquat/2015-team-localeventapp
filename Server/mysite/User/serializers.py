__author__ = 'cerschae'

from rest_framework import serializers
from models import participant
from django.utils import timezone


class ParticipantSerializer(serializers.ModelSerializer):
    comments = serializers.StringRelatedField(required=False)
    events = serializers.StringRelatedField(required=False)
    class Meta:
        model = participant
        fields = ('id',  'name', 'email','googleid','events', 'comments')
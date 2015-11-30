__author__ = 'cerschae'

from rest_framework import serializers
from models import participant
from django.utils import timezone


class ParticipantSerializer(serializers.ModelSerializer):
    class Meta:
        model = participant
        fields = ('id', 'name', 'email', 'comments')
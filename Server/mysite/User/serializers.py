__author__ = 'cerschae'

from rest_framework import serializers
from models import User
from django.utils import timezone


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'name', 'email')
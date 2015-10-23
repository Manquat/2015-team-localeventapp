
__author__ = 'Pujima'

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from events.models import Event
from events.serializers import EventSerializer
import datetime

todate = 1445613382
fromdate = 1414077381

tdate = datetime.datetime.fromtimestamp(float(todate))
idate = datetime.datetime.fromtimestamp(float(fromdate))

event = Event.objects.filter(date__range=[idate, tdate])


serializer = EventSerializer(event,many=True)

print serializer.data
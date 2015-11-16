from django.shortcuts import render
from django.http import HttpResponse

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from events.models import Event
from events.serializers import EventSerializer
import datetime
from oauth2client import client, crypt

@api_view(['GET', 'POST'])
def event_list(request, token, format=None):
    """
    List all events, or create an event.
    """

    if validate_user(token) is False:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)

    if request.method == 'GET':
        events = Event.objects.all()
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = EventSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET', 'PUT', 'DELETE'])
def event_detail(request, token, pk, format=None):
    """
    Retrieve, update or delete an event.
    """
    if validate_user(token) is False:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)

    try:
        event = Event.objects.get(pk=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = EventSerializer(event)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = EventSerializer(event, data= request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        event.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET'])
def event_requestdate(request, token, fromdate, todate, format=None):

    if validate_user(token) is False:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)

    tdate = datetime.datetime.fromtimestamp(float(todate))
    idate = datetime.datetime.fromtimestamp(float(fromdate))

    try:
        event = Event.objects.filter(date__range=[idate, tdate])
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    #event = Event.objects.all()
    if request.method == 'GET':
        serializer = EventSerializer(event, many=True)
        return Response(serializer.data)

@api_view(['GET'])
def event_request(request, token, fromdate, todate, mLongitude, mLatitude, mDistance, mNE=100, mId=0):
    """
    Eventrequest gives back event information in function of date and distance, optional arguments are Number of Events (default = 100) and starting ID (default = 0)
    """
    if validate_user(token) is False:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)

    tdate = datetime.datetime.fromtimestamp(float(todate))
    idate = datetime.datetime.fromtimestamp(float(fromdate))

    maxlongitude= float(mLongitude) + float(mDistance)
    minlongitude= float(mLongitude) - float(mDistance)
    maxlatitude = float(mLatitude) + float(mDistance)
    minlatitude = float(mLatitude) - float(mDistance)
    try:
        event = Event.objects.filter(date__range=[idate, tdate]).filter(longitude__range= (minlongitude,maxlongitude)).filter(latitude__range= (minlatitude,maxlatitude)).filter(id__gt= mId)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    #event = Event.objects.all()
    if request.method == 'GET':
        serializer = EventSerializer(event[:mNE], many=True)
        return Response(serializer.data)

def validate_user(token):
    """
    Validate User.
    """

    CLIENT_ID = "1038367220496-ceugpt9chaqucpjhhglmced2d2tat2lm.apps.googleusercontent.com"
    WEB_CLIENT_ID = "60350226207-ph9a1g0iuakvfe8bd7bbku01ktr40aur.apps.googleusercontent.com"

    #For debugging reason, has to be removed once tokenid works
    if token == "Gandalf":
       return True
    else:
        try:
            idinfo = client.verify_id_token(token, CLIENT_ID)
            # If multiple clients access the backend server:
            if idinfo['aud'] not in [WEB_CLIENT_ID]:
                raise crypt.AppIdentityError("Unrecognized client.")
            if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
                raise crypt.AppIdentityError("Wrong issuer.")
        except crypt.AppIdentityError:
            return False
        Userid = idinfo['sub']
        return True
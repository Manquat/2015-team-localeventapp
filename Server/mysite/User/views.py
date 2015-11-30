from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from User.models import participant
from events.models import Event
from events.serializers import EventSerializer
from User.serializers import ParticipantSerializer
from oauth2client import client, crypt

# Create your views here.

@api_view(['GET','POST'])
def create_user(request, format=None):
    """
    Create an User
    """
    if request.method == 'GET':
        user = participant.objects.all()
        serializer = ParticipantSerializer(user, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = ParticipantSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def update_user(request, pk, format=None):
    """
    Retrieve, update or delete an User.
    """
    try:
        user = participant.objects.get(pk=pk)
    except participant.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ParticipantSerializer(user)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = ParticipantSerializer(user, data= request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        user.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


@api_view(['GET'])
def created_events(request, pk, format=None):
    """
    Create an User
    """

    try:
        p = participant.objects.get(pk=pk)
    except participant.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        events = Event.objects.filter(creator=pk)
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)

@api_view(['GET'])
def joined_events(request, pk, format=None):
    """
    Create an User
    """

    try:
        p = participant.objects.get(pk=pk)
    except participant.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        events = p.event_set.all()
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)


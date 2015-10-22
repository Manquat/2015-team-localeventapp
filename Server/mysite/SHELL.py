__author__ = 'Pujima'

from events.models import Event
from events.serializers import EventSerializer
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser


e = Event(Event_name= "Buyah")
e.save()

#e = Event.objects.filter(id=1)

serializer = EventSerializer(e)

print serializer.data

content = JSONRenderer().render(serializer.data)

print content

from django.utils.six import BytesIO

stream = BytesIO(content)
data = JSONParser().parse(stream)

print data

serializer = EventSerializer(data=data)
print serializer.is_valid()
# True
print serializer.validated_data
# OrderedDict([('title', ''), ('code', 'print "hello, world"\n'), ('linenos', False), ('language', 'python'), ('style', 'friendly')])

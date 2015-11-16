import unittest
from events.models import Event
from events.serializers import EventSerializer
from django.utils.six import BytesIO
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
import datetime

class TestEvent(unittest.TestCase):
    "Basic tests"
    def setUp(self):
        self.event = Event(Event_name="Coffeebreak", creator="Christoph", description="This is my Coffeebreak", latitude=100.45, longitude=150.45, address="New York, Manhattan", date = "2015-11-10T13:04:48.356000Z", duration = "02:00:00", tags = "Coffee", image = "ME")

    def tearDown(self):
        del self.event

    def test_basic_name(self):
        assert self.event.Event_name =="Coffeebreak"

    def test_basic_creator(self):
        assert self.event.creator == "Christoph"

    def test_basic_description(self):
        assert self.event.description == "This is my Coffeebreak"

    def test_basic_latitude(self):
        assert self.event.latitude == 100.45

    def test_basic_longitude(self):
        assert self.event.longitude == 150.45

    def test_basic_date(self):
        assert self.event.date == "2015-11-10T13:04:48.356000Z"

    def test_basic_duration(self):
        assert self.event.duration == "02:00:00"

    def test_basic_tags(self):
        assert self.event.tags == "Coffee"

    def test_basic_image(self):
        assert self.event.image == "ME"


class TestSerializer(unittest.TestCase):
    "Show setup and teardown"

    def setUp(self):
        self.event = Event(Event_name="Coffeebreak", creator="Christoph", description="This is my Coffeebreak", latitude=100.45, longitude=150.45, address="New York, Manhattan", date = datetime.datetime(2008, 11, 22, 19, 53, 42), duration = datetime.timedelta(00,02,00,00), tags = "Coffee", image = "ME")

    def tearDown(self):
        del self.event

    def test_valid(self):
        serializer = EventSerializer(self.event)
        content = JSONRenderer().render(serializer.data)
        stream = BytesIO(content)
        data = JSONParser().parse(stream)
        serializer = EventSerializer(data=data)
        assert serializer.is_valid() == True

from django.test.client import Client

class TestViews(unittest.TestCase):

    def setUp(self):
        event = Event.objects.all()
        event.delete()
        self.event = Event.objects.create(id = 1, Event_name="Coffeebreak", creator="Christoph", description="This is my Coffeebreak", latitude=100.45, longitude=150.45, address="New York, Manhattan", date = datetime.datetime(2008, 11, 22, 19, 53, 42), duration = datetime.timedelta(00,02,00,00), tags = "Coffee", image = "ME")
    def tearDown(self):
        event = Event.objects.all()
        event.delete()

    def test_event_list_get(self):
        c = Client()
        response = c.get('/events/Gandalf/')
        print response
        assert response.status_code == 200

    def test_event_list_post(self):
        c = Client()
        response = response = c.post('/events/Gandalf/', {'id':2,'Event_name': 'Go back to work', 'creator': 'Boss'})
        assert response.status_code == 201

    def test_event_detail_get(self):
        c = Client()
        response = c.get('/events/Gandalf/1')
        assert response.status_code == 200
    """
    def test_event_detail_put(self):
        c = Client()
        response = c.put('/events/Gandalf/1', {'Event_name': 'Going back to work', 'creator': 'Christoph'})
        print response.status_code
        assert response.status_code == 415

    def test_event_detail_delete(self):
        c = Client()
        response = c.delete('/events/Gandalf/2')
        print response.status_code
        assert response.status_code == response.status_code
    """
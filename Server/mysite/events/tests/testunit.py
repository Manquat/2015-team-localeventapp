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
        self.assertEqual(self.event.Event_name, "Coffeebreak")

    def test_basic_creator(self):
        self.assertEqual(self.event.creator, "Christoph")

    def test_basic_description(self):
        self.assertEqual(self.event.description,  "This is my Coffeebreak")

    def test_basic_latitude(self):
        self.assertEqual(self.event.latitude,  100.45)

    def test_basic_longitude(self):
        self.assertEqual(self.event.longitude,  150.45)

    def test_basic_date(self):
        self.assertEqual(self.event.date,  "2015-11-10T13:04:48.356000Z")

    def test_basic_duration(self):
        self.assertEqual(self.event.duration,  "02:00:00")

    def test_basic_tags(self):
        self.assertEqual(self.event.tags,  "Coffee")

    def test_basic_image(self):
        self.assertEqual(self.event.image,  "ME")


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
        content = JSONRenderer().render(response.data)
        stream = BytesIO(content)
        data = JSONParser().parse(stream)
        assert data[0]["Event_name"] =="Coffeebreak"
        assert data[0]["creator"] == "Christoph"
        assert data[0]["description"] == "This is my Coffeebreak"
        assert data[0]["latitude"] == 100.45
        assert data[0]["longitude"] == 150.45
        assert data[0]["date"] == "2008-11-22T19:53:42Z"
        assert data[0]["duration"] == "00:00:02"
        assert data[0]["tags"] == "Coffee"
        assert data[0]["image"] == "ME"
        assert response.status_code == 200

    def test_event_list_post(self):
        c = Client()
        response = c.post('/events/Gandalf/', {'id':2,'Event_name': 'Go back to work', 'creator': 'Boss'})
        assert response.status_code == 201

    def test_event_detail_get(self):
        c = Client()
        response = c.get('/events/Gandalf/1')
        content = JSONRenderer().render(response.data)
        stream = BytesIO(content)
        data = JSONParser().parse(stream)
        assert data["Event_name"] == "Coffeebreak"
        assert data["creator"] == "Christoph"
        assert data["description"] == "This is my Coffeebreak"
        assert data["latitude"] == 100.45
        assert data["longitude"] == 150.45
        assert data["date"] == "2008-11-22T19:53:42Z"
        assert data["duration"] == "00:00:02"
        assert data["tags"] == "Coffee"
        assert data["image"] == "ME"
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
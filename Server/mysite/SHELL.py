__author__ = 'Pujima'

from events.models import Event
from events.serializers import EventSerializer
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from User.models import User
from User.serializers import UserSerializer
from oauth2client import client, crypt



token = "ya29.LQI9JjiDLX_E7rkW9wbnKDEN8t-0-6NPRMM2B9bNhImr8Hq9Z-uVL9l4NjR4D7rzxhxLDA"
CLIENT_ID = "1038367220496-ceugpt9chaqucpjhhglmced2d2tat2lm.apps.googleusercontent.com"
WEB_CLIENT_ID = "60350226207-ph9a1g0iuakvfe8bd7bbku01ktr40aur.apps.googleusercontent.com"
# (Receive token by HTTPS POST)

idinfo = client.verify_id_token(token, CLIENT_ID)

print idinfo

# (Receive token by HTTPS POST)


"""
    try:
        idinfo = client.verify_id_token(token, CLIENT_ID)
        # If multiple clients access the backend server:
        if idinfo['aud'] not in [WEB_CLIENT_ID]:
            raise crypt.AppIdentityError("Unrecognized client.")
        if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
            raise crypt.AppIdentityError("Wrong issuer.")
    except crypt.AppIdentityError:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)
    Userid = idinfo['sub']
    try:
        user = User.objects.filter(userid__equal = Userid)
        return Response(status=status.HTTP_202_ACCEPTED)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    print "noooo"
    # If multiple clients access the backend server:
    if idinfo['aud'] not in [WEB_CLIENT_ID]:
        raise crypt.AppIdentityError("Unrecognized client.")
    if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
        raise crypt.AppIdentityError("Wrong issuer.")
except crypt.AppIdentityError:
    print "noooo"
print idinfo['sub']
"""
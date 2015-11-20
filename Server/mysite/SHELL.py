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
from base64 import b64decode, b64encode


token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU2ODlkYWY2ODgzMTBmMzVmNDE5NmE5MTM3ZTdiZjhjZGZlNTU0ODkifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiIyNzQwNTg0NDE4MjctcTFqcTV2MDhzYnA5aTRnYmRmcHBxNjBxcTdqcmllanYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDIxNDg0OTIyMjMzMDE5NTg0OTQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXpwIjoiMjc0MDU4NDQxODI3LWsxYjBjb2YwdnNrbDA3OW5lMTQ4MjIwaDFvMGdqYjk3LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJlaW5zYW1lcl9zaWVkbGVyQGhvdG1haWwuY29tIiwiaWF0IjoxNDQ4MDI5NTc4LCJleHAiOjE0NDgwMzMxNzgsIm5hbWUiOiJlaW5zYW1lcl9zaWVkbGVyQGhvdG1haWwuY29tIn0.q9yvyJwu7XGpjWhTR_X4TYaDJX-kDf0yBMtYGvI74aXlL1ExtqlWmxd87HMRJg9vths23-f--lPulDWrPtiEWJb_e61eTwOl3ZxapZecm2dEAZAQj5VgVGUN_6RhK6Ou_zFixOn-PAgzIdwdraUnAIkGy1eESnIZDfEWW7lKB5mu9VVxPPihhuue5Q3BwWeHL1tZw6Q0YXI9OkLt27pGbBTxHY47Y8LqowMGtuO6lQzvm-WECuik5tNgs7Hi3_djgf4eUZDQ-N5YLPCzzcVLy0VDoQ2UceMW3w8tckkZnaGH_gSxUXaoJiYFhQiJCAysoFVfM6UDdeydCIZ4eYM7mA"
WEB_CLIENT_ID = "274058441827-q1jq5v08sbp9i4gbdfppq60qq7jriejv.apps.googleusercontent.com"
CLIENT_ID = "274058441827-k1b0cof0vskl079ne148220h1o0gjb97.apps.googleusercontent.com"
# (Receive token by HTTPS POST)
#idinfo = client.verify_id_token(token, WEB_CLIENT_ID)
type(token)
print b64encode(token)
print b64decode(b64encode(token))
#print idinfo
try:

    idinfo = client.verify_id_token(token, WEB_CLIENT_ID)
    print idinfo
# If multiple clients access the backend server:
    if idinfo['aud'] not in [WEB_CLIENT_ID]:
        #print "fuck"
        raise crypt.AppIdentityError("Unrecognized client.")
    if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
        raise crypt.AppIdentityError("Wrong issuer.")
    print "Authenticated"
except crypt.AppIdentityError is True:
    print "Fuck"

#print "fnished"
__author__ = 'Pujima'

from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from . import views


urlpatterns = [
    url(r'^events/(?P<token>[^/]+)/$', views.event_list),
    url(r'^events/(?P<token>[^/]+)/(?P<pk>[0-9]+)$', views.event_detail),
    url(r'^events/(?P<token>[^/]+)/(?P<username>[\w|\W]+)$', views.event_detailuser),
    url(r'^events/(?P<token>[^/]+)/(?P<fromdate>\d*)/(?P<todate>\d*)$', views.event_requestdate),
    #Eventrequest gives back event information in function of date and distance, optional arguments are Number of Events (default = 100) and starting ID (default = 0)
    url(r'^events/(?P<token>[^/]+)/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))$', views.event_request),
    url(r'^events/(?P<token>[^/]+)/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))/(?P<mNE>\d*)$', views.event_request),
    url(r'^events/(?P<token>[^/]+)/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))/(?P<mNE>\d*)/(?P<mId>\d*)$', views.event_request),
]

urlpatterns = format_suffix_patterns(urlpatterns)
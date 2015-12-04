__author__ = 'Pujima'

from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from . import views


urlpatterns = [
    url(r'^events/$', views.event_list),
    url(r'^events/(?P<pk>[0-9]+)$', views.event_detail),
    url(r'^events/user/(?P<pk>[0-9]+)$', views.event_detailParticipant),
    url(r'^events/(?P<pk>[0-9]+)/(?P<pk2>[0-9]+)$', views.event_addparticipant),
    url(r'^events/(?P<fromdate>\d*)/(?P<todate>\d*)$', views.event_requestdate),
    url(r'^events/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))$', views.event_request),
    url(r'^events/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))/(?P<mNE>\d*)$', views.event_request),
    url(r'^events/(?P<fromdate>\d*)/(?P<todate>\d*)/(?P<mLongitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mLatitude>[-+]?([0-9]*\.[0-9]+|[0-9]+))/(?P<mDistance>([0-9]*\.[0-9]+|[0-9]+))/(?P<mNE>\d*)/(?P<mId>\d*)$', views.event_request),
    url(r'^events/comments/$', views.event_addcomment),
    url(r'^events/comments/(?P<pk>[0-9]+)$', views.commented_events),
]

urlpatterns = format_suffix_patterns(urlpatterns)
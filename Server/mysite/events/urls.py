__author__ = 'Pujima'

from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from . import views


urlpatterns = [
    url(r'^events/$', views.event_list),
    url(r'^events/(?P<pk>[0-9]+)$', views.event_detail),
]

urlpatterns = format_suffix_patterns(urlpatterns)
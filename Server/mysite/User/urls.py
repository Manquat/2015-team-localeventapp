__author__ = 'Pujima'

from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from . import views


urlpatterns = [
    url(r'^user/$', views.create_user),
    url(r'^user/(?P<pk>[0-9]+)$', views.update_user),
    url(r'^user/creator/(?P<pk>[0-9]+)$', views.created_events),
    url(r'^user/participant/(?P<pk>[0-9]+)$', views.joined_events),
]

urlpatterns = format_suffix_patterns(urlpatterns)
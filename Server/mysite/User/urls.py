__author__ = 'Pujima'

from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from . import views


urlpatterns = [
    url(r'^user/$', views.create_user),
    url(r'^user/(?P<pk>[0-9]+)$', views.update_user),
]

urlpatterns = format_suffix_patterns(urlpatterns)
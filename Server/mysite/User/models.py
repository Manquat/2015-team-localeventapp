from django.db import models
from django.db.models.fields import PositiveIntegerField


class participant(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=200, default='No Name!')
    email = models.CharField(max_length=200, default='00')
    google_id = models.CharField(max_length=200, default='noid')
    def __unicode__(self):
        return self.name

    class Meta:
        ordering = ('name',)
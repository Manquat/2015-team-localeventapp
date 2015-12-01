from django.db import models

# Create your models here.

class participant(models.Model):
    id = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=200, default='No Name!')
    email = models.CharField(max_length=200, default='00')
    def __unicode__(self):
        return self.name

    class Meta:
        ordering = ('name',)
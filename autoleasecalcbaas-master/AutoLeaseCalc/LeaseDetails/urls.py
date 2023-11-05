from django.conf.urls import url
from .views import LeaseAmtCalcView

urlpatterns = [
    url(r'^calc/?$', LeaseAmtCalcView.as_view()),
]
from django.conf.urls import url
from Customer.views import CustomerCreateProfileView, CustomerGetProfileView

urlpatterns = [
    url(r'^save/?$', CustomerCreateProfileView.as_view()),
    url(r'^get/?$', CustomerGetProfileView.as_view()),
]
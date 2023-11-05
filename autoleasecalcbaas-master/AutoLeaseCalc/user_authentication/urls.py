from django.conf.urls import url

from user_authentication.views import LoginAPIView, RegistrationAPIView

urlpatterns = [
    url(r'^user/register/?$', RegistrationAPIView.as_view()),
    url(r'^login/?$', LoginAPIView.as_view()),
]
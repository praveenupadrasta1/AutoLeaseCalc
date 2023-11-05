# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework import generics
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.response import Response

from .renderers import UserJSONRenderer

from AutoLeaseCalc.config import USER_KEY
from .serializers import RegistrationSerializer, LoginSerializer

# Create your views here.


class RegistrationAPIView(generics.GenericAPIView):

    # Allow any user (authenticated or not) to hit this endpoint.
    permission_classes = (AllowAny,)
    renderer_classes = (UserJSONRenderer,)

    def get_serializer_class(self):
        return RegistrationSerializer

    def post(self, request):
        # Get the data in the USER_KEY
        user = request.data.get(USER_KEY,{})

        serializer = self.get_serializer(data=user)
        # Validate the inputs using RegistrationSerializerr. If any invalid inputs, it will raise exception
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(serializer.data, status=status.HTTP_200_OK)


class LoginAPIView(generics.GenericAPIView):
    # Allow any user (authenticated or not) to hit this endpoint.
    permission_classes = (AllowAny,)
    renderer_classes = (UserJSONRenderer,)

    def get_serializer_class(self):
        return LoginSerializer

    def post(self, request):
        serializer = self.get_serializer(data=request.data)
        # Validate the inputs using LoginSerializer. If any invalid inputs, it will raise exception
        serializer.is_valid(raise_exception=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

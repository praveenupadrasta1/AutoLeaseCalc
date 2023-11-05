# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework import generics
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status

from .serializers import LeaseDetailSerializer
from AutoLeaseCalc.config import DATA_KEY
from LeaseDetails.utils.lease_utils import frame_lease_calc_data
# Create your views here.


class LeaseAmtCalcView(generics.GenericAPIView):

    # Only users who logged in can access this API
    permission_classes = (IsAuthenticated,)

    def get_serializer_class(self):
        return LeaseDetailSerializer

    def post(self, request):
        data = request.data
        serializer = self.get_serializer(data=data)
        # Validate the inputs using Lease Detail serializer. If any invalid inputs, it will raise exception
        serializer.is_valid(raise_exception=True)

        return Response({DATA_KEY: frame_lease_calc_data(input_data=data)}, status=status.HTTP_200_OK)

# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework import generics
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status

from .serializers import CustomerProfileSerializer
from utils.customer_utils import get_customer_record_by_email, get_customer_record_by_id, frame_customer_data
from .models import Customer
from AutoLeaseCalc.config import EMAIL_KEY, UNIQUE_KEY, DATA_KEY, CUSTOMER_KEY, LEASE_KEY
from LeaseDetails.models import Lease

# Create your views here.


class CustomerCreateProfileView(generics.GenericAPIView):

    # Only users who logged in can access this API
    permission_classes = (IsAuthenticated,)

    def get_serializer_class(self):
        return CustomerProfileSerializer

    def post(self, request):
        # Get Customer data from request using CUSTOMER_KEY
        cust_data = request.data.get(CUSTOMER_KEY)
        # Get Lease data from request using LEASE_KEY
        lease_data = request.data.get(LEASE_KEY)
        # The customer data is sent to serializer to validate the input values
        serializer = self.get_serializer(data=cust_data)
        # This method explicitly calls the serializer to validate the input values and thus raise exception on any
        # bad inputs
        serializer.is_valid(raise_exception=True)

        record = get_customer_record_by_email(unique_key=cust_data.get(EMAIL_KEY))
        # If customer record already exists (Checking using emailID) update the customer record
        # else if the customer didn't exists, create a new customer
        if record:
            customer = Customer.object.update(data=cust_data, customer_record=record)
            Lease.object.create(data=lease_data, customer=customer)
        else:
            customer = Customer.object.create(data=cust_data)
            Lease.object.create(data=lease_data, customer=customer)

        return Response({DATA_KEY: customer.id}, status=status.HTTP_200_OK)


class CustomerGetProfileView(generics.GenericAPIView):

    # Only users who logged in can access this API
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        # This view searches customer either using emailID or Unique Customer ID
        data = request.data
        # Check if customer exists (Checking using emailID
        record = get_customer_record_by_email(unique_key=data.get(UNIQUE_KEY))
        if record:
            # IF customer record exists return customer record
            customer = frame_customer_data(customer_record=record)
            return Response({DATA_KEY: customer}, status=status.HTTP_200_OK)
        else:
            # Checking if customer exists while searched using UNIQUE CUSTOMER ID
            record = get_customer_record_by_id(unique_key=data.get(UNIQUE_KEY))
            if record:
                #If customer record exists return the customer
                customer = frame_customer_data(customer_record=record)
                return Response({DATA_KEY: customer}, status=status.HTTP_200_OK)

        return Response({DATA_KEY: None}, status=status.HTTP_400_BAD_REQUEST)
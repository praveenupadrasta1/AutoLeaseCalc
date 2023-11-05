from rest_framework import serializers

from AutoLeaseCalc.config import STICKER_PRICE_KEY, RESIDUAL_PERCENT_KEY, NEG_SALES_PRICE_KEY, DOWN_PAYMENT_KEY, \
    LEASE_ACQ_FEE_KEY, DOC_TIRE_FEE_KEY, MONEY_FACTOR_KEY, LEASE_MONTHS_KEY, TAX_RATE_KEY, REG_FEE_KEY, TOT_LEASE_WITH_TAX_KEY, \
    INVALID_DATA


class LeaseDetailSerializer(serializers.Serializer):
    sticker_price = serializers.IntegerField(required=True, allow_null=False)
    residual_percent = serializers.FloatField(required=True, allow_null=False)
    neg_sales_price = serializers.IntegerField(required=True, allow_null=False)
    down_payment = serializers.IntegerField(required=True, allow_null=False)
    lease_acq_fee = serializers.IntegerField(required=True, allow_null=False)
    doc_tire_fee = serializers.IntegerField(required=True, allow_null=False)
    money_factor = serializers.FloatField(required=True, allow_null=False)
    lease_months = serializers.IntegerField(required=True, allow_null=False)
    tax_rate = serializers.FloatField(required=True, allow_null=False)
    reg_fee = serializers.IntegerField(required=True, allow_null=False)
    tot_lease_with_tax = serializers.FloatField(required=False, allow_null=True)

    def validate(self, data):
        if data.get(STICKER_PRICE_KEY) > 0 and \
            data.get(RESIDUAL_PERCENT_KEY) > 0 and \
            data.get(NEG_SALES_PRICE_KEY) >= 0 and \
            data.get(DOWN_PAYMENT_KEY) >= 0 and \
            data.get(LEASE_ACQ_FEE_KEY) >= 0 and \
            data.get(DOC_TIRE_FEE_KEY) >= 0 and \
            data.get(MONEY_FACTOR_KEY) > 0 and \
            data.get(LEASE_MONTHS_KEY) > 0 and \
            data.get(TAX_RATE_KEY) >= 0 and \
            data.get(REG_FEE_KEY) >= 0:
            return data
        else:
            raise serializers.ValidationError(INVALID_DATA)

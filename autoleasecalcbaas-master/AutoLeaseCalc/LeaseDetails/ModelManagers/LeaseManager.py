from django.db import models
import logging
logger = logging.getLogger(__name__)

from AutoLeaseCalc.config import STICKER_PRICE_KEY, RESIDUAL_PERCENT_KEY, NEG_SALES_PRICE_KEY, DOWN_PAYMENT_KEY, \
    LEASE_ACQ_FEE_KEY, DOC_TIRE_FEE_KEY, MONEY_FACTOR_KEY, LEASE_MONTHS_KEY, TAX_RATE_KEY, REG_FEE_KEY, TOT_LEASE_WITH_TAX_KEY


class LeaseManager(models.Manager):

    def create(self, data, customer):
        # Create lease details for a customer
        lease_details = self.model(customer         = customer,
                                   sticker_price    = data.get(STICKER_PRICE_KEY),
                                   residual_percent = data.get(RESIDUAL_PERCENT_KEY),
                                   neg_sales_price  = data.get(NEG_SALES_PRICE_KEY),
                                   down_payment     = data.get(DOWN_PAYMENT_KEY),
                                   lease_acq_fee    = data.get(LEASE_ACQ_FEE_KEY),
                                   doc_tire_fee     = data.get(DOC_TIRE_FEE_KEY),
                                   money_factor     = data.get(MONEY_FACTOR_KEY),
                                   lease_months     = data.get(LEASE_MONTHS_KEY),
                                   tax_rate         = data.get(TAX_RATE_KEY),
                                   reg_fee          = data.get(REG_FEE_KEY),
                                   tot_lease_with_tax = data.get(TOT_LEASE_WITH_TAX_KEY))
        lease_details.save()
        return lease_details

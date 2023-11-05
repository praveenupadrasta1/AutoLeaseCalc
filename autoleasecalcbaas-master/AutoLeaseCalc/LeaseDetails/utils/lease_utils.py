from AutoLeaseCalc.config import STICKER_PRICE_KEY, RESIDUAL_PERCENT_KEY, NEG_SALES_PRICE_KEY, DOWN_PAYMENT_KEY, \
    LEASE_ACQ_FEE_KEY, DOC_TIRE_FEE_KEY, MONEY_FACTOR_KEY, LEASE_MONTHS_KEY, TAX_RATE_KEY, REG_FEE_KEY, TOT_LEASE_WITH_TAX_KEY, \
    INPUT_VALUES_KEY, RESIDUAL_VALUE_KEY, TOT_FEE_KEY, ADJ_CAP_COST_KEY, DEP_AMT_KEY, BASE_PAY_KEY, MONTH_RENT_KEY, \
    TOT_LEASE_WITHOUT_TAX_KEY, DRIVE_OFF_KEY, TOT_COST_OF_LEASE_KEY, OUTPUT_VALUES_KEY


def get_residual_value(sticker_price, residual_percent):
    return round(float(sticker_price) * float(residual_percent)/100, 2)


def get_tot_fee(lease_acq_fee, doc_tire_fee):
    return round(lease_acq_fee + doc_tire_fee, 2)


def get_adj_cap_cost(neg_sales_price, down_payment, tot_fee):
    return round(neg_sales_price - down_payment + tot_fee, 2)


def get_dep_amt(sticker_price, adj_cap_cost):
    return round(sticker_price - adj_cap_cost, 2)


def get_base_payment(dep_amt, lease_months):
    return round(float(dep_amt)/float(lease_months-1), 2)


def get_monthly_rent(adj_cap_cost, residual_val, money_factor):
    return round((adj_cap_cost + residual_val) * money_factor, 2)


def get_tot_lease_without_tax(base_amt, monthly_rent):
    return round(base_amt + monthly_rent, 2)


def get_tot_lease_with_tax(base_amt, monthly_rent):
    return round((base_amt + monthly_rent) * 1.06, 2)


def get_drive_off(tot_lease_without_tax, tot_fee):
    return round(tot_lease_without_tax + tot_fee, 2)


def get_tot_cost_of_lease(total_lease_without_tax):
    return round(28*total_lease_without_tax, 2)


def frame_lease_calc_data(input_data):
    response_data = dict()
    response_data[INPUT_VALUES_KEY] = input_data

    output = dict()
    output[RESIDUAL_VALUE_KEY] = get_residual_value(input_data.get(STICKER_PRICE_KEY), input_data.get(RESIDUAL_PERCENT_KEY))
    output[TOT_FEE_KEY] = get_tot_fee(input_data.get(LEASE_ACQ_FEE_KEY), input_data.get(DOC_TIRE_FEE_KEY))
    output[ADJ_CAP_COST_KEY] = get_adj_cap_cost(input_data.get(NEG_SALES_PRICE_KEY), input_data.get(DOWN_PAYMENT_KEY), output[TOT_FEE_KEY])
    output[DEP_AMT_KEY] = get_dep_amt(input_data.get(STICKER_PRICE_KEY), output[ADJ_CAP_COST_KEY])
    output[BASE_PAY_KEY] = get_base_payment(output[DEP_AMT_KEY], input_data.get(LEASE_MONTHS_KEY))
    output[MONTH_RENT_KEY] = get_monthly_rent(output[ADJ_CAP_COST_KEY], output[RESIDUAL_VALUE_KEY], input_data.get(MONEY_FACTOR_KEY))
    output[TOT_LEASE_WITHOUT_TAX_KEY] = get_tot_lease_without_tax(output[BASE_PAY_KEY], output[MONTH_RENT_KEY])
    output[TOT_LEASE_WITH_TAX_KEY] = get_tot_lease_with_tax(output[BASE_PAY_KEY], output[MONTH_RENT_KEY])
    output[DRIVE_OFF_KEY] = get_drive_off(output[TOT_LEASE_WITHOUT_TAX_KEY], output[TOT_FEE_KEY])
    output[TOT_COST_OF_LEASE_KEY] = get_tot_cost_of_lease(output[TOT_LEASE_WITHOUT_TAX_KEY])

    response_data[OUTPUT_VALUES_KEY] = output

    return response_data


package com.airtribe.meditrack.strategy;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.util.Validator;

import java.math.BigDecimal;

/** Applies the configurable default discount before tax is calculated. */
public class DiscountedBillingStrategy implements BillingStrategy {
    @Override
    public BillingType getBillingType() {
        return BillingType.DISCOUNTED;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal consultationFee) {
        BigDecimal fee = Validator.requirePositiveAmount(consultationFee, "Consultation fee");
        return fee.multiply(Constants.DEFAULT_DISCOUNT_RATE);
    }
}

package com.airtribe.meditrack.strategy;

import com.airtribe.meditrack.constants.BillingType;

import java.math.BigDecimal;

/** Charges the doctor's full consultation fee. */
public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public BillingType getBillingType() {
        return BillingType.STANDARD;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal consultationFee) {
        return BigDecimal.ZERO;
    }
}

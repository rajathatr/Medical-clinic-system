package com.airtribe.meditrack.strategy;

import com.airtribe.meditrack.constants.BillingType;

import java.math.BigDecimal;

/** Calculates the discount component of a bill. */
public interface BillingStrategy {
    BillingType getBillingType();

    BigDecimal calculateDiscount(BigDecimal consultationFee);
}

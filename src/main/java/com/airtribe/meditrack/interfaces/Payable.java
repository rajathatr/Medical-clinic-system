package com.airtribe.meditrack.interfaces;

import com.airtribe.meditrack.entity.BillSummary;

import java.math.BigDecimal;

/** Marks an object that can produce a payable bill summary. */
public interface Payable {
    BillSummary generateBill();

    default BigDecimal getPayableAmount() {
        return generateBill().getTotalAmount();
    }
}

package com.airtribe.meditrack.constants;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final String APPLICATION_NAME = "MediTrack";
    public static final BigDecimal TAX_RATE = new BigDecimal("0.05");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private Constants() {
    }
}

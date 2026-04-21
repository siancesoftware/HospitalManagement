package com.siance.hm.common.util;

import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;

public class UHIDGenerator {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    private UHIDGenerator() {}

    public static String generate(String prefix, long sequenceNumber) {
        return String.format("%s-%d-%06d", prefix, Year.now().getValue(), sequenceNumber);
    }
}

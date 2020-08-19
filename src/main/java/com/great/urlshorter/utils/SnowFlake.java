package com.great.urlshorter.utils;

import org.springframework.stereotype.Component;

/**
 * @author Great
 */
public class SnowFlake {

    private final static long TWEPOCH = 1597737021000L;

    private final static long WORKER_ID_BITS = 5L;

    private final static long DATACENTER_ID_BITS = 5L;

    private final static long MAX_WORKER_ID = ~(-1 << WORKER_ID_BITS);

    private final static long MAX_DATACENTER_ID = ~(-1 << DATACENTER_ID_BITS);

    private final static long SEQUENCE_BITS = 12L;

    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private final static long SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);

    private final long workerId;

    private final long dataCenterId;

    private long sequence = 0;

    private long lastTimestamp = -1L;

    public SnowFlake(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }

        if (dataCenterId > MAX_DATACENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMills(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    public static long tilNextMills(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    public static long timeGen() {
        return System.currentTimeMillis();
    }
}

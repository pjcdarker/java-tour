package com.pjcdarker.util.ratelimit;

import com.pjcdarker.util.rate.RequestLog;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitUtil {

    private static final int RATE_LIMIT_MAX = 100;
    private static final int TIME = 60 * 1000;

    private static ConcurrentMap<String, RequestLog> requestRateLimitMap = new ConcurrentHashMap<>();

    public static boolean isExpired(String secret) {
        RequestLog requestLog = requestRateLimitMap.get(secret);
        if (requestLog == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long timestamp = requestLog.getTimestamp();
        long time = currentTimeMillis - timestamp;
        if (time > requestLog.getExpired()) {
            return true;
        }
        return false;
    }

    public static boolean isLimit(String secret) {
        RequestLog requestLog = requestRateLimitMap.get(secret);
        long currentTimeMillis = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger();

        boolean isLimit = false;
        if (requestLog == null) {
            requestLog = new RequestLog();
            requestLog.setSecret(secret);
            requestLog.setIp("");
            requestLog.setExpired(60 * 60 * 1000);
        } else {
            long timestamp = requestLog.getTimestamp();
            long time = currentTimeMillis - timestamp;
            if (time > requestLog.getExpired()) {
                return true;
            }
            if (time < TIME) {
                count = requestLog.getCount();
                if (count.get() < RATE_LIMIT_MAX) {
                    isLimit = false;
                }
            }
        }

        if (!isLimit) {
            requestLog.setCount(new AtomicInteger(count.incrementAndGet()));
            requestLog.setTimestamp(currentTimeMillis);
        }
        requestRateLimitMap.put(secret, requestLog);
        return isLimit;
    }
}

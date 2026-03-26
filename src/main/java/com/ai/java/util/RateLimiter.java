package com.ai.java.util;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final Map<String, List<Long>> userRequests = new ConcurrentHashMap<>();
    private static final int LIMIT = 10; // 10 req/min

    public boolean isAllowed(String userId) {
        long now = System.currentTimeMillis();

        userRequests.putIfAbsent(userId, new ArrayList<>());

        List<Long> timestamps = userRequests.get(userId);

        // remove old (older than 1 min)
        timestamps.removeIf(time -> now - time > 60000);

        if (timestamps.size() >= LIMIT) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}
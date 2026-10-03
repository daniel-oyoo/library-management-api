package com.daniel.library_management.limiter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TieredRateLimiter {
    private final ProcessingTier root;
    private final Map<ProcessingTier, AtomicInteger> remainingCapacity = new ConcurrentHashMap<>();

    public TieredRateLimiter(ProcessingTier root) {
        this.root = root;
        registerTiers(root);
    }

    public Result process(int requestCount) {
        if (requestCount < 0) {
            throw new IllegalArgumentException("requestCount must not be negative");
        }

        Map<String, Integer> allocations = new LinkedHashMap<>();
        int unhandled = processAtTier(requestCount, root, allocations);
        return new Result(allocations, unhandled);
    }

    @Scheduled(fixedRateString = "${app.rate-limit.window-ms:60000}")
    public void reset() {
        remainingCapacity.forEach((tier, remaining) -> remaining.set(tier.capacity()));
    }

    private void registerTiers(ProcessingTier tier) {
        remainingCapacity.put(tier, new AtomicInteger(tier.capacity()));
        tier.nextOptions().forEach(this::registerTiers);
    }

    private int processAtTier(int requests, ProcessingTier tier, Map<String, Integer> allocations) {
        int reserved = reserve(tier, requests);
        if (reserved > 0) {
            allocations.merge(tier.name(), reserved, Integer::sum);
        }

        int unhandled = requests - reserved;
        for (ProcessingTier next : tier.nextOptions()) {
            if (unhandled == 0) {
                break;
            }
            unhandled = processAtTier(unhandled, next, allocations);
        }
        return unhandled;
    }

    private int reserve(ProcessingTier tier, int requests) {
        AtomicInteger remaining = remainingCapacity.get(tier);
        while (requests > 0) {
            int available = remaining.get();
            if (available == 0) {
                return 0;
            }
            int reserved = Math.min(requests, available);
            if (remaining.compareAndSet(available, available - reserved)) {
                return reserved;
            }
        }
        return 0;
    }

    public record Result(Map<String, Integer> allocations, int unhandledRequests) {
        public Result {
            allocations = Map.copyOf(allocations);
        }

        public String firstTierName() {
            return allocations.keySet().stream().findFirst().orElse(null);
        }
    }
}
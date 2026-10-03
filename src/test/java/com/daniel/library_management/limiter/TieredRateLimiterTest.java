package com.daniel.library_management.limiter;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TieredRateLimiterTest {
    @Test
    @DisplayName("Greedily allocates a burst across tiers and reports unhandled requests")
    void allocatesRequestsAcrossAvailableTiers() {
        ProcessingTier slow = new ProcessingTier("slow", 2, List.of());
        ProcessingTier fast = new ProcessingTier("fast", 1, List.of(slow));
        TieredRateLimiter limiter = new TieredRateLimiter(fast);

        TieredRateLimiter.Result result = limiter.process(4);

        assertThat(result.allocations()).containsEntry("fast", 1).containsEntry("slow", 2);
        assertThat(result.unhandledRequests()).isEqualTo(1);
    }

    @Test
    @DisplayName("Reset restores every tier capacity")
    void resetRestoresTierCapacity() {
        ProcessingTier tier = new ProcessingTier("fast", 1, List.of());
        TieredRateLimiter limiter = new TieredRateLimiter(tier);

        limiter.process(1);
        limiter.reset();

        assertThat(limiter.process(1).unhandledRequests()).isZero();
    }

    @Test
    @DisplayName("Concurrent requests cannot exceed the configured capacity")
    void concurrentRequestsRespectCapacity() {
        TieredRateLimiter limiter = new TieredRateLimiter(
            new ProcessingTier("fast", 10, List.of()));

        long accepted = IntStream.range(0, 1000)
            .parallel()
            .filter(index -> limiter.process(1).unhandledRequests() == 0)
            .count();

        assertThat(accepted).isEqualTo(10);
    }
}
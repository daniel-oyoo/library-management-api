package com.daniel.library_management.config;

import com.daniel.library_management.limiter.ProcessingTier;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessingTierConfiguration {
    @Bean
    ProcessingTier processingTier(
            @Value("${app.rate-limit.tiers.instant-capacity:1000}") int instantCapacity,
            @Value("${app.rate-limit.tiers.queue-capacity:5000}") int queueCapacity,
            @Value("${app.rate-limit.tiers.manual-capacity:10000}") int manualCapacity) {
        ProcessingTier manual = new ProcessingTier("Tier 3 - Manual / Slow Buffer", manualCapacity, List.of());
        ProcessingTier queue = new ProcessingTier("Tier 2 - Async Queue Buffer", queueCapacity, List.of(manual));
        return new ProcessingTier("Tier 1 - Instant / Direct Processing", instantCapacity, List.of(queue));
    }
}
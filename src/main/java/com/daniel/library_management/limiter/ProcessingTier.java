package com.daniel.library_management.limiter;

import java.util.List;
import java.util.Objects;

public record ProcessingTier(String name, int capacity, List<ProcessingTier> nextOptions) {
    public ProcessingTier {
        Objects.requireNonNull(name, "name");
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must not be negative");
        }
        nextOptions = List.copyOf(nextOptions);
    }
}
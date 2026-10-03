package com.daniel.library_management.web;

import com.daniel.library_management.limiter.TieredRateLimiter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    public static final String RATE_LIMIT_TIER = "RATE_LIMIT_TIER";

    private final TieredRateLimiter rateLimiter;
    private final ObjectMapper objectMapper;
    private final int retryAfterSeconds;

    public RateLimitInterceptor(
            TieredRateLimiter rateLimiter,
            ObjectMapper objectMapper,
            @Value("${app.rate-limit.retry-after-seconds:60}") int retryAfterSeconds) {
        this.rateLimiter = rateLimiter;
        this.objectMapper = objectMapper;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        TieredRateLimiter.Result result = rateLimiter.process(1);
        if (result.unhandledRequests() > 0) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Retry-After", Integer.toString(retryAfterSeconds));
            objectMapper.writeValue(response.getWriter(), Map.of(
                "error", "Too Many Requests",
                "message", "System saturated. Retry later."));
            return false;
        }

        request.setAttribute(RATE_LIMIT_TIER, result.firstTierName());
        return true;
    }
}
package com.daniel.library_management;

import com.daniel.library_management.limiter.TieredRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "app.rate-limit.tiers.instant-capacity=1",
    "app.rate-limit.tiers.queue-capacity=1",
    "app.rate-limit.tiers.manual-capacity=1",
    "app.rate-limit.window-ms=3600000"
})
@AutoConfigureMockMvc
class RateLimitInterceptorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TieredRateLimiter rateLimiter;

    @BeforeEach
    void resetRateLimitWindow() {
        rateLimiter.reset();
    }

    @Test
    @DisplayName("Versioned endpoint returns the selected processing tier")
    void versionedEndpointIncludesTierMetadata() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.rateLimitTier").value("Tier 1 - Instant / Direct Processing"));
    }

    @Test
    @DisplayName("Requests consume each tier before returning the saturation response")
    void burstUsesTiersThenReturnsTooManyRequests() throws Exception {
        expectTier("Tier 1 - Instant / Direct Processing");
        expectTier("Tier 2 - Async Queue Buffer");
        expectTier("Tier 3 - Manual / Slow Buffer");

        mockMvc.perform(get("/api/v1/books"))
            .andExpect(status().isTooManyRequests())
            .andExpect(header().string("Retry-After", "60"))
            .andExpect(content().json("""
                {"error":"Too Many Requests","message":"System saturated. Retry later."}
                """));
    }

    @Test
    @DisplayName("Invalid book requests return HTTP 400")
    void invalidBookReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"title":"","author":"Author","isbn":"123","publicationYear":2020}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Request validation failed"));
    }

    @Test
    @DisplayName("Legacy endpoint routes remain available")
    void legacyEndpointRemainsAvailable() throws Exception {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").doesNotExist());
    }

    private void expectTier(String tier) throws Exception {
        mockMvc.perform(get("/api/v1/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rateLimitTier").value(tier));
    }
}
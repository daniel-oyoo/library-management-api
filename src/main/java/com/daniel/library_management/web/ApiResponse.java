package com.daniel.library_management.web;

public record ApiResponse<T>(T data, String rateLimitTier) {
}
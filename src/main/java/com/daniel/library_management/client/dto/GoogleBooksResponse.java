package com.daniel.library_management.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleBooksResponse {
    private List<GoogleBookItem> items;
}

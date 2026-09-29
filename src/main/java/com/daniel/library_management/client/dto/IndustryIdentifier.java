package com.daniel.library_management.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndustryIdentifier {
    private String type;
    private String identifier;
}

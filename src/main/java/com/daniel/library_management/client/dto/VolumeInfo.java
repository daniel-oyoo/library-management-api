package com.daniel.library_management.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publishedDate;
    private List<IndustryIdentifier> industryIdentifiers;
    private Integer pageCount;
    private String description;
}

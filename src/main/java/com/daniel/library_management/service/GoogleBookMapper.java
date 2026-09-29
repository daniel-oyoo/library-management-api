package com.daniel.library_management.service;

import com.daniel.library_management.client.dto.GoogleBookItem;
import com.daniel.library_management.client.dto.IndustryIdentifier;
import com.daniel.library_management.client.dto.VolumeInfo;
import com.daniel.library_management.model.Book;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GoogleBookMapper {

    public Book toBook(GoogleBookItem item) {
        if (item == null || item.getVolumeInfo() == null) {
            return null;
        }

        VolumeInfo volumeInfo = item.getVolumeInfo();
        Book book = new Book();
        book.setId(item.getId());
        book.setTitle(volumeInfo.getTitle());
        book.setAuthor(getAuthor(volumeInfo.getAuthors()));
        book.setIsbn(getIsbn(volumeInfo.getIndustryIdentifiers()));
        book.setPublicationYear(parsePublicationYear(volumeInfo.getPublishedDate()));
        book.setAvailable(true);
        book.setAddedDate(LocalDate.now());
        book.setSource("google");
        return book;
    }

    private String getAuthor(List<String> authors) {
        if (authors == null || authors.isEmpty()) {
            return "Unknown";
        }
        return authors.get(0);
    }

    private String getIsbn(List<IndustryIdentifier> identifiers) {
        if (identifiers == null || identifiers.isEmpty()) {
            return null;
        }

        for (IndustryIdentifier identifier : identifiers) {
            if (identifier == null) {
                continue;
            }
            if ("ISBN_13".equalsIgnoreCase(identifier.getType())) {
                return identifier.getIdentifier();
            }
        }

        for (IndustryIdentifier identifier : identifiers) {
            if (identifier == null) {
                continue;
            }
            if ("ISBN_10".equalsIgnoreCase(identifier.getType())) {
                return identifier.getIdentifier();
            }
        }

        return identifiers.get(0).getIdentifier();
    }

    private Integer parsePublicationYear(String publishedDate) {
        if (publishedDate == null || publishedDate.isBlank()) {
            return null;
        }
        if (publishedDate.length() >= 4) {
            String year = publishedDate.substring(0, 4);
            if (year.chars().allMatch(Character::isDigit)) {
                return Integer.parseInt(year);
            }
        }
        return null;
    }
}

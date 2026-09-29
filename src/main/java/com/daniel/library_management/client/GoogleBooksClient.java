package com.daniel.library_management.client;

import com.daniel.library_management.client.dto.GoogleBookItem;
import com.daniel.library_management.client.dto.GoogleBooksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleBooks", url = "${google.books.base-url}")
public interface GoogleBooksClient {

    @GetMapping("/volumes")
    GoogleBooksResponse searchBooks(@RequestParam("q") String query,
                                   @RequestParam(value = "key", required = false) String apiKey);

    @GetMapping("/volumes/{volumeId}")
    GoogleBookItem getBookById(@PathVariable("volumeId") String volumeId,
                              @RequestParam(value = "key", required = false) String apiKey);
}

package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaginationResponseTest {

    @Test
    void testAllArgsConstructor() {
        // Given
        List<String> content = new ArrayList<>();
        content.add("Item 1");
        content.add("Item 2");
        content.add("Item 3");

        CustomPageable pageable = new CustomPageable(1, 10, 2, 25);

        // When
        PaginationResponse<String> response = new PaginationResponse<>(content, pageable);

        // Then
        assertNotNull(response);
        assertEquals(content, response.getContent(), "Getter for content did not work correctly");
        assertEquals(pageable, response.getPageable(), "Getter for pageable did not work correctly");
    }

    @Test
    void testCustomConstructor() {
        // Given
        List<String> content = new ArrayList<>();
        content.add("Item 1");
        content.add("Item 2");

        Pageable pageable = PageRequest.of(1, 10);
        Page<String> page = new PageImpl<>(content, pageable, 25);

        // When
        PaginationResponse<String> response = new PaginationResponse<>(content, page);

        // Then
        assertNotNull(response);
        assertEquals(content, response.getContent(), "Getter for content did not work correctly");
        assertEquals(pageable.getPageNumber(), response.getPageable().getPageNumber(),
                "Getter for pageable pageNumber did not work correctly");
        assertEquals(pageable.getPageSize(), response.getPageable().getPageSize(),
                "Getter for pageable pageSize did not work correctly");
        assertEquals(page.getTotalPages(), response.getPageable().getTotalPages(),
                "Getter for pageable totalPages did not work correctly");
        assertEquals(page.getTotalElements(), response.getPageable().getTotalElements(),
                "Getter for pageable totalElements did not work correctly");
    }

    @Test
    void testNoArgsConstructor() {
        // When
        PaginationResponse<String> response = new PaginationResponse<>();
        response.setContent(new ArrayList<>());
        response.setPageable(new CustomPageable());

        // Then
        assertNotNull(response, "NoArgsConstructor should initialize PaginationResponse object");
        assertNotNull(response.getContent(), "Content should be initialized");
        assertNotNull(response.getPageable(), "Pageable should be initialized");
        assertEquals(0, response.getContent().size(), "Content list should be empty");
    }
}

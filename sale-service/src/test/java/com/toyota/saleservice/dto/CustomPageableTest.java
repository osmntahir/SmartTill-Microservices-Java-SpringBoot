package com.toyota.saleservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomPageableTest {

    @Test
    void testNoArgsConstructor() {
        // When
        CustomPageable pageable = new CustomPageable();

        // Then
        assertEquals(0, pageable.getPageNumber(), "Default pageNumber should be 0");
        assertEquals(0, pageable.getPageSize(), "Default pageSize should be 0");
        assertEquals(0, pageable.getTotalPages(), "Default totalPages should be 0");
        assertEquals(0L, pageable.getTotalElements(), "Default totalElements should be 0");
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        int pageNumber = 1;
        int pageSize = 20;
        int totalPages = 5;
        long totalElements = 100;

        // When
        CustomPageable pageable = new CustomPageable(pageNumber, pageSize, totalPages, totalElements);

        // Then
        assertEquals(pageNumber, pageable.getPageNumber(), "Page number should be initialized correctly");
        assertEquals(pageSize, pageable.getPageSize(), "Page size should be initialized correctly");
        assertEquals(totalPages, pageable.getTotalPages(), "Total pages should be initialized correctly");
        assertEquals(totalElements, pageable.getTotalElements(), "Total elements should be initialized correctly");
    }
    @Test
    void testDataAnnotion()
    {
        // Given
        int pageNumber = 1;
        int pageSize = 20;
        int totalPages = 5;
        long totalElements = 100;

        // When
        CustomPageable pageable = new CustomPageable();
        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);
        pageable.setTotalPages(totalPages);
        pageable.setTotalElements(totalElements);

        // Then
        assertEquals(pageNumber, pageable.getPageNumber(), "Page number should be set correctly");
        assertEquals(pageSize, pageable.getPageSize(), "Page size should be set correctly");
        assertEquals(totalPages, pageable.getTotalPages(), "Total pages should be set correctly");
        assertEquals(totalElements, pageable.getTotalElements(), "Total elements should be set correctly");
    }
}

package com.toyota.saleservice.service.common;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {

    @Test
    void createSortOrder_shouldReturnAscendingOrder() {
        List<String> sortList = Arrays.asList("name", "price");
        List<Sort.Order> orders = SortUtil.createSortOrder(sortList, "Asc");

        assertEquals(2, orders.size());
        assertEquals(Sort.Direction.ASC, orders.get(0).getDirection());
        assertEquals("name", orders.get(0).getProperty());
        assertEquals(Sort.Direction.ASC, orders.get(1).getDirection());
        assertEquals("price", orders.get(1).getProperty());
    }

    @Test
    void createSortOrder_shouldReturnDescendingOrder() {
        List<String> sortList = Arrays.asList("name", "price");
        List<Sort.Order> orders = SortUtil.createSortOrder(sortList, "Desc");

        assertEquals(2, orders.size());
        assertEquals(Sort.Direction.DESC, orders.get(0).getDirection());
        assertEquals("name", orders.get(0).getProperty());
        assertEquals(Sort.Direction.DESC, orders.get(1).getDirection());
        assertEquals("price", orders.get(1).getProperty());
    }

    @Test
    void createSortOrder_shouldHandleNullSortDirection() {
        List<String> sortList = Arrays.asList("name", "price");
        List<Sort.Order> orders = SortUtil.createSortOrder(sortList, null);

        assertEquals(2, orders.size());
        assertEquals(Sort.Direction.DESC, orders.get(0).getDirection()); // Default to DESC
        assertEquals("name", orders.get(0).getProperty());
        assertEquals(Sort.Direction.DESC, orders.get(1).getDirection());
        assertEquals("price", orders.get(1).getProperty());
    }

    @Test
    void createSortOrder_shouldHandleEmptySortList() {
        List<Sort.Order> orders = SortUtil.createSortOrder(Arrays.asList(), "Asc");

        assertTrue(orders.isEmpty());
    }
}

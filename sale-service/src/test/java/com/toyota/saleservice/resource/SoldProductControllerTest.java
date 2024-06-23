package com.toyota.saleservice.resource;

import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.productservice.dto.ProductDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.service.abstracts.SoldProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {SoldProductController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SoldProductControllerTest {
    @Autowired
    private SoldProductController soldProductController;

    @MockBean
    private SoldProductService soldProductService;

    @Test
    void testAddSoldProduct() throws Exception {
        // Arrange
        when(soldProductService.addSoldProduct(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<SoldProductDto>any()))
                .thenReturn(new SoldProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setActive(true);
        productDto.setDescription("The characteristics of someone or something");
        productDto.setId(1L);
        productDto.setInventory(1);
        productDto.setName("Name");
        productDto.setPrice(10.0d);

        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setDeleted(true);
        soldProductDto.setDiscount(10.0d);
        soldProductDto.setId(1L);
        soldProductDto.setProductDto(productDto);
        soldProductDto.setQuantity(1);
        soldProductDto.setTotal(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(soldProductDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/sold-products/add/{productId}/{saleId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(soldProductController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"productDto\":null,\"discount\":0.0,\"total\":0.0,\"quantity\":0,\"deleted\":false}"));
    }

    @Test
    void testUpdateSoldProduct() throws Exception {
        // Arrange
        when(soldProductService.updateSoldProduct(Mockito.<Long>any(), Mockito.<SoldProductDto>any()))
                .thenReturn(new SoldProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setActive(true);
        productDto.setDescription("The characteristics of someone or something");
        productDto.setId(1L);
        productDto.setInventory(1);
        productDto.setName("Name");
        productDto.setPrice(10.0d);

        SoldProductDto soldProductDto = new SoldProductDto();
        soldProductDto.setDeleted(true);
        soldProductDto.setDiscount(10.0d);
        soldProductDto.setId(1L);
        soldProductDto.setProductDto(productDto);
        soldProductDto.setQuantity(1);
        soldProductDto.setTotal(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(soldProductDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/sold-products/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(soldProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"productDto\":null,\"discount\":0.0,\"total\":0.0,\"quantity\":0,\"deleted\":false}"));
    }


    @Test
    void testDeleteSoldProduct() throws Exception {
        // Arrange
        when(soldProductService.deleteSoldProduct(Mockito.<Long>any())).thenReturn(new SoldProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/sold-products/delete/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(soldProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"productDto\":null,\"discount\":0.0,\"total\":0.0,\"quantity\":0,\"deleted\":false}"));
    }

   
    @Test
    void testGetAllSoldProducts() throws Exception {
        // Arrange
        when(soldProductService.getSoldProducts(anyInt(), anyInt(), Mockito.<String>any(), Mockito.<Double>any(),
                Mockito.<Double>any(), anyBoolean(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new PaginationResponse<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/sold-products/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("deleted", String.valueOf(true)).param("name", "foo");
        MockHttpServletRequestBuilder paramResult2 = paramResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult2.param("size", String.valueOf(1))
                .param("sortBy", "foo")
                .param("sortDirection", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(soldProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"content\":null,\"pageable\":null}"));
    }
}

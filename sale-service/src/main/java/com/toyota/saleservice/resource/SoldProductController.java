package com.toyota.saleservice.resource;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.service.abstracts.SoldProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sold-products")
@RequiredArgsConstructor

public class SoldProductController {

    private final SoldProductService soldProductService;

    @GetMapping("/getAll")
    public PaginationResponse <SoldProductDto> getAllSoldProducts
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "") String name,
             @RequestParam(required = false, defaultValue = "0") Double minPrice,
             @RequestParam(required = false, defaultValue = Double.MAX_VALUE + "") Double maxPrice,
             @RequestParam(defaultValue = "false") boolean deleted,
             @RequestParam(defaultValue = "name") String sortBy,
             @RequestParam(defaultValue = "ASC") String sortDirection)
    {
        return soldProductService.getSoldProducts(page, size, name, minPrice, maxPrice, deleted, sortBy, sortDirection);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity <SoldProductDto> addSoldProduct(
            @PathVariable("productId") Long productId ,
            @RequestBody @Valid SoldProductDto soldProductDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(soldProductService.addSoldProduct(productId, soldProductDto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity <SoldProductDto> updateSoldProduct(
            @PathVariable Long id,
            @RequestBody @Valid SoldProductDto soldProductDto)
    {
        return ResponseEntity.ok(soldProductService.updateSoldProduct(id, soldProductDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity <SoldProductDto> deleteSoldProduct(@PathVariable Long id)
    {
        return ResponseEntity.ok(soldProductService.deleteSoldProduct(id));
    }
}
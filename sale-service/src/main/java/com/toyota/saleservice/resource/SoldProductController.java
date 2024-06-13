package com.toyota.saleservice.resource;

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
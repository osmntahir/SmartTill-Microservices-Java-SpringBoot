package com.toyota.saleservice.resource;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.service.abstracts.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping("/getAll")
    public PaginationResponse<SaleDto> getAllSales(@RequestParam(defaultValue = "") int page,
                                                   @RequestParam(defaultValue = "") int size,
                                                   @RequestParam(defaultValue = "") double totalPrice,
                                                   @RequestParam(defaultValue = "") LocalDateTime date,
                                                   @RequestParam(defaultValue = "") String paymentType,
                                                   @RequestParam(defaultValue = "false") boolean deleted,
                                                   @RequestParam(defaultValue = "") List<String> sortBy,
                                                   @RequestParam(defaultValue = "ASC") String sortOrder)
    {
        return saleService.getSalesFiltered(page,size,sortBy, sortOrder, totalPrice,
                date, paymentType, deleted);
    }

    @PostMapping("/add")
    public ResponseEntity <SaleDto> addSale(@RequestBody /*@Valid*/SaleDto saleDto){

            SaleDto response = saleService.addSale(saleDto);

            if(response == null){
                return ResponseEntity.badRequest().build();
            }

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity <SaleDto> updateSale(@PathVariable Long id,
                                               @RequestBody SaleDto saleDto){

            SaleDto response = saleService.updateSale(id, saleDto);

            if(response == null){
                return ResponseEntity.badRequest().build();
            }

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity <SaleDto> deleteSale (@PathVariable Long id){

            SaleDto response = saleService.deleteSale(id);

            if(response == null){
                return ResponseEntity.badRequest().build();
            }

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

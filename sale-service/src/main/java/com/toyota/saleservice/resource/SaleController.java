package com.toyota.saleservice.resource;

import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.service.abstracts.SaleService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {

    @Autowired
    private final SaleService saleService;


    @GetMapping("/getAll")
    public PaginationResponse<SaleDto> getAllSales(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "0.0") Double minTotalPrice,
                                                   @RequestParam(defaultValue = "" + Double.MAX_VALUE) Double maxTotalPrice,
                                                   @RequestParam(defaultValue = "2023-01-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                   @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().toString()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                   @RequestParam(defaultValue = "") String paymentType,
                                                   @RequestParam(defaultValue = "false") boolean deleted,
                                                   @RequestParam(defaultValue = "") List<String> sortBy,
                                                   @RequestParam(defaultValue = "ASC") String sortOrder) {
        return saleService.getSalesFiltered(page, size, sortBy, sortOrder, minTotalPrice, maxTotalPrice, startDate, endDate, paymentType, deleted);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity <SaleDto> getSale(@PathVariable Long id){

            SaleDto response = saleService.getSale(id);

            if(response == null){
                return ResponseEntity.badRequest().build();
            }

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @PostMapping("/add")
    public ResponseEntity<?> addSale(@RequestHeader("Authorization") String token, @RequestBody SaleDto saleDto) {

        String jwtToken = token.substring(7);


        String cashierName = getCashierNameFromToken(jwtToken);


        SaleDto sale = saleService.addSale(saleDto, cashierName);

        return ResponseEntity.ok(sale);
    }


    String getCashierNameFromToken(String token) {

        String[] parts = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));


        JSONObject jsonObject = new JSONObject(payload);
        return jsonObject.getString("name");
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

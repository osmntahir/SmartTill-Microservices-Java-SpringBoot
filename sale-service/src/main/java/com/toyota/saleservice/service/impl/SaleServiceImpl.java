package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.SaleAlreadyExistsException;
import com.toyota.saleservice.service.abstracts.SaleService;
import com.toyota.saleservice.service.common.MapUtil;
import com.toyota.saleservice.service.common.SortUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final Logger logger = LogManager.getLogger(SaleService.class);
    private final MapUtil mapUtil;
    @Override
    public PaginationResponse<SaleDto> getSalesFiltered(int page,
                                                        int size,
                                                        List<String> sortBy,
                                                        String sortOrder,
                                                        double minTotalPrice,
                                                        double maxTotalPrice,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate,
                                                        String paymentTypeStr,
                                                        boolean deleted) {

        logger.info("Getting sales with filters");

        Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtil.createSortOrder(sortBy, sortOrder)));

        PaymentType paymentType = null;
        if (paymentTypeStr != null && !paymentTypeStr.isEmpty()) {
            try {
                paymentType = PaymentType.valueOf(paymentTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid payment type: " + paymentTypeStr);
            }
        }

        Page<Sale> pageResponse = saleRepository.getSalesFiltered(minTotalPrice, maxTotalPrice, startDate, endDate, paymentType, deleted, pageable);

        logger.debug("Retrieved {} sales.", pageResponse.getContent().size());

        List<SaleDto> saleDtos = pageResponse.getContent().stream()
                .map(mapUtil::convertSaleToSaleDto)
                .collect(Collectors.toList());

        logger.info("Retrieved and converted {} sales to dto.", saleDtos.size());

        return new PaginationResponse<>(saleDtos, pageResponse);
    }

    @Override
    public SaleDto addSale(SaleDto saleDto) {
        logger.info("Adding sale with id {}", saleDto.getId());

        if (saleRepository.existsByIdAndDeletedIsFalse(saleDto.getId())) {
            logger.warn("Sale add failed due to existing sale with id: {}", saleDto.getId());
            throw new SaleAlreadyExistsException("Sale already exists with id: " + saleDto.getId());
        }

        // Toplam satış tutarını hesaplamak için başlangıç değeri
        double totalPrice = 0.0;

        // Her satılan ürünü dolaşarak toplam fiyatı hesapla
        for (SoldProductDto soldProductDto : saleDto.getSoldProducts()) {
            if (soldProductDto.getProductDto() != null) {
                double productPrice = soldProductDto.getProductDto().getPrice();
                int quantity = soldProductDto.getQuantity();
                totalPrice += productPrice * quantity;
            } else {
                logger.warn("Product details are missing for sold product with id: {}", soldProductDto.getId());
            }
        }

        // Toplam fiyatı saleDto'ya ayarla
        saleDto.setTotalPrice(totalPrice);

        // Eğer creationDate null ise, şu anki tarihi ayarla
        if (saleDto.getCreationDate() == null) {
            saleDto.setCreationDate(LocalDateTime.now());
        }

        // DTO'yu Entity'e dönüştür
        Sale sale = mapUtil.convertSaleDtoToSale(saleDto);
        Sale saved = saleRepository.save(sale);

        logger.info("Sale added with id: {}", saved.getId());

        // Kaydedilen entity'nin creationDate alanını kontrol edin ve DTO'ya dönüştürün
        SaleDto resultDto = mapUtil.convertSaleToSaleDto(saved);

        // Eğer resultDto'nun creationDate'i hala null ise manuel olarak ayarla
        if (resultDto.getCreationDate() == null) {
            resultDto.setCreationDate(saved.getDate());
        }

        return resultDto;
    }


    @Override
    public SaleDto updateSale(Long id, SaleDto saleDto) {
        return null;
    }


    @Override
    public SaleDto deleteSale(Long id) {
        return null;
    }
}

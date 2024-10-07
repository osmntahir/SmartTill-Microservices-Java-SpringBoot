package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.config.ProductServiceClient;
import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.dao.SoldProductRepository;
import com.toyota.saleservice.domain.PaymentType;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.domain.SoldProduct;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.ProductDTO;
import com.toyota.saleservice.dto.SaleDto;
import com.toyota.saleservice.dto.SoldProductDto;
import com.toyota.saleservice.exception.ProductNotFoundException;
import com.toyota.saleservice.exception.SaleAlreadyExistsException;
import com.toyota.saleservice.exception.SaleNotFoundException;
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
    private final SoldProductRepository soldProductRepository;
    private final ProductServiceClient productServiceClient;
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
    public SaleDto addSale(SaleDto saleDto, String cashierName) {
        logger.info("Adding sale with id {}", saleDto.getId());
        logger.info("Cashier name: {}", cashierName);

        if (saleRepository.existsByIdAndDeletedIsFalse(saleDto.getId())) {
            logger.warn("Sale add failed due to existing sale with id: {}", saleDto.getId());
            throw new SaleAlreadyExistsException("Sale already exists with id: " + saleDto.getId());
        }

        double totalPrice = 0.0;
        double totalDiscountAmount = 0.0;

        for (SoldProductDto soldProductDto : saleDto.getSoldProducts()) {
            if (soldProductDto != null) {
                double productPrice = soldProductDto.getPrice();
                int quantity = soldProductDto.getQuantity();
                totalPrice += productPrice * quantity;

                double discountAmount = soldProductDto.getDiscountAmount() * quantity;
                totalDiscountAmount += discountAmount;
            } else {
                logger.warn("Product details are missing for sold product with id: {}", soldProductDto.getId());
            }
        }

        double totalDiscountedPrice = totalPrice - totalDiscountAmount;

        saleDto.setTotalDiscountAmount(totalDiscountAmount);
        saleDto.setTotalDiscountedPrice(totalDiscountedPrice);
        saleDto.setTotalPrice(totalPrice);
        saleDto.setCashierName(cashierName);

        Sale sale = mapUtil.convertSaleDtoToSale(saleDto);
        Sale saved = saleRepository.save(sale);

        logger.info("Sale added with id: {}", saved.getId());

        return mapUtil.convertSaleToSaleDto(saved);
    }


    @Override
    public SaleDto updateSale(Long id, SaleDto saleDto) {
        logger.info("Updating sale with id: {}", id);

        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));

        existingSale.setPaymentType(saleDto.getPaymentType());

        if (saleDto.getSoldProducts() != null) {
            soldProductRepository.deleteAll(existingSale.getSoldProducts());
            existingSale.getSoldProducts().clear();

            double newTotalPrice = 0.0;
            double newTotalDiscountAmount = 0.0;
            double newTotalDiscountedPrice = 0.0;

            for (SoldProductDto soldProductDto : saleDto.getSoldProducts()) {

                ProductDTO product = productServiceClient.getProductById(soldProductDto.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + soldProductDto.getProductId()));

                SoldProduct soldProduct = mapUtil.convertSoldProductDtoToSoldProduct(soldProductDto);

                soldProduct.setSale(existingSale);

                double productTotal = product.getPrice() * soldProductDto.getQuantity();
                soldProduct.setTotal(productTotal);

                // Discount amount should be multiplied by quantity
                double discountAmount = soldProductDto.getDiscountAmount() * soldProductDto.getQuantity();
                soldProduct.setDiscountAmount(discountAmount);
                newTotalDiscountAmount += discountAmount;
                newTotalPrice += productTotal;

                existingSale.getSoldProducts().add(soldProduct);
            }
            newTotalDiscountedPrice = newTotalPrice - newTotalDiscountAmount;

            existingSale.setTotalPrice(newTotalPrice);
            existingSale.setTotalDiscountAmount(newTotalDiscountAmount);
            existingSale.setTotalDiscountedPrice(newTotalDiscountedPrice);
        }

        Sale updatedSale = saleRepository.save(existingSale);
        logger.info("Sale with id {} is updated", id);

        return mapUtil.convertSaleToSaleDto(updatedSale);
    }

    @Override
    public SaleDto deleteSale(Long id) {
        logger.info("Deleting sale with id: {}", id);

        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));

        existingSale.setDeleted(true);
        Sale savedSale = saleRepository.save(existingSale);

        logger.info("Sale with id {} is deleted", id);
        return mapUtil.convertSaleToSaleDto(savedSale);
    }

    @Override
    public SaleDto getSale(Long saleId) {
        logger.info("Getting sale with id: {}", saleId);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + saleId));

        // Retrieve all non-deleted sold products for this sale
        List<SoldProduct> soldProducts = soldProductRepository.findAllBySaleId(saleId);

        // Convert SoldProduct to SoldProductDto
        List<SoldProductDto> soldProductDtos = soldProducts.stream()
                .map(mapUtil::convertSoldProductToSoldProductDto)
                .collect(Collectors.toList());

        SaleDto saleDto = mapUtil.convertSaleToSaleDto(sale);
        saleDto.setSoldProducts(soldProductDtos);

        return saleDto;
    }
}

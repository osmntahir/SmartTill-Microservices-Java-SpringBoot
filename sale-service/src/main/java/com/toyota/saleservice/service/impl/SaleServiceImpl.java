package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.dao.SaleRepository;
import com.toyota.saleservice.domain.Sale;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.dto.SaleDto;
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


@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final Logger logger = LogManager.getLogger(SaleService.class);
    private final MapUtil mapUtil;

    @Override
    public PaginationResponse<SaleDto> getSalesFiltered(int page, int size, List<String> sortBy, String sortOrder, double totalPrice, LocalDateTime date, String paymentType, boolean deleted) {

        logger.info("Getting sales with filters");
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtil.createSortOrder(sortBy, sortOrder)));
        Page<Sale> pageResponse = saleRepository.getSalesFiltered(totalPrice, date, paymentType, deleted, pageable);
        logger.debug("Retrieved {} sales.", pageResponse.getContent().size());
        List<SaleDto> saleDtos = pageResponse.stream().map(
                mapUtil::convertSaleToSaleDto).toList();
        logger.info("Retrieved and converted {} sales to dto.", saleDtos.size());

        return new PaginationResponse<>(saleDtos, pageResponse);
    }

    @Override
    public SaleDto addSale(SaleDto saleDto) {
        logger.info("Adding sale with id "+saleDto.getId());

        if(saleRepository.existsByIdAndDeletedIsFalse(saleDto.getId())){
            logger.warn("Sale add failed due to existing sale with id: {}",saleDto.getId());
            throw new SaleAlreadyExistsException("Sale already exists with id: "+saleDto.getId());
        }
        Sale sale = mapUtil.convertSaleDtoToSale(saleDto);
        Sale saved = saleRepository.save(sale);

        logger.info("Sale added with id: {}", saved.getId());

        return mapUtil.convertSaleToSaleDto(saved);
    }

    @Override
    public SaleDto updateSale(Long id, SaleDto saleDto) {
        logger.info("Updating sale with id: {}", id);

        return null;
    }

    @Override
    public SaleDto deleteSale(Long id) {
        return null;
    }
}

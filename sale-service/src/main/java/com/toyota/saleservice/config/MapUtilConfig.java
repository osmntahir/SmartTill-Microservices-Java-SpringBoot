package com.toyota.saleservice.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.toyota.saleservice.service.common.MapUtil;
import org.springframework.web.client.RestTemplate;

/**
 *  MapUtilConfig class is used to create a bean of MapUtil class.
 * */

@Configuration
public class MapUtilConfig {

    private final RestTemplate restTemplate;

    public MapUtilConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public MapUtil mapUtil() {
        return new MapUtil(new ModelMapper(), new ProductServiceClient(restTemplate));
    }
}

package com.toyota.saleservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for configuring ModelMapper bean.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Bean definition for ModelMapper.
     *
     * @return ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

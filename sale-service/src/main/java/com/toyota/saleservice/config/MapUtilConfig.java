package com.toyota.saleservice.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.toyota.saleservice.service.common.MapUtil;
@Configuration
public class MapUtilConfig
{
    @Bean
    public MapUtil mapUtil()
    {
        return new MapUtil(new ModelMapper());
    }
}
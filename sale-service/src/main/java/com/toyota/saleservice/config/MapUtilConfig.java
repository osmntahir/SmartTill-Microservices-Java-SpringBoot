package com.toyota.saleservice.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.toyota.saleservice.service.common.MapUtil;

/**
 *  MapUtilConfig class is used to create a bean of MapUtil class.
 * */
@Configuration
public class MapUtilConfig
{

    /***
     *  This method is used to create a bean of MapUtil class.
     * **/
    @Bean
    public MapUtil mapUtil()
    {
        return new MapUtil(new ModelMapper());
    }
}
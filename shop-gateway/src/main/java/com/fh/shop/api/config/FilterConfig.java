package com.fh.shop.api.config;

import com.fh.shop.api.filter.CrossFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public CrossFilter crossFilter(){
        return new CrossFilter();
    }
}

package com.banker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


/**
 * @author Aleksei Chursin
 */
@Configuration
@ComponentScan("com.banker")
@EnableWebMvc
public class SpringConfig{
//    @Bean
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
//        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
//        adapter.setDefaultMimeType(MediaType.APPLICATION_JSON);
//        return adapter;
//    }
}

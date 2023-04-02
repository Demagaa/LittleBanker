package com.banker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * @author Aleksei Chursin
 */
@Configuration
@ComponentScan("com.banker")
@EnableWebMvc
public class SpringConfig{ }
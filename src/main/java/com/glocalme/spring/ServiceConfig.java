package com.glocalme.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = { "com.onewifi.service" , "com.glocalme.security", "com.onewifi.service"})
@Import({SecurityConfig.class}) 
public class ServiceConfig {

}

package com.victor.poc.prometheus.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class Configurer implements WebMvcConfigurer {

	@Value("${application.product}")
	private String product;
	
	@Autowired
	private MeterRegistry meterRegistry;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(new MetricaWebInterceptor(meterRegistry, product));
    }
}
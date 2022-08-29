package com.victor.poc.prometheus.interceptor;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.search.MeterNotFoundException;


public class MetricaWebInterceptor implements WebRequestInterceptor {
	
	private String product;

	private MeterRegistry meterRegistry;
	
	public MetricaWebInterceptor(MeterRegistry meterRegistry, String product) {
		System.out.println("Iniciando MetricaWebInterceptor...");
		this.product = product;
		this.meterRegistry = meterRegistry;
		
	}
	
	@Override
	public void preHandle(WebRequest request) throws Exception {
		
		String token = request.getHeader("Authorization");
		if(token != null) {
			String tokenPayloadEncoded = token.split("\\.")[1];
			String payloadToken = new String(Base64.getDecoder().decode(tokenPayloadEncoded), Charset.defaultCharset());
			PayloadWebToken payload = new ObjectMapper().readValue(payloadToken, PayloadWebToken.class);  

			String uri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
			System.out.println("tenant: "+payload.tenant+", user: "+payload.user+", uri: "+uri);
			
			try {
				meterRegistry.get("tracking_product_tenants_and_users").tags(getTags(payload, uri)).counter().increment();
			} catch (MeterNotFoundException e) {
				Counter.builder("tracking_product_tenants_and_users").tags(getTags(payload, uri)).register(meterRegistry).increment();
			}
		}	
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		
	}

	private Tags getTags(PayloadWebToken payload, String uri) {
		return Tags.of("product", product, "tenant", payload.tenant, "endpoint", uri, "user", payload.user);
	}
	
}

class PayloadWebToken {
	
	public String tenant;
	public String user;
	
}
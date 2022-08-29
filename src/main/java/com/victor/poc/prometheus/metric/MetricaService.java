package com.victor.poc.prometheus.metric;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.search.MeterNotFoundException;

@Service
public class MetricaService {

	private Tags tags = Tags.of("tag_teste", "valor_teste");
	private String metricName = "metrica_teste";
	
	private String metricGaugeName = "metrica_gauge_teste";
	private Tags tagsGauge = Tags.of("tag_gauge_teste", "valor_gauge_teste");

	@Autowired
	private MeterRegistry meterRegistry;
	
	public void registerCounter() {
		try {
			meterRegistry.get(metricName).tags(tags).counter().increment();
		} catch (MeterNotFoundException e) {
			Counter.builder(metricName).tags(tags).register(meterRegistry).increment();
		}
	}
	
	public double registerGauge() {
		Gauge g;
		try {
			g = meterRegistry.get(metricGaugeName).tags(tagsGauge).gauge();
		} catch (MeterNotFoundException e) {
			g = Gauge.builder(metricGaugeName, this, value -> usuariosSimultaneos()).tags(tagsGauge).register(meterRegistry);
		}
		return g.value();
	}
	
	public double usuariosSimultaneos() {
		return new Random().nextDouble(0, 10);
	}
}

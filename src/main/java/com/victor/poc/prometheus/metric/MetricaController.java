package com.victor.poc.prometheus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/metrica")
public class MetricaController {
	
	@GetMapping()
	@Timed(
		value= "testeteste",
		extraTags= {"myteste","1"}
	)
	public ResponseEntity<String> testeMetrica() {
		return ResponseEntity.ok("ok");
	}

}

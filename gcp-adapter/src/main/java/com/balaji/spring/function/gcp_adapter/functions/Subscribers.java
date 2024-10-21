package com.balaji.spring.function.gcp_adapter.functions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.balaji.spring.function.gcp_adapter.model.Subscriber;
import com.balaji.spring.function.gcp_adapter.service.SubscriberService;

@Configuration
public class Subscribers {
	
	
	@Autowired
	SubscriberService subscriberService;
	
	public static final Logger LOG=LoggerFactory.getLogger(Subscribers.class);
	
	@Bean
	public Supplier<List<Subscriber>> findAll() {
		LOG.info("inside findAll");
		return () -> subscriberService.findAll();
	}
	
	@Bean
	public Consumer<String> create() {
		LOG.info("inside create");
		return (email) -> subscriberService.create(email);
	}
}

package com.balaji.spring.function.newsletter_demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.balaji.spring.function.newsletter_demo.model.Subscriber;

@Service
public class SubscriberService {
	
	List<Subscriber> subscribers=new ArrayList<>();
	AtomicInteger id=new AtomicInteger(0);
	
	
	public List<Subscriber> findAll() {
		return subscribers;
	}
	
	
	public void create(String email) {
		subscribers.add(new Subscriber(id.addAndGet(1), email));
	}
	

}

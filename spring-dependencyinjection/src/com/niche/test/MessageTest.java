package com.niche.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.niche.pojo.MessagePojo;
import com.niche.publish.MessagePublisherService;
import com.niche.publish.implementation.FileMessagePublisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class MessageTest {
	
	@Autowired
	private MessagePublisherService mPublisherService;

	/*
	
	public void publishMessageWithoutSpring(){
		MessagePublisherService publisherService = new MessagePublisherService(new FileMessagePublisher());
		MessagePojo message = new MessagePojo();
		message.setMessageId(1);
		message.setMessage("Test Message");
		assertTrue(publisherService.publishMessage(message));
	}
	*/
	
	@Test
	public void publishMessageWithSpring() {
		MessagePojo message = new MessagePojo();
		message.setMessageId(1);
		message.setMessage("Test Message");
		assertTrue(mPublisherService.publishMessage(message));
	}	
}

package com.niche.publish;

import com.niche.pojo.MessagePojo;

public class MessagePublisherService {

	private MessagePublisher mPublisher = null;

	/*
	public MessagePublisherService(){
		
	}
	*/

	public MessagePublisherService(MessagePublisher mPublisher) {
		this.mPublisher = mPublisher;
	}

	public boolean publishMessage(MessagePojo message) {
		return mPublisher.publishMessage(message);
	}

	/*
	public void setmPublisher(MessagePublisher mPublisher) {
		this.mPublisher = mPublisher;
	}
	*/
}

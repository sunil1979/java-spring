package com.niche.publish;

import com.niche.pojo.MessagePojo;

public interface MessagePublisher {
	public boolean publishMessage(MessagePojo message);
}

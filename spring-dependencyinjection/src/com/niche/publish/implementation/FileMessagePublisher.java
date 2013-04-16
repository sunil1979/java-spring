package com.niche.publish.implementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.niche.pojo.MessagePojo;
import com.niche.publish.MessagePublisher;

public class FileMessagePublisher implements MessagePublisher {

	@Override
	public boolean publishMessage(MessagePojo message) {
		boolean returnStatus = true;
		try {
			File file = new File("messages.txt");
			if(!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(message.getMessageId()+" - "+message.getMessage());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			returnStatus = false;
		}
		return returnStatus;
	}
}

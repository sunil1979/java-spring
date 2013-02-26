package com.sample;

public class DroolsUtil {

	public User getSampleUser(int userId){
		User sampleUser = new User();
		sampleUser.setId(userId);
		sampleUser.setName("Sample Username1");
		return sampleUser;
	}
}

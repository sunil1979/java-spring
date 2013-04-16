package com.niche.publish.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.niche.pojo.MessagePojo;
import com.niche.publish.MessagePublisher;

public class DBMessagePublisher implements MessagePublisher {

	private Connection dbConnection;

	@Override
	public boolean publishMessage(MessagePojo message) {
		boolean returnStatus = true;
		try {
			this.getConnection();
			PreparedStatement stmt = dbConnection
					.prepareStatement("INSERT INTO MESSAGE VALUES(?,?)");
			stmt.setInt(1, message.getMessageId());
			stmt.setString(2, message.getMessage());
			stmt.executeUpdate();
			this.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
			returnStatus = false;
		}
		return returnStatus;
	}

	private void getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/spring_dependencyinjection",
				"root", "root");
	}

	private void closeConnection() throws Exception {
		if (dbConnection != null)
			dbConnection.close();
	}
}

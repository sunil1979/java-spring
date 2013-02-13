package com.sample;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
	private String name;
	private List<Task> tasks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(Task task){
		if(task == null)
			return;
		else{
			if(this.tasks == null)
				this.tasks = new ArrayList<Task>();
			this.tasks.add(task);
		}
					
	}
}

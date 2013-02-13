package com.ebuild.leap.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback=false)
public class ActivitiBusinessProcessTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected static Logger log = LoggerFactory.getLogger(ActivitiBusinessProcessTest.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private IdentityService identifyService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;

	
	public void deployProcesses() throws Exception {
		repositoryService.createDeployment().addClasspathResource("CreateHomeUnitProcess.bpmn").deploy();
	}

	public void createUsers() throws Exception {
		System.out.println("START - CREATE USER");
		for (User user : userRepository.findAll()) {
			org.activiti.engine.impl.persistence.entity.UserEntity activityUser = new org.activiti.engine.impl.persistence.entity.UserEntity();
			activityUser.setId(user.getId().toString());
			activityUser.setFirstName(user.getFirstName());
			activityUser.setLastName(user.getLastName());
			activityUser.setEmail(user.getEmail());
			activityUser.setPassword(user.getPassword());
			identifyService.saveUser(activityUser);
		}
		System.out.println("END - CREATE USER");
	}

	public void createGroups() throws Exception {
		org.activiti.engine.impl.persistence.entity.GroupEntity ebuildManagementGroup = new org.activiti.engine.impl.persistence.entity.GroupEntity();
		ebuildManagementGroup.setId("1");
		ebuildManagementGroup.setName("eBuild Management Group");
		ebuildManagementGroup.setType("Management");
		identifyService.saveGroup(ebuildManagementGroup);

		org.activiti.engine.impl.persistence.entity.GroupEntity ebuildAdministrationGroup = new org.activiti.engine.impl.persistence.entity.GroupEntity();
		ebuildAdministrationGroup.setId("2");
		ebuildAdministrationGroup.setName("eBuild Administration Group");
		ebuildAdministrationGroup.setType("Administration");
		identifyService.saveGroup(ebuildAdministrationGroup);

		org.activiti.engine.impl.persistence.entity.GroupEntity crmGroup = new org.activiti.engine.impl.persistence.entity.GroupEntity();
		crmGroup.setId("3");
		crmGroup.setName("TE CRM Group");
		crmGroup.setType("CRM");
		identifyService.saveGroup(crmGroup);
	}

	public void addUsersToGroups() throws Exception {
		identifyService.createMembership("22315112387837958", "2");
		identifyService.createMembership("22315112387837957", "3");
		identifyService.createMembership("94135915886149830", "1");
	}

	
	public void initiateCreateHomeUnitBusinessProcess() throws Exception {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("firstName", "Sunil");
		processVariables.put("lastName", "Kumar");
		processVariables.put("customerUserName", "sunil");
		processVariables.put("customerEmail", "sunil1979@gmail.com");
		processVariables.put("customerPhone", "1234");
		processVariables.put("projectName", "Windmills of Your Mind");
		processVariables.put("productName", "Windmills of Your Mind LifeStyle D50 Product");
		processVariables.put("projectId", "22380266521624577");
		processVariables.put("productId", "22380266521624579");
		runtimeService.startProcessInstanceByKey("ebuild_processes_2", processVariables);
	}
	
	@Test
	public void retrieveUserTasks() throws Exception{
		List<Task> userTasks = taskService.createTaskQuery().taskAssignee("22315112387837958").list();
		for(Task t : userTasks){
			System.out.println("Task Id :"+t.getId());
			System.out.println("Task Name :"+t.getName());
			System.out.println("Task Assignee :"+t.getAssignee());
			System.out.println("Task Owner :"+t.getOwner());
			System.out.println("Task Description :"+t.getDescription());
			System.out.println("Task Creation Time :"+t.getCreateTime());
			System.out.println("Task Due Date :"+t.getDueDate());
			System.out.println("Task Execution Id :"+t.getExecutionId());
		}
	}
	
	
	public void retrieveAndCompleteTask() throws Exception{
		Map<String, String> formVariables = new HashMap<String, String>();
		formVariables.put("requestApproved", "true");
		formVariables.put("explanation", "Please create Home Unit for this customer");
		Task task = taskService.createTaskQuery().taskId("3815").singleResult();
		System.out.println("Execution Id :"+task.getExecutionId());
		formVariables.put("executionId", task.getExecutionId());
		formVariables.put("costVersionId", "1");
		formService.submitTaskFormData(task.getId(), formVariables);
	}
}

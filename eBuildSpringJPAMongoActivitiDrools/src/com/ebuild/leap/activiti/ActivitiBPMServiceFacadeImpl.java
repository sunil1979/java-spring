package com.ebuild.leap.activiti;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.UserRepository;
import com.ebuild.leap.service.CustomizationServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;

@Component
@TransactionConfiguration(defaultRollback = false)
public class ActivitiBPMServiceFacadeImpl implements ActivitiBPMServiceFacade {

	protected static Logger log = LoggerFactory.getLogger(ActivitiBPMServiceFacadeImpl.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomizationServiceFacadeImpl homeUnitService;
	
	@Autowired
	RuntimeService runtimeService;

	
	@Override
	public void createUserAndHomeUnit(String executionId) throws Exception {
		System.out.println("Execution Id Receieved :"+executionId);
		Map<String, Object> processVariables = runtimeService.getVariables(executionId);
		log.debug("START - CreateHomeUnitDelegate.execute");
		System.out.println("START - CreateHomeUnitDelegate.execute");
		User newUser = new User();
		newUser.setFirstName((String)processVariables.get("firstName"));
		newUser.setLastName((String)processVariables.get("lastName"));
		newUser.setUsername((String)processVariables.get("customerUserName"));
		newUser.setPassword("cc03e747a6afbbcbf8be7668acfebee5");
		newUser.setActiveStatus(true);
		newUser.setEmail((String)processVariables.get("customerEmail"));
		newUser.setPrimaryPhone((String)processVariables.get("customerPhone"));
		newUser = userRepository.saveAndFlush(newUser);
		Product product = new Product();
		product.setId((long) processVariables.get("productId"));
		CostVersion costVersion = new CostVersion();
		costVersion.setId((long)processVariables.get("costVersionId"));
		EbuildleapResultObject ero = homeUnitService.createHomeUnit(newUser, product, costVersion);
		if(!ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			throw new Exception();
		}
		log.debug("END - CreateHomeUnitDelegate.execute");
		System.out.println("END - CreateHomeUnitDelegate.execute");
	}

}

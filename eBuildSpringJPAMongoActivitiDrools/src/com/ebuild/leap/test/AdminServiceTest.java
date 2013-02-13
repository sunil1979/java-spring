package com.ebuild.leap.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.service.AdminServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback=false)
public class AdminServiceTest {
	
	@Autowired
	AdminServiceFacadeImpl adminService;

	public void loadSpring() throws Exception{
		
	}
	
	
	public void testCreateBrand() throws Exception{
		Brand brand = new Brand();
		User createdBy = new User();
		createdBy.setId(new Long("78506188").longValue());
		brand.setCreatedBy(createdBy);
		brand.setName("New Brand");
		brand.setDescription("Brand Description");
		adminService.createBrand(brand);
	}
	
	
	public void testGetBrand() throws Exception{
		Brand brand = new Brand();
		brand.setId(new Long("22819550558945280").longValue());
		EbuildleapResultObject ero = adminService.getBrand(brand);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("Brand Retrieved Successfully :"+ ((Brand)ero.getResult().get(0)).getName());
		}else{
			System.out.println(ero.getErrDescription());
		}
	}
	
	
	public void updateBrand() throws Exception{
		Brand brand = new Brand();
		brand.setId(new Long("22819550558945280").longValue());
		brand.setName("Name Changed");
		User updatedBy = new User();
		updatedBy.setId(new Long("705074846").longValue());
		brand.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateBrand(brand);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("Brand Updated Successfully :"+ ((Brand)ero.getResult().get(0)).getName());
		}else{
			System.out.println(ero.getErrDescription());
		}		
	}
	
	
	public void deleteBrand() throws Exception{
		Brand brand = new Brand();
		brand.setId(new Long("22819550558945280").longValue());
		EbuildleapResultObject ero = adminService.deleteBrand(brand);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("Brand Deleted Successfully");
		}else{
			System.out.println(ero.getErrDescription());
		}				
	}
	
}

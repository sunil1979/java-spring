package com.ebuild.leap.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.HomeUnitRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRevisionRepository;
import com.ebuild.leap.repository.jpa.HomeUnitVersionRepository;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;
import com.ebuild.leap.service.DesignerServiceFacadeImpl;
import com.ebuild.leap.service.CustomizationServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback=false)
public class HomeUnitServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	protected static Logger log = LoggerFactory.getLogger(HomeUnitServiceTest.class);
	
	@Autowired
	CustomizationServiceFacadeImpl homeUnitService;
	
	@Autowired
	DesignerServiceFacadeImpl elementService;
	
	@Autowired
	HomeUnitRepository huRep;
	
	@Autowired
	HomeUnitVersionRepository huvRep;
	
	@Autowired
	HomeUnitRevisionRepository hurRep;
	
	@Autowired
	HomeUnitRevisionMongoRepository hurMongoRepository;
	
	@Test
	public void createSchema() throws Exception{
		
	}
	
	
	public void createHomeUnit() throws Exception{
		User user = new User();
		user.setId(new Long("94135915886149830").longValue());
		Product product = new Product();
		product.setId(new Long("22495066517933367").longValue());
		CostVersion costVersion = new CostVersion();
		costVersion.setId(new Long("1").longValue());
		EbuildleapResultObject ero = homeUnitService.createHomeUnit(user, product, costVersion);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("HomeUnit Created Successfully");
		}else{
			System.out.println("HomeUnit Creation Failed");
		}
	}
	
	
	public void getHomeUnitRevision() throws Exception{
		HomeUnitVersion huVersion = new HomeUnitVersion();
		huVersion.setId(new Long("105225315").longValue());
		EbuildleapResultObject ero = homeUnitService.getLatestHomeUnitRevision(huVersion);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("HomeUnit Revision Retrieved Successfully");
		}else{
			System.out.println("HomeUnit Revision Retrieval Failed");
		}
	}
	
	
	public void getLatestRevision() throws Exception{
		HomeUnitVersion huVersion = new HomeUnitVersion();
		huVersion.setId(new Long("105225315").longValue());
		EbuildleapResultObject ero = homeUnitService.getLatestHomeUnitRevision(huVersion);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("HomeUnit Revision Retrieved Successfully");
			HomeUnitRevision huRevision = (HomeUnitRevision) ero.getResult().get(0);
			System.out.println("Latest Revision Id :"+huRevision.getId());
			System.out.println("Latest Revision Number :"+huRevision.getRevisionNumber());
			//this.checkVariantExists(huRevision.getHomeUnitVersion().getHomeUnit().getProduct().getElement(),1);
		}else{
			System.out.println("HomeUnit Revision Retrieval Failed");
		}		
	}
	
	
	public void checkVariants() throws Exception{
		HomeUnitRevision hur = hurRep.findOne(new Long("79262476").longValue());
		this.checkVariantExists(hur.getHomeUnitVersion().getHomeUnit().getProduct().getElement(), 1);
	}
	
	private void checkVariantExists(Element element, int eleCount){
		System.out.println("Checking Variants for Element :"+element.getId());
		//System.out.println("Element Count :"+eleCount);
		if(element.getElementVariantList()!=null && element.getElementVariantList().getVariants().size() > 0){
			System.out.println("Element has Variants :"+element.getId());
			return;
		}
		for(ElementManifest em : element.getElementManifestList()){
			if(em.getChildElement() != null){
				this.checkVariantExists(em.getChildElement(),eleCount);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	
	public void getElementVariants() throws Exception{
		Element sourceElement = new Element();
		sourceElement.setId(new Long("94267842366553821").longValue());
		Element parentElement = new Element();
		parentElement.setId(new Long("22158060751421586").longValue());
		EbuildleapResultObject ero = elementService.getVariantElements(sourceElement, parentElement);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("Source Element Variants Retrieved Successfully");
			List<Element> variantList = ero.getResult();
			for(Element variantElement : variantList){
				System.out.println("Variant Element Id :"+variantElement.getId());
			}
		}else{
			System.out.println("No Variants for Source Elements");
		}		
	}
	
	
	public void createNewRevision() throws Exception{
		HomeUnitVersion huVersion = new HomeUnitVersion();
		huVersion.setId(new Long("1868770688").longValue());
		HomeUnitRevision currentRevision = new HomeUnitRevision();
		currentRevision.setId(new Long("253068812").longValue());
		Element tobeReplacedElement = new Element();
		tobeReplacedElement.setId(new Long("94267842366553821").longValue());
		Element parentElement = new Element();
		parentElement.setId(new Long("22158060751421586").longValue());
		Element newChildElement = new Element();
		newChildElement.setId(new Long("94267842366553825").longValue());
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(new Long("22495066518368775").longValue());
		currentElementManifest.setParentElement(parentElement);
		currentElementManifest.setChildElement(tobeReplacedElement);
		EbuildleapResultObject ero = homeUnitService.createNewRevision(currentRevision, tobeReplacedElement,currentElementManifest);
		if(ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)){
			System.out.println("New Revision Created Successfully");
			HomeUnitRevision newHURevision = (HomeUnitRevision) ero.getResult().get(0);
			System.out.println("New Revision Id :"+newHURevision.getId());
		}else{
			System.out.println("No Variants for Source Elements");
		}		
	}
	
	
	public void checkElementLevel() throws Exception{
		HomeUnitRevision huRevision = hurMongoRepository.findOne(new Long("1078860033").longValue());
		Element targetElement = new Element();
		targetElement.setId(new Long("94267842366553761").longValue());
		this.findLevel(huRevision.getHomeUnitVersion().getHomeUnit().getProduct().getElement(), targetElement,new ArrayList<String>());
	}
	
	private void findLevel(Element source, Element target,List<String> pathList){
		
		//System.out.println("Source Element ID :"+source.getId());
		for(ElementManifest em : source.getElementManifestList()){
			if(em.getChildElement().getId().equals(target.getId())){
				System.out.println("TargetElement Found :"+em.getChildElement().getId());
				System.out.println("Parent Element :"+source.getId());
				System.out.println("Path :");
				for(String pathStr : pathList){
					System.out.print(pathStr +"->");
				}
				break;
			}else{
				pathList.add(source.getId().toString());
				/*
				System.out.println("Path1 :");
				for(String pathStr : pathList){
					System.out.print(pathStr +"->");
				}
				System.out.println("");
				*/
				this.findLevel(em.getChildElement(), target,pathList);
				/*
				pathList.remove(source.getId().toString());
				System.out.println("Path2 :");
				for(String pathStr : pathList){
					System.out.print(pathStr +"->");
				}
				System.out.println("");
				*/
			}
		}
	}
	
	private void findLevel1(List<ElementManifest> levelElements, Element target){
		for(ElementManifest em : levelElements){
			if(em.getChildElement().getId().equals(target.getId())){
				System.out.println("Target Id Found");
			}
		}
	}
}

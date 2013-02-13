package com.ebuild.leap.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.ElementVariantList;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.BrandRepository;
import com.ebuild.leap.repository.jpa.ElementRepository;
import com.ebuild.leap.repository.jpa.ElementSearchSpecification;
import com.ebuild.leap.repository.jpa.ProductRepository;
import com.ebuild.leap.repository.mongodb.ProductMongoRepository;
import com.ebuild.leap.service.DesignerServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback = false)
public class DesignerServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected static Logger log = LoggerFactory.getLogger(DesignerServiceTest.class);

	@Autowired
	DesignerServiceFacadeImpl designerService;

	@Autowired
	ProductMongoRepository productMongoRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	ElementRepository elementRepository;

	public void testCreateElement() throws Exception {
		Element newElement = new Element();
		newElement.setActiveStatus(true);
		Brand brand = new Brand();
		brand.setId(new Long("20").longValue());
		newElement.setBrand(brand);

		Category category = new Category();
		category.setId(new Long("40").longValue());
		newElement.setCategory(category);

		newElement.setCode1("Test Element");
		CostVersion costVersion = new CostVersion();
		costVersion.setId(new Long("1").longValue());
		newElement.setCostVersion(costVersion);
		newElement.setCPR(new Double("1").doubleValue());
		User createdBy = new User();
		createdBy.setId(new Long("1").longValue());
		newElement.setCreatedBy(createdBy);
		newElement.setDeleteStatus(false);
		newElement.setDescription("Test Element Description");
		newElement.setDimension("100,100,100");
		Theme sampleTheme = new Theme();
		sampleTheme.setId(new Long("10").longValue());
		newElement.addElementTheme(sampleTheme);
		ElementVariantList elementVariantList = new ElementVariantList();
		elementVariantList.setId(new Long("22631637519181647").longValue());
		newElement.setElementVariantList(elementVariantList);
		Finish finish = new Finish();
		finish.setId(new Long("10").longValue());
		newElement.setFinish(finish);
		newElement.setFunction(1);
		Material material = new Material();
		material.setId(new Long("10").longValue());
		newElement.setMaterial(material);
		newElement.setName("Test Element Name");
		newElement.setPrice(new Double("100").doubleValue());
		newElement.setScope(1);
		SubType subType = new SubType();
		subType.setId(new Long("11").longValue());
		newElement.setSubType(subType);
		Type type = new Type();
		type.setId(new Long("10").longValue());
		newElement.setType(type);
		newElement.setView1("abcd.svg");
		newElement.setWeight("100");
		EbuildleapResultObject ero = designerService.createElement(newElement);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			List<Element> newElementList = ero.getResult();
			System.out.println("Element Created Successfully :" + newElementList.get(0).getId());
		} else {
			System.out.println("Element Creation Failed !!!!");
		}

	}

	public void testGetElement() throws Exception {
		Element element = new Element();
		element.setId(new Long("1").longValue());
		EbuildleapResultObject ero = designerService.getElement(element, EbuildleapConstants.DATASTORE_MONGO);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			List<Element> newElementList = ero.getResult();
			System.out.println("Element Retrived Successfully :" + newElementList.get(0).getId());
		} else {
			System.out.println("Element could not be Retrived !!!!");
			System.out.println(ero.getErrDescription());
		}
	}

	public void testUpdateElement() throws Exception {
		Element element = new Element();
		element.setId(new Long("1").longValue());
		element.setView1("xyz.svg");
		Type type = new Type();
		type.setId(new Long("100").longValue());
		element.setType(type);
		EbuildleapResultObject ero = designerService.updateElement(element, EbuildleapConstants.DATASTORE_RDBMS);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			List<Element> newElementList = ero.getResult();
			System.out.println("Element Updated Successfully :" + newElementList.get(0).getId());
		} else {
			System.out.println("Element could not be Updated !!!!");
			System.out.println(ero.getErrDescription());
		}
	}

	public void testDeleteElement() throws Exception {
		Element element = new Element();
		element.setId(new Long("1").longValue());
		EbuildleapResultObject ero = designerService.deleteElement(element, EbuildleapConstants.DATASTORE_RDBMS);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Element Deleted Successfully");
		} else {
			System.out.println("Element could not be Deleted !!!!");
			System.out.println(ero.getErrDescription());
		}
	}

	public void testCloneElement() throws Exception {
		Element element = new Element();
		element.setId(new Long("1").longValue());
		Element cloneElement = new Element();
		cloneElement.setName("mongo element copy");
		cloneElement.setCode1("mongo new element code1");
		EbuildleapResultObject ero = designerService.cloneElement(element, cloneElement, EbuildleapConstants.DATASTORE_MONGO);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Clone Element Successfull :" + ((Element) ero.getResult().get(0)).getId());
		} else {
			System.out.println("Clone Element Failed !!!!");
			System.out.println(ero.getErrDescription());
		}
	}

	public void testGetVariants() throws Exception {
		Element sourceElement = new Element();
		sourceElement.setId(new Long("22631637519267652").longValue());
		Element parentElement = new Element();
		parentElement.setId(new Long("22631637519267686").longValue());
		EbuildleapResultObject ero = designerService.getVariantElements(sourceElement, parentElement);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			List<Element> variants = ero.getResult();
			System.out.println("Variants Available :" + variants.size());
			System.out.println("****************************************");
			for (Element e : variants) {
				System.out.println("Variants Element Id :" + e.getId());
			}
			System.out.println("****************************************");
		} else {
			System.out.println("GetVariants Call Failed !!!!");
		}

	}

	public void testCreateProduct() throws Exception {
		Project project = new Project();
		project.setId(new Long("22749618693487305").longValue());

		Element rootProductElement = new Element();
		rootProductElement.setId(new Long("22749618693487296").longValue());
		Product newProduct = new Product();
		newProduct.setName("Sample Product - AFHW");
		newProduct.setProject(project);
		newProduct.setElement(rootProductElement);
		newProduct.setActiveStatus(true);
		newProduct.setDeleteStatus(false);
		newProduct.setCreated(new Date());
		newProduct.setDescription("Sample Product AFHW Description");
		EbuildleapResultObject ero = designerService.createProduct(newProduct);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Product Created Successfull");
		} else {
			System.out.println("Product Creation Failed !!!!");
		}
	}

	public void testAddChild2IL() throws Exception {
		Element ILElement = new Element();
		ILElement.setId(new Long("9").longValue());
		ElementManifest currentEM = new ElementManifest();
		currentEM.setId(new Long("1").longValue());
		Element childElement = new Element();
		childElement.setId(new Long("22631637519212491").longValue());
		ElementManifest em = new ElementManifest();
		em.setChildElement(childElement);
		EbuildleapResultObject ero = designerService.createManifest(ILElement, currentEM, em);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Add Child Node 2 IL Successfull");
		} else {
			System.out.println("Add Child Node 2 IL Failed !!!!");
		}
	}

	public void testRemoveElementFromIL() throws Exception {
		Element ILElement = new Element();
		ILElement.setId(new Long("9").longValue());
		Element parentElement = new Element();
		parentElement.setId(new Long("9").longValue());
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(new Long("7").longValue());
		EbuildleapResultObject ero = designerService.deleteManifest(ILElement, currentElementManifest, EbuildleapConstants.DATASTORE_MONGO);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Remove Element from IL Successfull");
		} else {
			System.out.println("Remove Element from IL Failed !!!!");
		}
	}

	public void testUpdateMongoElementManifest() throws Exception {
		Element ILElement = new Element();
		ILElement.setId(new Long("9").longValue());
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(new Long("1").longValue());
		currentElementManifest.setCustomize(true);
		currentElementManifest.setElementGroup(1);
		currentElementManifest.setElementGroupTag("Element Group Tag");
		EbuildleapResultObject ero = designerService.updateManifest(ILElement, currentElementManifest, EbuildleapConstants.DATASTORE_MONGO);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Update ElementManifest in Mongo Successfull");
		} else {
			System.out.println("Update ElementManifest in Mongo Failed !!!!");
		}
	}

	@Test
	public void testSearchElementsCriteria() throws Exception {
		Element elementCriteria = new Element();
		elementCriteria.setCode1("AFHW_*");
		Material material = new Material();
		material.setName("Stone");
		Finish finish = new Finish();
		finish.setName("Wen*");
		elementCriteria.setFinish(finish);
		Theme theme = new Theme();
		theme.setName("common");
		elementCriteria.addElementTheme(theme);
		EbuildleapResultObject ero = designerService.searchElements(elementCriteria);
		if (ero.getResultStatus().equalsIgnoreCase(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL)) {
			System.out.println("Search Successful");
			System.out.println("Search Result Length :" + ero.getResult().size());
		} else {
			System.out.println("Search Failed !!!!");
		}

		/*
		 * List<Element> result =
		 * elementRepository.findAll(ElementSearchSpecification
		 * .elementLike(elementCriteria));
		 * System.out.println("Result Length :"+result.size());
		 * 
		 * for(Element e : result){
		 * System.out.println("Result Code 1 :"+e.getCode1()); }
		 */
	}
}

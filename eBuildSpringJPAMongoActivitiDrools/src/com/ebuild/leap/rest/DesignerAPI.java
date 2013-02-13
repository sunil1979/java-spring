package com.ebuild.leap.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.multipart.FormDataParam;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.ElementVariantList;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.service.DesignerServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapUtil;

@Component
@Path("/designerapi")
public class DesignerAPI {

	@Context
	UriInfo uriInfo;
	@Context
	ServletContext srvContext;
	@Context
	SecurityContext sc;
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	protected static Logger log = LoggerFactory.getLogger(DesignerAPI.class);

	@Autowired
	DesignerServiceFacadeImpl designerService;

	@Autowired
	EbuildleapUtil ebuildLeapUtil;

	@POST
	@Path("createElement")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createElement(@FormParam("brandId") Long brandId, @FormParam("categoryId") Long categoryId,
			@FormParam("code1") String code1, @FormParam("costVersionId") Long costVersionId, @FormParam("cpr") Double cpr,
			@FormParam("createdByUserId") Long createdByUserId, @FormParam("elementDescription") String elementDescription,
			@FormParam("dimension") String dimension, @FormParam("elementThemeId") Long elementThemeId,
			@FormParam("elementVariantListId") Long elementVariantListId, @FormParam("finishId") Long finishId,
			@FormParam("function") Integer function, @FormParam("materialId") Long materialId, @FormParam("elementName") String elementName,
			@FormParam("scope") Integer scope, @FormParam("subTypeId") Long subTypeId, @FormParam("typeId") Long typeId,
			@FormParam("elementWeight") String elementWeight) throws JAXBException, IOException {

		Element newElement = new Element();

		Brand brand = new Brand();
		brand.setId(brandId);
		newElement.setBrand(brand);

		Category category = new Category();
		category.setId(categoryId);
		newElement.setCategory(category);

		CostVersion costVersion = new CostVersion();
		costVersion.setId(costVersionId);
		newElement.setCostVersion(costVersion);

		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newElement.setCreatedBy(createdBy);

		Theme elementTheme = new Theme();
		elementTheme.setId(elementThemeId);
		newElement.addElementTheme(elementTheme);

		ElementVariantList elementVariantList = new ElementVariantList();
		elementVariantList.setId(elementVariantListId);
		newElement.setElementVariantList(elementVariantList);

		Finish finish = new Finish();
		finish.setId(finishId);
		newElement.setFinish(finish);

		Material material = new Material();
		material.setId(materialId);
		newElement.setMaterial(material);

		SubType subType = new SubType();
		subType.setId(subTypeId);
		newElement.setSubType(subType);

		Type type = new Type();
		type.setId(typeId);
		newElement.setType(type);

		newElement.setCode1(code1);
		newElement.setCPR(cpr);
		newElement.setDescription(elementDescription);
		newElement.setDimension(dimension);
		newElement.setFunction(function);
		newElement.setName(elementName);
		newElement.setScope(scope);
		newElement.setWeight(elementWeight);

		EbuildleapResultObject ero = designerService.createElement(newElement);
		return ero;
	}

	@GET
	@Path("getElement")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getElement(@QueryParam("elementId") Long elementId, @QueryParam("context") String CONTEXT) {
		Element element = new Element();
		element.setId(elementId);
		EbuildleapResultObject ero = designerService.getElement(element, CONTEXT);
		if (ero.getResult() != null && ero.getResult().size() > 0) {
			Element resultElement = (Element) ero.getResult().get(0);
			resultElement.setElementManifestList(null);
			resultElement.setElementVariantList(null);
		}
		return ero;
	}

	@GET
	@Path("getElementTree")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getElementTree(@QueryParam("elementId") Long elementId, @QueryParam("context") String CONTEXT) {
		Element element = new Element();
		element.setId(elementId);
		EbuildleapResultObject ero = designerService.getElement(element, CONTEXT);
		return ero;
	}

	@GET
	@Path("getAllElement")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllElement() {
		EbuildleapResultObject ero = designerService.getAllElements();
		return ero;
	}

	@POST
	@Path("updateElement")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateElement(@FormParam("elementId") Long elementId, @FormParam("brandId") Long brandId,
			@FormParam("categoryId") Long categoryId, @FormParam("code1") String code1, @FormParam("costVersionId") Long costVersionId,
			@FormParam("cpr") Double cpr, @FormParam("createdByUserId") Long createdByUserId,
			@FormParam("elementDescription") String elementDescription, @FormParam("dimension") String dimension,
			@FormParam("elementThemeId") Long elementThemeId, @FormParam("elementVariantListId") Long elementVariantListId,
			@FormParam("finishId") Long finishId, @FormParam("function") Integer function, @FormParam("materialId") Long materialId,
			@FormParam("elementName") String elementName, @FormParam("scope") Integer scope, @FormParam("subTypeId") Long subTypeId,
			@FormParam("typeId") Long typeId, @FormParam("elementWeight") String elementWeight, @FormParam("context") String CONTEXT)
			throws JAXBException, IOException {

		Element updateElement = new Element();
		updateElement.setId(elementId);

		Brand brand = new Brand();
		brand.setId(brandId);
		updateElement.setBrand(brand);

		Category category = new Category();
		category.setId(categoryId);
		updateElement.setCategory(category);

		CostVersion costVersion = new CostVersion();
		costVersion.setId(costVersionId);
		updateElement.setCostVersion(costVersion);

		User createdBy = new User();
		createdBy.setId(createdByUserId);
		updateElement.setCreatedBy(createdBy);

		Theme elementTheme = new Theme();
		elementTheme.setId(elementThemeId);
		updateElement.addElementTheme(elementTheme);

		ElementVariantList elementVariantList = new ElementVariantList();
		elementVariantList.setId(elementVariantListId);
		updateElement.setElementVariantList(elementVariantList);

		Finish finish = new Finish();
		finish.setId(finishId);
		updateElement.setFinish(finish);

		Material material = new Material();
		material.setId(materialId);
		updateElement.setMaterial(material);

		SubType subType = new SubType();
		subType.setId(subTypeId);
		updateElement.setSubType(subType);

		Type type = new Type();
		type.setId(typeId);
		updateElement.setType(type);

		updateElement.setCode1(code1);

		updateElement.setCPR(cpr);

		updateElement.setDescription(elementDescription);

		updateElement.setDimension(dimension);

		updateElement.setFunction(function);

		updateElement.setName(elementName);

		updateElement.setScope(scope);

		updateElement.setWeight(elementWeight);

		EbuildleapResultObject ero = designerService.updateElement(updateElement, CONTEXT);
		return ero;
	}

	@GET
	@Path("deleteElement")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject deleteElement(@QueryParam("elementId") Long elementId, @QueryParam("context") String CONTEXT) {
		Element element = new Element();
		element.setId(elementId);
		EbuildleapResultObject ero = designerService.deleteElement(element, CONTEXT);
		return ero;
	}

	@GET
	@Path("searchElements")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject searchElements(@QueryParam("elementName") String elementName, @QueryParam("elementCode1") String elementCode1,
			@QueryParam("elementMaterialName") String elementMaterialName, @QueryParam("elementFinishName") String elementFinishName,
			@QueryParam("elementCategoryName") String elementCategoryName, @QueryParam("elementTypeName") String elementTypeName,
			@QueryParam("elementDescription") String elementDescription, @QueryParam("elementDimension") String elementDimension,
			@QueryParam("elementWeight") String elementWeight, @QueryParam("elementThemeName") String elementThemeName,
			@QueryParam("elementBrandName") String elementBrandName, @QueryParam("elementSubTypeName") String elementSubTypeName) {

		Element elementSearchCriteria = new Element();
		if (elementName != null && elementName.length() > 0) {
			elementSearchCriteria.setName(elementName);
		}
		if (elementCode1 != null && elementCode1.length() > 0) {
			elementSearchCriteria.setCode1(elementCode1);
		}
		if (elementMaterialName != null && elementMaterialName.length() > 0) {
			Material material = new Material();
			material.setName(elementMaterialName);
			elementSearchCriteria.setMaterial(material);
		}
		if (elementFinishName != null && elementFinishName.length() > 0) {
			Finish finish = new Finish();
			finish.setName(elementFinishName);
			elementSearchCriteria.setFinish(finish);
		}
		if (elementCategoryName != null && elementCategoryName.length() > 0) {
			Category category = new Category();
			category.setName(elementCategoryName);
			elementSearchCriteria.setCategory(category);
		}
		if (elementTypeName != null && elementTypeName.length() > 0) {
			Type type = new Type();
			type.setName(elementTypeName);
			elementSearchCriteria.setType(type);
		}
		if (elementDescription != null && elementDescription.length() > 0) {
			elementSearchCriteria.setDescription(elementDescription);
		}
		if (elementDimension != null && elementDimension.length() > 0) {
			elementSearchCriteria.setDimension(elementDimension);
		}
		if (elementWeight != null && elementWeight.length() > 0) {
			elementSearchCriteria.setWeight(elementWeight);
		}
		if (elementThemeName != null && elementThemeName.length() > 0) {
			Theme theme = new Theme();
			theme.setName(elementThemeName);
			elementSearchCriteria.addElementTheme(theme);
		}
		if (elementBrandName != null && elementBrandName.length() > 0) {
			Brand brand = new Brand();
			brand.setName(elementBrandName);
			elementSearchCriteria.setBrand(brand);
		}
		if (elementSubTypeName != null && elementSubTypeName.length() > 0) {
			SubType subType = new SubType();
			subType.setName(elementSubTypeName);
			elementSearchCriteria.setSubType(subType);
		}
		EbuildleapResultObject ero = designerService.searchElements(elementSearchCriteria);
		return ero;
	}

	@POST
	@Path("uploadElementViewFile")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public EbuildleapResultObject uploadElementViewFile(@FormDataParam("viewFile") InputStream fileInputStream,
			@FormDataParam("elementId") Long elementId, @FormDataParam("view") String view) throws JAXBException, IOException {
		Element element = new Element();
		element.setId(elementId);
		EbuildleapResultObject ero = designerService.uploadViewFile(element, IOUtils.toByteArray(fileInputStream), view);
		return ero;
	}

	@POST
	@Path("cloneElement")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject cloneElement(@FormParam("elementId") Long elementId, @FormParam("newElementName") String newElementName,
			@FormParam("newCode1") String newCode1, @FormParam("context") String CONTEXT) throws JAXBException, IOException {
		Element element = new Element();
		element.setId(elementId);
		Element cloneElement = new Element();
		cloneElement.setName(newElementName);
		cloneElement.setCode1(newCode1);
		EbuildleapResultObject ero = designerService.cloneElement(element, cloneElement, CONTEXT);
		return ero;
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("getElementVariants")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getElementVariants(@QueryParam("rootElementId") Long rootElementId,
			@QueryParam("currentElementManifestId") Long currentElementManifestId, @QueryParam("context") String CONTEXT) throws JAXBException,
			IOException {
		Element rootElement = new Element();
		rootElement.setId(rootElementId);
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(currentElementManifestId);
		EbuildleapResultObject ero = designerService.getVariantElements(rootElement, currentElementManifest, CONTEXT);
		List<Element> retVariants = new ArrayList<Element>();
		List<Element> serviceVariants = ero.getResult();
		if (serviceVariants != null) {
			for (Element variant : serviceVariants) {
				variant.setElementManifestList(null);
				variant.setElementVariantList(null);
				variant.setElementThemes(null);
				retVariants.add(variant);
			}
		}
		ero.setResult(retVariants);
		return ero;
	}

	@POST
	@Path("addVariant")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject addVariant(@FormParam("sourceElementId") Long sourceElementId, @FormParam("variantElementId") Long variantElementId)
			throws JAXBException, IOException {
		Element sourceElement = new Element();
		sourceElement.setId(sourceElementId);
		Element variantElement = new Element();
		variantElement.setId(variantElementId);
		EbuildleapResultObject ero = designerService.addVariantElement(sourceElement, variantElement);
		return ero;
	}

	@POST
	@Path("removeVariant")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject removeVariant(@FormParam("sourceElementId") Long sourceElementId,
			@FormParam("variantElementId") Long variantElementId) throws JAXBException, IOException {
		Element sourceElement = new Element();
		sourceElement.setId(sourceElementId);
		Element variantElement = new Element();
		variantElement.setId(variantElementId);
		EbuildleapResultObject ero = designerService.removeVariantElement(sourceElement, variantElement);
		return ero;
	}

	@POST
	@Path("createManifest")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createManifest(@FormParam("rootElementId") Long rootElementId,
			@FormParam("elementManifestId") Long elementManifestId, @FormParam("parentElementId") Long parentElementId,
			@FormParam("childElementId") Long childElementId, @FormParam("quantity") Double quantity, @FormParam("scale") Double scale,
			@FormParam("yscale") Double yscale, @FormParam("rotate") Double rotate, @FormParam("positionX") Double positionX,
			@FormParam("positionY") Double positionY, @FormParam("positionZ") Double positionZ, @FormParam("customize") Boolean customize,
			@FormParam("zOrder") Boolean zOrder, @FormParam("yOrder") Boolean yOrder, @FormParam("tag") String tag,
			@FormParam("elementGroup") Integer elementGroup, @FormParam("elementGroupTag") String elementGroupTag, @FormParam("prime") Integer prime,
			@FormParam("negativeElementVariants") String negativeElementVariants) throws JAXBException, IOException {
		Element rootElement = null;
		Element parentElement = null;
		Element childElement = null;
		ElementManifest contextElementManifest = null;

		rootElement = new Element();
		rootElement.setId(rootElementId);

		parentElement = new Element();
		parentElement.setId(parentElementId);

		childElement = new Element();
		childElement.setId(childElementId);

		contextElementManifest = new ElementManifest();
		contextElementManifest.setId(elementManifestId);

		ElementManifest newElementManifest = new ElementManifest();
		newElementManifest.setParentElement(parentElement);
		newElementManifest.setChildElement(childElement);
		if (negativeElementVariants != null && negativeElementVariants.length() > 0) {
			StringTokenizer st = new StringTokenizer(negativeElementVariants, ",");
			while (st.hasMoreTokens()) {
				Element negativeElement = new Element();
				negativeElement.setId(new Long(st.nextToken()).longValue());
				newElementManifest.addNegativeElementVariant(negativeElement);
			}
		}
		EbuildleapResultObject ero = designerService.createManifest(rootElement, contextElementManifest, newElementManifest);
		return ero;
	}

	@POST
	@Path("deleteManifest")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteManifest(@FormParam("rootElementId") Long rootElementId,
			@FormParam("elementManifestId") Long elementManifestId, @FormParam("context") String CONTEXT) throws JAXBException, IOException {
		Element rootElement = new Element();
		rootElement.setId(rootElementId);
		ElementManifest em = new ElementManifest();
		em.setId(elementManifestId);
		EbuildleapResultObject ero = designerService.deleteManifest(rootElement, em, CONTEXT);
		return ero;
	}

	@POST
	@Path("replaceManifest")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject replaceManifest(@FormParam("rootElementId") Long rootElementId,
			@FormParam("elementManifestId") Long elementManifestId, @FormParam("newChildElementId") Long newChildElementId,
			@FormParam("context") String CONTEXT) throws JAXBException, IOException {
		Element rootElement = new Element();
		rootElement.setId(rootElementId);
		ElementManifest em = new ElementManifest();
		em.setId(elementManifestId);
		Element newChildElement = new Element();
		newChildElement.setId(newChildElementId);
		EbuildleapResultObject ero = designerService.replaceManifest(rootElement, em, newChildElement, CONTEXT);
		return ero;
	}

	@POST
	@Path("updateElementManifest")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateElementManifest(@FormParam("rootElementId") Long rootElementId,
			@FormParam("currentElementManifestId") Long currentElementManifestId, @FormParam("quantity") Double quantity,
			@FormParam("scale") Double scale, @FormParam("yscale") Double yscale, @FormParam("rotate") Double rotate,
			@FormParam("positionX") Double positionX, @FormParam("positionY") Double positionY, @FormParam("positionZ") Double positionZ,
			@FormParam("customize") Boolean customize, @FormParam("zOrder") Boolean zOrder, @FormParam("yOrder") Boolean yOrder,
			@FormParam("tag") String tag, @FormParam("elementGroup") Integer elementGroup, @FormParam("elementGroupTag") String elementGroupTag,
			@FormParam("prime") Integer prime, @FormParam("negativeElementVariants") String negativeElementVariants,
			@FormParam("context") String CONTEXT) throws JAXBException, IOException {
		Element rootElement = new Element();
		rootElement.setId(rootElementId);
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(currentElementManifestId);
		currentElementManifest.setQuantity(quantity);
		currentElementManifest.setScale(scale);
		currentElementManifest.setYscale(yscale);
		currentElementManifest.setRotate(rotate);
		currentElementManifest.setPositionX(positionX);
		currentElementManifest.setPositionY(positionY);
		currentElementManifest.setPositionZ(positionZ);
		currentElementManifest.setCustomize(customize);
		currentElementManifest.setzOrder(zOrder);
		currentElementManifest.setyOrder(yOrder);
		currentElementManifest.setTag(tag);
		currentElementManifest.setElementGroup(elementGroup);
		currentElementManifest.setElementGroupTag(elementGroupTag);
		currentElementManifest.setPrime(prime);
		if (negativeElementVariants != null && negativeElementVariants.length() > 0) {
			StringTokenizer st = new StringTokenizer(negativeElementVariants, ",");
			while (st.hasMoreTokens()) {
				Element negativeElement = new Element();
				negativeElement.setId(new Long(st.nextToken()).longValue());
				currentElementManifest.addNegativeElementVariant(negativeElement);
			}
		}
		EbuildleapResultObject ero = designerService.updateManifest(rootElement, currentElementManifest, CONTEXT);
		return ero;
	}

	@GET
	@Path("getManifest")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getManifest(@QueryParam("rootElementId") Long rootElementId,
			@QueryParam("elementManifestId") Long elementManifestId, @QueryParam("context") String CONTEXT) throws JAXBException, IOException {
		Element rootElement = new Element();
		rootElement.setId(rootElementId);
		ElementManifest elementManifest = new ElementManifest();
		elementManifest.setId(elementManifestId);
		EbuildleapResultObject ero = designerService.getManifest(rootElement, elementManifest, CONTEXT);
		if (ero.getResult() != null && ero.getResult().size() > 0) {
			ElementManifest em = (ElementManifest) ero.getResult().get(0);
			em.getChildElement().setElementManifestList(null);
			if (em.getParentElement() != null) {
				em.getParentElement().setElementManifestList(null);
			}
			if (em.getNegativeElementVariants() != null) {
				em.setNegativeElementVariants(null);
			}
		}
		return ero;
	}

	@POST
	@Path("createProduct")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createProduct(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("code1") String code1, @FormParam("projectId") Long projectId, @FormParam("rootProductElementId") Long rootProductElementId)
			throws JAXBException, IOException {
		Element productElement = new Element();
		productElement.setId(rootProductElementId);
		Project project = new Project();
		project.setId(projectId);
		Product newProduct = new Product();
		newProduct.setName(name);
		newProduct.setDescription(description);
		newProduct.setCode1(code1);
		newProduct.setProject(project);
		newProduct.setElement(productElement);
		EbuildleapResultObject ero = designerService.createProduct(newProduct);
		return ero;
	}

	@POST
	@Path("createProductFromRevision")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createProductFromRevision(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("code1") String code1, @FormParam("homeUnitRevisionId") Long homeUnitRevisionId) throws JAXBException, IOException {
		Product newProduct = new Product();
		newProduct.setName(name);
		newProduct.setDescription(description);
		newProduct.setCode1(code1);
		HomeUnitRevision homeUnitRevision = new HomeUnitRevision();
		homeUnitRevision.setId(homeUnitRevisionId);
		EbuildleapResultObject ero = designerService.createProduct(homeUnitRevision, newProduct);
		return ero;
	}

	@GET
	@Path("getProduct")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getProduct(@QueryParam("productId") Long productId) throws JAXBException, IOException {
		Product product = new Product();
		product.setId(productId);
		EbuildleapResultObject ero = designerService.getProduct(product);
		return ero;
	}

	@GET
	@Path("getProducts")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getProducts(@QueryParam("projectId") Long projectId) throws JAXBException, IOException {
		Project project = new Project();
		project.setId(projectId);
		EbuildleapResultObject ero = designerService.getProducts(project);
		return ero;
	}
}

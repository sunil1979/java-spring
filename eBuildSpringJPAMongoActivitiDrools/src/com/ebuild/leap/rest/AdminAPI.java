package com.ebuild.leap.rest;

import java.io.IOException;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.service.AdminServiceFacadeImpl;

@Component
@Path("/adminapi")
public class AdminAPI {
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

	protected static Logger log = LoggerFactory.getLogger(AdminAPI.class);

	@Autowired
	private AdminServiceFacadeImpl adminService;

	@POST
	@Path("createBrand")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createBrand(@FormParam("brandName") String brandName, @FormParam("brandDescription") String brandDescription,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		Brand newBrand = new Brand();
		newBrand.setName(brandName);
		newBrand.setDescription(brandDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newBrand.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createBrand(newBrand);
		return ero;
	}

	@GET
	@Path("getBrand")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getBrand(@QueryParam("brandId") Long brandId) throws JAXBException, IOException {
		Brand brand = new Brand();
		brand.setId(brandId);
		EbuildleapResultObject ero = adminService.getBrand(brand);
		return ero;
	}

	@GET
	@Path("getAllBrand")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllBrand() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllBrand();
		return ero;
	}

	@POST
	@Path("updateBrand")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateBrand(@FormParam("brandId") Long brandId, @FormParam("brandName") String brandName,
			@FormParam("brandDescription") String brandDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Brand updatedBrand = new Brand();
		updatedBrand.setId(brandId);
		updatedBrand.setName(brandName);
		updatedBrand.setDescription(brandDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedBrand.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateBrand(updatedBrand);
		return ero;
	}

	@POST
	@Path("deleteBrand")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteBrand(@FormParam("brandId") Long brandId) throws JAXBException, IOException {
		Brand brand = new Brand();
		brand.setId(brandId);
		EbuildleapResultObject ero = adminService.deleteBrand(brand);
		return ero;
	}

	@POST
	@Path("createCategory")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createCategory(@FormParam("categoryName") String categoryName,
			@FormParam("categoryDescription") String categoryDescription, @FormParam("createdByUserId") Long createdByUserId) throws JAXBException,
			IOException {
		Category newCategory = new Category();
		newCategory.setName(categoryName);
		newCategory.setDescription(categoryDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newCategory.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createCategory(newCategory);
		return ero;
	}

	@GET
	@Path("getCategory")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getCategory(@QueryParam("categoryId") Long categoryId) throws JAXBException, IOException {
		Category category = new Category();
		category.setId(categoryId);
		EbuildleapResultObject ero = adminService.getCategory(category);
		return ero;
	}

	@GET
	@Path("getAllCategory")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllCategory() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllCategory();
		return ero;
	}

	@POST
	@Path("updateCategory")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateCategory(@FormParam("categoryId") Long categoryId, @FormParam("categoryName") String categoryName,
			@FormParam("categoryDescription") String categoryDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Category updatedCategory = new Category();
		updatedCategory.setId(categoryId);
		updatedCategory.setName(categoryName);
		updatedCategory.setDescription(categoryDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedCategory.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateCategory(updatedCategory);
		return ero;
	}

	@POST
	@Path("deleteCategory")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteCategory(@FormParam("categoryId") Long categoryId) throws JAXBException, IOException {
		Category category = new Category();
		category.setId(categoryId);
		EbuildleapResultObject ero = adminService.deleteCategory(category);
		return ero;
	}

	@POST
	@Path("createCPR")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createCPR(@FormParam("cprTag") String cprTag, @FormParam("cprValue") Double cprValue,
			@FormParam("cprComments") String cprComments, @FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		CPR newCPR = new CPR();
		newCPR.setTag(cprTag);
		newCPR.setValue(cprValue);
		newCPR.setComments(cprComments);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newCPR.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createCPR(newCPR);
		return ero;
	}

	@GET
	@Path("getCPR")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getCPR(@QueryParam("cprId") Long cprId) throws JAXBException, IOException {
		CPR cpr = new CPR();
		cpr.setId(cprId);
		EbuildleapResultObject ero = adminService.getCPR(cpr);
		return ero;
	}

	@GET
	@Path("getAllCPR")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllCPR() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllCPR();
		return ero;
	}

	@POST
	@Path("updateCPR")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateCPR(@FormParam("cprId") Long cprId, @FormParam("cprTag") String cprTag,
			@FormParam("cprValue") Double cprValue, @FormParam("cprComments") String cprComments, @FormParam("updatedByUserId") Long updatedByUserId)
			throws JAXBException, IOException {
		CPR updatedCPR = new CPR();
		updatedCPR.setId(cprId);
		updatedCPR.setTag(cprTag);
		updatedCPR.setComments(cprComments);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedCPR.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateCPR(updatedCPR);
		return ero;
	}

	@POST
	@Path("deleteCPR")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteCPR(@FormParam("cprId") Long cprId) throws JAXBException, IOException {
		CPR cpr = new CPR();
		cpr.setId(cprId);
		EbuildleapResultObject ero = adminService.deleteCPR(cpr);
		return ero;
	}

	@POST
	@Path("createCostVersion")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createCostVersion(@FormParam("costVersion") int costVersion, @FormParam("costValue") Double costValue,
			@FormParam("cprId") Long cprId, @FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		CostVersion newCostVersion = new CostVersion();
		newCostVersion.setCostVersion(costVersion);
		newCostVersion.setCostValue(costValue);
		CPR cpr = new CPR();
		cpr.setId(cprId);
		newCostVersion.setCpr(cpr);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newCostVersion.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createCostVersion(newCostVersion);
		return ero;
	}

	@GET
	@Path("getCostVersion")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getCostVersion(@QueryParam("costVersionId") Long costVersionId) throws JAXBException, IOException {
		CostVersion costVersion = new CostVersion();
		costVersion.setId(costVersionId);
		EbuildleapResultObject ero = adminService.getCostVersion(costVersion);
		return ero;
	}

	@GET
	@Path("getAllCostVersion")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllCostVersion() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllCostVersion();
		return ero;
	}

	@POST
	@Path("updateCostVersion")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateCostVersion(@FormParam("costVersionId") Long costVersionId, @FormParam("costVersion") int costVersion,
			@FormParam("costValue") Double costValue, @FormParam("cprId") Long cprId, @FormParam("updatedByUserId") Long updatedByUserId)
			throws JAXBException, IOException {
		CostVersion updatedCostVersion = new CostVersion();
		updatedCostVersion.setId(costVersionId);
		updatedCostVersion.setCostVersion(costVersion);
		updatedCostVersion.setCostValue(costValue);
		CPR cpr = new CPR();
		cpr.setId(cprId);
		updatedCostVersion.setCpr(cpr);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedCostVersion.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateCostVersion(updatedCostVersion);
		return ero;
	}

	@POST
	@Path("deleteCostVersion")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteCostVersion(@FormParam("costVersionId") Long costVersionId) throws JAXBException, IOException {
		CostVersion costVersion = new CostVersion();
		costVersion.setId(costVersionId);
		EbuildleapResultObject ero = adminService.deleteCostVersion(costVersion);
		return ero;
	}

	@POST
	@Path("createFinish")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createFinish(@FormParam("finishName") String finishName, @FormParam("finishDescription") String finishDescription,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		Finish newFinish = new Finish();
		newFinish.setName(finishName);
		newFinish.setDescription(finishDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newFinish.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createFinish(newFinish);
		return ero;
	}

	@GET
	@Path("getFinish")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getFinish(@QueryParam("finishId") Long finishId) throws JAXBException, IOException {
		Finish finish = new Finish();
		finish.setId(finishId);
		EbuildleapResultObject ero = adminService.getFinish(finish);
		return ero;
	}

	@GET
	@Path("getAllFinish")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllFinish() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllFinish();
		return ero;
	}

	@POST
	@Path("updateFinish")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateFinish(@FormParam("finishId") Long finishId, @FormParam("finishName") String finishName,
			@FormParam("finishDescription") String finishDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Finish updatedFinish = new Finish();
		updatedFinish.setId(finishId);
		updatedFinish.setName(finishName);
		updatedFinish.setDescription(finishDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedFinish.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateFinish(updatedFinish);
		return ero;
	}

	@POST
	@Path("deleteFinish")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteFinish(@FormParam("finishId") Long finishId) throws JAXBException, IOException {
		Finish finish = new Finish();
		finish.setId(finishId);
		EbuildleapResultObject ero = adminService.deleteFinish(finish);
		return ero;
	}

	@POST
	@Path("createMaterial")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createMaterial(@FormParam("materialName") String materialName,
			@FormParam("materialDescription") String materialDescription, @FormParam("createdByUserId") Long createdByUserId) throws JAXBException,
			IOException {
		Material newMaterial = new Material();
		newMaterial.setName(materialName);
		newMaterial.setDescription(materialDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newMaterial.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createMaterial(newMaterial);
		return ero;
	}

	@GET
	@Path("getMaterial")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getMaterial(@QueryParam("materialId") Long materialId) throws JAXBException, IOException {
		Material material = new Material();
		material.setId(materialId);
		EbuildleapResultObject ero = adminService.getMaterial(material);
		return ero;
	}

	@GET
	@Path("getAllMaterial")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllMaterial() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllMaterial();
		return ero;
	}

	@POST
	@Path("updateMaterial")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateMaterial(@FormParam("materialId") Long materialId, @FormParam("materialName") String materialName,
			@FormParam("materialDescription") String materialDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Material updatedMaterial = new Material();
		updatedMaterial.setId(materialId);
		updatedMaterial.setName(materialName);
		updatedMaterial.setDescription(materialDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedMaterial.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateMaterial(updatedMaterial);
		return ero;
	}

	@POST
	@Path("deleteMaterial")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteMaterial(@FormParam("materialId") Long materialId) throws JAXBException, IOException {
		Material material = new Material();
		material.setId(materialId);
		EbuildleapResultObject ero = adminService.deleteMaterial(material);
		return ero;
	}

	@POST
	@Path("createProject")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createProject(@FormParam("projectName") String projectName,
			@FormParam("projectDescription") String projectDescription, @FormParam("code1") String code1,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		Project newProject = new Project();
		newProject.setName(projectName);
		newProject.setDescription(projectDescription);
		newProject.setCode1(code1);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newProject.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createProject(newProject);
		return ero;
	}

	@GET
	@Path("getProject")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getProject(@QueryParam("projectId") Long projectId) throws JAXBException, IOException {
		Project project = new Project();
		project.setId(projectId);
		EbuildleapResultObject ero = adminService.getProject(project);
		return ero;
	}

	@GET
	@Path("getAllProject")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllProject() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllProject();
		return ero;
	}

	@POST
	@Path("updateProject")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateProject(@FormParam("projectId") Long projectId, @FormParam("projectName") String projectName,
			@FormParam("projectDescription") String projectDescription, @FormParam("code1") String code1,
			@FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException, IOException {
		Project updatedProject = new Project();
		updatedProject.setId(projectId);
		updatedProject.setName(projectName);
		updatedProject.setDescription(projectDescription);
		updatedProject.setCode1(code1);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedProject.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateProject(updatedProject);
		return ero;
	}

	@POST
	@Path("deleteProject")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteProject(@FormParam("projectId") Long projectId) throws JAXBException, IOException {
		Project project = new Project();
		project.setId(projectId);
		EbuildleapResultObject ero = adminService.deleteProject(project);
		return ero;
	}

	@POST
	@Path("createSubType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createSubType(@FormParam("subTypeName") String subTypeName,
			@FormParam("subTypeDescription") String subTypeDescription, @FormParam("createdByUserId") Long createdByUserId) throws JAXBException,
			IOException {
		SubType newSubType = new SubType();
		newSubType.setName(subTypeName);
		newSubType.setDescription(subTypeDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newSubType.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createSubType(newSubType);
		return ero;
	}

	@GET
	@Path("getSubType")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getSubType(@QueryParam("subTypeId") Long subTypeId) throws JAXBException, IOException {
		SubType subType = new SubType();
		subType.setId(subTypeId);
		EbuildleapResultObject ero = adminService.getSubType(subType);
		return ero;
	}

	@GET
	@Path("getAllSubType")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllSubType() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllSubType();
		return ero;
	}

	@POST
	@Path("updateSubType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateSubType(@FormParam("subTypeId") Long subTypeId, @FormParam("subTypeName") String subTypeName,
			@FormParam("subTypeDescription") String subTypeDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		SubType updatedSubType = new SubType();
		updatedSubType.setId(subTypeId);
		updatedSubType.setName(subTypeName);
		updatedSubType.setDescription(subTypeDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedSubType.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateSubType(updatedSubType);
		return ero;
	}

	@POST
	@Path("deleteSubType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteSubType(@FormParam("subTypeId") Long subTypeId) throws JAXBException, IOException {
		SubType subType = new SubType();
		subType.setId(subTypeId);
		EbuildleapResultObject ero = adminService.deleteSubType(subType);
		return ero;
	}

	@POST
	@Path("createTheme")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createTheme(@FormParam("themeName") String themeName, @FormParam("themeDescription") String themeDescription,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		Theme newTheme = new Theme();
		newTheme.setName(themeName);
		newTheme.setDescription(themeDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newTheme.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createTheme(newTheme);
		return ero;
	}

	@GET
	@Path("getTheme")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getTheme(@QueryParam("themeId") Long themeId) throws JAXBException, IOException {
		Theme theme = new Theme();
		theme.setId(themeId);
		EbuildleapResultObject ero = adminService.getTheme(theme);
		return ero;
	}

	@GET
	@Path("getAllTheme")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllTheme() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllTheme();
		return ero;
	}

	@POST
	@Path("updateTheme")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateTheme(@FormParam("themeId") Long themeId, @FormParam("themeName") String themeName,
			@FormParam("themeDescription") String themeDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Theme updatedTheme = new Theme();
		updatedTheme.setId(themeId);
		updatedTheme.setName(themeName);
		updatedTheme.setDescription(themeDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedTheme.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateTheme(updatedTheme);
		return ero;
	}

	@POST
	@Path("deleteTheme")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteTheme(@FormParam("themeId") Long themeId) throws JAXBException, IOException {
		Theme theme = new Theme();
		theme.setId(themeId);
		EbuildleapResultObject ero = adminService.deleteTheme(theme);
		return ero;
	}

	@POST
	@Path("createType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createType(@FormParam("typeName") String typeName, @FormParam("typeDescription") String typeDescription,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		Type newType = new Type();
		newType.setName(typeName);
		newType.setDescription(typeDescription);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newType.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createType(newType);
		return ero;
	}

	@GET
	@Path("getType")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getType(@QueryParam("typeId") Long typeId) throws JAXBException, IOException {
		Type type = new Type();
		type.setId(typeId);
		EbuildleapResultObject ero = adminService.getType(type);
		return ero;
	}

	@GET
	@Path("getAllType")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllType() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllType();
		return ero;
	}

	@POST
	@Path("updateType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateType(@FormParam("typeId") Long typeId, @FormParam("typeName") String typeName,
			@FormParam("typeDescription") String typeDescription, @FormParam("updatedByUserId") Long updatedByUserId) throws JAXBException,
			IOException {
		Type updatedType = new Type();
		updatedType.setId(typeId);
		updatedType.setName(typeName);
		updatedType.setDescription(typeDescription);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedType.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateType(updatedType);
		return ero;
	}

	@POST
	@Path("deleteType")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteType(@FormParam("typeId") Long typeId) throws JAXBException, IOException {
		Type type = new Type();
		type.setId(typeId);
		EbuildleapResultObject ero = adminService.deleteType(type);
		return ero;
	}

	@POST
	@Path("createUser")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createUser(@FormParam("firstName") String firstName, @FormParam("middleName") String middleName,
			@FormParam("lastName") String lastName, @FormParam("userName") String userName, @FormParam("password") String password,
			@FormParam("primaryPhone") String primaryPhone, @FormParam("alternatePhone") String alternatePhone, @FormParam("email") String email,
			@FormParam("createdByUserId") Long createdByUserId) throws JAXBException, IOException {
		User newUser = new User();
		newUser.setFirstName(firstName);
		newUser.setMiddleName(middleName);
		newUser.setLastName(lastName);
		newUser.setUsername(userName);
		newUser.setPassword(password);
		newUser.setPrimaryPhone(primaryPhone);
		newUser.setAlternatePhone(alternatePhone);
		newUser.setEmail(email);
		User createdBy = new User();
		createdBy.setId(createdByUserId);
		newUser.setCreatedBy(createdBy);
		EbuildleapResultObject ero = adminService.createUser(newUser);
		return ero;
	}

	@GET
	@Path("getUser")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getUser(@QueryParam("userId") Long userId) throws JAXBException, IOException {
		User user = new User();
		user.setId(userId);
		EbuildleapResultObject ero = adminService.getUser(user);
		return ero;
	}

	@GET
	@Path("getAllUser")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getAllUser() throws JAXBException, IOException {
		EbuildleapResultObject ero = adminService.getAllUser();
		return ero;
	}

	@POST
	@Path("updateUser")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject updateUser(@FormParam("userId") Long userId, @FormParam("firstName") String firstName,
			@FormParam("middleName") String middleName, @FormParam("lastName") String lastName, @FormParam("userName") String userName,
			@FormParam("password") String password, @FormParam("primaryPhone") String primaryPhone,
			@FormParam("alternatePhone") String alternatePhone, @FormParam("email") String email, @FormParam("updatedByUserId") Long updatedByUserId)
			throws JAXBException, IOException {
		User updatedUser = new User();
		updatedUser.setId(userId);
		updatedUser.setFirstName(firstName);
		updatedUser.setMiddleName(middleName);
		updatedUser.setLastName(lastName);
		updatedUser.setUsername(userName);
		updatedUser.setPassword(password);
		updatedUser.setPrimaryPhone(primaryPhone);
		updatedUser.setAlternatePhone(alternatePhone);
		updatedUser.setEmail(email);
		User updatedBy = new User();
		updatedBy.setId(updatedByUserId);
		updatedUser.setUpdatedBy(updatedBy);
		EbuildleapResultObject ero = adminService.updateUser(updatedUser);
		return ero;
	}

	@POST
	@Path("deleteUser")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject deleteUser(@FormParam("userId") Long userId) throws JAXBException, IOException {
		User user = new User();
		user.setId(userId);
		EbuildleapResultObject ero = adminService.deleteUser(user);
		return ero;
	}
}

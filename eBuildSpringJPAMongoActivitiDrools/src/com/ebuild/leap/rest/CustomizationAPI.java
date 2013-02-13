package com.ebuild.leap.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.service.CustomizationServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;

@Component
@Path("/customizationapi")
public class CustomizationAPI {

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

	protected static Logger log = LoggerFactory.getLogger(CustomizationAPI.class);

	@Autowired
	private CustomizationServiceFacadeImpl customizationService;
	
	
	@POST
	@Path("createHomeUnit")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createHomeUnit(
			@FormParam("ownerUserId") Long ownerUserId, 
			@FormParam("productId") Long productId, 
			@FormParam("costVersionId") Long costVersionId)
			throws JAXBException, IOException {
		User user = new User();
		user.setId(ownerUserId);
		Product product = new Product();
		product.setId(productId);
		CostVersion costVersion = new CostVersion();
		costVersion.setId(costVersionId);
		EbuildleapResultObject ero = customizationService.createHomeUnit(user, product, costVersion);
		ero.setResult(null);//Only to deal with browser client
		return ero;
	}

	
	@SuppressWarnings("unchecked")
	@GET
	@Path("getHomeUnitVersions")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getHomeUnitVersions(
			@QueryParam("homeUnitVersionId") Long homeUnitVersionId
			) throws JAXBException, IOException{
		HomeUnit homeUnit = new HomeUnit();
		homeUnit.setId(homeUnitVersionId);
		List<HomeUnitVersion> retHomeUnitVersions = new ArrayList<HomeUnitVersion>();
		EbuildleapResultObject ero = customizationService.getHomeUnitVersions(homeUnit);
		List<HomeUnitVersion> homeUnitVersionList = ero.getResult();
		for(HomeUnitVersion homeUnitVersion : homeUnitVersionList){
			homeUnitVersion.setHomeUnit(null);
			homeUnitVersion.setHomeUnitRevisions(null);
			retHomeUnitVersions.add(homeUnitVersion);
		}
		ero.setResult(retHomeUnitVersions);
		return ero;
	}
	
	
	@GET
	@Path("getLatestRevision")
	@Produces({ MediaType.APPLICATION_XML })
	public EbuildleapResultObject getLatestRevision(
			@QueryParam("homeUnitVersionId") Long homeUnitVersionId
			) throws JAXBException, IOException {
		HomeUnitVersion huVersion = new HomeUnitVersion();
		huVersion.setId(homeUnitVersionId);
		EbuildleapResultObject ero = customizationService.getLatestHomeUnitRevision(huVersion);
		return ero;
	}
	
	
	@POST
	@Path("createNewRevision")
	@Produces({ MediaType.APPLICATION_XML })
	@Consumes("application/x-www-form-urlencoded")
	public EbuildleapResultObject createNewRevision(
			@FormParam("currentHomeUnitRevisionId") Long currentHomeUnitRevisionId, 
			@FormParam("newChildElementId") Long newChildElementId,
			@FormParam("currentElementManifestId") Long currentElementManifestId)
			throws JAXBException, IOException {
		HomeUnitRevision homeUnitRevision = new HomeUnitRevision();
		homeUnitRevision.setId(currentHomeUnitRevisionId);
		Element newChildElement = new Element();
		newChildElement.setId(newChildElementId);
		ElementManifest currentElementManifest = new ElementManifest();
		currentElementManifest.setId(currentElementManifestId);
		EbuildleapResultObject ero = customizationService.createNewRevision(homeUnitRevision,newChildElement,currentElementManifest);
		ero.setResult(null);
		return ero;
	}
}

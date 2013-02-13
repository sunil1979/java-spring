package com.ebuild.leap.service;

import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;

public interface DesignerServiceFacade {

	public EbuildleapResultObject createElement(Element newElement);

	public EbuildleapResultObject getElement(Element element, String CONTEXT);

	public EbuildleapResultObject updateElement(Element updatedElement, String CONTEXT);

	public EbuildleapResultObject deleteElement(Element element, String CONTEXT);

	public EbuildleapResultObject cloneElement(Element sourceElement, Element cloneElement, String SOURCE_ELEMENT_CONTEXT);

	public EbuildleapResultObject getAllElements();

	public EbuildleapResultObject searchElements(Element searchCriteriaElement);

	public EbuildleapResultObject uploadViewFile(Element element, byte[] bytes, String VIEW);

	public EbuildleapResultObject createManifest(Element rootElement, ElementManifest contextElementManifest, ElementManifest newElementManifest);
	
	public EbuildleapResultObject deleteManifest(Element rootElement, ElementManifest elementManifest, String CONTEXT);

	public EbuildleapResultObject replaceManifest(Element rootElement, ElementManifest elementManifest, Element newChildElement, String CONTEXT);

	public EbuildleapResultObject updateManifest(Element rootElement, ElementManifest elementManifest, String CONTEXT);
	
	public EbuildleapResultObject getManifest(Element rootElement, ElementManifest elementManifest, String CONTEXT);

	public EbuildleapResultObject createProduct(Product product);

	public EbuildleapResultObject getProduct(Product product);

	public EbuildleapResultObject getProducts(Project project);

	public EbuildleapResultObject createProduct(HomeUnitRevision homeUnitRevision, Product product);

	/*
	 * Add variant related methods
	 */
	public EbuildleapResultObject getVariantElements(Element sourceElement, Element parentElement);
	
	public EbuildleapResultObject getVariantElements(Element rootElement, ElementManifest currentElementManifest, String CONTEXT);

	public EbuildleapResultObject addVariantElement(Element sourceElement, Element variantElement);

	public EbuildleapResultObject removeVariantElement(Element sourceElement, Element variantElement);

	
}

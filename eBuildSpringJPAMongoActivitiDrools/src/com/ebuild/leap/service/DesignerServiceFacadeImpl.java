package com.ebuild.leap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebuild.leap.drools.KnowledgeFactoryBean;
import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.ElementVariantList;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.BrandRepository;
import com.ebuild.leap.repository.jpa.CategoryRepository;
import com.ebuild.leap.repository.jpa.CostVersionRepository;
import com.ebuild.leap.repository.jpa.ElementManifestRepository;
import com.ebuild.leap.repository.jpa.ElementRepository;
import com.ebuild.leap.repository.jpa.ElementSearchSpecification;
import com.ebuild.leap.repository.jpa.ElementVariantListRepository;
import com.ebuild.leap.repository.jpa.FinishRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRepository;
import com.ebuild.leap.repository.jpa.MaterialRepository;
import com.ebuild.leap.repository.jpa.ProductRepository;
import com.ebuild.leap.repository.jpa.ProjectRepository;
import com.ebuild.leap.repository.jpa.SubTypeRepository;
import com.ebuild.leap.repository.jpa.ThemeRepository;
import com.ebuild.leap.repository.jpa.TypeRepository;
import com.ebuild.leap.repository.jpa.UserRepository;
import com.ebuild.leap.repository.mongodb.ElementMongoRepository;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;
import com.ebuild.leap.repository.mongodb.ProductMongoRepository;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapPropertiesUtil;
import com.ebuild.leap.util.EbuildleapUtil;
import com.ebuild.leap.util.NullAwareBeanUtilsBean;
import com.ebuild.leap.util.RandomIdGenerator;

@Service
public class DesignerServiceFacadeImpl implements DesignerServiceFacade {

	protected static Logger log = LoggerFactory.getLogger(DesignerServiceFacadeImpl.class);
	private EbuildleapResultObject ero = new EbuildleapResultObject();

	@Autowired
	EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;

	@Autowired
	ElementRepository elementRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	HomeUnitRepository homeUnitRepository;

	@Autowired
	HomeUnitRevisionMongoRepository homeUnitRevisionMongoRepository;

	@Autowired
	ElementMongoRepository elementMongoRepository;

	@Autowired
	ProductMongoRepository productMongoRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ElementManifestRepository elementManifestRepository;

	@Autowired
	CostVersionRepository costVersionRepository;

	@Autowired
	ElementVariantListRepository elementVariantListRepository;

	@Autowired
	ThemeRepository themeRepository;

	@Autowired
	FinishRepository finishRepository;

	@Autowired
	MaterialRepository materialRepository;

	@Autowired
	SubTypeRepository subTypeRepository;

	@Autowired
	TypeRepository typeRepository;

	@Autowired
	EbuildleapUtil ebuildLeapUtil;

	@Autowired
	NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

	@Autowired
	RandomIdGenerator randomIdGenerator;

	@Autowired
	@Qualifier("flattenTreeRulesKnowledge")
	KnowledgeFactoryBean flattenTreeRulesKnowledge;

	@PersistenceContext(unitName = "ebuildPersistenceUnit")
	private EntityManager entityManager;

	@Override
	@Transactional
	public EbuildleapResultObject createElement(Element newElement) {
		log.debug("START ---- INSIDE DesignerServiceFacadeImpl - getElement");
		try {
			ero.clear();
			/*
			 * user input validation for mandatory fields
			 */

			/*
			 * Perform validation on newElement. Throw exception if Validation
			 * fails
			 */
			newElement = validateElement(newElement);
			newElement = elementRepository.save(newElement);
			ArrayList<Element> result = new ArrayList<Element>();
			result.add(newElement);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE DesignerServiceFacadeImpl - createElement");
		return ero;
	}

	@Override
	public EbuildleapResultObject getElement(Element element, String CONTEXT) {
		log.debug("START ---- INSIDE DesignerServiceFacadeImpl - getElement");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (element == null || element.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			if (CONTEXT == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			Element resultElement = null;
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				// fetch from MONGO
				resultElement = elementMongoRepository.findOne(element.getId());
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				// fetch from RDBMS
				resultElement = elementRepository.findOne(element.getId());
			}
			if (resultElement == null) {
				// Element not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getId());
			}
			ArrayList<Element> result = new ArrayList<Element>();
			result.add(resultElement);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE DesignerServiceFacadeImpl - getElement");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateElement(Element updatedElement, String CONTEXT) {
		log.debug("START ---- INSIDE DesignerServiceFacadeImpl - updateElement");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (updatedElement == null || updatedElement.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			if (CONTEXT == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			// Check if element to be updated exists in DB
			Element persistedElement = null;
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				persistedElement = elementMongoRepository.findOne(updatedElement.getId());
				if (persistedElement != null) {
					updatedElement = validateElement(updatedElement);
					nullAwareBeanUtilsBean.copyProperties(persistedElement, updatedElement);
					persistedElement = elementMongoRepository.save(persistedElement);
				} else {
					// Element to be updated does not exist - throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + updatedElement.getId());
				}
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				persistedElement = elementRepository.findOne(updatedElement.getId());
				if (persistedElement != null) {
					updatedElement = validateElement(updatedElement);
					nullAwareBeanUtilsBean.copyProperties(persistedElement, updatedElement);
					persistedElement = elementRepository.save(persistedElement);
				} else {
					// Element to be updated does not exist - throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + updatedElement.getId());
				}
			}
			ArrayList<Element> result = new ArrayList<Element>();
			result.add(persistedElement);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE DesignerServiceFacadeImpl - updateElement");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteElement(Element element, String CONTEXT) {
		log.debug("START ---- INSIDE DesignerServiceFacadeImpl - deleteElement");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (element == null || element.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			if (CONTEXT == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			Element deleteElement = null;
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				deleteElement = elementMongoRepository.findOne(element.getId());
				if (deleteElement != null) {
					elementMongoRepository.delete(deleteElement);
				} else {
					// Element not found - throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + element.getId());
				}
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				deleteElement = elementRepository.findOne(element.getId());
				if (deleteElement != null) {
					elementRepository.delete(deleteElement);
				} else {
					// Element not found - throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + element.getId());
				}
			}
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE DesignerServiceFacadeImpl - deleteElement");
		return ero;
	}

	/*
	 * Clone an Element prensent in RDBMS or in MONGO. Create only an entry in
	 * ELEMENT table with new ID. All other attributes of Element should be
	 * copied to MongoDB. When copying to MongoDB, ensure to have different
	 * ElementManifestID for each ElementManifest present under Element Object
	 * (non-Javadoc)
	 */
	@Override
	@Transactional(rollbackFor = java.lang.Exception.class)
	public EbuildleapResultObject cloneElement(Element sourceElement, Element cloneElement, String SOURCE_ELEMENT_CONTEXT) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - cloneElement");
		try {
			/*
			 * validate user input
			 */
			if (sourceElement == null || ebuildLeapUtil.isNull(sourceElement.getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			if (SOURCE_ELEMENT_CONTEXT == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			ero.clear();
			Element newMongoElementClone = new Element();
			Element newRDBMSElementClone = new Element();
			Element sourceDataElement = null;
			if (SOURCE_ELEMENT_CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				sourceDataElement = elementMongoRepository.findOne(sourceElement.getId());
				if (sourceDataElement == null) {
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + sourceElement.getId());
				}
				nullAwareBeanUtilsBean.copyProperties(sourceDataElement, cloneElement);
				newRDBMSElementClone = deepCopyElementObject(sourceDataElement, newRDBMSElementClone, EbuildleapConstants.DATASTORE_RDBMS);
				newRDBMSElementClone = validateElement(newRDBMSElementClone);
				newMongoElementClone = deepCopyElementObject(sourceDataElement, newMongoElementClone, EbuildleapConstants.DATASTORE_MONGO);
				entityManager.detach(sourceDataElement);
				newRDBMSElementClone.setElementVariantList(null);
				newMongoElementClone.setElementVariantList(null);
				newRDBMSElementClone = elementRepository.save(newRDBMSElementClone);
				newMongoElementClone.setId(newRDBMSElementClone.getId());
				newMongoElementClone = elementMongoRepository.save(newMongoElementClone);
			}
			if (SOURCE_ELEMENT_CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				sourceDataElement = elementRepository.findOne(sourceElement.getId());
				if (sourceDataElement == null) {
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + sourceElement.getId());
				}
				nullAwareBeanUtilsBean.copyProperties(sourceDataElement, cloneElement);
				newRDBMSElementClone = deepCopyElementObject(sourceDataElement, newRDBMSElementClone, EbuildleapConstants.DATASTORE_RDBMS);
				newMongoElementClone = deepCopyElementObject(sourceDataElement, newMongoElementClone, EbuildleapConstants.DATASTORE_MONGO);
				entityManager.detach(sourceDataElement);
				newRDBMSElementClone.setElementVariantList(null);
				newMongoElementClone.setElementVariantList(null);
				newRDBMSElementClone = elementRepository.save(newRDBMSElementClone);
				newMongoElementClone.setId(newRDBMSElementClone.getId());
				newMongoElementClone = elementMongoRepository.save(newMongoElementClone);
			}
			/*
			 * Change all ElementManifest. Cannot change while saving since the
			 * JPA context still exists
			 */
			newMongoElementClone = elementMongoRepository.findOne(newMongoElementClone.getId());
			if (newMongoElementClone.getElementManifestList() != null) {
				for (ElementManifest em : newMongoElementClone.getElementManifestList()) {
					em = modifyElementManifestId(em);
				}
				newMongoElementClone = elementMongoRepository.save(newMongoElementClone);
			}
			// elementRepository.flush();
			ArrayList<Element> result = new ArrayList<Element>();
			result.add(newMongoElementClone);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CLONING_IL_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CLONING_IL_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - cloneElement");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllElements() {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - getAllElements");
		try {
			ero.clear();
			ArrayList<Element> result = (ArrayList<Element>) elementRepository.findAll();
			result.addAll(elementMongoRepository.findAll());
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_IL_ELEMENTS);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_IL_ELEMENTS) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getAllElements");
		return ero;
	}

	@Override
	public EbuildleapResultObject searchElements(Element searchCriteriaElement) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - searchElements");
		try {
			ero.clear();
			ero.setResult(elementRepository.findAll(ElementSearchSpecification.elementLike(searchCriteriaElement)));
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_SEARCHING_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_SEARCHING_ELEMENT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - searchElements");
		return ero;
	}

	@Override
	public EbuildleapResultObject uploadViewFile(Element element, byte[] bytes, String VIEW) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - uploadViewFile");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (element == null || element.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			if (VIEW == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_VIEW_TYPE));
			}

			String elementDataStoreContext = null;
			String dirPath = null;
			String physicalfileName = randomIdGenerator.getUniqueID() + ".svg";
			if (elementRepository.exists(element.getId())) {
				elementDataStoreContext = EbuildleapConstants.DATASTORE_RDBMS;
			} else if (elementMongoRepository.exists(element.getId())) {
				elementDataStoreContext = EbuildleapConstants.DATASTORE_MONGO;
			} else {
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getId());
			}
			if (VIEW.equalsIgnoreCase(EbuildleapConstants.ELEMENT_VIEW_1)) {
				dirPath = ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ELEMENT_VIEW_1_DIR);
				element.setView1(physicalfileName);
			}
			if (VIEW.equalsIgnoreCase(EbuildleapConstants.ELEMENT_VIEW_2)) {
				dirPath = ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ELEMENT_VIEW_2_DIR);
				element.setView2(physicalfileName);
			}
			if (VIEW.equalsIgnoreCase(EbuildleapConstants.ELEMENT_VIEW_3)) {
				dirPath = ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ELEMENT_VIEW_3_DIR);
				element.setView3(physicalfileName);
			}
			if (VIEW.equalsIgnoreCase(EbuildleapConstants.ELEMENT_VIEW_4)) {
				dirPath = ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ELEMENT_VIEW_4_DIR);
				element.setView4(physicalfileName);
			}
			/*
			 * write file
			 */
			Path filePath = Paths.get(dirPath);
			physicalfileName = dirPath + "/" + physicalfileName;
			File f = null;
			if (Files.exists(filePath)) {
				f = new File(physicalfileName);
			} else {
				Files.createDirectories(filePath);
				f = new File(physicalfileName);
			}
			System.out.println("Absolute Path :"+f.getAbsolutePath());
			System.out.println("Canonical Path :"+f.getCanonicalPath());
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
			/*
			 * update element
			 */
			ero = this.updateElement(element, elementDataStoreContext);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPLOADING_VIEW_FILE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPLOADING_VIEW_FILE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - uploadViewFile");
		return ero;
	}

	/*
	 * 
	 * CASE 1:- If rootElement is not passed, then add context is RDBMS else
	 * MONGO CASE 2:- If rootElement is passed and contextElementManifest is not
	 * passed, then add newElementManifest to rootElement in MONGO CASE 3:- if
	 * rootElement is not passed and contextElementManifest is not passed, then
	 * newElementManifest needs to be added to RDBMS. newElementManifest must
	 * contain ParentElement and ChildElement(in this case addToRootElement will
	 * be set to TRUE)
	 */
	public EbuildleapResultObject createManifest(Element rootElement, ElementManifest contextElementManifest, ElementManifest newElementManifest) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - createManifest");
		try {
			ero.clear();
			String CONTEXT = null;
			Boolean addToRootElement = false;
			if (rootElement == null || ebuildLeapUtil.isNull(rootElement.getId())) {
				// create manifest in RDBMS
				CONTEXT = EbuildleapConstants.DATASTORE_RDBMS;
			} else {
				CONTEXT = EbuildleapConstants.DATASTORE_MONGO;
			}

			if (contextElementManifest == null || ebuildLeapUtil.isNull(contextElementManifest.getId())) {
				addToRootElement = true;
			}

			if (newElementManifest == null || ebuildLeapUtil.isNull(newElementManifest.getChildElement())
					|| ebuildLeapUtil.isNull(newElementManifest.getChildElement().getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CHILD_ELEMENTID_IN_ELEMENTMANIFEST));
			}

			newElementManifest = validateElementManifest(newElementManifest);

			if (CONTEXT.equals(EbuildleapConstants.DATASTORE_RDBMS)) {
				/*
				 * validate user input for RDBMS
				 */
				if (addToRootElement) {
					if (newElementManifest == null || ebuildLeapUtil.isNull(newElementManifest.getParentElement())
							|| ebuildLeapUtil.isNull(newElementManifest.getParentElement().getId())) {
						throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PARENT_ELEMENTID_IN_ELEMENTMANIFEST));
					}
				} else {
					ElementManifest contextElementManifestData = elementManifestRepository.findOne(contextElementManifest.getId());
					if (contextElementManifestData == null) {
						// throw exception
						throw new DataRetrievalFailureException(
								ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - "
										+ contextElementManifest.getId());
					}
					newElementManifest.setParentElement(contextElementManifestData.getChildElement());
				}
				newElementManifest = elementManifestRepository.save(newElementManifest);
			}
			if (CONTEXT.equals(EbuildleapConstants.DATASTORE_MONGO)) {
				/*
				 * validate user input for MONGO
				 */
				Element rootElementData = elementMongoRepository.findOne(rootElement.getId());
				newElementManifest.setId(randomIdGenerator.getUniqueID());
				if (rootElementData == null) {
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}
				if (addToRootElement) {
					rootElementData.addElementManifest(newElementManifest);
				} else {
					/*
					 * Get the flattened list of tree containing elements
					 * connected by elementmanifest
					 */
					KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
					StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
					ArrayList<Object> treeObjects = new ArrayList<Object>();
					ksession.setGlobal("treeObjects", treeObjects);
					ksession.insert(rootElementData);
					ksession.fireAllRules();
					ksession.dispose();
					/*
					 * Loop thru flattened tree containing elements and
					 * elementmanifest. add the newly created elementManifest
					 * under parent element (**Explore the possibility of using
					 * lambdaj**)
					 */
					for (Object o : treeObjects) {
						if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(contextElementManifest.getId())) {
							((ElementManifest) o).getChildElement().addElementManifest(newElementManifest);
						}
					}
				}
				for (ElementManifest em : rootElementData.getElementManifestList()) {
					em = modifyElementManifestId(em);
				}
				rootElementData = elementMongoRepository.save(rootElementData);
			}
			List<ElementManifest> result = new ArrayList<ElementManifest>();
			result.add(newElementManifest);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - createManifest");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteManifest(Element rootElement, ElementManifest contextElementManifest, String CONTEXT) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - deleteManifest");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (contextElementManifest == null || ebuildLeapUtil.isNull(contextElementManifest.getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}
			if (ebuildLeapUtil.isNull(CONTEXT)) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			Element rootDataElement = null;
			// elementManifest = validateElementManifest(elementManifest);
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				if (rootElement == null || ebuildLeapUtil.isNull(rootElement.getId())) {
					throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
				}
				rootDataElement = elementMongoRepository.findOne(rootElement.getId());
				if (rootDataElement == null) {
					// Root Element not found in MONGO
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}
				/*
				 * Get the flattened list of tree containing elements connected
				 * by elementmanifest
				 */
				KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				ArrayList<Object> treeObjects = new ArrayList<Object>();
				ksession.setGlobal("treeObjects", treeObjects);
				ksession.insert(rootDataElement);
				ksession.fireAllRules();
				ksession.dispose();
				/*
				 * Loop thru flattened tree containing elements and
				 * elementmanifest. remove elementManifest under parent element
				 * (**Explore the possibility of using lambdaj**)
				 */
				ElementManifest emTobeRemoved = null;
				Boolean elementManifestFound = false;
				for (Object o : treeObjects) {
					if (o instanceof Element) {

						for (ElementManifest em : ((Element) o).getElementManifestList()) {
							if (em.getId().equals(contextElementManifest.getId())) {
								emTobeRemoved = em;
							}
						}
						if (emTobeRemoved != null) {
							((Element) o).getElementManifestList().remove(emTobeRemoved);
							elementManifestFound = true;
						}
					}
				}
				if (!elementManifestFound) {
					// Throw exception - child element to be removed not found
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + contextElementManifest.getId());
				}
				rootDataElement = elementMongoRepository.save(rootDataElement);
			}

			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				if (!entityManager.contains(contextElementManifest)) {
					ElementManifest elementManifestData = elementManifestRepository.findOne(contextElementManifest.getId());
					if (elementManifestData == null) {
						// ElementManifest does not exist in RDBMS
						throw new DataRetrievalFailureException(
								ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - "
										+ contextElementManifest.getId());
					}
					elementManifestRepository.delete(elementManifestData);
				}
			}
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - deleteManifest");
		return ero;
	}

	public EbuildleapResultObject replaceManifest(Element rootElement, ElementManifest contextElementManifest, Element newChildElement, String CONTEXT) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - replaceManifest");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (contextElementManifest == null || ebuildLeapUtil.isNull(contextElementManifest.getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}
			if (ebuildLeapUtil.isNull(CONTEXT)) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}

			ArrayList<Element> result = new ArrayList<Element>();
			Element rootDataElement = null;
			contextElementManifest = validateElementManifest(contextElementManifest);
			Element newChildDataElement = elementRepository.findOne(newChildElement.getId());
			if (newChildDataElement == null) {
				// NewChildElement not found in RDBMS
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + newChildElement.getId());
			} else {
				// Check if there exists a latest element with manifest in MONGO
				if (newChildDataElement.getElementManifestList() == null && elementMongoRepository.exists(newChildDataElement.getId())) {
					newChildDataElement = elementMongoRepository.findOne(newChildDataElement.getId());
				}
				// change manifestid for all elementmanifest under
				// newchilddataelement
				for (ElementManifest em : newChildDataElement.getElementManifestList()) {
					em = modifyElementManifestId(em);
				}
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				if (rootElement == null || ebuildLeapUtil.isNull(rootElement.getId()) || newChildElement == null
						|| ebuildLeapUtil.isNull(newChildElement.getId())) {
					throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
				}
				rootDataElement = elementMongoRepository.findOne(rootElement.getId());
				if (rootDataElement == null) {
					// Root Element not found in MONGO
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}

				/*
				 * Get the flattened list of tree containing elements connected
				 * by elementmanifest
				 */
				KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				ArrayList<Object> treeObjects = new ArrayList<Object>();
				ksession.setGlobal("treeObjects", treeObjects);
				ksession.insert(rootDataElement);
				ksession.fireAllRules();
				ksession.dispose();
				/*
				 * Loop thru flattened tree containing elements and
				 * elementmanifest. Modify the elementmanifest by replacing old
				 * childelement with new child element (**Explore the
				 * possibility of using lambdaj**)
				 */
				for (Object o : treeObjects) {
					if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(contextElementManifest.getId())) {
						((ElementManifest) o).setChildElement(newChildDataElement);
					}
				}
				elementMongoRepository.save(rootDataElement);
				/*
				 * Returns ROOT DATA ELEMENT if MONGO
				 */
				result.add(rootDataElement);
			}

			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				ElementManifest elementManifestData = elementManifestRepository.findOne(contextElementManifest.getId());
				if (elementManifestData == null) {
					// ElementManifest not found in RDBMS
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + contextElementManifest.getId());
				}
				elementManifestData.setChildElement(newChildDataElement);
				elementManifestRepository.save(elementManifestData);
				/*
				 * Returns element containing modified ElementManifest
				 */
				result.add(elementManifestData.getParentElement());
			}
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_ADDING_ELEMENT_TO_IL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - replaceManifest");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateManifest(Element rootElement, ElementManifest elementManifest, String CONTEXT) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - updateManifest");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (elementManifest == null || ebuildLeapUtil.isNull(elementManifest.getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}
			if (CONTEXT == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}
			ArrayList<Element> result = new ArrayList<Element>();
			ElementManifest targetElementManifest = null;
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				if (rootElement == null || ebuildLeapUtil.isNull(rootElement.getId())) {
					throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
				}
				Element rootDataElement = elementMongoRepository.findOne(rootElement.getId());
				if (rootDataElement == null) {
					// RootElement not found in MONGO
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}

				/*
				 * Get the flattened list of tree containing elements connected
				 * by elementmanifest
				 */
				KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				ArrayList<Object> treeObjects = new ArrayList<Object>();
				ksession.setGlobal("treeObjects", treeObjects);
				ksession.insert(rootDataElement);
				ksession.fireAllRules();
				ksession.dispose();
				/*
				 * Loop thru flattened tree containing elements and
				 * elementmanifest. Modify the elementmanifest by replacing old
				 * childelement with new child element (**Explore the
				 * possibility of using lambdaj**)
				 */
				for (Object o : treeObjects) {
					if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(elementManifest.getId())) {
						nullAwareBeanUtilsBean.copyProperties(((ElementManifest) o), elementManifest);
						o = validateElementManifest(((ElementManifest) o));
					}
				}

				/*
				 * Updated MONGO
				 */
				rootDataElement = elementMongoRepository.save(rootDataElement);
				result.add(rootDataElement);
			}

			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				targetElementManifest = elementManifestRepository.findOne(elementManifest.getId());
				if (targetElementManifest == null) {
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + elementManifest.getId());
				}
				nullAwareBeanUtilsBean.copyProperties(targetElementManifest, elementManifest);
				targetElementManifest = validateElementManifest(targetElementManifest);
				targetElementManifest = elementManifestRepository.save(targetElementManifest);
				result.add(targetElementManifest.getParentElement());
			}
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_IL_ELEMENT_MANIFEST);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_IL_ELEMENT_MANIFEST) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}

		log.debug("END ---- INSIDE ElementServiceFacadeImpl - updateManifest");
		return ero;
	}

	public EbuildleapResultObject getManifest(Element rootElement, ElementManifest elementManifest, String CONTEXT) {
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getManifest");
		try {
			ero.clear();
			List<ElementManifest> result = new ArrayList<ElementManifest>();
			if (ebuildLeapUtil.isNull(CONTEXT)) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}
			if (elementManifest == null || ebuildLeapUtil.isNull(elementManifest.getId())) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				ElementManifest elementManifestData = elementManifestRepository.findOne(elementManifest.getId());
				if (elementManifestData == null) {
					// throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + elementManifest.getId());
				}
				result.add(elementManifestData);
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				if (rootElement == null || ebuildLeapUtil.isNull(rootElement)) {
					throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
				}
				Element rootElementData = elementMongoRepository.findOne(rootElement.getId());
				if (rootElementData == null) {
					// throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}

				/*
				 * Get the flattened list of tree containing elements connected
				 * by elementmanifest
				 */
				KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				ArrayList<Object> treeObjects = new ArrayList<Object>();
				ksession.setGlobal("treeObjects", treeObjects);
				ksession.insert(rootElementData);
				ksession.fireAllRules();
				ksession.dispose();
				/*
				 * loop thru tree objects to find the required elementmanifest
				 */
				for (Object o : treeObjects) {
					if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(elementManifest.getId())) {
						result.add((ElementManifest) o);
					}
				}
			}
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_ELEMENTMANIFEST);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_ELEMENTMANIFEST) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getManifest");
		return ero;
	}

	@Override
	@Transactional
	public EbuildleapResultObject createProduct(Product product) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - createProduct");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (product == null || product.getProject() == null || product.getProject().getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PROJECT_ID));
			}
			if (product == null || product.getElement() == null || product.getElement().getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			Project project = projectRepository.findOne(product.getProject().getId());
			if (project == null) {
				// Throw Exception - project not found
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getProject().getId());
			}
			product.setProject(project);
			Element productRootElement = elementRepository.findOne(product.getElement().getId());
			if (productRootElement == null) {
				// Throw Exception - root element not found
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getElement().getId());
			}
			product.setElement(productRootElement);
			product = productRepository.save(product);
			product = productMongoRepository.save(product);
			ArrayList<Product> result = new ArrayList<Product>();
			result.add(product);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_PRODUCT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_PRODUCT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - createProduct");
		return ero;
	}

	@Override
	public EbuildleapResultObject createProduct(HomeUnitRevision homeUnitRevision, Product product) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - createProduct");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (homeUnitRevision == null || homeUnitRevision.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_HOMEUNITREVISION_ID));
			}
			HomeUnitRevision homeUnitRevisionData = homeUnitRevisionMongoRepository.findOne(homeUnitRevision.getId());
			if (homeUnitRevisionData == null) {
				// HomeUnitRevision not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getElement().getId());
			}

			Product revisionProduct = homeUnitRevisionData.getHomeUnitVersion().getHomeUnit().getProduct();
			nullAwareBeanUtilsBean.copyProperties(revisionProduct, product);
			Product productRDBMS = new Product();
			nullAwareBeanUtilsBean.copyProperties(productRDBMS, revisionProduct);
			// product.setElement(null);
			productRDBMS = validateProduct(productRDBMS);
			productRDBMS = productRepository.save(productRDBMS);
			revisionProduct.setId(productRDBMS.getId());
			revisionProduct = productMongoRepository.save(revisionProduct);
			ArrayList<Product> result = new ArrayList<Product>();
			result.add(revisionProduct);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_PRODUCT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_PRODUCT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - createProduct");
		return ero;
	}

	@Override
	public EbuildleapResultObject getProduct(Product product) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - getProduct");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (product == null || product.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PRODUCT_ID));
			}

			Product productData = productMongoRepository.findOne(product.getId());
			if (productData == null) {
				// Throw Exception - product not found
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getId());
			}
			ArrayList<Product> result = new ArrayList<Product>();
			result.add(productData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_PRODUCT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_PRODUCT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getProduct");
		return ero;
	}

	@Override
	public EbuildleapResultObject getProducts(Project project) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - getProducts");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (project == null || project.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PROJECT_ID));
			}
			ArrayList<Product> result = (ArrayList<Product>) productMongoRepository.findByProject(project);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_PRODUCTS);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_PRODUCTS) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getProducts");
		return ero;
	}

	@Override
	public EbuildleapResultObject getVariantElements(Element sourceElement, Element parentElement) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - getVariantElements");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (sourceElement == null || ebuildLeapUtil.isNull(sourceElement.getId()) || parentElement == null
					|| ebuildLeapUtil.isNull(parentElement.getId())) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			/*
			 * STEP 1:- Refresh sourceDataElement and parentDataElement
			 */
			Element sourceDataElement = elementRepository.findOne(sourceElement.getId());
			if (sourceDataElement == null) {
				sourceDataElement = elementMongoRepository.findOne(sourceElement.getId());
				if (sourceDataElement == null) {
					// Source Element not found in RDBMS or MONGO
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + sourceElement.getId());
				}
			}
			Element parentDataElement = elementRepository.findOne(parentElement.getId());
			if (parentDataElement == null) {
				parentDataElement = elementMongoRepository.findOne(parentElement.getId());
				if (parentDataElement == null) {
					// Parent Element not found in RDBMS or MONGO
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + parentElement.getId());
				}
			}
			List<Element> negativeVariants = new ArrayList<Element>();
			/*
			 * STEP 2:- Find Variants for sourceElement. Remove elements from
			 * sourceElement variants which are mentioned as negative variants
			 * in manifest having sourceElement as child and parentElement as
			 * parent
			 */
			for (ElementManifest em : parentDataElement.getElementManifestList()) {
				if (em.getChildElement().getId().equals(sourceDataElement.getId())) {
					negativeVariants.addAll(em.getNegativeElementVariants());
				}
			}
			if (sourceDataElement.getElementVariantList() != null) {
				ero.setResult(ebuildLeapUtil.getUniqueItems(sourceDataElement.getElementVariantList().getVariants(), negativeVariants));
			}
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT_VARIANTS);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT_VARIANTS) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getVariantElements");
		return ero;
	}

	@Override
	public EbuildleapResultObject getVariantElements(Element rootElement, ElementManifest currentElementManifest, String CONTEXT) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - getVariantElements");
		try {
			ero.clear();
			List<Element> result = new ArrayList<Element>();
			if (currentElementManifest == null || ebuildLeapUtil.isNull(currentElementManifest.getId())) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}
			if (ebuildLeapUtil.isNull(CONTEXT)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CONTEXT));
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_RDBMS)) {
				ElementManifest currentElementManifestData = elementManifestRepository.findOne(currentElementManifest.getId());
				if (currentElementManifestData == null) {
					// throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + currentElementManifest.getId());
				}
				// variant elements
				if (currentElementManifestData.getChildElement().getElementVariantList() != null) {
					// check for negative variants
					if (currentElementManifestData.getNegativeElementVariants() != null
							&& currentElementManifestData.getNegativeElementVariants().size() > 0) {
						result.addAll(ebuildLeapUtil.getUniqueItems(currentElementManifestData.getChildElement().getElementVariantList()
								.getVariants(), currentElementManifestData.getNegativeElementVariants()));
					} else {
						result.addAll(currentElementManifestData.getChildElement().getElementVariantList().getVariants());
					}
				}
			}
			if (CONTEXT.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO)) {
				if (rootElement != null && ebuildLeapUtil.isNull(rootElement.getId())) {
					// throw exception
					throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
				}
				Element rootElementData = elementMongoRepository.findOne(rootElement.getId());
				if (rootElementData == null) {
					// throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + rootElement.getId());
				}

				/*
				 * Get the flattened list of tree containing elements connected
				 * by elementmanifest
				 */
				KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				ArrayList<Object> treeObjects = new ArrayList<Object>();
				ksession.setGlobal("treeObjects", treeObjects);
				ksession.insert(rootElementData);
				ksession.fireAllRules();
				ksession.dispose();
				/*
				 * Loop thru flattened tree containing elements and
				 * elementmanifest. Modify the elementmanifest by replacing old
				 * childelement with new child element (**Explore the
				 * possibility of using lambdaj**)
				 */
				for (Object o : treeObjects) {
					if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(currentElementManifest.getId())) {
						if (((ElementManifest) o).getChildElement().getElementVariantList() != null) {
							ElementVariantList elementVariantList = elementVariantListRepository.findOne(((ElementManifest) o).getChildElement().getElementVariantList().getId());
							if(elementVariantList == null){
								//throw exception
								throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
										+ " - " + ((ElementManifest) o).getChildElement().getElementVariantList().getId());
							}
							
							if (((ElementManifest) o).getNegativeElementVariants() != null
									&& ((ElementManifest) o).getNegativeElementVariants().size() > 0) {
								result.addAll(ebuildLeapUtil.getUniqueElementList(elementVariantList.getVariants(), ((ElementManifest) o).getNegativeElementVariants()));
							} else {
								result.addAll(elementVariantList.getVariants());
							}
						}
					}
				}
			}
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT_VARIANTS);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_ELEMENT_VARIANTS) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - getVariantElements");
		return ero;
	}

	@Override
	public EbuildleapResultObject addVariantElement(Element sourceElement, Element variantElement) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - addVariantElement");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (sourceElement == null || sourceElement.getId() == null || variantElement == null || variantElement.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}

			Element sourceDataElement = elementRepository.findOne(sourceElement.getId());
			if (sourceDataElement == null) {
				// sourceelement not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + sourceElement.getId());
			}
			Element variantDataElement = elementRepository.findOne(variantElement.getId());
			if (variantDataElement == null) {
				// variantelement not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + variantElement.getId());
			}
			if (sourceDataElement.getElementVariantList() != null) {
				// add variantelement to elementvariantlist
				if(variantDataElement.getElementVariantList() != null && variantDataElement.getElementVariantList().getVariants() != null){
					variantDataElement.getElementVariantList().removeVariantElement(variantDataElement);
				}
				sourceDataElement.getElementVariantList().addVariantElement(variantDataElement);
				variantDataElement.setElementVariantList(sourceDataElement.getElementVariantList());
				
			} else {
				ElementVariantList elementVariantList = new ElementVariantList();
				elementVariantList.addVariantElement(variantDataElement);
				elementVariantList.addVariantElement(sourceDataElement);
				sourceDataElement.setElementVariantList(elementVariantList);
				variantDataElement.setElementVariantList(elementVariantList);
			}
			elementRepository.save(variantDataElement);
			elementRepository.save(sourceDataElement);
			elementRepository.flush();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_ADDING_VARIANT_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_ADDING_VARIANT_ELEMENT) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - addVariantElement");
		return ero;
	}

	@Override
	public EbuildleapResultObject removeVariantElement(Element sourceElement, Element variantElement) {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - removeVariantElement");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (sourceElement == null || sourceElement.getId() == null || variantElement == null || variantElement.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}
			Element sourceDataElement = elementRepository.findOne(sourceElement.getId());
			if (sourceDataElement == null) {
				// sourceelement not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + sourceElement.getId());
			}
			Element variantDataElement = elementRepository.findOne(variantElement.getId());
			if (variantDataElement == null) {
				// variantelement not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + variantElement.getId());
			}
			if (sourceDataElement.getElementVariantList() != null) {
				// add variantelement to elementvariantlist
				sourceDataElement.getElementVariantList().getVariants().remove(variantDataElement);
			} else {
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.NO_VARIANTLIST_FOUND) + " - "
						+ sourceDataElement.getId());
			}
			sourceDataElement = elementRepository.save(sourceDataElement);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_VARIANT_ELEMENT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_VARIANT_ELEMENT) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - removeVariantElement");
		return ero;
	}

	/*
	 * Utility method to validate Product object
	 */
	private Product validateProduct(Product product) throws Exception {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - validateProduct");
		if (product.getCreatedBy() != null && !entityManager.contains(product.getCreatedBy())) {
			User createdBy = userRepository.findOne(product.getCreatedBy().getId());
			if (createdBy == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getCreatedBy().getId());
			}
			product.setCreatedBy(createdBy);
		}

		if (product.getElement() != null && !entityManager.contains(product.getElement())) {
			Element element = elementRepository.findOne(product.getElement().getId());
			if (element == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getElement().getId());
			}
			product.setElement(element);
		}

		if (product.getProject() != null && !entityManager.contains(product.getProject())) {
			Project project = projectRepository.findOne(product.getProject().getId());
			if (project == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getProject().getId());
			}
			product.setProject(project);
		}

		if (product.getProductHomeUnits() != null) {
			List<HomeUnit> homeUnitList = new ArrayList<HomeUnit>();
			homeUnitList.addAll(product.getProductHomeUnits());
			product.getProductHomeUnits().clear();
			for (HomeUnit hm : homeUnitList) {
				if (!entityManager.contains(hm)) {
					HomeUnit homeUnit = homeUnitRepository.findOne(hm.getId());
					if (homeUnit == null) {
						// Throw exception
						throw new DataRetrievalFailureException(
								ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - " + hm.getId());
					}
					product.getProductHomeUnits().add(homeUnit);
				} else {
					product.getProductHomeUnits().add(hm);
				}
			}
		}

		if (product.getUpdatedBy() != null && !entityManager.contains(product.getUpdatedBy())) {
			User updatedBy = userRepository.findOne(product.getUpdatedBy().getId());
			if (updatedBy == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getUpdatedBy().getId());
			}
			product.setUpdatedBy(updatedBy);
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - validateProduct");
		return product;
	}

	/*
	 * Utility method to validate Element object
	 */
	private Element validateElement(Element element) throws Exception {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - validateElement");
		if (!ebuildLeapUtil.isNull(element.getBrand()) && !entityManager.contains(element.getBrand())) {
			Brand brand = brandRepository.findOne(element.getBrand().getId());
			if (brand == null) {
				// brand not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getBrand().getId());
			} else {
				element.setBrand(brand);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getCategory()) && !entityManager.contains(element.getCategory())) {
			Category category = categoryRepository.findOne(element.getCategory().getId());
			if (category == null) {
				// category not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getCategory().getId());
			} else {
				element.setCategory(category);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getCostVersion()) && !entityManager.contains(element.getCostVersion())) {
			CostVersion costVersion = costVersionRepository.findOne(element.getCostVersion().getId());
			if (costVersion == null) {
				// costversion not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getCostVersion().getId());
			} else {
				element.setCostVersion(costVersion);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getCreatedBy()) && !entityManager.contains(element.getCreatedBy())) {
			User createdBy = userRepository.findOne(element.getCreatedBy().getId());
			if (createdBy == null) {
				// createdBy user not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getCreatedBy().getId());
			} else {
				element.setCreatedBy(createdBy);
			}
		}

		if (element.getElementManifestList() != null) {
			List<ElementManifest> elementManifestList = new ArrayList<ElementManifest>();
			elementManifestList.addAll(element.getElementManifestList());
			element.getElementManifestList().clear();
			for (ElementManifest em : elementManifestList) {
				if (!ebuildLeapUtil.isNull(em) && !entityManager.contains(em)) {
					ElementManifest newEm = elementManifestRepository.findOne(em.getId());
					if (newEm == null) {
						// ElementManifest not found - throw exception
						throw new DataRetrievalFailureException(
								ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - " + em.getId());
					} else {
						element.addElementManifest(newEm);
					}
				} else {
					element.addElementManifest(em);
				}
			}
		}

		if (element.getElementThemes() != null) {
			List<Theme> elementThemesList = new ArrayList<Theme>();
			elementThemesList.addAll(element.getElementThemes());
			element.getElementThemes().clear();
			for (Theme theme : elementThemesList) {
				if (!ebuildLeapUtil.isNull(theme) && !entityManager.contains(theme)) {
					Theme newTheme = themeRepository.findOne(theme.getId());
					if (newTheme == null) {
						// Theme not found - throw exception
						throw new DataRetrievalFailureException(
								ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - " + theme.getId());
					} else {
						element.addElementTheme(newTheme);
					}
				} else {
					element.addElementTheme(theme);
				}
			}
		}

		if (!ebuildLeapUtil.isNull(element.getElementVariantList()) && !entityManager.contains(element.getElementVariantList())) {
			ElementVariantList evList = elementVariantListRepository.findOne(element.getElementVariantList().getId());
			if (evList == null) {
				// ElementVariantList not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getElementVariantList().getId());
			} else {
				element.setElementVariantList(evList);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getFinish()) && !entityManager.contains(element.getFinish())) {
			Finish finish = finishRepository.findOne(element.getFinish().getId());
			if (finish == null) {
				// Finish not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getFinish().getId());
			} else {
				element.setFinish(finish);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getMaterial()) && !entityManager.contains(element.getMaterial())) {
			Material newMaterial = materialRepository.findOne(element.getMaterial().getId());
			if (newMaterial == null) {
				// Material not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getMaterial().getId());
			} else {
				element.setMaterial(newMaterial);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getSubType()) && !entityManager.contains(element.getSubType())) {
			SubType newSubType = subTypeRepository.findOne(element.getSubType().getId());
			if (newSubType == null) {
				// SubType not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getSubType().getId());
			} else {
				element.setSubType(newSubType);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getType()) && !entityManager.contains(element.getType())) {
			Type newType = typeRepository.findOne(element.getType().getId());
			if (newType == null) {
				// Type not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getType().getId());
			} else {
				element.setType(newType);
			}
		}

		if (!ebuildLeapUtil.isNull(element.getUpdatedBy()) && !entityManager.contains(element.getUpdatedBy())) {
			User updatedBy = userRepository.findOne(element.getUpdatedBy().getId());
			if (updatedBy == null) {
				// User not found - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + element.getUpdatedBy().getId());
			} else {
				element.setUpdatedBy(updatedBy);
			}
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - validateElement");
		return element;
	}

	/*
	 * Used to deep copy sourceElement to clonedElement BeanUtils.copyProperties
	 * will copy only references for List objects. Explicit copy is required for
	 * List type attributes
	 */
	private Element deepCopyElementObject(Element sourceElement, Element clonedILElement, String context) throws Exception {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - deepCopyElementObject");
		org.springframework.beans.BeanUtils.copyProperties(sourceElement, clonedILElement);
		clonedILElement.setId(null);
		clonedILElement.setElementManifestList(null);
		if (context.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO) && sourceElement.getElementManifestList() != null) {
			for (ElementManifest em : sourceElement.getElementManifestList()) {
				clonedILElement.addElementManifest(em);
			}
		}
		clonedILElement.setElementThemes(null);
		if (context.equalsIgnoreCase(EbuildleapConstants.DATASTORE_MONGO) && sourceElement.getElementThemes() != null) {
			for (Theme t : sourceElement.getElementThemes()) {
				clonedILElement.addElementTheme(t);
			}
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - deepCopyElementObject");
		return clonedILElement;
	}

	/*
	 * This method changes the ID of ElementManifest object and all other
	 * ElementManifest objects under the input ElementManifest object
	 */
	private ElementManifest modifyElementManifestId(ElementManifest elementManifest) throws Exception {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - modifyElementManifestId");
		elementManifest.setId(randomIdGenerator.getUniqueID());
		if (elementManifest.getChildElement().getElementManifestList() != null
				&& elementManifest.getChildElement().getElementManifestList().size() > 0) {
			for (ElementManifest em : elementManifest.getChildElement().getElementManifestList()) {
				modifyElementManifestId(em);
			}
		}
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - modifyElementManifestId");
		return elementManifest;
	}

	private ElementManifest validateElementManifest(ElementManifest elementManifest) throws Exception {
		log.debug("START ---- INSIDE ElementServiceFacadeImpl - validateElementManifest");
		if (!ebuildLeapUtil.isNull(elementManifest.getChildElement()) && !entityManager.contains(elementManifest.getChildElement())) {
			Element childElement = elementRepository.findOne(elementManifest.getChildElement().getId());
			if (childElement != null) {
				elementManifest.setChildElement(childElement);
			} else {
				childElement = elementMongoRepository.findOne(elementManifest.getChildElement().getId());
				if (childElement != null) {
					elementManifest.setChildElement(childElement);
				} else {
					// child element not found in RDBMS
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + elementManifest.getChildElement().getId());
				}
			}
		}

		if (!ebuildLeapUtil.isNull(elementManifest.getCreatedBy()) && !entityManager.contains(elementManifest.getCreatedBy())) {
			User createdBy = userRepository.findOne(elementManifest.getCreatedBy().getId());
			if (createdBy != null) {
				elementManifest.setCreatedBy(createdBy);
			} else {
				// created by User not found in RDBMS
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + elementManifest.getCreatedBy().getId());
			}
		}

		if (elementManifest.getNegativeElementVariants() != null) {
			List<Element> negativeElementVariants = new ArrayList<Element>();
			negativeElementVariants.addAll(elementManifest.getNegativeElementVariants());
			elementManifest.getNegativeElementVariants().clear();
			for (Element negativeElement : negativeElementVariants) {
				if (!ebuildLeapUtil.isNull(negativeElement) && !entityManager.contains(negativeElement)) {
					Element negativeDataElement = elementRepository.findOne(negativeElement.getId());
					if (negativeDataElement != null) {
						elementManifest.addNegativeElementVariant(negativeDataElement);
					} else {
						negativeDataElement = elementMongoRepository.findOne(negativeElement.getId());
						if (negativeDataElement != null) {
							elementManifest.addNegativeElementVariant(negativeDataElement);
						} else {
							// NegativeElementVariant not found in RDBMS
							throw new DataRetrievalFailureException(
									ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE) + " - "
											+ negativeElement.getId());
						}
					}
				} else {
					elementManifest.addNegativeElementVariant(negativeElement);
				}

			}
		}

		if (!ebuildLeapUtil.isNull(elementManifest.getParentElement()) && !entityManager.contains(elementManifest.getParentElement())) {
			Element parentDataElement = elementRepository.findOne(elementManifest.getParentElement().getId());
			if (parentDataElement != null) {
				elementManifest.setParentElement(parentDataElement);
			} else {
				parentDataElement = elementMongoRepository.findOne(elementManifest.getParentElement().getId());
				if (parentDataElement != null) {
					elementManifest.setParentElement(parentDataElement);
				} else {
					// ParentElement not found in RDBMS
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + elementManifest.getParentElement().getId());
				}
			}
		}

		if (!ebuildLeapUtil.isNull(elementManifest.getUpdatedBy()) && !entityManager.contains(elementManifest.getUpdatedBy())) {
			User updatedBy = userRepository.findOne(elementManifest.getUpdatedBy().getId());
			if (updatedBy != null) {
				elementManifest.setUpdatedBy(updatedBy);
			} else {
				// User not found in RDBMS
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + elementManifest.getUpdatedBy().getId());
			}
		}
		log.debug("END ---- INSIDE ElementServiceFacadeImpl - validateElementManifest");
		return elementManifest;
	}
}

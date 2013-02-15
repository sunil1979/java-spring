package com.ebuild.leap.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ebuild.leap.drools.KnowledgeFactoryBean;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.CostVersionRepository;
import com.ebuild.leap.repository.jpa.ElementRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRevisionRepository;
import com.ebuild.leap.repository.jpa.HomeUnitVersionRepository;
import com.ebuild.leap.repository.jpa.ProductRepository;
import com.ebuild.leap.repository.jpa.UserRepository;
import com.ebuild.leap.repository.mongodb.ElementManifestMongoRepository;
import com.ebuild.leap.repository.mongodb.ElementMongoRepository;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapPropertiesUtil;

@Component
@TransactionConfiguration(defaultRollback = false)
public class CustomizationServiceFacadeImpl implements CustomizationServiceFacade {

	protected static Logger log = LoggerFactory.getLogger(CustomizationServiceFacadeImpl.class);
	private EbuildleapResultObject ero = new EbuildleapResultObject();

	@Autowired
	EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ElementRepository elementRepository;

	@Autowired
	ElementMongoRepository elementMongoRepository;

	@Autowired
	CostVersionRepository costVersionRepository;

	@Autowired
	HomeUnitRepository homeUnitRepository;

	@Autowired
	HomeUnitRevisionRepository homeUnitRevisionRepository;

	@Autowired
	HomeUnitVersionRepository homeUnitVersionRepository;

	@Autowired
	HomeUnitRevisionMongoRepository homeUnitRevisionMongoRepository;

	@Autowired
	@Qualifier("bathroomRulesKnowledge")
	KnowledgeFactoryBean bathroomRulesKnowledge;

	@Autowired
	@Qualifier("flattenTreeRulesKnowledge")
	KnowledgeFactoryBean flattenTreeRulesKnowledge;

	@Autowired
	@Qualifier("finishPaletteKnowledge")
	private KnowledgeFactoryBean finishPaletteKnowledge;

	@Autowired
	@Qualifier("impactLinkKnowledge")
	private KnowledgeFactoryBean impactLinkKnowledge;

	@Override
	@Transactional
	public EbuildleapResultObject createHomeUnit(User user, Product product, CostVersion costVersion) {
		log.debug("START ---- INSIDE HomeUnitServiceFacadeImpl - createHomeUnit");
		try {
			ero.clear();
			/*
			 * Validate User Input
			 */
			if (user == null || user.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_USER_ID));
			}

			if (product == null || product.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PRODUCT_ID));
			}

			if (costVersion == null || costVersion.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_COSTVERSION_ID));
			}
			/*
			 * STEP 1:- Refresh User, Product and CostVersion objects
			 */
			User userData = userRepository.findOne(user.getId());
			if (userData == null) {
				// User not found in RDBMS - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + user.getId());
			}
			Product productData = productRepository.findOne(product.getId());
			if (productData == null) {
				// Product not found in RDBMS - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + product.getId());
			}
			CostVersion costVersionData = costVersionRepository.findOne(costVersion.getId());
			if (costVersionData == null) {
				// CostVersion not found in RDBMS - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + costVersion.getId());
			}
			/*
			 * STEP 2:- Create Home Unit along with default Version and
			 * Revision. Attach User, Product and CostVersion to the created
			 * HomeUnit
			 */
			HomeUnit homeUnit = new HomeUnit();
			homeUnit.setOwnerUser(userData);
			homeUnit.setProduct(productData);
			homeUnit.setCostVersion(costVersionData);
			homeUnit.setName(product.getName() + "_" + user.getUsername());
			HomeUnitVersion defaultVersion = new HomeUnitVersion();
			defaultVersion.setVersionNumber(EbuildleapConstants.DEFAULT_HOMEUNIT_VERSION_NUMBER);
			defaultVersion.setVersionTag(EbuildleapConstants.DEFAULT_HOMEUNIT_REVISIONTAG);
			HomeUnitRevision defaultRevision = new HomeUnitRevision();
			defaultRevision.setRevisionNumber(EbuildleapConstants.DEFAULT_HOMEUNIT_REVISION_NUMBER);
			defaultRevision.setRevisionTag(EbuildleapConstants.DEFAULT_HOMEUNIT_REVISIONTAG);
			defaultVersion.addHomeUnitRevision(defaultRevision);
			homeUnit.addHomeUnitVersion(defaultVersion);
			homeUnit = homeUnitRepository.saveAndFlush(homeUnit);
			log.debug("Home Unit Id :" + homeUnit.getId());
			HomeUnitRevision latestRevision = homeUnit.getHomeUnitVersions().get(0).getHomeUnitRevisions().get(0);
			/*
			 * STEP 3:- Save HomeUnit Revision in Mongo
			 */
			HomeUnitRevision mongoLatestRevision = homeUnitRevisionMongoRepository.save(latestRevision);
			ArrayList<HomeUnitRevision> result = new ArrayList<HomeUnitRevision>();
			result.add(mongoLatestRevision);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
			ero.setResult(result);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_HOMEUNIT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_HOMEUNIT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END ---- INSIDE HomeUnitServiceFacadeImpl - createHomeUnit");
		return ero;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ebuild.leap.service.HomeUnitServiceFacade#getHomeUnitVersion(com.
	 * ebuild.leap.pojo.HomeUnitVersion)
	 * 
	 * Returns Latest HomeUnit Revision for the given HomeUnit Version
	 */
	@Override
	public EbuildleapResultObject getLatestHomeUnitRevision(HomeUnitVersion homeUnitVersion) {
		log.debug("START ---- INSIDE HomeUnitServiceFacadeImpl - getLatestHomeUnitRevision");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (homeUnitVersion == null || homeUnitVersion.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_HOMEUNITVERSION_ID));
			}
			/*
			 * STEP 1:- Pick the latest HomeUnit Revision for the HomeUnit
			 * Version
			 */
			Long latestRevisionId = homeUnitRevisionRepository.getLatestRevisionId(homeUnitVersion.getId());
			if (latestRevisionId == null) {
				// NO REVISIONS FOR THE VERSION. SET ERROR and RETURN
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.NO_REVISION_FOUND_FOR_VERSION)
						+ " - " + homeUnitVersion.getId());
			}
			/*
			 * STEP 2:- Pick the Home Unit Revision from NOSQL DB
			 */
			HomeUnitRevision mongoLatestRevision = homeUnitRevisionMongoRepository.findOne(latestRevisionId);
			if (mongoLatestRevision == null) {
				// Revision not found in MONGO - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + latestRevisionId);
			}
			ArrayList<HomeUnitRevision> result = new ArrayList<HomeUnitRevision>();
			result.add(mongoLatestRevision);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
			ero.setResult(result);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_LATEST_HOMEUNITREVISION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_LATEST_HOMEUNITREVISION) + " - "
					+ e.getClass() + ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE HomeUnitServiceFacadeImpl - getLatestHomeUnitRevision");
		return ero;
	}

	@Override
	public EbuildleapResultObject createNewRevision(HomeUnitRevision currentHomeUnitRevision, Element newChildElement,
			ElementManifest currentElementManifest, Element ILElement) {
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (currentHomeUnitRevision == null || currentHomeUnitRevision.getId() == null) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_HOMEUNITREVISION_ID));
			}

			if (newChildElement == null || newChildElement.getId() == null) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
			}

			if (currentElementManifest == null || currentElementManifest.getId() == null) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENTMANIFEST_ID));
			}

			/*
			 * STEP 1:- Create new HomeUnitRevision in RDBMS and retrieve new
			 * HomeUnitRevision ID
			 */
			HomeUnitRevision currentHomeUnitRevisionData = homeUnitRevisionRepository.findOne(currentHomeUnitRevision.getId());
			if (currentHomeUnitRevisionData == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + currentHomeUnitRevision.getId());
			}
			Element newChildElementData = elementRepository.findOne(newChildElement.getId());
			if (newChildElementData == null) {
				// Throw exception - childElement not found in RDBMS
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + newChildElement.getId());
			} else if (elementMongoRepository.exists(newChildElementData.getId())) {
				newChildElementData = elementMongoRepository.findOne(newChildElementData.getId());
			}
			HomeUnitVersion homeUnitVersion = currentHomeUnitRevisionData.getHomeUnitVersion();

			HomeUnitRevision newRevision = new HomeUnitRevision();
			newRevision.setHomeUnitVersion(homeUnitVersion);
			newRevision.setRevisionNumber(currentHomeUnitRevisionData.getRevisionNumber() + 1);
			newRevision.setRevisionTag(EbuildleapConstants.AUTO_NEW_HOMEUNIT_REVISIONTAG + homeUnitVersion.getVersionTag());
			newRevision = homeUnitRevisionRepository.saveAndFlush(newRevision);
			/*
			 * STEP 2:- Retrieve old HomeUnitRevision from NOSQL DB
			 */
			HomeUnitRevision newMongoHomeUnitRevision = homeUnitRevisionMongoRepository.findOne(currentHomeUnitRevisionData.getId());
			/*
			 * STEP 3:- Change the HomeUnitRevisionID and other relevant
			 * properties of the HomeUnitRevision retrieved from NOSQL DB
			 */
			newMongoHomeUnitRevision.setId(newRevision.getId());
			newMongoHomeUnitRevision.setRevisionTag(newRevision.getRevisionTag());
			newMongoHomeUnitRevision.setRevisionNumber(newRevision.getRevisionNumber());
			/*
			 * Get the flattened list of tree containing elements connected by
			 * elementmanifest
			 */
			KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			ArrayList<Object> treeObjects = new ArrayList<Object>();
			ksession.setGlobal("treeObjects", treeObjects);
			ksession.insert(newMongoHomeUnitRevision.getHomeUnitVersion().getHomeUnit().getProduct().getElement());
			ksession.fireAllRules();
			ksession.dispose();
			/*
			 * Loop thru flattened tree containing elements and elementmanifest.
			 * Modify the elementmanifest by replacing old childelement with new
			 * child element (**Explore the possibility of using lambdaj**)
			 */
			for (Object o : treeObjects) {
				if (o instanceof ElementManifest && ((ElementManifest) o).getId().equals(currentElementManifest.getId())) {
					((ElementManifest) o).setChildElement(newChildElementData);
				}
			}
			/*
			 * APPLY RULES
			 */
			newMongoHomeUnitRevision = applyFinishRules(newMongoHomeUnitRevision, ILElement, newChildElementData);
			/*
			 * Save New Revision to Mongo Repository
			 */
			newMongoHomeUnitRevision = homeUnitRevisionMongoRepository.save(newMongoHomeUnitRevision);
			ArrayList<HomeUnitRevision> result = new ArrayList<HomeUnitRevision>();
			result.add(newMongoHomeUnitRevision);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
			ero.setResult(result);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_NEW_HOMEUNITREVISION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_NEW_HOMEUNITREVISION) + " - "
					+ e.getClass() + ": " + e.getMessage());
		}
		return ero;
	}

	private HomeUnitRevision applyFinishRules(HomeUnitRevision newMongoHomeUnitRevision, Element ILElement, Element newChildElementData)
			throws Exception {
		/*
		 * Lookup for compatible finish values
		 */
		KnowledgeBase kbase = (KnowledgeBase) finishPaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		List<Integer> compatibleList = new ArrayList<Integer>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentFinish", newChildElementData.getFinish().getId());
		ksession.fireAllRules();
		ksession.dispose();
		System.out.println("Compatible List Size :" + compatibleList.size());
		for (Integer entry : compatibleList) {
			System.out.println("List Entry :" + entry);
		}

		/*
		 * Identify types and categories of elements that need to be verified
		 * with in IL
		 */
		kbase = (KnowledgeBase) impactLinkKnowledge.getObject();
		ksession = kbase.newStatefulKnowledgeSession();
		Set<Integer> categoryCriteriaList = new HashSet<Integer>();
		Set<Integer> typeCriteriaList = new HashSet<Integer>();
		ksession.setGlobal("categoryCriteriaList", categoryCriteriaList);
		ksession.setGlobal("typeCriteriaList", typeCriteriaList);
		ksession.setGlobal("rootElement", ILElement);
		ksession.setGlobal("changedElement", newChildElementData);
		ksession.fireAllRules();
		ksession.dispose();
		System.out.println("categoryCriteriaList List Size :" + categoryCriteriaList.size());
		for (Integer entry : categoryCriteriaList) {
			System.out.println("List Entry :" + entry);
		}
		System.out.println("typeCriteriaList List Size :" + typeCriteriaList.size());
		for (Integer entry : typeCriteriaList) {
			System.out.println("List Entry :" + entry);
		}

		return null;
	}

	private HomeUnitRevision applyBathroomRules(HomeUnitRevision newMongoHomeUnitRevision) throws Exception {
		/*
		 * STEP 1:- Retrieve all the BATHROOM IL node elements
		 */
		List<Element> nodeList = new ArrayList<Element>();
		/*
		 * STEP 2:- For each BATHROOM IL nodel elements, apply BATHROOM related
		 * rules
		 */
		for (Element bathroomILElement : nodeList) {
			KnowledgeBase bathroomKnowledgeBase = (KnowledgeBase) bathroomRulesKnowledge.getObject();
			StatefulKnowledgeSession bathroomKnowledgeSession = bathroomKnowledgeBase.newStatefulKnowledgeSession();
			bathroomKnowledgeSession.insert(bathroomILElement);
			bathroomKnowledgeSession.fireAllRules();
		}
		return newMongoHomeUnitRevision;
	}

	@Override
	public EbuildleapResultObject getHomeUnitVersions(HomeUnit homeUnit) {
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (homeUnit == null || homeUnit.getId() == null) {
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_HOMEUNIT_ID));
			}
			ArrayList<HomeUnitVersion> result = new ArrayList<HomeUnitVersion>();
			result.addAll(homeUnitVersionRepository.findByHomeUnit_Id(homeUnit.getId()));
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
			ero.setResult(result);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_HOMEUNIT_VERSIONS);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_HOMEUNIT_VERSIONS) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		return ero;
	}
}

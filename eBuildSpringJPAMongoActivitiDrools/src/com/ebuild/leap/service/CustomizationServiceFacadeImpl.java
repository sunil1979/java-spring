package com.ebuild.leap.service;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.azeckoski.reflectutils.ReflectUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.type.FactType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ebuild.leap.drools.KnowledgeFactoryBean;
import com.ebuild.leap.drools.LookupPalette;
import com.ebuild.leap.drools.LookupPaletteUtil;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Rule;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.CostVersionRepository;
import com.ebuild.leap.repository.jpa.ElementRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRepository;
import com.ebuild.leap.repository.jpa.HomeUnitRevisionRepository;
import com.ebuild.leap.repository.jpa.HomeUnitVersionRepository;
import com.ebuild.leap.repository.jpa.ProductRepository;
import com.ebuild.leap.repository.jpa.RuleRepository;
import com.ebuild.leap.repository.jpa.UserRepository;
import com.ebuild.leap.repository.mongodb.ElementManifestMongoRepository;
import com.ebuild.leap.repository.mongodb.ElementMongoRepository;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;
import com.ebuild.leap.repository.mongodb.ProductMongoRepository;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapPropertiesUtil;

@Service
@TransactionConfiguration(defaultRollback = false)
public class CustomizationServiceFacadeImpl implements CustomizationServiceFacade {

	protected static Logger log = LoggerFactory.getLogger(CustomizationServiceFacadeImpl.class);
	private EbuildleapResultObject ero = new EbuildleapResultObject();
	private Element rootElement = null;
	private Element newChildElementData = null;

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	private EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMongoRepository productMongoRepository;

	@Autowired
	private ElementRepository elementRepository;

	@Autowired
	private ElementMongoRepository elementMongoRepository;

	@Autowired
	private CostVersionRepository costVersionRepository;

	@Autowired
	private HomeUnitRepository homeUnitRepository;

	@Autowired
	private HomeUnitRevisionRepository homeUnitRevisionRepository;

	@Autowired
	private HomeUnitVersionRepository homeUnitVersionRepository;

	@Autowired
	private HomeUnitRevisionMongoRepository homeUnitRevisionMongoRepository;

	@Autowired
	private DesignerServiceFacadeImpl designerService;

	@Autowired
	private LookupPalette lookupPalette;

	@Autowired
	@Qualifier("flattenTreeRulesKnowledge")
	private KnowledgeFactoryBean flattenTreeRulesKnowledge;

	public DesignerServiceFacadeImpl getDesignerService() {
		return designerService;
	}

	public void setDesignerService(DesignerServiceFacadeImpl designerService) {
		this.designerService = designerService;
	}

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
			Product producMongoData = productMongoRepository.findOne(product.getId());
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
			defaultVersion.setVersionTag(EbuildleapConstants.DEFAULT_HOMEUNIT_VERSIONTAG);
			HomeUnitRevision defaultRevision = new HomeUnitRevision();
			defaultRevision.setRevisionNumber(EbuildleapConstants.DEFAULT_HOMEUNIT_REVISION_NUMBER);
			defaultRevision.setRevisionTag(EbuildleapConstants.DEFAULT_HOMEUNIT_REVISIONTAG);
			defaultVersion.addHomeUnitRevision(defaultRevision);
			homeUnit.addHomeUnitVersion(defaultVersion);
			System.out.println("BEFORE VERSION ID :" + defaultVersion.getId());
			System.out.println("BEFORE REVISION ID :" + defaultRevision.getId());
			homeUnit = homeUnitRepository.save(homeUnit);
			System.out.println("AFTER VERSION ID :" + defaultVersion.getId());
			System.out.println("AFTER REVISION ID :" + defaultRevision.getId());
			log.debug("Home Unit Id :" + homeUnit.getId());
			HomeUnitRevision latestRevision = homeUnit.getHomeUnitVersions().get(0).getHomeUnitRevisions().get(0);
			/*
			 * STEP 3:- Save HomeUnit Revision in Mongo
			 */
			if (producMongoData != null) {
				latestRevision.getHomeUnitVersion().getHomeUnit().setProduct(producMongoData);
			}
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
			HomeUnitRevision latestRevision = homeUnitRevisionRepository.getLatestRevisionId(homeUnitVersion.getId());
			if (latestRevision == null) {
				// NO REVISIONS FOR THE VERSION. SET ERROR and RETURN
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.NO_REVISION_FOUND_FOR_VERSION)
						+ " - " + homeUnitVersion.getId());
			}
			/*
			 * STEP 2:- Pick the Home Unit Revision from NOSQL DB
			 */
			HomeUnitRevision mongoLatestRevision = homeUnitRevisionMongoRepository.findOne(latestRevision.getId());
			if (mongoLatestRevision == null) {
				// Revision not found in MONGO - throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + latestRevision.getId());
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
	public EbuildleapResultObject getHomeUnitRevision(HomeUnitRevision homeUnitRevision) {
		log.debug("START ---- INSIDE HomeUnitServiceFacadeImpl - getHomeUnitRevision");
		try {
			ero.clear();
			/*
			 * validate user input
			 */
			if (homeUnitRevision == null || homeUnitRevision.getId() == null) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_HOMEUNITREVISION_ID));
			}
			HomeUnitRevision homeUnitRevisionData = homeUnitRevisionMongoRepository.findOne(homeUnitRevision.getId());
			if (homeUnitRevisionData == null) {
				// Throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + homeUnitRevision.getId());
			}
			ArrayList<HomeUnitRevision> result = new ArrayList<HomeUnitRevision>();
			result.add(homeUnitRevisionData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
			ero.setResult(result);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_HOMEUNITREVISION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_HOMEUNITREVISION) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END ---- INSIDE HomeUnitServiceFacadeImpl - getHomeUnitRevision");
		return ero;
	}

	@Override
	public EbuildleapResultObject createNewRevision(HomeUnitRevision currentHomeUnitRevision, Element newChildElement,
			ElementManifest currentElementManifest, Element scopeElement) {
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

			if (scopeElement == null || scopeElement.getId() == null) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_ELEMENT_ID));
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
			newChildElementData = elementRepository.findOne(newChildElement.getId());
			if (newChildElementData == null) {
				// Throw exception - childElement not found in RDBMS
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + newChildElement.getId());
			} else if (elementMongoRepository.exists(newChildElementData.getId())) {
				newChildElementData = elementMongoRepository.findOne(newChildElementData.getId());
			}

			Element scopeElementData = elementRepository.findOne(scopeElement.getId());
			if (scopeElementData == null) {
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + scopeElement.getId());
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
			List<Object> treeObjects = new ArrayList<Object>();
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
			treeObjects = applyRules(treeObjects, newChildElementData, scopeElementData);

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

	private List<Object> applyRules(List<Object> treeObjects, Element newChildElementData, Element scopeElement) throws Exception {
		List<Rule> rules = ruleRepository.getRuleBywatchObjectAndWatchCategoryAndWatchSubType(Element.class.toString(), scopeElement.getCategory(),
				scopeElement.getSubType());
		for (Object o : treeObjects) {
			if (o instanceof Element && ((Element) o).getId().equals(scopeElement.getId())) {
				rootElement = (Element) o;
			}
		}
		for (Rule rule : rules) {
			System.out.println("Applying Rule :" + rule.getRuleName());
			KnowledgeBase kbase = createKnowledgeBase(rule.getRuleFile(), rule.getRuleFileType());
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			StringTokenizer st = new StringTokenizer(rule.getRuleTriggerFact(), "|");
			String triggerFactPackage = st.nextToken();
			String triggerFactName = st.nextToken();
			FactType triggerFact = kbase.getFactType(triggerFactPackage, triggerFactName);
			Object triggerFactInstance = triggerFact.newInstance();
			if (rule.getRuleTriggerFactParams() != null) {
				triggerFactInstance = setTriggerFactInstanceParams(triggerFact, triggerFactInstance, rule.getRuleTriggerFactParams());
			}
			if (rule.getRuleParams() != null) {
				ksession = setSessionVariables(ksession, rule.getRuleParams());
			}
			ksession.insert(triggerFactInstance);
			ksession.insert(rootElement);
			ksession.fireAllRules();
			ksession.dispose();
		}
		return treeObjects;
	}

	private KnowledgeBase createKnowledgeBase(String ruleFile, String ruleFileType) throws Exception {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		if (ruleFileType.equalsIgnoreCase(EbuildleapConstants.DROOLS_RULE_DRL)) {
			builder.add(ResourceFactory.newClassPathResource(ruleFile), ResourceType.DRL);
		}
		if (ruleFileType.equalsIgnoreCase(EbuildleapConstants.DROOLS_RULE_XLS)) {
			DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
			config.setInputType(DecisionTableInputType.XLS);
			builder.add(ResourceFactory.newClassPathResource(ruleFile), ResourceType.DTABLE, config);
		}

		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
		return knowledgeBase;
	}

	private Object setTriggerFactInstanceParams(FactType triggerFact, Object triggerFactInstance, String triggerFactInstanceParams) throws Exception {
		StringTokenizer st = new StringTokenizer(triggerFactInstanceParams, "|");
		while (st.hasMoreTokens()) {
			StringTokenizer st1 = new StringTokenizer(st.nextToken(), ",");
			String paramName = st1.nextToken();
			String paramValue = st1.nextToken();
			Object o = null;
			if (paramValue.equalsIgnoreCase("EXIST")) {
				Field f = getClass().getDeclaredField(paramName);
				System.out.println(f.get(this));
				o = f.get(this);
			}
			triggerFact.set(triggerFactInstance, paramName, o);
		}
		return triggerFactInstance;
	}

	private StatefulKnowledgeSession setSessionVariables(StatefulKnowledgeSession session, String params) throws Exception {
		StringTokenizer st = new StringTokenizer(params, "|");
		while (st.hasMoreTokens()) {
			StringTokenizer st1 = new StringTokenizer(st.nextToken(), ",");
			while (st1.hasMoreTokens()) {
				String globalVariableName = st1.nextToken();
				String globalVariableType = st1.nextToken();
				if (globalVariableType != null && globalVariableType.length() > 0) {
					Object o = null;
					if (globalVariableType.equalsIgnoreCase("ArrayList")) {
						o = ReflectUtils.getInstance().constructClass(ArrayList.class);
					}
					if (globalVariableType.equalsIgnoreCase("HashSet")) {
						o = ReflectUtils.getInstance().constructClass(HashSet.class);
					}
					if (globalVariableType.equalsIgnoreCase("EXIST")) {
						Field f = getClass().getDeclaredField(globalVariableName);
						System.out.println(f.get(this));
						o = f.get(this);
					}
					session.setGlobal(globalVariableName, o);
				}
			}
		}
		return session;
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

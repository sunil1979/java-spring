package com.ebuild.leap.test;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.azeckoski.reflectutils.ReflectUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.type.FactType;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.Globals;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.WorkingMemory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.drools.KnowledgeFactoryBean;
import com.ebuild.leap.drools.LookupPaletteUtil;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.Rule;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.repository.jpa.CategoryRepository;
import com.ebuild.leap.repository.jpa.ElementRepository;
import com.ebuild.leap.repository.jpa.RuleRepository;
import com.ebuild.leap.repository.jpa.SubTypeRepository;
import com.ebuild.leap.repository.mongodb.ElementMongoRepository;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;
import com.ebuild.leap.service.CustomizationServiceFacadeImpl;
import com.ebuild.leap.service.DesignerServiceFacadeImpl;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback = false)
public class DroolsTest extends AbstractTransactionalJUnit4SpringContextTests {

	int x = 10;

	/*
	 * @Autowired
	 * 
	 * @Qualifier("findNodeKnowledge") private KnowledgeFactoryBean
	 * findNodeKnowledge;
	 */

	@Autowired
	private RuleRepository ruleRepository;

	@Autowired
	@Qualifier("finishPaletteKnowledge")
	private KnowledgeFactoryBean finishPaletteKnowledge;

	@Autowired
	@Qualifier("flattenTreeRulesKnowledge")
	private KnowledgeFactoryBean flattenTreeRulesKnowledge;

	@Autowired
	private HomeUnitRevisionMongoRepository homeUnitMongoRepository;

	private Object oldElementFinishID;
	private Object currentFinish;
	private Element newElement;
	private Element rootElement;
	@SuppressWarnings("rawtypes")
	private Hashtable<String, Object> ruleParams = new Hashtable<String, Object>();

	@Autowired
	private DesignerServiceFacadeImpl designerService;

	@Autowired
	private ElementMongoRepository elementMongoRepository;

	@Autowired
	private ElementRepository elementRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubTypeRepository subTypeRepository;

	@Autowired
	private LookupPaletteUtil lookupPaletteUtil;

	@Autowired
	private CustomizationServiceFacadeImpl customizationService;

	private Long finishId;
	private String flooring;
	private List<Long> themes = new ArrayList<Long>();

	public void loadSpring() throws Exception {

	}

	@Test
	public void createNewRevision() throws Exception {
		try {
			HomeUnitRevision currentHomeUnitRevision = new HomeUnitRevision();
			currentHomeUnitRevision.setId(new Long("94906032781787136").longValue());
			Element newChildElement = new Element();
			newChildElement.setId(new Long("22631637519183755").longValue());
			ElementManifest currentElementManifest = new ElementManifest();
			currentElementManifest.setId(new Long("94904324961861633").longValue());
			Element ILElement = new Element();
			ILElement.setId(new Long("94897517103153153").longValue());
			EbuildleapResultObject ero = customizationService.createNewRevision(currentHomeUnitRevision, newChildElement, currentElementManifest, ILElement);
			System.out.println("Result Status :"+ero.getResultStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void testLookupUtil() throws Exception {
		
		Set<Object> lookUpResult = lookupPaletteUtil.lookupFinishforFlooringPalette("FL83");
		System.out.println("lookUpResult length :" + lookUpResult.size());
		for (Object entry : lookUpResult) {
			System.out.println("entry :" + entry);
		}
	}

	
	public void loadRules() throws Exception {
		Rule bedroomRule = new Rule();
		bedroomRule.setWatchObject(Element.class.toString());
		bedroomRule.setWatchAttribute("finish.id");
		bedroomRule.setRuleFile("com/ebuild/leap/drools/bedroom_rules.drl");
		bedroomRule.setRuleName("Bedroom IL rules");
		bedroomRule.setRuleDescription("Bedroom IL rules");
		bedroomRule.setRuleFileType(EbuildleapConstants.DROOLS_RULE_DRL);
		bedroomRule.setRuleParams("designerService,EXIST|lookupPaletteUtil,EXIST|rootElement,EXIST");
		Category watchCategory = categoryRepository.findOne(new Long("60").longValue());
		bedroomRule.setWatchCategory(watchCategory);
		SubType watchSubType = subTypeRepository.findOne(new Long("10").longValue());
		bedroomRule.setWatchSubType(watchSubType);
		bedroomRule.setRuleTriggerFact("com.ebuild.leap.drools|Bedroom");
		bedroomRule.setRuleTriggerFactParams("newElementId,EXIST|finishId,EXIST|themes,EXIST|flooring,EXIST");
		ruleRepository.save(bedroomRule);
	}

	public void executeRules() throws Exception {
		try {
			Category category = categoryRepository.findOne(new Long("60").longValue());
			SubType subType = subTypeRepository.findOne(new Long("10").longValue());
			List<Rule> rulesForWatchObject = ruleRepository.getRuleBywatchObjectAndWatchCategoryAndWatchSubType(Element.class.toString(), category,
					subType);
			for (Rule rule : rulesForWatchObject) {
				System.out.println("Rule Id :" + rule.getId());
				rootElement = elementMongoRepository.findOne(new Long("94897517103153153").longValue());
				KnowledgeBase kbase = createKnowledgeBase("com/ebuild/leap/drools/bedroom_rules.drl", rule.getRuleFileType());
				// KnowledgeBase kbase = readKnowledgeBase();
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				StringTokenizer st = new StringTokenizer(rule.getRuleTriggerFact(), "|");
				String triggerFactPackage = st.nextToken();
				String triggerFactName = st.nextToken();
				FactType triggerFact = kbase.getFactType(triggerFactPackage, triggerFactName);
				Object triggerFactInstance = triggerFact.newInstance();
				finishId = 10L;
				flooring = "FL01";
				themes.add(101L);
				// themesList.add(202L);
				themes.add(600L);
				themes.add(700L);
				triggerFactInstance = setTriggerFactInstanceParams(triggerFact, triggerFactInstance, rule.getRuleTriggerFactParams());
				ksession = setSessionVariables(ksession, rule.getRuleParams());
				ksession.insert(triggerFactInstance);
				ksession.insert(rootElement);
				ksession.fireAllRules();
				ksession.dispose();
				System.out.println("******************************ROOTELEMENT ***********************************");
				// printXML(rootElement);
				for (ElementManifest em : rootElement.getElementManifestList()) {
					Element childElement = em.getChildElement();
					System.out.println("Id :" + childElement.getId() + ": Name :" + childElement.getName() + " : Category :"
							+ childElement.getCategory().getId() + " : Type :" + childElement.getType().getId() + " : Theme :"
							+ childElement.getElementThemes().get(0).getId() + " : Finish :" + childElement.getFinish().getId());
				}
				System.out.println("******************************ROOTELEMENT ***********************************");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void executeBedroomRules() throws Exception {
		Element rootElement = elementMongoRepository.findOne(new Long("94897517103153153").longValue());
		System.out.println("******************************ROOTELEMENT ***********************************");
		// printXML(rootElement);
		System.out.println("******************************ROOTELEMENT ***********************************");
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		// ksession.addEventListener( new DebugAgendaEventListener() );
		// ksession.addEventListener( new DebugWorkingMemoryEventListener() );
		// setup the audit logging
		// KnowledgeRuntimeLogger logger =
		// KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
		FactType bedroom = kbase.getFactType("com.ebuild.leap.drools", "Bedroom");
		Object bedroomFactInstance = bedroom.newInstance();
		bedroom.set(bedroomFactInstance, "finishId", 10L);
		List<Long> themesList = new ArrayList<Long>();
		themesList.add(101L);
		// themesList.add(202L);
		themesList.add(600L);
		themesList.add(700L);
		bedroom.set(bedroomFactInstance, "themes", themesList);
		bedroom.set(bedroomFactInstance, "flooring", "FL21");
		ksession.setGlobal("designerService", designerService);
		ksession.setGlobal("lookupPaletteUtil", lookupPaletteUtil);
		ksession.setGlobal("rootElement", rootElement);
		ksession.insert(bedroomFactInstance);
		ksession.insert(rootElement);
		ksession.fireAllRules();
		// logger.close();
		ksession.dispose();
		// elementMongoRepository.save(rootElement);
		System.out.println("******************************ROOTELEMENT ***********************************");
		// printXML(rootElement);
		for (ElementManifest em : rootElement.getElementManifestList()) {
			Element element = em.getChildElement();
			System.out.println("Id :" + element.getId() + ": Name :" + element.getName() + " : Category :" + element.getCategory().getId()
					+ " : Type :" + element.getType().getId() + " : Theme :" + element.getElementThemes().get(0).getId() + " : Finish :"
					+ element.getFinish().getId());
		}
		System.out.println("******************************ROOTELEMENT ***********************************");
	}

	/*
	 * public void getRulesForWatchObject() throws Exception {
	 * 
	 * Element oldElement = new Element(); Finish oldFinish = new Finish();
	 * oldFinish.setId(new Long("20").longValue());
	 * oldElement.setFinish(oldFinish); newElement = new Element(); Finish
	 * newFinish = new Finish(); newFinish.setId(new Long("10").longValue());
	 * newElement.setFinish(newFinish); Category newElementCategory = new
	 * Category(); newElementCategory.setId(new Long(40).longValue());
	 * newElement.setCategory(newElementCategory); Type newElementType = new
	 * Type(); newElementType.setId(new Long(20).longValue());
	 * newElement.setType(newElementType); rootElement = new Element(); Category
	 * rootElementCategory = new Category(); rootElementCategory.setId(new
	 * Long(60).longValue()); rootElement.setCategory(rootElementCategory);
	 * 
	 * 
	 * List<Rule> rulesForWatchObject =
	 * ruleRepository.getRuleBywatchObject(Element.class.toString());
	 * System.out.println("Retrieved Number of Rules :" +
	 * rulesForWatchObject.size());
	 * 
	 * for (Rule rule : rulesForWatchObject) { oldElementFinishID =
	 * ReflectUtils.getInstance().getFieldValue(oldElement,
	 * rule.getWatchAttribute()); currentFinish =
	 * ReflectUtils.getInstance().getFieldValue(newElement,
	 * rule.getWatchAttribute());
	 * System.out.println("Old Element Watch Attribute Value :" +
	 * oldElementFinishID.toString());
	 * System.out.println("New Element Watch Attribute Value :" +
	 * currentFinish.toString()); if (oldElementFinishID != currentFinish) {
	 * System.out.println("NOT EQUAL"); // Apply Lookup Rule KnowledgeBase
	 * lookUpKnowledge = createKnowledgeBase(rule.getLookupRule(),
	 * rule.getLookupRuleFileType()); StatefulKnowledgeSession lookUpRulesession
	 * = lookUpKnowledge.newStatefulKnowledgeSession(); Globals globals =
	 * lookUpRulesession.getGlobals(); System.out.println("Globals :" +
	 * globals.toString()); lookUpRulesession =
	 * setSessionVariables(lookUpRulesession, rule.getLookupRuleParams());
	 * lookUpRulesession.fireAllRules(); lookUpRulesession.dispose(); Set<?>
	 * compatibleList = (HashSet<?>) ruleParams.get("compatibleList");
	 * System.out.println("Compatible List Size :" + compatibleList.size()); for
	 * (Object entry : compatibleList) { System.out.println("Set Entry :" +
	 * entry); } // Apply ImpactLink Rule KnowledgeBase impactLinkKnowledge =
	 * createKnowledgeBase(rule.getImpactRule(), rule.getImpactRuleFileType());
	 * StatefulKnowledgeSession impactLinkRulesession =
	 * impactLinkKnowledge.newStatefulKnowledgeSession(); impactLinkRulesession
	 * = setSessionVariables(impactLinkRulesession, rule.getImpactRuleParams());
	 * impactLinkRulesession.fireAllRules(); impactLinkRulesession.dispose();
	 * Set<?> categoryCriteriaList = (HashSet<?>)
	 * ruleParams.get("categoryCriteriaList");
	 * System.out.println("categoryCriteriaList size :" +
	 * categoryCriteriaList.size()); for (Object entry : categoryCriteriaList) {
	 * System.out.println("categoryCriteriaList Entry :" + entry); } Set<?>
	 * typeCriteriaList = (HashSet<?>) ruleParams.get("typeCriteriaList");
	 * System.out.println("typeCriteriaList size :" + typeCriteriaList.size());
	 * for (Object entry : typeCriteriaList) {
	 * System.out.println("typeCriteriaList Entry :" + entry); } } else {
	 * System.out.println("EQUAL"); } } }
	 */
	private StatefulKnowledgeSession setSessionVariables1(StatefulKnowledgeSession session) throws Exception {
		Globals globals = session.getGlobals();
		globals.toString();
		return session;
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
						try {
							Field f = getClass().getDeclaredField(globalVariableName);
							System.out.println(f.get(this));
							o = f.get(this);
						} catch (NoSuchFieldException nsfe) {
							if (ruleParams.containsKey(globalVariableName)) {
								o = ruleParams.get(globalVariableName);
							}
						}
					}
					session.setGlobal(globalVariableName, o);
					if (!ruleParams.containsKey(globalVariableName)) {
						ruleParams.put(globalVariableName, o);
					}
				}
			}
		}
		return session;
	}

	/*
	 * public void executeRules() throws Exception{ KnowledgeBase kbase =
	 * (KnowledgeBase) findNodeKnowledge.getObject(); StatefulKnowledgeSession
	 * ksession = kbase.newStatefulKnowledgeSession(); List<Element> nodeList =
	 * new ArrayList<Element>(); ksession.setGlobal("nodeList",nodeList);
	 * ksession.setGlobal("nodeId",new Long("94267842366556097").longValue());
	 * KnowledgeRuntimeLogger logger =
	 * KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test"); // go !
	 * HomeUnitRevision homeUnitRevision = homeUnitMongoRepository.findOne(new
	 * Long("743456108").longValue());
	 * ksession.insert(homeUnitRevision.getHomeUnitVersion
	 * ().getHomeUnit().getProduct().getElement()); ksession.fireAllRules();
	 * System
	 * .out.println("******************************************************");
	 * System.out.println("Object Found :"+nodeList.get(0).getId());
	 * logger.close(); }
	 */

	public void executeLookUpRule() throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) finishPaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		List<Integer> compatibleList = new ArrayList<Integer>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentFinish", 10);
		ksession.fireAllRules();
		ksession.dispose();
		System.out.println("Compatible List Size :" + compatibleList.size());
		for (Integer entry : compatibleList) {
			System.out.println("List Entry :" + entry);
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		config.setInputType(DecisionTableInputType.XLS);
		kbuilder.add(ResourceFactory
				.newUrlResource("file:///C:/Users/sunil/git/github/eBuildSpringJPAMongoActivitiDrools/src/com/ebuild/leap/drools/bedroom_rules.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	public void executeImpactRule() throws Exception {
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Set<Integer> categoryCriteriaList = new HashSet<Integer>();
		Set<Integer> typeCriteriaList = new HashSet<Integer>();
		Element rootElement = new Element();
		Category rootElementCategory = new Category();
		rootElementCategory.setId(new Long(60).longValue());
		SubType rootElementSubType = new SubType();
		rootElementSubType.setId(new Long(12).longValue());
		rootElement.setCategory(rootElementCategory);
		// rootElement.setSubType(rootElementSubType);
		Element changedElement = new Element();
		Category changedElementCategory = new Category();
		changedElementCategory.setId(new Long(40).longValue());
		Type changedElementType = new Type();
		changedElementType.setId(new Long(20).longValue());
		changedElement.setCategory(changedElementCategory);
		changedElement.setType(changedElementType);
		ksession.setGlobal("categoryCriteriaList", categoryCriteriaList);
		ksession.setGlobal("typeCriteriaList", typeCriteriaList);
		ksession.setGlobal("rootElement", rootElement);
		ksession.setGlobal("changedElement", changedElement);
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
	}

	public List<Object> getTestData(Element rootElement) throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) flattenTreeRulesKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		// WorkingMemory workingMemory = ksession.
		ArrayList<Object> treeObjects = new ArrayList<Object>();
		ksession.setGlobal("treeObjects", treeObjects);
		ksession.insert(rootElement);
		ksession.fireAllRules();
		ksession.dispose();
		return treeObjects;
	}

	private void printXML(Element element) throws Exception {
		try {
			JAXBContext ctx = JAXBContext.newInstance(Element.class);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(element, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeDuplicateElementThemes() throws Exception {
		List<Element> elements = elementRepository.findAll();
		int elementCount = 0;
		for (Element element : elements) {
			if (element.getElementThemes() != null && element.getElementThemes().size() > 1) {
				element.getElementThemes().remove(0);
				elementRepository.save(element);
			}
			elementCount = elementCount + 1;
			System.out.println("elementCount :" + elementCount);
		}
	}

	public void testListContains() throws Exception {
		Set<Long> set1 = new HashSet<Long>();
		set1.add(101L);
		set1.add(102L);
		set1.add(103L);
		List<Long> list1 = new ArrayList<Long>();
		list1.add(101L);
		System.out.println(set1.contains(list1.get(0)));
	}

}

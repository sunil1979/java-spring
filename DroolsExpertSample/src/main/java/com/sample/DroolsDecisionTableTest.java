package com.sample;

import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Rule;
import org.drools.definitions.impl.KnowledgePackageImp;
import org.drools.rule.GroupElement;
import org.drools.runtime.Globals;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public class DroolsDecisionTableTest {

	public static final int TEMP_CONSTANT = 1;

	public static void main(String[] args) {
		try {
			testStatefulDroolsSession();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        config.setInputType(DecisionTableInputType.XLS);
        kbuilder.add(ResourceFactory.newClassPathResource("new_rule.xls"), ResourceType.DTABLE, config);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
	}

	@SuppressWarnings("restriction")
	private static void testStatefulDroolsSession() throws Exception {
		// load up the knowledge base
		KnowledgeBase kbase = readKnowledgeBase();
		System.out.println("Knowledgebase read successfully :");
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
		// go !
		User user = new User();
		user.setId(1);
		user.setName("user1");
		Task task1 = new Task();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setCompleted(false);
		task1.setOwner(user);
		user.addTask(task1);
		//ksession.setGlobal("operation", "ADD");
		ksession.insert(user);
		ksession.insert(task1);
		ksession.fireAllRules();
		//Globals globalVars = ksession.getGlobals();
		//System.out.println("Global Variable Operation :" + globalVars.get("operation"));
		for(KnowledgePackage kp : kbase.getKnowledgePackages()){
			System.out.println("Knowledgepackage name :"+kp.getName());
			for( Rule rule: kp.getRules() ){
				System.out.println("**************RULES*****************");
			    String ruleName = rule.getName(); 
			    org.drools.rule.Rule realRule = 
			        (org.drools.rule.Rule) 
			((KnowledgePackageImp)kp).getRule( ruleName );
			    String agName = realRule.getAgendaGroup();
			    System.out.println("Rule Name :"+realRule.getName());
			    Map<String,Object> ruleMetaData = realRule.getMetaData();
			    System.out.println("Rule Metadata Size :"+ruleMetaData.size());
			    GroupElement ge = realRule.getLhs();
			    System.out.println("Rule LHS :"+ge.toString());
			    System.out.println("Agenda Group :"+agName);
			    System.out.println("**************RULES*****************");
			} 			
		}
		for (FactHandle fh : ksession.getFactHandles()) {
			System.out.println("Fact Handle :" + ksession.getObject(fh).toString());
		}
		ksession.dispose();
		System.out.println("All rules invoked");
		logger.close();
	}
}

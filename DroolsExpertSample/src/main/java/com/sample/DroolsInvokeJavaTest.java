package com.sample;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.Globals;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsInvokeJavaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			variableTest();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("variable_test.drl"), ResourceType.DRL);
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

	
	public static void invokeJavaTest() throws Exception{
        KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        DroolsUtil droolsUtil = new DroolsUtil();
        // go !
        User user = new User();
        user.setId(1);
        user.setName("user1");
        ksession.setGlobal("droolsUtil", droolsUtil);
        ksession.insert(user);
        ksession.fireAllRules();
        ksession.dispose();
        logger.close();
	}
	
	public static void variableTest() throws Exception{
        KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        // go !
        User user = new User();
        user.setId(1);
        user.setName("user1");
        ksession.insert(user);
        ksession.fireAllRules();
        ksession.dispose();
        logger.close();		
	}

}

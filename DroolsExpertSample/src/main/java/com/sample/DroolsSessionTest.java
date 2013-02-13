package com.sample;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBaseConfiguration;
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
import org.drools.runtime.StatelessKnowledgeSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsSessionTest {
	
	public static final int TEMP_CONSTANT = 1;

    public static final void main(String[] args) {
        try {
        	testStatefulDroolsSession();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("drools_sessiontest.drl"), ResourceType.DRL);
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
	private static void testStatelessDroolsSession() throws Exception{
        KnowledgeBase kbase = readKnowledgeBase();
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        RuleBaseConfiguration.getDefaultInstance().setSequential(true);
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
        List<Object> facts = new ArrayList<Object>();
        facts.add(user);
        facts.add(task1);
        ksession.setGlobal("operation", "ADD");
        ksession.execute(facts);
        Globals globalVars = ksession.getGlobals();
        System.out.println("Global Variable Operation :"+globalVars.get("operation"));
        logger.close();
    }
    
    private static void testStatefulDroolsSession() throws Exception{
        // load up the knowledge base
        KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
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
        ksession.setGlobal("operation", "ADD");
        ksession.insert(user);
        ksession.insert(task1);
        ksession.fireAllRules();
        Globals globalVars = ksession.getGlobals();
        System.out.println("Global Variable Operation :"+globalVars.get("operation"));
        ksession.dispose();
        logger.close();
    }

}

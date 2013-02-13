package com.ebuild.leap.test;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.drools.KnowledgeFactoryBean;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.repository.mongodb.HomeUnitRevisionMongoRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback=false)
public class DroolsTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	@Qualifier("findNodeKnowledge")
	private KnowledgeFactoryBean findNodeKnowledge;
	
	@Autowired
	private HomeUnitRevisionMongoRepository homeUnitMongoRepository;
	
	@Test
	public void executeRules() throws Exception{
        KnowledgeBase kbase = (KnowledgeBase) findNodeKnowledge.getObject();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        List<Element> nodeList = new ArrayList<Element>();
        ksession.setGlobal("nodeList",nodeList);
        ksession.setGlobal("nodeId",new Long("94267842366556097").longValue());
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        // go !
        HomeUnitRevision homeUnitRevision = homeUnitMongoRepository.findOne(new Long("743456108").longValue());
        ksession.insert(homeUnitRevision.getHomeUnitVersion().getHomeUnit().getProduct().getElement());
        ksession.fireAllRules();
        System.out.println("******************************************************");
        System.out.println("Object Found :"+nodeList.get(0).getId());
        logger.close();
	}

}

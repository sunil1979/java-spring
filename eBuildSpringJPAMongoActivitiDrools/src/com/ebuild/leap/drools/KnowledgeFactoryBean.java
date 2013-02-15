package com.ebuild.leap.drools;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.springframework.beans.factory.FactoryBean;

import com.ebuild.leap.util.EbuildleapConstants;

public class KnowledgeFactoryBean implements FactoryBean<Object> {

	private KnowledgeBase knowledgeBase;

	public KnowledgeFactoryBean(Map<String, String> resourceMap) throws IOException {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (Entry<String, String> entry : resourceMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equalsIgnoreCase(EbuildleapConstants.DROOLS_RULE_DRL)) {
				builder.add(ResourceFactory.newClassPathResource(entry.getKey()), ResourceType.DRL);
			}
			if (entry.getValue() != null && entry.getValue().equalsIgnoreCase(EbuildleapConstants.DROOLS_RULE_XLS)) {
				DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
				config.setInputType(DecisionTableInputType.XLS);
				builder.add(ResourceFactory.newClassPathResource(entry.getKey()), ResourceType.DTABLE, config);
			}
		}
		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
	}

	@Override
	public Object getObject() throws Exception {
		return this.knowledgeBase;
	}

	@Override
	public Class<KnowledgeBase> getObjectType() {
		return KnowledgeBase.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}

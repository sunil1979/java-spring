package com.ebuild.leap.drools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class LookupPaletteUtil {

	@Autowired
	@Qualifier("finishPaletteKnowledge")
	private KnowledgeFactoryBean finishPaletteKnowledge;

	@Autowired
	@Qualifier("themePaletteKnowledge")
	private KnowledgeFactoryBean themePaletteKnowledge;
	
	@Autowired
	@Qualifier("finishFlooringPaletteKnowledge")
	private KnowledgeFactoryBean finishFlooringPaletteKnowledge;
	
	@Autowired
	@Qualifier("flooringFinishPaletteKnowledge")
	private KnowledgeFactoryBean flooringFinishPaletteKnowledge;
	
	

	public Set<Integer> lookupFinishPalette(Long currentFinishId) throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) finishPaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Set<Integer> compatibleList = new HashSet<Integer>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentFinish", currentFinishId);
		ksession.fireAllRules();
		ksession.dispose();
		return compatibleList;
	}

	public Set<Integer> lookupThemePalette(Long currentThemeId) throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) themePaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Set<Integer> compatibleList = new HashSet<Integer>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentTheme", currentThemeId);
		ksession.fireAllRules();
		ksession.dispose();
		return compatibleList;
	}
	
	public Set<Object> lookupFlooringforFinishPalette(Long currentFinish) throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) finishFlooringPaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Set<Object> compatibleList = new HashSet<Object>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentFinish", currentFinish);
		ksession.fireAllRules();
		ksession.dispose();
		return compatibleList;
	}
	
	public Set<Object> lookupFinishforFlooringPalette(String currentFlooring) throws Exception {
		KnowledgeBase kbase = (KnowledgeBase) flooringFinishPaletteKnowledge.getObject();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Set<Object> compatibleList = new HashSet<Object>();
		ksession.setGlobal("compatibleList", compatibleList);
		ksession.setGlobal("currentFlooring", currentFlooring);
		ksession.fireAllRules();
		ksession.dispose();
		return compatibleList;
	}

}

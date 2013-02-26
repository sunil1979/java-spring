package com.ebuild.leap.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.Rule;
import com.ebuild.leap.pojo.SubType;

public interface RuleRepository extends JpaRepository<Rule, Long> {
	
	public List<Rule> getRuleBywatchObjectAndWatchCategoryAndWatchSubType(String watchObject,Category category, SubType subType);

}

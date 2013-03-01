package com.ebuild.leap.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "rule")
@Indexed
public class Rule {

	public Long id;
	private Integer version;
	private String watchObject;
	private String watchAttribute;
	private Category watchCategory;
	private Type watchType;
	private SubType watchSubType;
	private SubCategory watchSubCategory;
	private String ruleTriggerFact;
	private String ruleTriggerFactParams;
	private String ruleFile;
	private String ruleName;
	private String ruleDescription;
	private String ruleParams;
	private String ruleFileType;

	@Id
	@Column(name = "rule_id")
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.ebuild.leap.util.RandomIdGenerator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getWatchObject() {
		return watchObject;
	}

	public void setWatchObject(String watchObject) {
		this.watchObject = watchObject;
	}

	public String getWatchAttribute() {
		return watchAttribute;
	}

	public void setWatchAttribute(String watchAttribute) {
		this.watchAttribute = watchAttribute;
	}

	@ManyToOne
	public Category getWatchCategory() {
		return watchCategory;
	}

	public void setWatchCategory(Category watchCategory) {
		this.watchCategory = watchCategory;
	}

	@ManyToOne
	public Type getWatchType() {
		return watchType;
	}

	public void setWatchType(Type watchType) {
		this.watchType = watchType;
	}

	@ManyToOne
	public SubType getWatchSubType() {
		return watchSubType;
	}

	public void setWatchSubType(SubType watchSubType) {
		this.watchSubType = watchSubType;
	}

	@ManyToOne
	public SubCategory getWatchSubCategory() {
		return watchSubCategory;
	}

	public void setWatchSubCategory(SubCategory watchSubCategory) {
		this.watchSubCategory = watchSubCategory;
	}

	public String getRuleTriggerFact() {
		return ruleTriggerFact;
	}

	public void setRuleTriggerFact(String ruleTriggerFact) {
		this.ruleTriggerFact = ruleTriggerFact;
	}

	public String getRuleTriggerFactParams() {
		return ruleTriggerFactParams;
	}

	public void setRuleTriggerFactParams(String ruleTriggerFactParams) {
		this.ruleTriggerFactParams = ruleTriggerFactParams;
	}

	public String getRuleParams() {
		return ruleParams;
	}

	public void setRuleParams(String ruleParams) {
		this.ruleParams = ruleParams;
	}

	public String getRuleFileType() {
		return ruleFileType;
	}

	public void setRuleFileType(String ruleFileType) {
		this.ruleFileType = ruleFileType;
	}

	public String getRuleFile() {
		return ruleFile;
	}

	public void setRuleFile(String ruleFile) {
		this.ruleFile = ruleFile;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}
}

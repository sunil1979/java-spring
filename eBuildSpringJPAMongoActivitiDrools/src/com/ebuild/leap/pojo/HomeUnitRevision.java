package com.ebuild.leap.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Entity
@Table(name = "revision")
@Indexed
@XmlRootElement
@Document
/*
@NamedNativeQuery(name="HomeUnitRevision.getLatestRevision",
query="select * from revision where revision.homeUnitVersion=?1 ORDER BY revision.revisionNumber DESC LIMIT 1",
resultClass=com.ebuild.leap.pojo.HomeUnitRevision.class)
*/
public class HomeUnitRevision extends EbuildBaseObject {
	private Long id;
	
	@Transient
	private Integer version;
	
	private Integer revisionNumber;
	private String revisionTag;
	private String revisionDescription;
	private String revisionXML;
	private String code1;
	private Double budget;
	private Double price;
	private HomeUnitVersion homeUnitVersion;
	
	@Id
	@Column(name = "revision_id")
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.ebuild.leap.util.RandomIdGenerator")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Version
	@XmlTransient
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(Integer revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public String getRevisionTag() {
		return revisionTag;
	}

	public void setRevisionTag(String revisionTag) {
		this.revisionTag = revisionTag;
	}

	public String getRevisionDescription() {
		return revisionDescription;
	}

	public void setRevisionDescription(String revisionDescription) {
		this.revisionDescription = revisionDescription;
	}

	@XmlTransient
	public String getRevisionXML() {
		return revisionXML;
	}

	public void setRevisionXML(String revisionXML) {
		this.revisionXML = revisionXML;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@ManyToOne
	public HomeUnitVersion getHomeUnitVersion() {
		return homeUnitVersion;
	}

	public void setHomeUnitVersion(HomeUnitVersion homeUnitVersion) {
		this.homeUnitVersion = homeUnitVersion;
	}

}

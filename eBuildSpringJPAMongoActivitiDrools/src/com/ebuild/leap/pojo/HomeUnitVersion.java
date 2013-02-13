package com.ebuild.leap.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "version")
@Indexed
@XmlRootElement
public class HomeUnitVersion extends EbuildBaseObject {
	private Long id;
	
	@Transient
	private Integer version;
	
	private Integer versionNumber;
	private String versionTag;
	private String versionDescription;
	private String code1;
	private Double budget;
	private Double price;
	private HomeUnit homeUnit;
	
	@Transient
	private List<HomeUnitRevision> homeUnitRevisions;
	

	@Id
	@Column(name = "version_id")
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

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionTag() {
		return versionTag;
	}

	public void setVersionTag(String versionTag) {
		this.versionTag = versionTag;
	}

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
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
	public HomeUnit getHomeUnit() {
		return homeUnit;
	}

	public void setHomeUnit(HomeUnit homeUnit) {
		this.homeUnit = homeUnit;
	}

	@OneToMany(mappedBy="homeUnitVersion",cascade=CascadeType.ALL)
	@XmlTransient
	public List<HomeUnitRevision> getHomeUnitRevisions() {
		return homeUnitRevisions;
	}

	public void setHomeUnitRevisions(List<HomeUnitRevision> homeUnitRevisions) {
		this.homeUnitRevisions = homeUnitRevisions;
	}
	
	public void addHomeUnitRevision(HomeUnitRevision homeUnitRevision){
		if(homeUnitRevision == null){
			return;
		}else if(this.homeUnitRevisions == null){
			this.homeUnitRevisions = new ArrayList<HomeUnitRevision>();
		}
		this.homeUnitRevisions.add(homeUnitRevision);
		homeUnitRevision.setHomeUnitVersion(this);
	}
}

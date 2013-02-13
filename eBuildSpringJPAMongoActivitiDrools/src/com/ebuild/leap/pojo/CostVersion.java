package com.ebuild.leap.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "costversion")
@Indexed
@XmlRootElement
public class CostVersion extends EbuildBaseObject {
	private Long id;
	@Transient
	private Integer version;
	private Integer costVersion;
	private Double costValue;
	
	@Transient
	private CPR cpr;
	@Transient
	private List<HomeUnit> associatedHomeUnits;
	@Transient
	private List<Element> associatedElements;
	
	@Id
	@Column(name = "costversion_id")
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

	public Integer getCostVersion() {
		return costVersion;
	}

	public void setCostVersion(Integer costVersion) {
		this.costVersion = costVersion;
	}

	public Double getCostValue() {
		return costValue;
	}

	public void setCostValue(Double costValue) {
		this.costValue = costValue;
	}

	@ManyToOne
	@XmlTransient
	public CPR getCpr() {
		return cpr;
	}

	public void setCpr(CPR cpr) {
		this.cpr = cpr;
	}

	@OneToMany(mappedBy="costVersion")
	@XmlTransient
	public List<HomeUnit> getAssociatedHomeUnits() {
		return associatedHomeUnits;
	}

	public void setAssociatedHomeUnits(List<HomeUnit> associatedHomeUnits) {
		this.associatedHomeUnits = associatedHomeUnits;
	}

	@OneToMany(mappedBy="costVersion")
	@XmlTransient
	public List<Element> getAssociatedElements() {
		return associatedElements;
	}

	public void setAssociatedElements(List<Element> associatedElements) {
		this.associatedElements = associatedElements;
	}
}

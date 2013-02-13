package com.ebuild.leap.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;

@SuppressWarnings("serial")
@Entity
@Table(name="cpr")
@Indexed
@XmlRootElement
public class CPR extends EbuildBaseObject {
	private Long id;
	private Integer version;
	private Double value;
	private String tag;
	private String comments;
	private List<CostVersion> costVersions;
	

	@Id
	@Column(name = "cpr_id")
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@OneToMany(mappedBy="cpr")
	public List<CostVersion> getCostVersions() {
		return costVersions;
	}

	public void setCostVersions(List<CostVersion> costVersions) {
		this.costVersions = costVersions;
	}
}

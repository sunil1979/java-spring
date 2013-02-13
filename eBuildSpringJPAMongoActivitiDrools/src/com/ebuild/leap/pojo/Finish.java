package com.ebuild.leap.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "finish")
@Indexed
@XmlRootElement
public class Finish extends EbuildBaseObject {
	private Long id;
	private Integer version;
	@Transient
	private List<Element> finishElements;
	private String name;
	private String description;

	@Id
	@Column(name = "finish_id")
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

	@OneToMany(orphanRemoval = true, cascade = {
			javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE }, mappedBy = "finish", fetch = FetchType.LAZY)
	@XmlTransient
	public List<Element> getFinishElements() {
		return finishElements;
	}

	public void setFinishElements(List<Element> finishElements) {
		this.finishElements = finishElements;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

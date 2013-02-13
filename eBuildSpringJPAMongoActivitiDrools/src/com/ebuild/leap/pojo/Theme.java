package com.ebuild.leap.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name="theme")
@Indexed
@XmlRootElement
public class Theme extends EbuildBaseObject {
	private Long id;
	private Integer version;
	@Transient
	private List<Element> themeElements;
	private String name;
	private String description;
	
	@Id
	@Column(name = "theme_id")
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

	@ManyToMany(mappedBy = "elementThemes", cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE })
	@XmlTransient
	public List<Element> getThemeElements() {
		return themeElements;
	}
	
	public void setThemeElements(List<com.ebuild.leap.pojo.Element> themeElements) {
		this.themeElements = themeElements;
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

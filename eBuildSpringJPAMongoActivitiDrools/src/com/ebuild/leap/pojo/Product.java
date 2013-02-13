package com.ebuild.leap.pojo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name="product")
@Indexed
@XmlRootElement
public class Product extends EbuildBaseObject {
	private Long id;
	
	@Transient
	private Integer version;
	
	private String name;
	private String description;
	private String productXML;
	private String code1;
	private String libVersion;
	private Double budget;
	private Double price;
	
	private Project project;
	
	private Element element;
	
	@Transient
	private List<HomeUnit> productHomeUnits;
	
	@Id
	@Column(name = "product_id")
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

	@XmlTransient
	public String getProductXML() {
		return productXML;
	}

	public void setProductXML(String productXML) {
		this.productXML = productXML;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getLibVersion() {
		return libVersion;
	}

	public void setLibVersion(String libVersion) {
		this.libVersion = libVersion;
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
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToOne
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@OneToMany(mappedBy="product")
	@XmlTransient
	public List<HomeUnit> getProductHomeUnits() {
		return productHomeUnits;
	}

	public void setProductHomeUnits(List<HomeUnit> productHomeUnits) {
		this.productHomeUnits = productHomeUnits;
	}
}

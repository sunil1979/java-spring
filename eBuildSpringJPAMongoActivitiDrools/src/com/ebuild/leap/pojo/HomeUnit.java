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
@Table(name = "homeunit")
@Indexed
@XmlRootElement
public class HomeUnit extends EbuildBaseObject {
	private Long id;
	
	@Transient
	private Integer version;
	
	private String name;
	private String description;
	private String code1;
	private Double budget;
	private Double price;
	private Product product;
	private User ownerUser;
	
	@Transient
	private List<HomeUnitVersion> homeUnitVersions;
	
	private CostVersion costVersion;

	@Id
	@Column(name = "homeunit_id")
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
	@XmlElement
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	public User getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(User ownerUser) {
		this.ownerUser = ownerUser;
	}

	@OneToMany(mappedBy = "homeUnit",cascade=CascadeType.ALL)
	@XmlTransient
	public List<HomeUnitVersion> getHomeUnitVersions() {
		return homeUnitVersions;
	}

	public void setHomeUnitVersions(List<HomeUnitVersion> homeUnitVersions) {
		this.homeUnitVersions = homeUnitVersions;
	}

	public void addHomeUnitVersion(HomeUnitVersion homeUnitVersion){
		if(homeUnitVersion == null){
			return;
		}else if(this.homeUnitVersions == null){
			this.homeUnitVersions = new ArrayList<HomeUnitVersion>();
		}
		this.homeUnitVersions.add(homeUnitVersion);
		homeUnitVersion.setHomeUnit(this);
	}
	
	@ManyToOne
	public CostVersion getCostVersion() {
		return costVersion;
	}

	public void setCostVersion(CostVersion costVersion) {
		this.costVersion = costVersion;
	}
}

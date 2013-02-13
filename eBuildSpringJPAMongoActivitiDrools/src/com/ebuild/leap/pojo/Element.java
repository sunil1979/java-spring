package com.ebuild.leap.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.metamodel.StaticMetamodel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

@SuppressWarnings("serial")
@Entity
@Table(name = "element")
@Indexed
@XmlRootElement(name="E")
public class Element extends EbuildBaseObject {
	private Long id;
	@Transient
	private Integer version;
	private Integer scope;
	private Category category;
	private Type type;
	private String name;
	private String description;
	private String code1;
	private String code2;
	private String code3;
	private String code4;
	private String code5;
	private String code6;
	private String view1;
	private String view2;
	private String view3;
	private String view4;
	private String dimension;
	private String weight;
	private List<Theme> elementThemes;
	private Integer function;
	private Material material;
	private Finish finish;
	private Brand brand;
	private Double price;
	private Double cost;
	private Double CPR;
	//Initialize the list - MONGO throws exception when list is accessed when it is null....RDBMS works fine
	private List<ElementManifest> elementManifestList;
	private CostVersion costVersion;
	@Transient
	private Product product;
	private ElementVariantList elementVariantList;
	private SubType subType;

	@Id
	@Column(name = "element_id")
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

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	@ManyToOne
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String getCode3() {
		return code3;
	}

	public void setCode3(String code3) {
		this.code3 = code3;
	}

	public String getCode4() {
		return code4;
	}

	public void setCode4(String code4) {
		this.code4 = code4;
	}

	public String getCode5() {
		return code5;
	}

	public void setCode5(String code5) {
		this.code5 = code5;
	}

	public String getCode6() {
		return code6;
	}

	public void setCode6(String code6) {
		this.code6 = code6;
	}

	public String getView1() {
		return view1;
	}

	public void setView1(String view1) {
		this.view1 = view1;
	}

	public String getView2() {
		return view2;
	}

	public void setView2(String view2) {
		this.view2 = view2;
	}

	public String getView3() {
		return view3;
	}

	public void setView3(String view3) {
		this.view3 = view3;
	}

	public String getView4() {
		return view4;
	}

	public void setView4(String view4) {
		this.view4 = view4;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Integer getFunction() {
		return function;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	@ManyToOne
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@ManyToOne
	public Finish getFinish() {
		return finish;
	}

	public void setFinish(Finish finish) {
		this.finish = finish;
	}

	@ManyToOne()
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@XmlTransient
	public Double getCPR() {
		return CPR;
	}

	public void setCPR(Double cPR) {
		CPR = cPR;
	}

	@ManyToMany
	@JoinTable(name = "element_theme", joinColumns = @JoinColumn(name = "element_id"), inverseJoinColumns = @JoinColumn(name = "theme_id"))
	public List<Theme> getElementThemes() {
		return elementThemes;
	}

	public void setElementThemes(List<Theme> elementThemes) {
		this.elementThemes = elementThemes;
	}

	public void addElementTheme(Theme elementTheme) {
		if (elementTheme == null) {
			return;
		} else {
			if (this.elementThemes == null) {
				this.elementThemes = new ArrayList<Theme>();
			}
			this.elementThemes.add(elementTheme);
		}
	}

	@OneToMany(orphanRemoval = true, mappedBy = "parentElement", fetch = FetchType.LAZY)
	// @XmlElementWrapper(name="elementManifestList")
	@XmlElement(name = "M")
	public List<ElementManifest> getElementManifestList() {
		return elementManifestList;
	}

	public void setElementManifestList(List<ElementManifest> elementManifestList) {
		this.elementManifestList = elementManifestList;
	}

	@OneToOne
	@XmlTransient
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void addElementManifest(ElementManifest elementManifest) {
		if (elementManifest == null) {
			return;
		} else {
			if (this.elementManifestList == null) {
				this.elementManifestList = new ArrayList<ElementManifest>();
			}
			this.elementManifestList.add(elementManifest);
		}
	}

	public void removeElementManifest(ElementManifest elementManifest) {
		if (elementManifest == null) {
			return;
		} else {
			if (this.elementManifestList == null) {
				return;
			}
			this.elementManifestList.remove(elementManifest);
		}
	}

	@ManyToOne
	public CostVersion getCostVersion() {
		return costVersion;
	}

	public void setCostVersion(CostVersion costVersion) {
		this.costVersion = costVersion;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ElementVariantList getElementVariantList() {
		return elementVariantList;
	}

	public void setElementVariantList(ElementVariantList elementVariantList) {
		this.elementVariantList = elementVariantList;
	}

	@ManyToOne
	public SubType getSubType() {
		return subType;
	}

	public void setSubType(SubType subType) {
		this.subType = subType;
	}
}

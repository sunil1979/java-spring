package com.ebuild.leap.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.springframework.data.mongodb.core.mapping.Document;

@SuppressWarnings("serial")
@Entity
@Table(name = "elementmanifest")
@Indexed
@XmlRootElement
@Document
public class ElementManifest extends EbuildBaseObject {

	private Long id;
	
	@Transient
	private Integer version;
	
	@Transient
	private Element parentElement;
	private Element childElement;
	private Double quantity;
	private Double scale;
	private Double yscale;
	private Double rotate;
	private Double positionX;
	private Double positionY;
	private Double positionZ;
	private Boolean customize;
	private String vlTrim;
	private Integer zOrder;
	private Integer yOrder;
	private String tag;
	private Integer elementGroup;
	private String elementGroupTag;
	private Integer prime;
	
	@XmlTransient
	private List<Element> negativeElementVariants;

	@Id
	@Column(name = "elementmanifest_id")
	@GeneratedValue(generator = "IdGenerator")
	@GenericGenerator(name = "IdGenerator", strategy = "com.ebuild.leap.util.RandomIdGenerator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ManyToOne
	@XmlTransient
	public Element getParentElement() {
		return parentElement;
	}

	public void setParentElement(Element parentElement) {
		this.parentElement = parentElement;
	}

	@ManyToOne
	@XmlElement(name = "element")
	public Element getChildElement() {
		return childElement;
	}

	public void setChildElement(Element childElement) {
		this.childElement = childElement;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	public Double getYscale() {
		return yscale;
	}

	public void setYscale(Double yscale) {
		this.yscale = yscale;
	}

	public Double getRotate() {
		return rotate;
	}

	public void setRotate(Double rotate) {
		this.rotate = rotate;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	public Double getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(Double positionZ) {
		this.positionZ = positionZ;
	}

	public Boolean getCustomize() {
		return customize;
	}

	public void setCustomize(Boolean customize) {
		this.customize = customize;
	}

	public String getVlTrim() {
		return vlTrim;
	}

	public void setVlTrim(String vlTrim) {
		this.vlTrim = vlTrim;
	}

	public Integer getzOrder() {
		return zOrder;
	}

	public void setzOrder(Integer zOrder) {
		this.zOrder = zOrder;
	}

	public Integer getyOrder() {
		return yOrder;
	}

	public void setyOrder(Integer yOrder) {
		this.yOrder = yOrder;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getElementGroup() {
		return elementGroup;
	}

	public void setElementGroup(Integer elementGroup) {
		this.elementGroup = elementGroup;
	}

	public String getElementGroupTag() {
		return elementGroupTag;
	}

	public void setElementGroupTag(String elementGroupTag) {
		this.elementGroupTag = elementGroupTag;
	}

	public Integer getPrime() {
		return prime;
	}

	public void setPrime(Integer prime) {
		this.prime = prime;
	}

	@ManyToMany
	@JoinTable(name = "manifest_negativeelements", joinColumns = @JoinColumn(name = "elementmanifest_id"), inverseJoinColumns = @JoinColumn(name = "element_id"))
	public List<Element> getNegativeElementVariants() {
		return negativeElementVariants;
	}

	public void setNegativeElementVariants(List<Element> negativeElementVariants) {
		this.negativeElementVariants = negativeElementVariants;
	}

	public void addNegativeElementVariant(Element negativeElementVariant) {
		if (negativeElementVariant == null) {
			return;
		} else if (this.negativeElementVariants == null) {
			this.negativeElementVariants = new ArrayList<Element>();
		}
		this.negativeElementVariants.add(negativeElementVariant);
	}
}

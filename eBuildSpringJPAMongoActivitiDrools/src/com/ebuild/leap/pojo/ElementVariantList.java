package com.ebuild.leap.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "variantlist")
@Indexed
@XmlRootElement
public class ElementVariantList extends EbuildBaseObject {
	private Long id;
	private Integer version;
	@Transient
	private List<Element> variants;

	@Id
	@Column(name = "variantlist_id")
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

	@OneToMany(mappedBy = "elementVariantList")
	@XmlTransient
	public List<Element> getVariants() {
		return variants;
	}

	public void setVariants(List<Element> variants) {
		this.variants = variants;
	}

	public void addVariantElement(Element variantElement) {
		if (variantElement == null) {
			return;
		} else if (this.variants == null) {
			this.variants = new ArrayList<Element>();
		}
		this.variants.add(variantElement);
	}

	public void removeVariantElement(Element variantElement) {
		if (variantElement == null || this.variants == null) {
			return;
		} else {
			this.variants.remove(variantElement);
		}
	}
}

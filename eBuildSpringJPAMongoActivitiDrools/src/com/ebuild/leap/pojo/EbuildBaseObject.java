package com.ebuild.leap.pojo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

@SuppressWarnings("serial")
@MappedSuperclass
public class EbuildBaseObject implements Serializable {

	@Transient
	private Date created;
	
	@Transient
	private Date updated;
	
	@Transient
	private User createdBy;
	
	@Transient
	private User updatedBy;
	
	@Transient
	private Boolean deleteStatus = false;
	
	@Transient
	private Boolean activeStatus = true;

	@PrePersist
	@XmlTransient
	protected void onCreate() {
		this.updated = this.created = new Date();
	}

	@PreUpdate
	@XmlTransient
	protected void onUpdate() {
		this.updated = new Date();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	@XmlTransient
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedDate")
	@XmlTransient
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@ManyToOne
	@XmlTransient
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@ManyToOne
	@XmlTransient
	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(nullable = true)
	@XmlTransient
	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	@Column(nullable = true)
	@XmlTransient
	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
}

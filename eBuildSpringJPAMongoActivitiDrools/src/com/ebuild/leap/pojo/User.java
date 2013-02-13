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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name="user")
@Indexed
@XmlRootElement
public class User extends EbuildBaseObject{
	private Long id;
	
	@Transient
	private Integer version;
	
	@Transient
	private List<HomeUnit> userHomeUnits;
	private String username;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String primaryPhone;
	private String alternatePhone;
	
	
	@Id
	@Column(name = "user_id")
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

	@OneToMany(mappedBy="ownerUser")
	@XmlTransient
	public List<HomeUnit> getUserHomeUnits() {
		return userHomeUnits;
	}

	public void setUserHomeUnits(List<HomeUnit> userHomeUnits) {
		this.userHomeUnits = userHomeUnits;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public String getAlternatePhone() {
		return alternatePhone;
	}

	public void setAlternatePhone(String alternatePhone) {
		this.alternatePhone = alternatePhone;
	}
}

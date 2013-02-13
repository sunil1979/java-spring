package com.ebuild.leap.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@SuppressWarnings("rawtypes")
@XmlRootElement(namespace = "com.ebuild.leap.pojo")
@XmlSeeAlso({ Brand.class, Category.class, CPR.class, Element.class,
		ElementManifest.class, Finish.class, HomeUnit.class,
		HomeUnitRevision.class, HomeUnitVersion.class, Material.class,
		Product.class, Project.class, Theme.class, Type.class, User.class })
public class EbuildleapResultObject {
	private List result;
	private String resultStatus;
	private String errCode;
	private String errDescription;

	@XmlElementWrapper(name = "resultList")
	@XmlAnyElement
	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDescription() {
		return errDescription;
	}

	public void setErrDescription(String errDescription) {
		this.errDescription = errDescription;
	}
	
	public void clear(){
		this.result = null;
		this.resultStatus = null;
		this.errCode = null;
		this.errDescription = null;
	}
}

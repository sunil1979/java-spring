package com.ebuild.leap.service;

import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.User;

public interface CustomizationServiceFacade {
	
	public EbuildleapResultObject createHomeUnit(User user, Product product, CostVersion costVersion);
	public EbuildleapResultObject getHomeUnitVersions(HomeUnit homeUnit);
	public EbuildleapResultObject getLatestHomeUnitRevision(HomeUnitVersion homeUnitVersion);
	public EbuildleapResultObject createNewRevision(HomeUnitRevision currentHomeUnitRevision,
			Element newChildElement, ElementManifest currentElementManifest);
}

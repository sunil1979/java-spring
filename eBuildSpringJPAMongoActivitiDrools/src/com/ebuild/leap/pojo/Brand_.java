package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Brand.class)
public class Brand_ {
	public static volatile SingularAttribute<Brand, Long> id;
	public static volatile SingularAttribute<Brand, String> name;
	public static volatile SingularAttribute<Brand, String> description;
}

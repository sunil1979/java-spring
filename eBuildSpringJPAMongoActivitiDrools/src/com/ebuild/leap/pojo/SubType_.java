package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SubType.class)
public class SubType_ {
	public static volatile SingularAttribute<SubType, Long> id;
	public static volatile SingularAttribute<SubType, String> name;
	public static volatile SingularAttribute<SubType, String> description;
}

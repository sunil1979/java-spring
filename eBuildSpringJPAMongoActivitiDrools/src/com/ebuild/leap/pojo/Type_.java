package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Type.class)
public class Type_ {
	public static volatile SingularAttribute<Type, Long> id;
	public static volatile SingularAttribute<Type, String> name;
	public static volatile SingularAttribute<Type, String> description;
}

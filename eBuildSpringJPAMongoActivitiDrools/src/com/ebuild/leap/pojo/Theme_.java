package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Theme.class)
public class Theme_ {
	public static volatile SingularAttribute<Theme, Long> id;
	public static volatile SingularAttribute<Theme, String> name;
	public static volatile SingularAttribute<Theme, String> description;
}

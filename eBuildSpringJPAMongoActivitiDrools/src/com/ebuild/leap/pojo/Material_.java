package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(Material.class)
public class Material_ {
	public static volatile SingularAttribute<Material, Long> id;
	public static volatile SingularAttribute<Material, String> name;
	public static volatile SingularAttribute<Material, String> description;
}

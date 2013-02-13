package com.ebuild.leap.pojo;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Finish.class)
public class Finish_ {
	public static volatile SingularAttribute<Finish, Long> id;
	public static volatile SingularAttribute<Finish, String> name;
	public static volatile SingularAttribute<Finish, String> description;
}

package com.ebuild.leap.pojo;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Element.class)
public class Element_ {
	public static volatile SingularAttribute<Element, Long> id;
	public static volatile SingularAttribute<Element, Integer> scope;
	public static volatile SingularAttribute<Element, Category> category;
	public static volatile SingularAttribute<Element, Type> type;
	public static volatile SingularAttribute<Element, String> name;
	public static volatile SingularAttribute<Element, String> description;
	public static volatile SingularAttribute<Element, String> code1;
	public static volatile SingularAttribute<Element, String> code2;
	public static volatile SingularAttribute<Element, String> code3;
	public static volatile SingularAttribute<Element, String> code4;
	public static volatile SingularAttribute<Element, String> code5;
	public static volatile SingularAttribute<Element, String> code6;
	public static volatile SingularAttribute<Element, String> view1;
	public static volatile SingularAttribute<Element, String> view2;
	public static volatile SingularAttribute<Element, String> view3;
	public static volatile SingularAttribute<Element, String> view4;
	public static volatile SingularAttribute<Element, String> dimension;
	public static volatile SingularAttribute<Element, String> weight;
	public static volatile ListAttribute<Element, Theme> elementThemes;
	public static volatile SingularAttribute<Element, Integer> function;
	public static volatile SingularAttribute<Element, Material> material;
	public static volatile SingularAttribute<Element, Finish> finish;
	public static volatile SingularAttribute<Element, Brand> brand;
	public static volatile SingularAttribute<Element, Double> price;
	public static volatile SingularAttribute<Element, Double> cost;
	public static volatile SingularAttribute<Element, Double> CPR;
	public static volatile ListAttribute<Element, ElementManifest> elementManifestList;
	public static volatile SingularAttribute<Element, CostVersion> costVersion;
	public static volatile SingularAttribute<Element, Product> product;
	public static volatile SingularAttribute<Element, ElementVariantList> elementVariantList;
	public static volatile SingularAttribute<Element, SubType> subType;
}

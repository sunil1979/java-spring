package com.ebuild.leap.repository.jpa;

import java.util.ArrayList;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.data.jpa.domain.Specification;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.Brand_;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.Category_;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.Element_;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.Finish_;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Material_;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.SubType_;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Theme_;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.Type_;

import com.ebuild.leap.util.EbuildleapConstants;
import java.util.List;

/*
 * Search Element based on following attributes
 * 
 * ElementName, ElementCode1, ElementMaterialName, ElementFinishName, ElementCategoryName, ElementTypeName, ElementDescription, ElementDimension, ElementWeight, 
 * ElementThemeName, ElementBrandName, ElementSubTypeName
 */
public class ElementSearchSpecification {

	public static Specification<Element> elementLike(final Element elementCritera) {
		return new Specification<Element>() {

			@Override
			public Predicate toPredicate(Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				final List<Predicate> predicateList = new ArrayList<Predicate>();
				/*
				 * ElementCode1 Predicate
				 */
				if (elementCritera.getCode1() != null) {
					predicateList.add(getCode1Predicate(elementCritera.getCode1(), root, query, builder));
				}
				/*
				 * ElementName Predicate
				 */
				if (elementCritera.getName() != null) {
					predicateList.add(getElementNamePredicate(elementCritera.getName(), root, query, builder));
				}
				/*
				 * ElementTypeName Predicate
				 */
				if(elementCritera.getType() != null && elementCritera.getType().getName() != null){
					Join<Element, Type> type = root.join(Element_.type);
					predicateList.add(getTypeNamePredicate(elementCritera.getType().getName(), type, query, builder));
				}
				/*
				 * ElementDescription Predicate
				 */
				if (elementCritera.getDescription() != null) {
					predicateList.add(getElementDescriptionPredicate(elementCritera.getDescription(), root, query, builder));
				}
				/*
				 * ElementDimension Predicate
				 */
				if(elementCritera.getDimension() != null){
					predicateList.add(getElementDimensionPredicate(elementCritera.getDimension(), root, query, builder));
				}
				/*
				 * ElementWeight Predicate
				 */
				if(elementCritera.getWeight() != null){
					predicateList.add(getElementWeightPredicate(elementCritera.getWeight(), root, query, builder));
				}
				/*
				 * ElementMaterialName Predicate
				 */
				if (elementCritera.getMaterial() != null && elementCritera.getMaterial().getName() != null) {
					Join<Element, Material> material = root.join(Element_.material);
					predicateList.add(getMaterialNamePredicate(elementCritera.getMaterial().getName(), material, query, builder));
				}
				/*
				 * ElementFinishName Predicate
				 */
				if (elementCritera.getFinish() != null && elementCritera.getFinish().getName() != null) {
					Join<Element, Finish> finish = root.join(Element_.finish);
					predicateList.add(getFinishNamePredicate(elementCritera.getFinish().getName(), finish, query, builder));
				}
				/*
				 * ElementCategoryName Predicate
				 */
				if(elementCritera.getCategory() != null && elementCritera.getCategory().getName() != null){
					Join<Element, Category> category = root.join(Element_.category);
					predicateList.add(getCategoryNamePredicate(elementCritera.getCategory().getName(), category, query, builder));
				}
				/*
				 * ElementBrandName Predicate
				 */
				if(elementCritera.getBrand() != null && elementCritera.getBrand().getName() != null){
					Join<Element, Brand> brand = root.join(Element_.brand);
					predicateList.add(getBrandNamePredicate(elementCritera.getBrand().getName(), brand, query, builder));
				}
				/*
				 * ElementSubTypeName Predicate
				 */
				if(elementCritera.getSubType() != null && elementCritera.getSubType().getName() != null){
					Join<Element, SubType> subType = root.join(Element_.subType);
					predicateList.add(getSubTypeNamePredicate(elementCritera.getSubType().getName(), subType, query, builder));
				}				
				/*
				 * ElementThemeName Predicate
				 */
				if (elementCritera.getElementThemes() != null && elementCritera.getElementThemes().size() > 0) {
					Theme themeCriteria = elementCritera.getElementThemes().get(0);
					ListJoin<Element, Theme> themes = root.join(Element_.elementThemes);
					predicateList.add(getThemeNamePredicate(themeCriteria.getName(), themes, query, builder));
				}
				Predicate p = builder.or(predicateList.toArray(new Predicate[predicateList.size()]));
				return p;
			}

			private Predicate getCode1Predicate(String code1Criteria, Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate code1Predicate = null;
				if (code1Criteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
					code1Predicate = builder.like(builder.lower(root.get(Element_.code1)),
							code1Criteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
				} else {
					code1Predicate = builder.equal(builder.lower(root.get(Element_.code1)), code1Criteria.toLowerCase());
				}
				return code1Predicate;
			}

			private Predicate getElementNamePredicate(String nameCriteria, Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate elementNamePredicate = null;
				if (nameCriteria != null) {
					if (nameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						elementNamePredicate = builder.like(builder.lower(root.get(Element_.name)),
								nameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						elementNamePredicate = builder.equal(builder.lower(root.get(Element_.name)), nameCriteria.toLowerCase());
					}
				}
				return elementNamePredicate;
			}

			private Predicate getElementDescriptionPredicate(String descriptionCriteria, Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate elementDescriptionPredicate = null;
				if (descriptionCriteria != null) {
					if (descriptionCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						elementDescriptionPredicate = builder.like(builder.lower(root.get(Element_.description)),
								descriptionCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						elementDescriptionPredicate = builder.equal(builder.lower(root.get(Element_.description)), descriptionCriteria.toLowerCase());
					}
				}
				return elementDescriptionPredicate;
			}
			
			private Predicate getElementDimensionPredicate(String dimensionCriteria, Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate elementDimensionPredicate = null;
				if (dimensionCriteria != null) {
					if (dimensionCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						elementDimensionPredicate = builder.like(builder.lower(root.get(Element_.dimension)),
								dimensionCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						elementDimensionPredicate = builder.equal(builder.lower(root.get(Element_.dimension)), dimensionCriteria.toLowerCase());
					}
				}
				return elementDimensionPredicate;
			}

			private Predicate getElementWeightPredicate(String weightCriteria, Root<Element> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate elementWeightPredicate = null;
				if (weightCriteria != null) {
					if (weightCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						elementWeightPredicate = builder.like(builder.lower(root.get(Element_.weight)),
								weightCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						elementWeightPredicate = builder.equal(builder.lower(root.get(Element_.weight)), weightCriteria.toLowerCase());
					}
				}
				return elementWeightPredicate;
			}

			private Predicate getMaterialNamePredicate(String materialNameCriteria, Join<Element, Material> material, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate materialNamePredicate = null;
				if (materialNameCriteria != null) {
					if (materialNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						materialNamePredicate = builder.like(builder.lower(material.get(Material_.name)),
								materialNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						materialNamePredicate = builder.equal(builder.lower(material.get(Material_.name)), materialNameCriteria.toLowerCase());
					}
				}
				return materialNamePredicate;
			}

			private Predicate getFinishNamePredicate(String finishNameCriteria, Join<Element, Finish> finish, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate finishNamePredicate = null;
				if (finishNameCriteria != null) {
					if (finishNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						finishNamePredicate = builder.like(builder.lower(finish.get(Finish_.name)),
								finishNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						finishNamePredicate = builder.equal(builder.lower(finish.get(Finish_.name)), finishNameCriteria.toLowerCase());
					}
				}
				return finishNamePredicate;
			}

			private Predicate getCategoryNamePredicate(String categoryNameCriteria, Join<Element, Category> category, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate categoryNamePredicate = null;
				if (categoryNameCriteria != null) {
					if (categoryNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						categoryNamePredicate = builder.like(builder.lower(category.get(Category_.name)),
								categoryNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						categoryNamePredicate = builder.equal(builder.lower(category.get(Category_.name)), categoryNameCriteria.toLowerCase());
					}
				}
				return categoryNamePredicate;
			}

			private Predicate getBrandNamePredicate(String brandNameCriteria, Join<Element, Brand> brand, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate brandNamePredicate = null;
				if (brandNameCriteria != null) {
					if (brandNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						brandNamePredicate = builder.like(builder.lower(brand.get(Brand_.name)),
								brandNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						brandNamePredicate = builder.equal(builder.lower(brand.get(Brand_.name)), brandNameCriteria.toLowerCase());
					}
				}
				return brandNamePredicate;
			}

			
			private Predicate getThemeNamePredicate(String themeNameCriteria, ListJoin<Element, Theme> themes, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate themeNamePredicate = null;
				if (themeNameCriteria != null) {
					if (themeNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						themeNamePredicate = builder.like(builder.lower(themes.get(Theme_.name)),
								themeNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						themeNamePredicate = builder.equal(builder.lower(themes.get(Theme_.name)), themeNameCriteria.toLowerCase());
					}
				}
				return themeNamePredicate;
			}
			
			private Predicate getTypeNamePredicate(String typeNameCriteria, Join<Element, Type> type, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate typeNamePredicate = null;
				if (typeNameCriteria != null) {
					if (typeNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						typeNamePredicate = builder.like(builder.lower(type.get(Type_.name)),
								typeNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						typeNamePredicate = builder.equal(builder.lower(type.get(Type_.name)), typeNameCriteria.toLowerCase());
					}
				}
				return typeNamePredicate;
			}

			private Predicate getSubTypeNamePredicate(String subTypeNameCriteria, Join<Element, SubType> subType, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Predicate subTypeNamePredicate = null;
				if (subTypeNameCriteria != null) {
					if (subTypeNameCriteria.contains(EbuildleapConstants.SEARCH_WILDCARD_CHAR)) {
						subTypeNamePredicate = builder.like(builder.lower(subType.get(SubType_.name)),
								subTypeNameCriteria.replace(EbuildleapConstants.SEARCH_WILDCARD_CHAR, "%").toLowerCase());
					} else {
						subTypeNamePredicate = builder.equal(builder.lower(subType.get(SubType_.name)), subTypeNameCriteria.toLowerCase());
					}
				}
				return subTypeNamePredicate;
			}
		};
	}
}

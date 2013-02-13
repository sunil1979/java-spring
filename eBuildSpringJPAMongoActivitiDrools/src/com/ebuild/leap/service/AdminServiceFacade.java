package com.ebuild.leap.service;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;

public interface AdminServiceFacade {
	/*
	 * BRAND related CRUD
	 */
	public EbuildleapResultObject createBrand(Brand brand);
	public EbuildleapResultObject getBrand(Brand brand);
	public EbuildleapResultObject updateBrand(Brand brand);
	public EbuildleapResultObject deleteBrand(Brand brand);
	public EbuildleapResultObject getAllBrand();
	
	/*
	 * CATEGORY related CRUD
	 */
	public EbuildleapResultObject createCategory(Category category);
	public EbuildleapResultObject getCategory(Category category);
	public EbuildleapResultObject updateCategory(Category category);
	public EbuildleapResultObject deleteCategory(Category category);
	public EbuildleapResultObject getAllCategory();
	
	/*
	 * CPR related CRUD
	 */
	public EbuildleapResultObject createCPR(CPR cpr);
	public EbuildleapResultObject getCPR(CPR cpr);
	public EbuildleapResultObject updateCPR(CPR cpr);
	public EbuildleapResultObject deleteCPR(CPR cpr);
	public EbuildleapResultObject getAllCPR();

	/*
	 * COSTVERSION related CRUD
	 */
	public EbuildleapResultObject createCostVersion(CostVersion costVersion);
	public EbuildleapResultObject getCostVersion(CostVersion costVersion);
	public EbuildleapResultObject updateCostVersion(CostVersion costVersion);
	public EbuildleapResultObject deleteCostVersion(CostVersion costVersion);
	public EbuildleapResultObject getAllCostVersion();
	
	/*
	 * FINISH related CRUD
	 */
	public EbuildleapResultObject createFinish(Finish finish);
	public EbuildleapResultObject getFinish(Finish finish);
	public EbuildleapResultObject updateFinish(Finish finish);
	public EbuildleapResultObject deleteFinish(Finish finish);
	public EbuildleapResultObject getAllFinish();
	
	/*
	 * MATERIAL related CRUD
	 */
	public EbuildleapResultObject createMaterial(Material material);
	public EbuildleapResultObject getMaterial(Material material);
	public EbuildleapResultObject updateMaterial(Material material);
	public EbuildleapResultObject deleteMaterial(Material material);
	public EbuildleapResultObject getAllMaterial();
	
	/*
	 * PROJECT related CRUD
	 */
	public EbuildleapResultObject createProject(Project project);
	public EbuildleapResultObject getProject(Project project);
	public EbuildleapResultObject updateProject(Project project);
	public EbuildleapResultObject deleteProject(Project project);
	public EbuildleapResultObject getAllProject();
	
	/*
	 * SUBTYPE related CRUD
	 */
	public EbuildleapResultObject createSubType(SubType subType);
	public EbuildleapResultObject getSubType(SubType subType);
	public EbuildleapResultObject updateSubType(SubType subType);
	public EbuildleapResultObject deleteSubType(SubType subType);
	public EbuildleapResultObject getAllSubType();
	
	/*
	 * THEME related CRUD
	 */
	public EbuildleapResultObject createTheme(Theme theme);
	public EbuildleapResultObject getTheme(Theme theme);
	public EbuildleapResultObject updateTheme(Theme theme);
	public EbuildleapResultObject deleteTheme(Theme theme);
	public EbuildleapResultObject getAllTheme();
	
	/*
	 * TYPE related CRUD
	 */
	public EbuildleapResultObject createType(Type type);
	public EbuildleapResultObject getType(Type type);
	public EbuildleapResultObject updateType(Type type);
	public EbuildleapResultObject deleteType(Type type);
	public EbuildleapResultObject getAllType();
	
	/*
	 * USER related CRUD
	 */
	public EbuildleapResultObject createUser(User user);
	public EbuildleapResultObject getUser(User user);
	public EbuildleapResultObject updateUser(User user);
	public EbuildleapResultObject deleteUser(User user);
	public EbuildleapResultObject getAllUser();
	
}

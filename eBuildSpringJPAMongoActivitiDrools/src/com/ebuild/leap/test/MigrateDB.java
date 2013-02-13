package com.ebuild.leap.test;

import java.sql.DriverManager;
import java.sql.ResultSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.ElementVariantList;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.*;

import java.sql.Statement;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class MigrateDB {

	protected static Logger log = LoggerFactory.getLogger(MigrateDB.class);

	private Connection dbConnection;

	@Autowired
	BrandRepository brandDao;

	@Autowired
	CategoryRepository catDao;

	@Autowired
	CPRRepository cprDao;

	@Autowired
	TypeRepository typeDao;

	@Autowired
	ElementRepository elementDao;

	@Autowired
	ElementManifestRepository elementManifestDao;

	@Autowired
	FinishRepository finishDao;

	@Autowired
	MaterialRepository materialDao;

	@Autowired
	ThemeRepository themeDao;

	@Autowired
	ProjectRepository projectDao;

	@Autowired
	ProductRepository productDao;

	@Autowired
	UserRepository userDao;

	@Autowired
	HomeUnitRepository homeUnitDao;

	@Autowired
	HomeUnitVersionRepository homeUnitVersionDao;

	@Autowired
	HomeUnitRevisionRepository homeUnitRevisionDao;

	@Autowired
	CostVersionRepository costVersionDao;

	@Autowired
	ElementVariantListRepository elementVariantListDao;

	
	public void loadBrands() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM BRAND");
		while (rst.next()) {
			Brand brand = new Brand();
			brand.setId(new Long(rst.getString("id")).longValue());
			brand.setName(rst.getString("name"));
			brand.setDescription(rst.getString("desc"));
			brandDao.save(brand);
		}
		this.closeConnection();
	}

	
	public void loadCategory() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM CATEGORY");
		while (rst.next()) {
			Category category = new Category();
			category.setId(new Long(rst.getString("id")).longValue());
			category.setName(rst.getString("name"));
			category.setDescription(rst.getString("comments"));
			System.out.println("category created");
			catDao.save(category);
		}
		this.closeConnection();
	}

	
	public void loadCPR() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM CPR");
		while (rst.next()) {
			CPR cpr = new CPR();
			cpr.setId(new Long(rst.getString("id")).longValue());
			cpr.setValue(new Double(rst.getString("value")).doubleValue());
			cpr.setTag(rst.getString("tag"));
			cpr.setComments(rst.getString("comments"));
			cprDao.save(cpr);
		}
		this.closeConnection();
	}

	
	public void loadCostVersion() throws Exception {
		CostVersion costVersion = new CostVersion();
		costVersion.setId(new Long("1").longValue());
		costVersion.setCostValue((double) 100);
		costVersion.setCostVersion(1);
		costVersion.setCpr(cprDao.findOne(new Long("1").longValue()));
		costVersionDao.save(costVersion);
	}

	
	public void loadTypes() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM TYPE");
		while (rst.next()) {
			Type type = new Type();
			type.setId(new Long(rst.getString("id")).longValue());
			type.setName(rst.getString("tag"));
			type.setDescription(rst.getString("comments"));
			typeDao.save(type);
		}
		this.closeConnection();
	}

	
	public void getItems() throws Exception {
		Category category = this.catDao.findOne(new Long(40).longValue());
		log.debug("*************************************************************************");
		if (category != null) {
			log.debug("Category Found :" + category.getId());
		} else {
			log.debug("Category Not Found ");
		}
		log.debug("*************************************************************************");
	}

	
	public void loadFinish() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM FINISH");
		while (rst.next()) {
			Finish finish = new Finish();
			finish.setId(new Long(rst.getString("id")).longValue());
			finish.setName(rst.getString("name"));
			finish.setDescription(rst.getString("desc"));
			finishDao.save(finish);
		}
		this.closeConnection();
	}

	
	public void loadMaterial() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM MATERIAL");
		while (rst.next()) {
			Material material = new Material();
			material.setId(new Long(rst.getString("id")).longValue());
			material.setName(rst.getString("name"));
			material.setDescription(rst.getString("desc"));
			materialDao.save(material);
		}
		this.closeConnection();
	}

	
	public void loadTheme() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM THEME");
		while (rst.next()) {
			Theme theme = new Theme();
			theme.setId(new Long(rst.getString("id")).longValue());
			theme.setName(rst.getString("name"));
			theme.setDescription(rst.getString("desc"));
			themeDao.save(theme);
		}
		this.closeConnection();
	}

	
	public void loadElements() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM ELEMENT");
		int elementCount = 0;
		ArrayList<Long> elementIds = new ArrayList<Long>();
		while (rst.next()) {
			Element element = new Element();
			elementIds.add(new Long(rst.getString("id")).longValue());
			Category category = catDao.findOne(new Long(rst.getString("category")).longValue());
			Type type = typeDao.findOne(new Long(rst.getString("type")).longValue());
			System.out.println("*************************************************************************");
			if (category != null) {
				System.out.println("Category Found :" + category.getId());
			} else {
				System.out.println("Category Not Found :" + rst.getString("category"));
			}
			if (type != null) {
				System.out.println("Type Found :" + type.getId());
			} else {
				System.out.println("Type Not Found :" + rst.getString("type"));
			}
			System.out.println("*************************************************************************" + rst.getString("id"));
			element.setId(new Long(rst.getString("id")).longValue());
			element.setCategory(category);
			element.setType(type);
			element.setName(rst.getString("name"));
			element.setDescription(rst.getString("description"));
			element.setCode1(rst.getString("code1"));
			element.setCode2(rst.getString("code2"));
			element.setCode3(rst.getString("code3"));
			element.setCode4(rst.getString("code4"));
			element.setCode5(rst.getString("code5"));
			element.setCode6(rst.getString("code6"));
			element.setView1(rst.getString("view1"));
			element.setView2(rst.getString("view2"));
			element.setView3(rst.getString("view3"));
			element.setView4(rst.getString("view4"));
			element.setDimension(rst.getString("dimension"));
			element.setWeight(rst.getString("weight"));
			element.setPrice(new Double(rst.getString("price")).doubleValue());
			element.setCost(new Double(rst.getString("cost")).doubleValue());
			element.setCostVersion(costVersionDao.findOne(new Long("1").longValue()));
			elementDao.save(element);
			elementCount = elementCount + 1;
			System.out.println(elementCount + " elements inserted");
			element = null;
			category = null;
			type = null;

		}
		System.out.println("ALL ELEMENTS INSERTED");
		this.closeConnection();
	}

	
	public void loadVariants() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		String sql1 = "SELECT distinct(id) FROM ELEMENT";
		ResultSet rst = stmt.executeQuery(sql1);
		int elementCount = 0;
		while (rst.next()) {
			Element mainElement = elementDao.findOne(new Long(rst.getString("id")).longValue());
			Statement stmt1 = dbConnection.createStatement();
			ResultSet rst1 = stmt1.executeQuery("select distinct(variant.variantListId) from variant,variantlist where variant.optionId="+ rst.getString("id")+" and variantlist.id=variant.variantListId and variantlist.`type`=0");
			while (rst1.next()) {
				String variantListSQL = "select distinct(optionId) from variant where variantListId="+ rst1.getString("variantListId");
				Statement stmt2 = dbConnection.createStatement();
				ResultSet rst2 = stmt2.executeQuery(variantListSQL);
				ElementVariantList evList = new ElementVariantList();
				evList.setId(new Long(rst1.getString("variantListId")).longValue());
				while (rst2.next()) {
					Long optionId = new Long(rst2.getString("optionId"))
							.longValue();
					Element variantElement = elementDao.findOne(optionId);
					evList.addVariantElement(variantElement);
					variantElement.setElementVariantList(evList);
				}
				System.out.println(evList.getVariants().size() +" Variants found for Element " + mainElement.getId());
				mainElement.setElementVariantList(evList);
				elementDao.save(mainElement);
			}
			elementCount = elementCount + 1;
			System.out.println("elementCount :"+elementCount);
		}
		
	}

	
	public void loadManifest() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM MANIFEST");
		int manifestCount = 0;
		while (rst.next()) {
			ElementManifest manifest = new ElementManifest();
			manifest.setId(new Long(rst.getString("id")).longValue());
			log.debug("PARENT ID :" + rst.getString("parentId"));
			log.debug("CHILD ID :" + rst.getString("childId"));
			Element parentElement = elementDao.findOne(new Long(rst.getString("parentId")).longValue());
			Element childElement = elementDao.findOne(new Long(rst.getString("childId")).longValue());
			manifest.setChildElement(childElement);

			String manifestStatus = rst.getString("active");
			if (manifestStatus.equalsIgnoreCase("1")) {
				manifest.setActiveStatus(true);
			} else {
				manifest.setActiveStatus(false);
			}
			String manifestCustomize = rst.getString("customize");
			if (manifestCustomize.equalsIgnoreCase("1")) {
				manifest.setCustomize(true);
			} else {
				manifest.setCustomize(false);
			}
			String zOrder = rst.getString("zorder");
			if (zOrder.equalsIgnoreCase("1")) {
				manifest.setzOrder(true);
			} else {
				manifest.setzOrder(false);
			}
			String yOrder = rst.getString("yorder");
			if (yOrder.equalsIgnoreCase("1")) {
				manifest.setyOrder(true);
			} else {
				manifest.setyOrder(false);
			}

			manifest.setQuantity(new Double(rst.getString("quantity")).doubleValue());
			manifest.setScale(new Double(rst.getString("scale")).doubleValue());
			manifest.setYscale(new Double(rst.getString("yscale")).doubleValue());
			manifest.setRotate(new Double(rst.getString("rotate")).doubleValue());
			manifest.setPositionX(new Double(rst.getString("positionx")).doubleValue());
			manifest.setPositionY(new Double(rst.getString("positiony")).doubleValue());
			manifest.setPositionZ(new Double(rst.getString("positionz")).doubleValue());
			String vlTrim = rst.getString("vlTrim");
			if (vlTrim != null && vlTrim.length() > 0) {
				StringTokenizer st = new StringTokenizer(vlTrim, ",");
				while (st.hasMoreTokens()) {
					Element negativeVariantElement = elementDao.findOne(new Long(st.nextToken()).longValue());
					manifest.addNegativeElementVariant(negativeVariantElement);
				}
			}
			manifest.setTag(rst.getString("tag"));
			manifest.setElementGroup(rst.getInt("group"));
			manifest.setElementGroupTag(rst.getString("groupTag"));
			manifest.setPrime(rst.getInt("prime"));
			manifest.setParentElement(parentElement);
			manifest = elementManifestDao.save(manifest);
			manifest = null;
			manifestCount = manifestCount + 1;
			System.out.println("Manifest Count :" + manifestCount);
		}
		this.closeConnection();
	}

	
	public void loadProject() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM PROJECT");
		while (rst.next()) {
			Project project = new Project();
			project.setId(new Long(rst.getString("id")).longValue());
			project.setName(rst.getString("name"));
			project.setDescription(rst.getString("description"));
			project.setCode1(rst.getString("code1"));
			String status = rst.getString("status");
			if (status.equalsIgnoreCase("1")) {
				project.setActiveStatus(true);
			} else {
				project.setActiveStatus(false);
			}
			projectDao.save(project);
		}
		this.closeConnection();
	}

	
	public void loadProducts() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM PRODUCT");
		while (rst.next()) {
			Product product = new Product();
			product.setId(new Long(rst.getString("id")).longValue());
			product.setName(rst.getString("name"));
			product.setDescription(rst.getString("description"));
			// product.setProductXML(rst.getString("productXML"));
			product.setCode1(rst.getString("code1"));
			product.setLibVersion(rst.getString("libVersion"));
			String status = rst.getString("status");
			if (status.equalsIgnoreCase("1")) {
				product.setActiveStatus(true);
			} else {
				product.setActiveStatus(false);
			}
			product.setBudget(new Double(rst.getString("budget")).doubleValue());
			product.setPrice(new Double(rst.getString("price")).doubleValue());
			Project project = projectDao.findOne(new Long(rst.getString("projectId")).longValue());
			Element element = elementDao.findOne(new Long(rst.getString("elementId")).longValue());
			product.setProject(project);
			product.setElement(element);
			productDao.save(product);
		}
		this.closeConnection();
	}

	
	public void loadUsers() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM USER");
		while (rst.next()) {
			User user = new User();
			user.setId(new Long(rst.getString("id")).longValue());
			String status = rst.getString("active");
			if (status.equalsIgnoreCase("1")) {
				user.setActiveStatus(true);
			} else {
				user.setActiveStatus(false);
			}
			user.setAlternatePhone(rst.getString("contact"));
			user.setEmail(rst.getString("email"));
			user.setFirstName(rst.getString("name"));
			user.setLastName(rst.getString("name"));
			user.setMiddleName(rst.getString("name"));
			user.setPassword(rst.getString("password"));
			user.setPrimaryPhone(rst.getString("phone"));
			user.setUsername(rst.getString("login"));
			userDao.save(user);
		}
		this.closeConnection();
	}

	
	public void loadHomeUnits() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM HOMEUNIT");
		CostVersion costversion = costVersionDao.findOne(new Long("1").longValue());
		while (rst.next()) {
			HomeUnit homeUnit = new HomeUnit();
			homeUnit.setId(new Long(rst.getString("id")).longValue());
			String status = rst.getString("active");
			if (status.equalsIgnoreCase("1")) {
				homeUnit.setActiveStatus(true);
			} else {
				homeUnit.setActiveStatus(false);
			}

			homeUnit.setBudget(new Double(rst.getString("budget")).doubleValue());
			homeUnit.setCode1(rst.getString("code1"));
			homeUnit.setDescription(rst.getString("description"));
			homeUnit.setName(rst.getString("name"));
			homeUnit.setPrice(new Double(rst.getString("price")).doubleValue());
			User ownerUser = userDao.findOne(new Long(rst.getString("customerId")).longValue());
			homeUnit.setOwnerUser(ownerUser);
			homeUnit.setCostVersion(costversion);
			Product product = productDao.findOne(new Long(rst.getString("productId")).longValue());
			homeUnit.setProduct(product);
			homeUnitDao.save(homeUnit);
		}
		this.closeConnection();
	}

	
	public void loadVersions() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM VERSION");
		while (rst.next()) {
			HomeUnitVersion version = new HomeUnitVersion();
			version.setId(new Long(rst.getString("id")).longValue());
			String status = rst.getString("active");
			if (status.equalsIgnoreCase("1")) {
				version.setActiveStatus(true);
			} else {
				version.setActiveStatus(false);
			}
			version.setBudget(new Double(rst.getString("budget")).doubleValue());
			version.setCode1(rst.getString("code1"));
			HomeUnit homeUnit = homeUnitDao.findOne(new Long(rst.getString("homeunitId")).longValue());
			version.setHomeUnit(homeUnit);
			version.setPrice(new Double(rst.getString("price")).doubleValue());
			version.setVersionDescription(rst.getString("description"));
			version.setVersionNumber(new Integer(rst.getString("number")));
			version.setVersionTag(rst.getString("tag"));
			homeUnitVersionDao.save(version);
		}
		this.closeConnection();
	}

	
	public void loadRevisions() throws Exception {
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM REVISION");
		while (rst.next()) {
			HomeUnitRevision revision = new HomeUnitRevision();
			revision.setId(new Long(rst.getString("id")).longValue());
			String status = rst.getString("active");
			if (status.equalsIgnoreCase("1")) {
				revision.setActiveStatus(true);
			} else {
				revision.setActiveStatus(false);
			}

			revision.setBudget(new Double(rst.getString("budget")).doubleValue());
			revision.setCode1(rst.getString("code1"));
			HomeUnitVersion version = homeUnitVersionDao.findOne(new Long(rst.getString("versionId")).longValue());
			revision.setHomeUnitVersion(version);
			revision.setPrice(new Double(rst.getString("price")).doubleValue());
			revision.setRevisionDescription(rst.getString("description"));
			revision.setRevisionNumber(new Integer(rst.getString("number")));
			revision.setRevisionTag(rst.getString("tag"));
			// revision.setRevisionXML(rst.getString("versionXML"));
			homeUnitRevisionDao.save(revision);
		}
		this.closeConnection();
	}

	private void getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebuild_afhw", "root", "root");
	}

	private void closeConnection() throws Exception {
		if (dbConnection != null)
			dbConnection.close();
	}

}

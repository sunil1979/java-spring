package com.ebuild.leap.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.EbuildBaseObject;
import com.ebuild.leap.pojo.EbuildleapResultObject;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;
import com.ebuild.leap.repository.jpa.BrandRepository;
import com.ebuild.leap.repository.jpa.CPRRepository;
import com.ebuild.leap.repository.jpa.CategoryRepository;
import com.ebuild.leap.repository.jpa.CostVersionRepository;
import com.ebuild.leap.repository.jpa.FinishRepository;
import com.ebuild.leap.repository.jpa.MaterialRepository;
import com.ebuild.leap.repository.jpa.ProjectRepository;
import com.ebuild.leap.repository.jpa.SubTypeRepository;
import com.ebuild.leap.repository.jpa.ThemeRepository;
import com.ebuild.leap.repository.jpa.TypeRepository;
import com.ebuild.leap.repository.jpa.UserRepository;
import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapPropertiesUtil;
import com.ebuild.leap.util.EbuildleapUtil;
import com.ebuild.leap.util.NullAwareBeanUtilsBean;

@Component
@TransactionConfiguration(defaultRollback = false)
public class AdminServiceFacadeImpl implements AdminServiceFacade {

	protected static Logger log = LoggerFactory.getLogger(AdminServiceFacadeImpl.class);
	private EbuildleapResultObject ero = new EbuildleapResultObject();

	@Autowired
	private EbuildleapUtil ebuildLeapUtil;

	@Autowired
	private EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;

	@Autowired
	private NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CostVersionRepository costVersionRepository;

	@Autowired
	private FinishRepository finishRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SubTypeRepository subTypeRepository;

	@Autowired
	private ThemeRepository themeRepository;

	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private CPRRepository cprRepository;

	@PersistenceContext(unitName = "ebuildPersistenceUnit")
	private EntityManager entityManager;

	@Override
	public EbuildleapResultObject createBrand(Brand brand) {
		log.debug("START - AdminServiceFacadeImpl - createBrand");
		try {
			ero.clear();
			brand = (Brand) validateObject(brand);
			brand = brandRepository.save(brand);
			List<Brand> result = new ArrayList<Brand>();
			result.add(brand);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_BRAND);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_BRAND) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createBrand");
		return ero;
	}

	@Override
	public EbuildleapResultObject getBrand(Brand brand) {
		log.debug("START - AdminServiceFacadeImpl - getBrand");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(brand)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_BRAND_ID));
			}
			Brand brandData = brandRepository.findOne(brand.getId());
			if (brandData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + brand.getId());
			}
			List<Brand> result = new ArrayList<Brand>();
			result.add(brandData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_BRAND);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_BRAND) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getBrand");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateBrand(Brand brand) {
		log.debug("START - AdminServiceFacadeImpl - updateBrand");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(brand)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_BRAND_ID));
			}
			Brand brandData = brandRepository.findOne(brand.getId());
			if (brandData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + brand.getId());
			}
			brand = (Brand) validateObject(brand);
			nullAwareBeanUtilsBean.copyProperties(brandData, brand);
			
			brandData = brandRepository.save(brandData);
			List<Brand> result = new ArrayList<Brand>();
			result.add(brandData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_BRAND);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_BRAND) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateBrand");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteBrand(Brand brand) {
		log.debug("START - AdminServiceFacadeImpl - deleteBrand");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(brand)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_BRAND_ID));
			}
			Brand brandData = brandRepository.findOne(brand.getId());
			if (brandData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + brand.getId());
			}
			brandRepository.delete(brandData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_BRAND);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_BRAND) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteBrand");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllBrand() {
		log.debug("START - AdminServiceFacadeImpl - getAllBrand");
		try {
			ero.clear();
			ero.setResult(brandRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_BRAND);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_BRAND) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllBrand");
		return ero;
	}

	@Override
	public EbuildleapResultObject createCategory(Category category) {
		log.debug("START - AdminServiceFacadeImpl - createCategory");
		try {
			ero.clear();
			category = (Category) validateObject(category);
			category = categoryRepository.save(category);
			List<Category> result = new ArrayList<Category>();
			result.add(category);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_CATEGORY);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_CATEGORY) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createCategory");
		return ero;
	}

	@Override
	public EbuildleapResultObject getCategory(Category category) {
		log.debug("START - AdminServiceFacadeImpl - getCategory");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(category)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CATEGORY_ID));
			}
			Category categoryData = categoryRepository.findOne(category.getId());
			if (categoryData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + category.getId());
			}
			List<Category> result = new ArrayList<Category>();
			result.add(categoryData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_CATEGORY);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_CATEGORY) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getCategory");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateCategory(Category category) {
		log.debug("START - AdminServiceFacadeImpl - updateCategory");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(category)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CATEGORY_ID));
			}
			Category categoryData = categoryRepository.findOne(category.getId());
			if (categoryData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + category.getId());
			}
			category = (Category) validateObject(category);
			nullAwareBeanUtilsBean.copyProperties(categoryData, category);
			categoryData = categoryRepository.save(categoryData);
			List<Category> result = new ArrayList<Category>();
			result.add(categoryData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_CATEGORY);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_CATEGORY) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateCategory");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteCategory(Category category) {
		log.debug("START - AdminServiceFacadeImpl - deleteCategory");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(category)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CATEGORY_ID));
			}
			Category categoryData = categoryRepository.findOne(category.getId());
			if (categoryData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + category.getId());
			}
			categoryRepository.delete(categoryData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_CATEGORY);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_CATEGORY) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteCategory");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllCategory() {
		log.debug("START - AdminServiceFacadeImpl - getAllCategory");
		try {
			ero.clear();
			ero.setResult(categoryRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_CATEGORY);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_CATEGORY) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllCategory");
		return ero;
	}

	@Override
	public EbuildleapResultObject createCostVersion(CostVersion costVersion) {
		log.debug("START - AdminServiceFacadeImpl - createCostVersion");
		try {
			ero.clear();
			costVersion = (CostVersion) validateObject(costVersion);
			costVersion = costVersionRepository.save(costVersion);
			List<CostVersion> result = new ArrayList<CostVersion>();
			result.add(costVersion);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_COSTVERSION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_COSTVERSION) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createCostVersion");
		return ero;
	}

	@Override
	public EbuildleapResultObject getCostVersion(CostVersion costVersion) {
		log.debug("START - AdminServiceFacadeImpl - getCostVersion");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(costVersion)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_COSTVERSION_ID));
			}
			CostVersion costVersionData = costVersionRepository.findOne(costVersion.getId());
			if (costVersionData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + costVersion.getId());
			}
			List<CostVersion> result = new ArrayList<CostVersion>();
			result.add(costVersionData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_COSTVERSION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_COSTVERSION) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getCostVersion");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateCostVersion(CostVersion costVersion) {
		log.debug("START - AdminServiceFacadeImpl - updateCostVersion");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(costVersion)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_COSTVERSION_ID));
			}
			CostVersion costVersionData = costVersionRepository.findOne(costVersion.getId());
			if (costVersionData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + costVersion.getId());
			}
			costVersion = (CostVersion) validateObject(costVersion);
			nullAwareBeanUtilsBean.copyProperties(costVersionData, costVersion);
			costVersionData = costVersionRepository.save(costVersionData);
			List<CostVersion> result = new ArrayList<CostVersion>();
			result.add(costVersionData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_COSTVERSION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_COSTVERSION) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateCostVersion");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteCostVersion(CostVersion costVersion) {
		log.debug("START - AdminServiceFacadeImpl - deleteCostVersion");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(costVersion)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_COSTVERSION_ID));
			}
			CostVersion costVersionData = costVersionRepository.findOne(costVersion.getId());
			if (costVersionData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + costVersion.getId());
			}
			costVersionRepository.delete(costVersionData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_COSTVERSION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_COSTVERSION) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteCostVersion");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllCostVersion() {
		log.debug("START - AdminServiceFacadeImpl - getAllCostVersion");
		try {
			ero.clear();
			ero.setResult(costVersionRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_COSTVERSION);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_COSTVERSION) + " - " + e.getClass()
					+ ": " + e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllCostVersion");
		return ero;
	}

	@Override
	public EbuildleapResultObject createFinish(Finish finish) {
		log.debug("START - AdminServiceFacadeImpl - createFinish");
		try {
			ero.clear();
			finish = (Finish) validateObject(finish);
			finish = finishRepository.save(finish);
			List<Finish> result = new ArrayList<Finish>();
			result.add(finish);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_FINISH);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_FINISH) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createFinish");
		return ero;
	}

	@Override
	public EbuildleapResultObject getFinish(Finish finish) {
		log.debug("START - AdminServiceFacadeImpl - getFinish");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(finish)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_FINISH_ID));
			}
			Finish finishData = finishRepository.findOne(finish.getId());
			if (finishData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + finish.getId());
			}
			List<Finish> result = new ArrayList<Finish>();
			result.add(finishData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_FINISH);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_FINISH) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getFinish");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateFinish(Finish finish) {
		log.debug("START - AdminServiceFacadeImpl - updateFinish");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(finish)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_FINISH_ID));
			}
			Finish finishData = finishRepository.findOne(finish.getId());
			if (finishData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + finish.getId());
			}
			finish = (Finish) validateObject(finish);
			nullAwareBeanUtilsBean.copyProperties(finishData, finish);
			finishData = finishRepository.save(finishData);
			List<Finish> result = new ArrayList<Finish>();
			result.add(finishData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_FINISH);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_FINISH) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateFinish");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteFinish(Finish finish) {
		log.debug("START - AdminServiceFacadeImpl - deleteFinish");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(finish)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_FINISH_ID));
			}
			Finish finishData = finishRepository.findOne(finish.getId());
			if (finishData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + finish.getId());
			}
			finishRepository.delete(finishData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_FINISH);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_FINISH) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteFinish");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllFinish() {
		log.debug("START - AdminServiceFacadeImpl - getAllFinish");
		try {
			ero.clear();
			ero.setResult(finishRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_FINISH);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_FINISH) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllFinish");
		return ero;
	}

	@Override
	public EbuildleapResultObject createMaterial(Material material) {
		log.debug("START - AdminServiceFacadeImpl - createMaterial");
		try {
			ero.clear();
			material = (Material) validateObject(material);
			material = materialRepository.save(material);
			List<Material> result = new ArrayList<Material>();
			result.add(material);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_MATERIAL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_MATERIAL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createMaterial");
		return ero;
	}

	@Override
	public EbuildleapResultObject getMaterial(Material material) {
		log.debug("START - AdminServiceFacadeImpl - getMaterial");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(material)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_MATERIAL_ID));
			}
			Material materialData = materialRepository.findOne(material.getId());
			if (materialData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + material.getId());
			}
			List<Material> result = new ArrayList<Material>();
			result.add(materialData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_MATERIAL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_MATERIAL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getMaterial");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateMaterial(Material material) {
		log.debug("START - AdminServiceFacadeImpl - updateMaterial");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(material)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_MATERIAL_ID));
			}
			Material materialData = materialRepository.findOne(material.getId());
			if (materialData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + material.getId());
			}
			material = (Material) validateObject(material);
			nullAwareBeanUtilsBean.copyProperties(materialData, material);
			materialData = materialRepository.save(materialData);
			List<Material> result = new ArrayList<Material>();
			result.add(materialData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_MATERIAL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_MATERIAL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateMaterial");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteMaterial(Material material) {
		log.debug("START - AdminServiceFacadeImpl - deleteMaterial");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(material)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_MATERIAL_ID));
			}
			Material materialData = materialRepository.findOne(material.getId());
			if (materialData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + material.getId());
			}
			materialRepository.delete(materialData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_MATERIAL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_MATERIAL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteMaterial");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllMaterial() {
		log.debug("START - AdminServiceFacadeImpl - getAllMaterial");
		try {
			ero.clear();
			ero.setResult(materialRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_MATERIAL);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_MATERIAL) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllMaterial");
		return ero;
	}

	@Override
	public EbuildleapResultObject createProject(Project project) {
		log.debug("START - AdminServiceFacadeImpl - createProject");
		try {
			ero.clear();
			project = (Project) validateObject(project);
			project = projectRepository.save(project);
			List<Project> result = new ArrayList<Project>();
			result.add(project);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_PROJECT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_PROJECT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createProject");
		return ero;
	}

	@Override
	public EbuildleapResultObject getProject(Project project) {
		log.debug("START - AdminServiceFacadeImpl - getProject");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(project)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PROJECT_ID));
			}
			Project projectData = projectRepository.findOne(project.getId());
			if (projectData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + project.getId());
			}
			List<Project> result = new ArrayList<Project>();
			result.add(projectData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_PROJECT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_PROJECT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getProject");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateProject(Project project) {
		log.debug("START - AdminServiceFacadeImpl - updateProject");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(project)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PROJECT_ID));
			}
			Project projectData = projectRepository.findOne(project.getId());
			if (projectData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + project.getId());
			}
			project = (Project) validateObject(project);
			nullAwareBeanUtilsBean.copyProperties(projectData, project);
			projectData = projectRepository.save(projectData);
			List<Project> result = new ArrayList<Project>();
			result.add(projectData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_PROJECT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_PROJECT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateProject");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteProject(Project project) {
		log.debug("START - AdminServiceFacadeImpl - deleteProject");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(project)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_PROJECT_ID));
			}
			Project projectData = projectRepository.findOne(project.getId());
			if (projectData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + project.getId());
			}
			projectRepository.delete(projectData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_PROJECT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_PROJECT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteProject");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllProject() {
		log.debug("START - AdminServiceFacadeImpl - getAllProject");
		try {
			ero.clear();
			ero.setResult(projectRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_PROJECT);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_PROJECT) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllProject");
		return ero;
	}

	@Override
	public EbuildleapResultObject createSubType(SubType subType) {
		log.debug("START - AdminServiceFacadeImpl - createSubType");
		try {
			ero.clear();
			subType = (SubType) validateObject(subType);
			subType = subTypeRepository.save(subType);
			List<SubType> result = new ArrayList<SubType>();
			result.add(subType);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_SUBTYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_SUBTYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createSubType");
		return ero;
	}

	@Override
	public EbuildleapResultObject getSubType(SubType subType) {
		log.debug("START - AdminServiceFacadeImpl - getSubType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(subType)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_SUBTYPE_ID));
			}
			SubType subTypeData = subTypeRepository.findOne(subType.getId());
			if (subTypeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + subType.getId());
			}
			List<SubType> result = new ArrayList<SubType>();
			result.add(subTypeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_SUBTYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_SUBTYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getSubType");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateSubType(SubType subType) {
		log.debug("START - AdminServiceFacadeImpl - updateSubType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(subType)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_SUBTYPE_ID));
			}
			SubType subTypeData = subTypeRepository.findOne(subType.getId());
			if (subTypeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + subType.getId());
			}
			subType = (SubType) validateObject(subType);
			nullAwareBeanUtilsBean.copyProperties(subTypeData, subType);
			subTypeData = subTypeRepository.save(subTypeData);
			List<SubType> result = new ArrayList<SubType>();
			result.add(subTypeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_SUBTYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_SUBTYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateSubType");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteSubType(SubType subType) {
		log.debug("START - AdminServiceFacadeImpl - deleteSubType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(subType)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_SUBTYPE_ID));
			}
			SubType subTypeData = subTypeRepository.findOne(subType.getId());
			if (subTypeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + subType.getId());
			}
			subTypeRepository.delete(subTypeData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_SUBTYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_SUBTYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteSubType");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllSubType() {
		log.debug("START - AdminServiceFacadeImpl - getAllSubType");
		try {
			ero.clear();
			ero.setResult(subTypeRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_SUBTYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_SUBTYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllSubType");
		return ero;
	}

	@Override
	public EbuildleapResultObject createTheme(Theme theme) {
		log.debug("START - AdminServiceFacadeImpl - createTheme");
		try {
			ero.clear();
			theme = (Theme) validateObject(theme);
			theme = themeRepository.save(theme);
			List<Theme> result = new ArrayList<Theme>();
			result.add(theme);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_THEME);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_THEME) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createTheme");
		return ero;
	}

	@Override
	public EbuildleapResultObject getTheme(Theme theme) {
		log.debug("START - AdminServiceFacadeImpl - getTheme");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(theme)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_THEME_ID));
			}
			Theme themeData = themeRepository.findOne(theme.getId());
			if (themeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + theme.getId());
			}
			List<Theme> result = new ArrayList<Theme>();
			result.add(themeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_THEME);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_THEME) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getTheme");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateTheme(Theme theme) {
		log.debug("START - AdminServiceFacadeImpl - updateTheme");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(theme)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_THEME_ID));
			}
			Theme themeData = themeRepository.findOne(theme.getId());
			if (themeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + theme.getId());
			}
			theme = (Theme) validateObject(theme);
			nullAwareBeanUtilsBean.copyProperties(themeData, theme);
			themeData = themeRepository.save(themeData);
			List<Theme> result = new ArrayList<Theme>();
			result.add(themeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_THEME);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_THEME) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateTheme");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteTheme(Theme theme) {
		log.debug("START - AdminServiceFacadeImpl - deleteTheme");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(theme)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_THEME_ID));
			}
			Theme themeData = themeRepository.findOne(theme.getId());
			if (themeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + theme.getId());
			}
			themeRepository.delete(themeData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_THEME);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_THEME) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteTheme");
		return ero;
	}

	
	@Override
	public EbuildleapResultObject getAllTheme() {
		log.debug("START - AdminServiceFacadeImpl - getAllTheme");
		try {
			ero.clear();
			ero.setResult(themeRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_THEME);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_THEME) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllTheme");
		return ero;
	}

	
	@Override
	public EbuildleapResultObject createType(Type type) {
		log.debug("START - AdminServiceFacadeImpl - createType");
		try {
			ero.clear();
			type = (Type) validateObject(type);
			type = typeRepository.save(type);
			List<Type> result = new ArrayList<Type>();
			result.add(type);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_TYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_TYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createType");
		return ero;
	}

	@Override
	public EbuildleapResultObject getType(Type type) {
		log.debug("START - AdminServiceFacadeImpl - getType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(type)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_TYPE_ID));
			}
			Type typeData = typeRepository.findOne(type.getId());
			if (typeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + type.getId());
			}
			List<Type> result = new ArrayList<Type>();
			result.add(typeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_TYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_TYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getType");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateType(Type type) {
		log.debug("START - AdminServiceFacadeImpl - updateType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(type)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_TYPE_ID));
			}
			Type typeData = typeRepository.findOne(type.getId());
			if (typeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + type.getId());
			}
			nullAwareBeanUtilsBean.copyProperties(typeData, type);
			typeData = (Type) validateObject(typeData);
			typeData = typeRepository.save(typeData);
			List<Type> result = new ArrayList<Type>();
			result.add(typeData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_TYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_TYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateType");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteType(Type type) {
		log.debug("START - AdminServiceFacadeImpl - deleteType");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(type)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_TYPE_ID));
			}
			Type typeData = typeRepository.findOne(type.getId());
			if (typeData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + type.getId());
			}
			typeRepository.delete(typeData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_TYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_TYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteType");
		return ero;
	}

	
	@Override
	public EbuildleapResultObject getAllType() {
		log.debug("START - AdminServiceFacadeImpl - getAllType");
		try {
			ero.clear();
			ero.setResult(typeRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_TYPE);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_TYPE) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllType");
		return ero;
	}

	
	
	@Override
	public EbuildleapResultObject createUser(User user) {
		log.debug("START - AdminServiceFacadeImpl - createUser");
		try {
			ero.clear();
			user = (User) validateObject(user);
			user = userRepository.save(user);
			List<User> result = new ArrayList<User>();
			result.add(user);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_USER);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_USER) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createUser");
		return ero;
	}

	@Override
	public EbuildleapResultObject getUser(User user) {
		log.debug("START - AdminServiceFacadeImpl - getUser");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(user)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_USER_ID));
			}
			User userData = userRepository.findOne(user.getId());
			if (userData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + user.getId());
			}
			List<User> result = new ArrayList<User>();
			result.add(userData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_USER);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_USER) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getUser");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateUser(User user) {
		log.debug("START - AdminServiceFacadeImpl - updateUser");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(user)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_USER_ID));
			}
			User userData = userRepository.findOne(user.getId());
			if (userData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + user.getId());
			}
			user = (User) validateObject(user);
			nullAwareBeanUtilsBean.copyProperties(userData, user);
			userData = userRepository.save(userData);
			List<User> result = new ArrayList<User>();
			result.add(userData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_USER);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_USER) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateUser");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteUser(User user) {
		log.debug("START - AdminServiceFacadeImpl - deleteUser");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(user)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_USER_ID));
			}
			User userData = userRepository.findOne(user.getId());
			if (userData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + user.getId());
			}
			userRepository.delete(userData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_USER);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_USER) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteUser");
		return ero;
	}

	
	@Override
	public EbuildleapResultObject getAllUser() {
		log.debug("START - AdminServiceFacadeImpl - getAllUser");
		try {
			ero.clear();
			ero.setResult(userRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_USER);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_USER) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllUser");
		return ero;
	}

	
	@Override
	public EbuildleapResultObject createCPR(CPR cpr) {
		log.debug("START - AdminServiceFacadeImpl - createCPR");
		try {
			ero.clear();
			cpr = (CPR) validateObject(cpr);
			cpr = cprRepository.save(cpr);
			List<CPR> result = new ArrayList<CPR>();
			result.add(cpr);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_CREATING_CPR);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_CREATING_CPR) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - createCPR");
		return ero;
	}

	@Override
	public EbuildleapResultObject getCPR(CPR cpr) {
		log.debug("START - AdminServiceFacadeImpl - getCPR");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(cpr)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CPR_ID));
			}
			CPR cprData = cprRepository.findOne(cpr.getId());
			if (cprData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + cpr.getId());
			}
			List<CPR> result = new ArrayList<CPR>();
			result.add(cprData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_CPR);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_CPR) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getCPR");
		return ero;
	}

	@Override
	public EbuildleapResultObject updateCPR(CPR cpr) {
		log.debug("START - AdminServiceFacadeImpl - updateCPR");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(cpr)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CPR_ID));
			}
			CPR cprData = cprRepository.findOne(cpr.getId());
			if (cprData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + cpr.getId());
			}
			cpr = (CPR) validateObject(cpr);
			nullAwareBeanUtilsBean.copyProperties(cprData, cpr);
			cprData = cprRepository.save(cprData);
			List<CPR> result = new ArrayList<CPR>();
			result.add(cprData);
			ero.setResult(result);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_UPDATING_CPR);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_UPDATING_CPR) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - updateCPR");
		return ero;
	}

	@Override
	public EbuildleapResultObject deleteCPR(CPR cpr) {
		log.debug("START - AdminServiceFacadeImpl - deleteCPR");
		try {
			ero.clear();
			if (ebuildLeapUtil.isNull(cpr)) {
				// throw exception
				throw new Exception(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.MISSING_CPR_ID));
			}
			CPR cprData = cprRepository.findOne(cpr.getId());
			if (cprData == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + cpr.getId());
			}
			cprRepository.delete(cprData);
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_DELETING_CPR);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_DELETING_CPR) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - deleteCPR");
		return ero;
	}

	@Override
	public EbuildleapResultObject getAllCPR() {
		log.debug("START - AdminServiceFacadeImpl - getAllCPR");
		try {
			ero.clear();
			ero.setResult(cprRepository.findAll());
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_SUCCESSFUL);
		} catch (Exception e) {
			log.debug(e.getClass() + ": " + e.getMessage(), e);
			e.printStackTrace();
			ero.setResultStatus(EbuildleapConstants.SERVICE_CALL_FAILED);
			ero.setErrCode(EbuildleapConstants.ERROR_RETRIEVING_CPR);
			ero.setErrDescription(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.ERROR_RETRIEVING_CPR) + " - " + e.getClass() + ": "
					+ e.getMessage());
		}
		log.debug("END - AdminServiceFacadeImpl - getAllCPR");
		return ero;
	}

	
	private Object validateObject(Object t) throws Exception {
		t = validateEbuildBaseObject((EbuildBaseObject) t);

		if (t instanceof CostVersion) {
			if (!ebuildLeapUtil.isNull(((CostVersion) t).getCpr())) {
				CPR cprData = cprRepository.findOne(((CostVersion) t).getCpr().getId());
				if (cprData == null) {
					// throw exception
					throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
							+ " - " + ((CostVersion) t).getCpr().getId());
				}
				((CostVersion) t).setCpr(cprData);
			}
		}

		return t;
	}

	private Object validateEbuildBaseObject(EbuildBaseObject baseObject) throws Exception {
		if (baseObject == null) {
			// throw exception
		}

		if (!ebuildLeapUtil.isNull(baseObject.getCreatedBy()) && !entityManager.contains(baseObject.getCreatedBy())) {
			User createdBy = userRepository.findOne(baseObject.getCreatedBy().getId());
			if (createdBy == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + baseObject.getCreatedBy().getId());
			}
			baseObject.setCreatedBy(createdBy);
		}

		if (!ebuildLeapUtil.isNull(baseObject.getUpdatedBy()) && !entityManager.contains(baseObject.getUpdatedBy())) {
			User updatedBy = userRepository.findOne(baseObject.getUpdatedBy().getId());
			if (updatedBy == null) {
				// throw exception
				throw new DataRetrievalFailureException(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.OBJECT_NOT_FOUND_IN_DATASTORE)
						+ " - " + baseObject.getUpdatedBy().getId());
			}
			baseObject.setUpdatedBy(updatedBy);
		}
		return baseObject;
	}

}

package com.ebuild.leap.util;

public interface EbuildleapConstants {

	// PROPERTIES FILE NAMES
	public static final String EBUILDLEAP_PROPERTIES_FILE = "ebuildleap.properties";

	// DATASTORE CONTEXT
	public static final String DATASTORE_RDBMS = "RDBMS";
	public static final String DATASTORE_MONGO = "MONGO";

	// SERVICE CALL RESULT
	public static final String SERVICE_CALL_SUCCESSFUL = "SUCCESS";
	public static final String SERVICE_CALL_FAILED = "FAILURE";

	// ERROR CODES
	public static final String GET_ELEMENTS_FOR_CATEGORY_FAILED = "1";
	public static final String ERROR_CREATING_HOMEUNIT = "2";
	public static final String ERROR_RETRIEVING_ELEMENT_VARIANTS = "3";
	public static final String ERROR_RETRIEVING_LATEST_HOMEUNITREVISION = "4";
	public static final String ERROR_CREATING_NEW_HOMEUNITREVISION = "5";
	public static final String ERROR_RETRIEVING_HOMEUNIT_VERSIONS = "6";
	public static final String ERROR_CLONING_IL_ELEMENT = "7";
	public static final String ERROR_RETRIEVING_IL_ELEMENT = "8";
	public static final String ERROR_RETRIEVING_IL_ELEMENTS = "9";
	public static final String ERROR_ADDING_IL_ELEMENTMANIFEST = "10";
	public static final String ERROR_CREATING_PRODUCT = "11";
	public static final String ERROR_RETRIEVING_PRODUCT = "12";
	public static final String ERROR_RETRIEVING_PRODUCTS = "13";
	public static final String ERROR_ADDING_ELEMENT_TO_IL = "14";
	public static final String ERROR_ADDING_ELEMENT_TO_PRODUCT = "15";
	public static final String ERROR_REMOVING_ELEMENT_FROM_IL = "16";
	public static final String ERROR_REMOVING_IL_FROM_PRODUCT = "17";
	public static final String ERROR_REPLACING_ELEMENT_IN_IL = "18";
	public static final String ERROR_REPLACING_IL_IN_PRODUCT = "19";
	public static final String ERROR_UPDATING_IL_ELEMENT_MANIFEST = "20";
	public static final String ERROR_CREATING_ELEMENT = "21";
	public static final String ERROR_RETRIEVING_ELEMENT = "22";
	public static final String ERROR_UPDATING_ELEMENT = "23";
	public static final String ERROR_DELETING_ELEMENT = "24";
	public static final String ERROR_ADDING_VARIANT_ELEMENT = "25";
	public static final String ERROR_DELETING_VARIANT_ELEMENT = "26";
	public static final String ERROR_SEARCHING_ELEMENT = "27";
	public static final String ERROR_UPLOADING_VIEW_FILE = "28";
	public static final String ERROR_RETRIEVING_ELEMENTMANIFEST = "29";
	public static final String ERROR_CREATING_BRAND = "30";
	public static final String ERROR_RETRIEVING_BRAND = "31";
	public static final String ERROR_UPDATING_BRAND = "32";
	public static final String ERROR_DELETING_BRAND = "33";
	public static final String ERROR_CREATING_CATEGORY = "34";
	public static final String ERROR_RETRIEVING_CATEGORY = "35";
	public static final String ERROR_UPDATING_CATEGORY = "36";
	public static final String ERROR_DELETING_CATEGORY = "37";
	public static final String ERROR_CREATING_COSTVERSION = "38";
	public static final String ERROR_RETRIEVING_COSTVERSION = "39";
	public static final String ERROR_UPDATING_COSTVERSION = "40";
	public static final String ERROR_DELETING_COSTVERSION = "41";
	public static final String ERROR_CREATING_FINISH = "42";
	public static final String ERROR_RETRIEVING_FINISH = "43";
	public static final String ERROR_UPDATING_FINISH = "44";
	public static final String ERROR_DELETING_FINISH = "45";
	public static final String ERROR_CREATING_MATERIAL = "46";
	public static final String ERROR_RETRIEVING_MATERIAL = "47";
	public static final String ERROR_UPDATING_MATERIAL = "48";
	public static final String ERROR_DELETING_MATERIAL = "49";
	public static final String ERROR_CREATING_PROJECT = "50";
	public static final String ERROR_RETRIEVING_PROJECT = "51";
	public static final String ERROR_UPDATING_PROJECT = "52";
	public static final String ERROR_DELETING_PROJECT = "53";
	public static final String ERROR_CREATING_SUBTYPE = "54";
	public static final String ERROR_RETRIEVING_SUBTYPE = "55";
	public static final String ERROR_UPDATING_SUBTYPE = "56";
	public static final String ERROR_DELETING_SUBTYPE = "57";
	public static final String ERROR_CREATING_THEME = "58";
	public static final String ERROR_RETRIEVING_THEME = "59";
	public static final String ERROR_UPDATING_THEME = "60";
	public static final String ERROR_DELETING_THEME = "61";
	public static final String ERROR_CREATING_TYPE = "62";
	public static final String ERROR_RETRIEVING_TYPE = "63";
	public static final String ERROR_UPDATING_TYPE = "64";
	public static final String ERROR_DELETING_TYPE = "65";
	public static final String ERROR_CREATING_USER = "66";
	public static final String ERROR_RETRIEVING_USER = "67";
	public static final String ERROR_UPDATING_USER = "68";
	public static final String ERROR_DELETING_USER = "69";
	public static final String ERROR_CREATING_CPR = "70";
	public static final String ERROR_RETRIEVING_CPR = "71";
	public static final String ERROR_UPDATING_CPR = "72";
	public static final String ERROR_DELETING_CPR = "73";

	// ELEMENT CATEGORY
	public static final Long IL_CATEGORY_ID = 60L;
	public static final Long IL_UNIT_ID = 40L;

	// ELEMENT SUBTYPE
	public static final Long BATHROOM_SUBTYPE_ID = 12L;
	public static final Long DADO_SUBTYPE_ID = 511L;
	public static final Long FLOORING_SUBTYPE_ID = 502L;
	public static final Long BFL_SUBTYPE_ID = 531L;
	public static final Long BDD_SUBTYPE_ID = 541L;

	// OPERATION TYPE FOR DROOLS RULE
	public static final String OPERATION_MODIFY = "OPERATION_MODIFY";
	public static final String OPERATION_ADD = "OPERATION_ADD";
	public static final String OPERATION_REMOVE = "OPERATION_REMOVE";

	// ELEMENT VIEW TYPES
	public static final String ELEMENT_VIEW_1 = "ELEMENT_VIEW_1";
	public static final String ELEMENT_VIEW_2 = "ELEMENT_VIEW_2";
	public static final String ELEMENT_VIEW_3 = "ELEMENT_VIEW_3";
	public static final String ELEMENT_VIEW_4 = "ELEMENT_VIEW_4";

	// ELEMENT VIEW FILES DIRECTORY
	public static final String ELEMENT_VIEW_1_DIR = "ELEMENT_VIEW_1_DIR";
	public static final String ELEMENT_VIEW_2_DIR = "ELEMENT_VIEW_2_DIR";
	public static final String ELEMENT_VIEW_3_DIR = "ELEMENT_VIEW_3_DIR";
	public static final String ELEMENT_VIEW_4_DIR = "ELEMENT_VIEW_4_DIR";

	// EXCEPTION CONSTANTS
	public static final String OBJECT_NOT_FOUND_IN_DATASTORE = "1001";
	public static final String NO_VARIANTLIST_FOUND = "1002";
	public static final String NO_REVISION_FOUND_FOR_VERSION = "1003";
	public static final String MISSING_USER_ID = "1004";
	public static final String MISSING_PRODUCT_ID = "1005";
	public static final String MISSING_COSTVERSION_ID = "1006";
	public static final String MISSING_HOMEUNITVERSION_ID = "1007";
	public static final String MISSING_HOMEUNITREVISION_ID = "1008";
	public static final String MISSING_ELEMENT_ID = "1009";
	public static final String MISSING_ELEMENTMANIFEST_ID = "1010";
	public static final String MISSING_HOMEUNIT_ID = "1011";
	public static final String MISSING_CONTEXT = "1012";
	public static final String MISSING_VIEW_TYPE = "1013";
	public static final String MISSING_CHILD_ELEMENTID_IN_ELEMENTMANIFEST = "1014";
	public static final String MISSING_PARENT_ELEMENTID_IN_ELEMENTMANIFEST = "1015";
	public static final String MISSING_PROJECT_ID = "1016";
	public static final String MISSING_BRAND_ID = "1017";
	public static final String MISSING_CATEGORY_ID = "1018";
	public static final String MISSING_FINISH_ID = "1019";
	public static final String MISSING_MATERIAL_ID = "1020";
	public static final String MISSING_SUBTYPE_ID = "1021";
	public static final String MISSING_THEME_ID = "1022";
	public static final String MISSING_TYPE_ID = "1023";
	public static final String MISSING_CPR_ID = "1024";


	// SEARCH WILDCARD CONSTANT
	public static final String SEARCH_WILDCARD_CHAR = "*";

	// DEFAULT HOME UNIT RELATED CONSTANTS
	public static final Integer DEFAULT_HOMEUNIT_VERSION_NUMBER = 1;
	public static final String DEFAULT_HOMEUNIT_VERSIONTAG = "DEFAULT VERSION TAG";
	public static final Integer DEFAULT_HOMEUNIT_REVISION_NUMBER = 1;
	public static final String DEFAULT_HOMEUNIT_REVISIONTAG = "DEFAULT REVISION TAG";
	public static final String AUTO_NEW_HOMEUNIT_REVISIONTAG = "INTERMEDIATE REVISION AUTO CREATED FOR VERSION :";

}

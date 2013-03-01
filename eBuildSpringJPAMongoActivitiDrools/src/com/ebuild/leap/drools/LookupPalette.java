package com.ebuild.leap.drools;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import com.ebuild.leap.util.EbuildleapConstants;
import com.ebuild.leap.util.EbuildleapPropertiesUtil;

public class LookupPalette {

	private HashMap<String, Object> lookupMap;
	private EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;

	/*
	 * @Autowired private EbuildleapPropertiesUtil ebuildLeapPropertiesUtil;
	 */

	public LookupPalette(EbuildleapPropertiesUtil ebuildLeapPropertiesUtil) {
		this.ebuildLeapPropertiesUtil = ebuildLeapPropertiesUtil;
		init();
	}

	public void init() {
		lookupMap = new HashMap<String, Object>();
		File lookupFolder = new File(ebuildLeapPropertiesUtil.getProperty(EbuildleapConstants.LOOKUP_FOLDER));
		for (final File fileEntry : lookupFolder.listFiles()) {
			if (!fileEntry.isDirectory() && fileEntry.getName().contains(".properties")) {
				try {
					Properties lookupProperties = new Properties();
					lookupProperties.load(new FileInputStream(fileEntry));
					lookupMap.put(fileEntry.getName(), lookupProperties);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void dispose() {
		Set<String> keys = lookupMap.keySet();
		for (String key : keys) {
			Properties prop = (Properties) lookupMap.get(key);
			prop.clear();
			prop = null;
		}
		lookupMap.clear();
		lookupMap = null;
	}

	public Set<Object> getValues(String fileName, String key) {
		Set<Object> retSet = new HashSet<Object>();
		Properties lookupFile = (Properties) lookupMap.get(fileName);
		String value = lookupFile.getProperty(key);
		if (value != null) {
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				retSet.add(st.nextToken());
			}
		}
		return retSet;
	}

	public Set<Object> getValues(String fileName, List<String> keys) {
		Set<Object> retSet = new HashSet<Object>();
		for (String key : keys) {
			retSet.addAll(this.getValues(fileName, key));
		}
		return retSet;
	}
}

package com.ebuild.leap.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbuildleapPropertiesUtil {
	protected static Logger log = LoggerFactory.getLogger(EbuildleapPropertiesUtil.class);
	private Properties sysconst = null;

	public EbuildleapPropertiesUtil() {
		sysconst = new Properties();
		try {
			log.debug("LOADING SYSTEM PROPERTIES FILE ------------- START :"+EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE);
			//sysconst.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE));
			sysconst.load(this.getClass().getClassLoader().getResourceAsStream(EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE));
			log.debug("LOADING SYSTEM PROPERTIES FILE ------------- END :"+EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE);
		} catch (FileNotFoundException e) {
			log.error(EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE + ": FILE NOT FOUND");
			e.printStackTrace();
			log.error(e.getClass() + ": " + e.getMessage(), e);
		} catch (IOException e) {
			log.error(EbuildleapConstants.EBUILDLEAP_PROPERTIES_FILE + ": IO EXCEPTION");
			log.error(e.getClass() + ": " + e.getMessage(), e);
		}
	}

	public String getProperty(String key) {
		return sysconst.getProperty(key);
	}
}

package com.ebuild.leap.util;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtilsBean;

/*
 * Extends Apache BeanUtilsBean. This was written to ignore null values in the source bean properties when copying to target bean
 */
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

	@Override
	public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		if (value == null)
			return;
		super.copyProperty(dest, name, value);
	}
}

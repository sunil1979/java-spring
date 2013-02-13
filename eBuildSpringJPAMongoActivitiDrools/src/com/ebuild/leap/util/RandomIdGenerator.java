package com.ebuild.leap.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomIdGenerator implements IdentifierGenerator, Configurable {

	protected static Logger log = LoggerFactory.getLogger(RandomIdGenerator.class);
	private long server_id = 0;
	private long servier_startup_time_in_seconds = new Date().getTime() / 1000;
	private long incremented_variable = 0;

	@Override
	public void configure(Type arg0, Properties arg1, Dialect arg2) throws MappingException {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		return getUniqueID();
	}

	public String getUUID() {
		UUID idOne = UUID.randomUUID();
		log.debug("NEW UID GENERATED :" + idOne.toString());
		return idOne.toString();
	}

	/*
	 * Implementation of mysql SHORT_UUID() function
	 * (server_id & 255) << 56 + (server_startup_time_in_seconds << 24) +
	 * incremented_variable++;
	 */

	public Long getUniqueID() {
		long uuid = server_id & 255;
		uuid = uuid << 56;
		long time_seed = servier_startup_time_in_seconds << 24;
		uuid = uuid + time_seed + incremented_variable++;
		return uuid;
	}
}

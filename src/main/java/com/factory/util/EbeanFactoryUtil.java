package com.factory.util;

import com.avaje.ebean.EbeanServer;
import com.tvc.be.db.PersistenceFactory;

public abstract class EbeanFactoryUtil {
	public static EbeanServer ebean ;
	public static EbeanServer getEbean() {
		if(ebean==null) {
			ebean = PersistenceFactory.load("classpath:dbconfig.properties").getEbeanServer();
		}
		return ebean;
	}
}

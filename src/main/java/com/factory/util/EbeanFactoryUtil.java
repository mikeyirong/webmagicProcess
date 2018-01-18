package com.factory.util;

import com.avaje.ebean.EbeanServer;

import mike.dpwrapp.PersisterFactory;

public abstract class EbeanFactoryUtil {
	public static EbeanServer ebean ;
	public static EbeanServer getEbean() {
		if(ebean==null) {
			ebean = PersisterFactory.load("classpath:dbconfig.properties").getEbean();
		}
		return ebean;
	}
}

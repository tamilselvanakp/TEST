package com.utility;

import java.sql.Connection;

import org.apache.log4j.Logger;

public class SingletonStorage {
	static Logger log = Logger.getLogger(SingletonStorage.class.getName());
	Connection l_databaseConnection;
	Connection l_imgdatabaseConnection;

	public Connection getL_imgdatabaseConnection() {
		return l_imgdatabaseConnection;
	}

	public void setL_imgdatabaseConnection(Connection l_imgdatabaseConnection) {
		this.l_imgdatabaseConnection = l_imgdatabaseConnection;
	}

	public Connection getL_databaseConnection() {
		return l_databaseConnection;
	}

	public void setL_databaseConnection(Connection l_databaseConnection) {
		this.l_databaseConnection = l_databaseConnection;
	}

	// Creating the object
	private static SingletonStorage instance = null;

	private SingletonStorage() {
	}

	public static SingletonStorage getSingletonInstances() {
		if (instance == null) {
			log.warn("***************************************************************************************");
			log.debug("Creating Obj for SingletonStorage class");
			synchronized (SingletonStorage.class) {
				if (instance == null) {
					instance = new SingletonStorage();
				}
			}
		}
		log.debug("Returning Obj for Singleton class ");
		return instance;
	}

}

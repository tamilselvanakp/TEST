package com.utility;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class SingletonStorage {
	static Logger log = Logger.getLogger(SingletonStorage.class.getName());

	Map<String, Map<String, String>> l_blocklistMap = new HashMap<String, Map<String, String>>();

	public Map<String, Map<String, String>> getL_blocklistMap() {
		return l_blocklistMap;
	}

	public void setL_blocklistMap(Map<String, Map<String, String>> l_blocklistMap) {
		this.l_blocklistMap = l_blocklistMap;
	}

	Document configReaderDocument;

	public Document getConfigReaderDocument() {
		return configReaderDocument;
	}

	public void setConfigReaderDocument(Document configReaderDocument) {
		this.configReaderDocument = configReaderDocument;
	}

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

	static PriorityBlockingQueue<String> l_HttpAsyncRequestQueue = new PriorityBlockingQueue<>();

	public PriorityBlockingQueue<String> getL_HttpAsyncRequestQueue() {
		return l_HttpAsyncRequestQueue;
	}

	public void setL_HttpAsyncRequestQueue(PriorityBlockingQueue<String> l_HttpAsyncRequestQueue) {
		SingletonStorage.l_HttpAsyncRequestQueue = l_HttpAsyncRequestQueue;
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

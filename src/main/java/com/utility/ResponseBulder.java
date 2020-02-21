package com.utility;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class ResponseBulder {
	String clientResponse;

	public String getGenCode() {
		return clientResponse;
	}

	public void setGenCode(String genCode) {
		this.clientResponse = genCode;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public ResponseBulder() {

	}

	public ResponseBulder(String imsi, int responseCode, String message) {
		super();
		this.imsi = imsi;
		this.responseCode = responseCode;
		this.message = message;
	}

	public ResponseBulder(String imsi, String clientResponse, String message) {
		super();
		this.imsi = imsi;
		this.clientResponse = clientResponse;
		this.message = message;
	}

	public ResponseBulder(String imsi, String clientResponse, int responseCode, String message) {
		super();
		this.imsi = imsi;
		this.clientResponse = clientResponse;
		this.message = message;
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getReferenceURI() {
		return referenceURI;
	}

	public void setReferenceURI(String referenceURI) {
		this.referenceURI = referenceURI;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;

	private String imsi;
	private int responseCode;
	private String referenceURI;

}

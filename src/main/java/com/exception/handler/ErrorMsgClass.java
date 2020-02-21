package com.exception.handler;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class ErrorMsgClass {
	private String errorMesage;
	private int errorcode;
	private String errorReason;
	private String referenceURI;

	public String getReferenceURI() {
		return referenceURI;
	}

	public void setReferenceURI(String referenceURI) {
		this.referenceURI = referenceURI;
	}

	public ErrorMsgClass() {

	}

	public ErrorMsgClass(int errorcode, String errorMesage, String errorReason) {
		super();

		this.errorcode = errorcode;
		this.errorReason = errorReason;
		this.errorMesage = errorMesage;
	}

	public ErrorMsgClass(int errorcode, String errorMesage, String errorReason, String referenceURI) {
		super();

		this.errorcode = errorcode;
		this.errorReason = errorReason;
		this.errorMesage = errorMesage;
		this.referenceURI = referenceURI;
	}

	public int getErrorcode() {
		return errorcode;
	}

	public ErrorMsgClass(int errorcode, String errorMesage) {
		super();

		this.errorcode = errorcode;

		this.errorMesage = errorMesage;
	}

	public String getErrorMesage() {
		return errorMesage;
	}

	public void setErrorMesage(String errorMesage) {
		this.errorMesage = errorMesage;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

}

package com.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class OverRideWpsResponse {
	private String message;
	private String transcation_Id;
	private int responseCode;

	private String resason;

	public OverRideWpsResponse() {

	}

	public OverRideWpsResponse(String message, String transcation_Id, int responseCode, String imsi, String resason) {

		this.message = message;
		this.transcation_Id = transcation_Id;
		this.responseCode = responseCode;
		this.resason = resason;
		this.imsi = imsi;
	}

	private String imsi;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTranscation_Id() {
		return transcation_Id;
	}

	public void setTranscation_Id(String transcation_Id) {
		this.transcation_Id = transcation_Id;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResason() {
		return resason;
	}

	public void setResason(String resason) {
		this.resason = resason;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
}

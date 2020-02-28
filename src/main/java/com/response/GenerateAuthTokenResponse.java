package com.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class GenerateAuthTokenResponse {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTranscation_Id() {
		return Transcation_Id;
	}

	public void setTranscation_Id(String transcation_Id) {
		Transcation_Id = transcation_Id;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getAUTH_TOKEN() {
		return AUTH_TOKEN;
	}

	public void setAUTH_TOKEN(String aUTH_TOKEN) {
		AUTH_TOKEN = aUTH_TOKEN;
	}

	private String Transcation_Id;
	private int responseCode;
	private String AUTH_TOKEN;

	public GenerateAuthTokenResponse() {

	}

	private String Resason;

	public String getResason() {
		return Resason;
	}

	public void setResason(String resason) {
		Resason = resason;
	}

	public GenerateAuthTokenResponse(String Transcation_Id, int responseCode, String AUTH_TOKEN, String message) {
		this.Transcation_Id = Transcation_Id;
		this.responseCode = responseCode;
		this.message = message;
		this.AUTH_TOKEN = AUTH_TOKEN;

	}

	public GenerateAuthTokenResponse(String Transcation_Id, int responseCode, String message, String Resason,
			String AUTH_TOKEN) {
		this.Transcation_Id = Transcation_Id;
		this.responseCode = responseCode;
		this.message = message;
		this.Resason = Resason;

	}
}

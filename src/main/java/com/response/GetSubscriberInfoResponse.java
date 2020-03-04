package com.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class GetSubscriberInfoResponse {

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

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getRequest_Type() {
		return request_Type;
	}

	public void setRequest_Type(String request_Type) {
		this.request_Type = request_Type;
	}

	public GetSubscriberInfoResponse(String message, String transcation_Id, int responseCode, String responseBody,
			String request_Type) {

		this.message = message;
		this.transcation_Id = transcation_Id;
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.request_Type = request_Type;
	}

	public GetSubscriberInfoResponse() {

	}

	private String message;

	private String transcation_Id;
	private int responseCode;
	private String responseBody;
	private String request_Type;

}

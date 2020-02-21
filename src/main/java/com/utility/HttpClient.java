package com.utility;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HttpClient {

	public static String sendHttpRequest(String url, String request) {

		// client Instances created
		Client l_hhtpclient = ClientBuilder.newClient();

		String l_form = String.format("Connecting to server [%s].....", url);
		System.out.println(l_form);
		WebTarget l_destinationServercon = l_hhtpclient.target(url);
		Invocation.Builder l_Builder = l_destinationServercon.request(MediaType.APPLICATION_XML);
		// Invocation.Builder l_Builder =
		// l_WebTarget.request(MediaType.APPLICATION_JSON);
		System.out.println("Request sent to server");

		Response l_Tmoresponse = l_Builder.post(Entity.entity(request, MediaType.APPLICATION_XML));
		System.out.println("Waiting for the response from server......");
		String result = l_Tmoresponse.readEntity(String.class);
		String frm = String.format("Response received with length [%d] and response is [%s].....", result.length(),
				result);
		System.out.println(frm);

		l_Tmoresponse.close();
		return result;
	}

	public static void main(String arg[]) throws Exception {
		String request = SoapXmlFormater.GetUpdateSubscriberDetailsSOAPapi("12345", "UpdateWPS", "45678954565",
				"3546548787", "", "", "1234564", "", "123456");
		HttpClient.sendHttpRequest("http://192.168.151.52:7154", request);
	}

}

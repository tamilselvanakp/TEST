package com.utility;

import javax.ws.rs.core.Response;

public class ResponseSender {

	public Response Sucessresponsebulder(String imsi, int code, String message) {

		ResponseBulder o_ResponseBulder = new ResponseBulder(imsi, code, message);
		Response l_res = Response.status(200).entity(o_ResponseBulder).build();
		return l_res;
	}

	public Response Sucessresponsebulder(String imsi, String Gencode, int code, String message) {

		ResponseBulder o_ResponseBulder = new ResponseBulder(imsi, Gencode, code, message);
		Response l_res = Response.status(200).entity(o_ResponseBulder).build();
		return l_res;
	}

}

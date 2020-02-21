package com.exception.handler;

import javax.ws.rs.core.Response;

public class CustomException {

	public Response riseexception(String Errormsg, int code, String rootcause) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg, rootcause);
		Response l_res = Response.status(code).entity(o_errmsg).build();
		System.out.println("Response to be sent is ---->" + l_res.getEntity());
		return l_res;
	}

	public Response riseexceptionwithURI(String Errormsg, int code, String rootcause, String refURI) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg, rootcause, refURI);
		Response l_res = Response.status(code).entity(o_errmsg).build();
		System.out.println("Response to be sent is ---->" + l_res.getEntity().toString());
		return l_res;
	}

	public Response riseexceptionwithoutrootcause(String Errormsg, int code) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg);
		Response l_res = Response.status(code).entity(o_errmsg).build();
		System.out.println("Response to be sent is ---->" + l_res.toString());
		return l_res;
	}

}

package com.exception.handler;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class CustomException {
	static Logger log = Logger.getLogger(CustomException.class.getName());

	public Response riseexception(String Errormsg, int code, String rootcause) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg, rootcause);
		Response l_res = Response.status(code).entity(o_errmsg).build();

		log.error("Response to be sent is ---->" + l_res.getEntity() + o_errmsg.toString());
		return l_res;
	}

	public Response riseexceptionwithURI(String Errormsg, int code, String rootcause, String refURI) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg, rootcause, refURI);
		Response l_res = Response.status(code).entity(o_errmsg).build();
		log.error("Response to be sent is ---->" + l_res.getEntity().toString());
		return l_res;
	}

	public Response riseexceptionwithoutrootcause(String Errormsg, int code) {
		ErrorMsgClass o_errmsg = new ErrorMsgClass(code, Errormsg);
		Response l_res = Response.status(code).entity(o_errmsg).build();
		log.error("Response to be sent is ---->" + l_res.toString());
		return l_res;
	}

}

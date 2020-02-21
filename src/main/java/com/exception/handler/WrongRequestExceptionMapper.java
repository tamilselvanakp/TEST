package com.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WrongRequestExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		System.out.println("In Providers");
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(500, "INTERNAL SERVER ERROR", exception.getMessage());
		return Response.status(500).entity(o_ErrorMsgClass).build();
	}

}

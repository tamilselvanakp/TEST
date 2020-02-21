package com.exception.handler;

import javax.ws.rs.WebApplicationException;

public class ExceptionHandle extends WebApplicationException {

	public ExceptionHandle() {
		super();
	}

	public ExceptionHandle(String msg) {

		super(msg);

	}

	public ExceptionHandle(String msg, Exception e) {
		super(msg, e);
	}

}

package com.exception.handler;

public class WrongRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongRequestException(String message) {

		super(message);

		System.out.println("TEST");
	}

}

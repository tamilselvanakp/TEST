package com.dob.base;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.exception.handler.CustomException;

public class GetauthTokenDob {
	CustomException o_customexception = new CustomException();

	public boolean validateauthtiken(String authtoken) {

		if (authtoken.length() >= 20)
			return true;

		else {
			Response e_response = o_customexception.riseexceptionwithURI("Invalid Auth token", 403,
					"Please generate New auth token", "abc.com");

			throw new WebApplicationException(e_response);
		}

	}

}

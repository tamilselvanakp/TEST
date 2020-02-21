package demo1.date14;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.dob.base.GetauthTokenDob;
import com.exception.handler.ErrorMsgClass;
import com.utility.Secured;

@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {
	static Logger log = Logger.getLogger(AuthenticationFilter.class.getName());
	private static final String AUTHENTICATION_SCHEME = "Token :";
	String Uri = "www.abcd.com";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		/*String l_authorizationHeader = null;
		try {
			l_authorizationHeader = requestContext.getHeaderString("X-API-TOKEN").trim();
		} catch (Exception e) {
			TokenNotPresent(requestContext, l_authorizationHeader);
		}
		log.debug("l_authorizationHeader[" + l_authorizationHeader + "]");
		if (checkAuthtokenisvalidformate(l_authorizationHeader) == true) {
			log.debug("Valid formate Auth token");
			String[] auth_token = l_authorizationHeader.split(":");
			String strAuthToken = auth_token[auth_token.length - 1].trim();
			log.debug("Received Auth token length is [" + strAuthToken.length() + "] and token is ["
					+ strAuthToken + "]");
			if (isvalidauthToken(strAuthToken) == true) {
		
			} else {
		
			}
		} else {
			log.debug("Received invalidTokenFormate");
			invalidTokenFormate(requestContext, l_authorizationHeader);
			return;
		}*/
		validationProcess(requestContext);

	}

	public void validationProcess(ContainerRequestContext requestContext) {
		String l_authorizationHeader = null;
		try {
			l_authorizationHeader = requestContext.getHeaderString("X-API-TOKEN").trim();
		} catch (Exception e) {
			TokenNotPresent(requestContext, l_authorizationHeader);
		}
		log.debug("l_authorizationHeader[" + l_authorizationHeader + "]");
		if (checkAuthtokenisvalidformate(l_authorizationHeader) == true) {
			log.debug("Valid formate Auth token [" + Thread.currentThread().getName() + "]");
			String[] auth_token = l_authorizationHeader.split(":");
			String strAuthToken = auth_token[auth_token.length - 1].trim();
			log.debug("Received Auth token length is [" + strAuthToken.length() + "] and token is [" + strAuthToken
					+ "]");
			if (isvalidauthToken(strAuthToken) == true) {

			} else {

			}
		} else {
			log.debug("Received invalidTokenFormate");
			invalidTokenFormate(requestContext, l_authorizationHeader);
			return;
		}
	}

	public boolean checkAuthtokenisvalidformate(String l_authorizationHeader) {
		// Check if the Authorization header is valid
		// It must not be null and must be prefixed with "Token :" plus a
		// whitespace
		// The authentication scheme comparison must be case-insensitive
		return l_authorizationHeader != null
				&& (l_authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.trim().toLowerCase() + " "));

	}

	public void invalidTokenFormate(ContainerRequestContext requestContext, String l_authorizationHeader) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "Invalid Auth token",
				"Auth token is [" + l_authorizationHeader + "] invalid formate so rejected", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
				.header("X-API-TOKEN", AUTHENTICATION_SCHEME + l_authorizationHeader).entity(o_ErrorMsgClass).build());
	}

	public boolean isvalidauthToken(String authToken) {
		GetauthTokenDob o_dod = new GetauthTokenDob();
		return o_dod.validateauthtiken(authToken);

	}

	public void TokenNotPresent(ContainerRequestContext requestContext, String l_authorizationHeader) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "Token Not present in xml Header",
				"Auth token is [" + l_authorizationHeader + "] invalid formate so rejected", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
				.header("X-API-TOKEN", AUTHENTICATION_SCHEME + l_authorizationHeader).entity(o_ErrorMsgClass).build());
	}

}

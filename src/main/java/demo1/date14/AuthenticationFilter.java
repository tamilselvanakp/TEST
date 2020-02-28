package demo1.date14;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.dob.base.GetauthTokenDob;
import com.exception.handler.ErrorMsgClass;
import com.utility.Secured;
import com.utility.SingletonStorage;
import com.utility.Utilities;
import com.utility.XmlParser;

@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {
	static Logger log = Logger.getLogger(AuthenticationFilter.class.getName());
	private static final String AUTHENTICATION_SCHEME = "Token :";
	SingletonStorage o_singleton = SingletonStorage.getSingletonInstances();
	String Uri = "www.abcd.com";

	@Context
	HttpServletRequest l_HttpServletRequest;

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
		Document l_docc = o_singleton.getConfigReaderDocument();

		int l_ipBlockListFlag = 0;
		int l_ipBlockListDuration = 600;
		int l_ipBlockListCount = 10;
		try {
			l_ipBlockListFlag = Integer
					.parseInt((XmlParser.getdocValuebyXpath(l_docc, "//IP_BLOCKLIST/FLAG/text()")).get(0));
			l_ipBlockListDuration = Integer
					.parseInt((XmlParser.getdocValuebyXpath(l_docc, "//IP_BLOCKLIST/DURATION_SEC/text()")).get(0));
			l_ipBlockListCount = Integer
					.parseInt((XmlParser.getdocValuebyXpath(l_docc, "//IP_BLOCKLIST/NO_OF_ATTEMPTS/text()")).get(0));
			if (l_ipBlockListFlag == 1) {
				log.debug("Black List Flag is enabled");

			}

		} catch (Exception e) {
			log.error("Invalid l_ipBlockList config so continud with out Balcklist");
			e.printStackTrace();
		}

		log.error("Received Uri" + requestContext.getHeaders());
		validationProcess(requestContext, l_ipBlockListDuration, l_ipBlockListCount, l_ipBlockListFlag);

	}

	public boolean validateBlockListProcess(@Context HttpServletRequest request, int l_ipconfigBlockListDuration,
			int l_ipBlockListConfigCount) {
		log.error("Remote Ip " + request.getRemoteAddr());
		String remote_Ip = request.getRemoteAddr();
		SimpleDateFormat l_dateFormat = new SimpleDateFormat("dow mon dd hh:mm:ss zzz yyyy");
		Map<String, Map<String, String>> l_blocklistMap = o_singleton.getL_blocklistMap();
		// Map<String, Map<String, String>> l_blocklistMap = new HashMap<String,
		// Map<String, String>>();
		try {
			if (!l_blocklistMap.isEmpty()) {

				if (l_blocklistMap.containsKey(remote_Ip)) {
					log.debug("Contains Ip in Map");

					Map<String, String> innerMap = l_blocklistMap.get(remote_Ip);
					log.debug(innerMap);
					int m_count = Integer.parseInt(innerMap.get("Count"));
					String m_firstdate = innerMap.get("firstdate");
					String m_finaldate = innerMap.get("finaldate");

					Date d_firstdate = l_dateFormat.parse(m_firstdate);
					Date d_finaldate = l_dateFormat.parse(m_finaldate);

					log.debug("Map d_firstdate[" + d_firstdate + "] d_finaldate [" + d_finaldate + "]");
					long diffInmiliSec = Math.abs(d_finaldate.getTime() - d_firstdate.getTime());

					long diffinsec = TimeUnit.SECONDS.convert(diffInmiliSec, TimeUnit.MILLISECONDS);
					if (l_ipconfigBlockListDuration > diffinsec) {

						log.debug("diffinsec " + diffinsec + "is not  grater then configured Time "
								+ l_ipconfigBlockListDuration);

						if (m_count > l_ipBlockListConfigCount) {
							log.error(" m_count is " + m_count + " greater then l_ipBlockListConfigCount "
									+ l_ipBlockListConfigCount);

							return false;
						} else {
							log.debug("Count not expired so....adding count");
							m_count += m_count;
							innerMap.put("Count", Integer.toString(m_count));
							log.debug("Inner map" + innerMap);

							l_blocklistMap.put(remote_Ip, innerMap);
							log.debug("Outer map" + l_blocklistMap);
							o_singleton.setL_blocklistMap(l_blocklistMap);
							return true;
						}

					} else {
						log.debug("diffinsec " + diffinsec + "is  grater then configured Time "
								+ l_ipconfigBlockListDuration);

						innerMap.put("Count", Integer.toString(1));
						String currentdate = Utilities.getCurrentDateTime("dow mon dd hh:mm:ss zzz yyyy");
						innerMap.put("firstdate", currentdate);
						Date d_currentdate = l_dateFormat.parse(currentdate);

						Calendar l_cal = Calendar.getInstance();
						l_cal.setTime(d_currentdate);
						l_cal.add(Calendar.SECOND, l_ipconfigBlockListDuration);

						Date d_Finaldate = l_cal.getTime();
						String l_finaldate = d_Finaldate.toString();
						log.info("Count [" + 1 + "] firstdate[" + currentdate + "] l_finaldate[" + l_finaldate + "] ");
						innerMap.put("finaldate", l_finaldate);
						l_blocklistMap.put(remote_Ip, innerMap);
						o_singleton.setL_blocklistMap(l_blocklistMap);
						return true;

					}

				} else {
					log.info("Add to new entry to Map");
					Map<String, String> innerMap = new HashMap<>();
					innerMap.put("Count", Integer.toString(1));
					String currentdate = Utilities.getCurrentDateTime("dow mon dd hh:mm:ss zzz yyyy");
					innerMap.put("firstdate", currentdate);
					Date d_currentdate = l_dateFormat.parse(currentdate);

					Calendar l_cal = Calendar.getInstance();
					l_cal.setTime(d_currentdate);
					l_cal.add(Calendar.SECOND, l_ipconfigBlockListDuration);

					Date d_Finaldate = l_cal.getTime();
					String l_finaldate = d_Finaldate.toString();
					log.info("Count [" + 1 + "] firstdate[" + currentdate + "] l_finaldate[" + l_finaldate + "] ");
					innerMap.put("finaldate", l_finaldate);
					l_blocklistMap.put(remote_Ip, innerMap);
					o_singleton.setL_blocklistMap(l_blocklistMap);
					return true;
				}

			} else {
				log.info("Add to new entry to Map");
				Map<String, String> innerMap = new HashMap<>();
				innerMap.put("Count", Integer.toString(1));
				String currentdate = Utilities.getCurrentDateTime("dow mon dd hh:mm:ss zzz yyyy");
				innerMap.put("firstdate", currentdate);
				Date d_currentdate = l_dateFormat.parse(currentdate);

				Calendar l_cal = Calendar.getInstance();
				l_cal.setTime(d_currentdate);
				l_cal.add(Calendar.SECOND, l_ipconfigBlockListDuration);

				Date d_Finaldate = l_cal.getTime();
				String l_finaldate = d_Finaldate.toString();
				log.info("Count [" + 1 + "] firstdate[" + currentdate + "] l_finaldate[" + l_finaldate + "] ");
				innerMap.put("finaldate", l_finaldate);
				l_blocklistMap.put(remote_Ip, innerMap);
				o_singleton.setL_blocklistMap(l_blocklistMap);
				return true;
			}
		} catch (Exception e) {
			log.error("Exception while Processing so return true [" + e.getMessage() + "]");
			e.printStackTrace();
			return true;
		}

	}

	public void validationProcess(ContainerRequestContext requestContext, int ipBlockListDuration, int ipBlockListCount,
			int l_ipBlockListFlag) {

		String l_authorizationHeader = null;
		int l_client_id = 0;
		Document l_docc = o_singleton.getConfigReaderDocument();
		int l_AuthenicationConfigFlag = 0;
		ArrayList<String> arrayAuthenicationConfigFlag = null;
		try {
			arrayAuthenicationConfigFlag = XmlParser.getdocValuebyXpath(l_docc, "//AUTHENICATION/FLAG/text()");
		} catch (XPathExpressionException e1) {
			InternalServererror(requestContext, ipBlockListDuration, ipBlockListCount, l_ipBlockListFlag);
			e1.printStackTrace();

		}
		log.debug("strAuthenicationConfigFlag " + arrayAuthenicationConfigFlag);
		try {
			l_AuthenicationConfigFlag = Integer.parseInt(arrayAuthenicationConfigFlag.get(0));
			log.debug("l_AuthenicationConfigFlag " + l_AuthenicationConfigFlag);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Invalid AuthenicationConfigFlag in config, so continue with out Authenication");

		}
		if (l_AuthenicationConfigFlag == 1) {
			log.info("Authenication Enabled");
			int l_autherizationWindowInterval = 60;
			int l_AuthenicationTimerConfigFlag = 0;

			try {
				log.info(" checking Timer...");
				ArrayList<String> arrayAuthenicationTimerConfigFlag = XmlParser.getdocValuebyXpath(l_docc,
						"//AUTHENICATION/TIMER/FLAG/text()");

				l_AuthenicationTimerConfigFlag = Integer.parseInt(arrayAuthenicationTimerConfigFlag.get(0));
				log.debug("l_AuthenicationTimerConfigFlag " + l_AuthenicationTimerConfigFlag);

			} catch (Exception e) {
				e.printStackTrace();
				log.error(
						"Invalid AuthenicationTimerConfigFlag in config, so continue with out Timer conditionAuthenication");

			}
			try {
				l_autherizationWindowInterval = Integer.parseInt(
						(XmlParser.getdocValuebyXpath(l_docc, "//AUTHENICATION/TIMER/INTERVAL/text()")).get(0));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(
						"Invalid AuthenicationTimerConfig  in config, so continue with 60min Timer conditionAuthenication");
			}

			try

			{
				log.debug("continueing Authenication");
				l_authorizationHeader = requestContext.getHeaderString("X-API-TOKEN").trim();

			} catch (Exception e) {
				TokenNotPresent(requestContext, l_authorizationHeader, ipBlockListDuration, ipBlockListCount,
						l_ipBlockListFlag);
			}
			log.debug("l_authorizationHeader[" + l_authorizationHeader + "]");
			if (checkAuthtokenisvalidformate(l_authorizationHeader) == true) {
				log.debug("Valid formate Auth token [" + Thread.currentThread().getName() + "]");
				String[] auth_token = l_authorizationHeader.split(":");
				String strAuthToken = auth_token[auth_token.length - 1].trim();
				log.debug("Received Auth token length is [" + strAuthToken.length() + "] and token is [" + strAuthToken
						+ "]");
				if (isvalidauthToken(strAuthToken) == true) {

					log.info("Valid Token checking client Id");
					try {
						String strClientid = requestContext.getHeaderString("Client-id").trim();
						log.debug("received Clientid [" + strClientid + "]");
						l_client_id = Integer.parseInt(strClientid);
						log.debug("received Clientid [" + l_client_id + "]");

						Connection l_ImgCon = o_singleton.getL_imgdatabaseConnection();

						Map<String, String> l_map = GetauthTokenDob.IMG_TEST_GETAUTH(l_ImgCon, l_client_id,
								strAuthToken);

						String errorcode = l_map.get("error_code");
						if (errorcode.equals("0")) {

							if (l_AuthenicationTimerConfigFlag == 1) {
								log.info("Authenication Timer Enabled so Checking Last Updated Time");
								String l_lastgeneratedTime = l_map.get("Date");
								log.debug("l_lastgeneratedTime [" + l_lastgeneratedTime + "]");
								String l_currentDatestr = Utilities.getCurrentDateTime("yyyy-dd-mm HH:mm:SS");
								log.debug("l_currentDate [" + l_currentDatestr + "]");

								SimpleDateFormat l_dobfor = new SimpleDateFormat("yyyy-dd-mm HH:mm:SS");
								/*
								*/
								Date currentdate = l_dobfor.parse(l_currentDatestr);
								Date tkenGeneratedate = l_dobfor.parse(l_lastgeneratedTime);

								log.debug(" in date lastgeneratedTime [" + tkenGeneratedate + "] currentDate ["
										+ currentdate + "]");
								long diffInmiliSec = Math.abs(tkenGeneratedate.getTime() - currentdate.getTime());
								log.info(" diffInmiliSec " + diffInmiliSec);
								long diff = TimeUnit.MINUTES.convert(diffInmiliSec, TimeUnit.MILLISECONDS);
								log.info(" diff " + diff);
								log.debug("l_autherizationWindowInterval" + l_autherizationWindowInterval);
								if (diff > l_autherizationWindowInterval) {
									log.info("Token Expired....");
									TokenExpired(requestContext, ipBlockListDuration, ipBlockListCount,
											l_ipBlockListFlag);
									return;
								} else {
									log.info("Token Window not expired....");
								}
							} else {
								log.info("Authenication Timer not  Enabled");
							}
							log.info("Authenciation Sucess");
						} else {
							log.error("Authenciation Failed");
							invalidToken(requestContext, ipBlockListDuration, ipBlockListCount, l_ipBlockListFlag);
						}

					} catch (Exception e) {
						e.printStackTrace();
						log.error("Invalid Formate of client Id   " + e.getMessage());
						invalidClientId(requestContext, ipBlockListDuration, ipBlockListCount, l_ipBlockListFlag);

					}

				}
			} else {
				log.debug("Received invalidTokenFormate");
				invalidTokenFormate(requestContext, l_authorizationHeader, ipBlockListDuration, ipBlockListCount,
						l_ipBlockListFlag);
				return;
			}
		} else {
			log.info("No validation");
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

	public void invalidTokenFormate(ContainerRequestContext requestContext, String l_authorizationHeader,
			int ipBlockListDuration, int ipBlockListCount, int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "Invalid Auth token",
				"Auth token is [" + l_authorizationHeader + "] invalid formate so rejected", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
				.header("X-API-TOKEN", AUTHENTICATION_SCHEME + l_authorizationHeader).entity(o_ErrorMsgClass).build());
		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public boolean isvalidauthToken(String authToken) {
		GetauthTokenDob o_dod = new GetauthTokenDob();
		return o_dod.validateauthtoken(authToken);

	}

	public void TokenNotPresent(ContainerRequestContext requestContext, String l_authorizationHeader,
			int ipBlockListDuration, int ipBlockListCount, int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "Token Not present in xml Header",
				"Auth token is [" + l_authorizationHeader + "] invalid formate so rejected", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
				.header("X-API-TOKEN", AUTHENTICATION_SCHEME + l_authorizationHeader).entity(o_ErrorMsgClass).build());

		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public void invalidClientId(ContainerRequestContext requestContext, int ipBlockListDuration, int ipBlockListCount,
			int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "Invalid Client Id", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(o_ErrorMsgClass).build());
		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public void invalidToken(ContainerRequestContext requestContext, int ipBlockListDuration, int ipBlockListCount,
			int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(401, "invalidToken", Uri);
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(o_ErrorMsgClass).build());
		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public void InternalServererror(ContainerRequestContext requestContext, int ipBlockListDuration,
			int ipBlockListCount, int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(500, "Internal Server error", Uri);
		requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR).entity(o_ErrorMsgClass).build());
		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public void TokenExpired(ContainerRequestContext requestContext, int ipBlockListDuration, int ipBlockListCount,
			int l_ipBlockListFlag) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(403, "Token Expired", Uri);
		requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(o_ErrorMsgClass).build());
		if (l_ipBlockListFlag == 1) {
			Boolean flag = validateBlockListProcess(l_HttpServletRequest, ipBlockListDuration, ipBlockListCount);
			if (flag == false) {
				bloclisted(requestContext);
			}
		}
	}

	public void bloclisted(ContainerRequestContext requestContext) {
		ErrorMsgClass o_ErrorMsgClass = new ErrorMsgClass(489, "Block Listed", Uri);
		requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(o_ErrorMsgClass).build());

	}

}

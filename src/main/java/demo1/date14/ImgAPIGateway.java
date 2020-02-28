package demo1.date14;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.NodeList;

import com.dob.base.ImgDbQueryExecutor;
import com.exception.handler.CustomException;
import com.response.OverRideWpsResponse;
import com.utility.HttpClient;
import com.utility.ResponseSender;
import com.utility.Secured;
import com.utility.SingletonStorage;
import com.utility.SoapXmlFormater;
import com.utility.Utilities;
import com.utility.XmlParser;

@Path("ImgAPIGateway")
/*<OVERRIDE_WPS><TRANSCATION_ID>12@$65465</TRANSCATION_ID><IMSI>372290000000282</IMSI><OVERRIDE_WPS_FLAG>1</OVERRIDE_WPS_FLAG><REQUESTED_OVERRIDE_WPS>545645</REQUESTED_OVERRIDE_WPS><INITIATION_DATE>20200229</INITIATION_DATE></OVERRIDE_WPS>*/
public class ImgAPIGateway {
	static Logger log = Logger.getLogger(ImgAPIGateway.class.getName());
	@Context
	private ServletContext context;
	XmlParser l_XmlParser = new XmlParser();
	CustomException o_customexception = new CustomException();
	SingletonStorage o_singleton = SingletonStorage.getSingletonInstances();
	ResponseSender o_ResponseSender = new ResponseSender();
	String TmoRequestXmlBody = "";
	String TmoResponseXmlBody = "";

	@Path("overridewps")
	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)

	public Response imgapipost(String Received_Body) {
		Map<String, String> resultMap = null;
		log.debug("Thread current [" + Thread.currentThread().getName() + "]");
		log.debug("Received Body [" + Received_Body + "] and length" + Received_Body.length());
		String rootElement = XmlParser.getXmlrootElement(Received_Body);
		log.debug("Root element is :" + rootElement);
		Utilities.addAPIinMDCtologger(rootElement);
		if (rootElement.equalsIgnoreCase("OVERRIDE_WPS") || rootElement.equalsIgnoreCase("OVERRIDE_WPS_REQUEST")) {
			org.w3c.dom.Document l_doc = l_XmlParser.Parser_xml(Received_Body);
			// to split into node list
			NodeList l_nodelist = l_XmlParser.get_Doc_to_NodeList(l_doc, "*");
			// Getting the particular Element in the Node list
			String l_imsi = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "IMSI");

			String L_msisdn = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "MSISDN");
			String l_iccid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "ICC_ID");
			String l_overrideflag = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "OVERRIDE_WPS_FLAG");
			String l_requestedWps = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "REQUESTED_OVERRIDE_WPS");
			String l_initiateDate = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "INITIATION_DATE");
			String l_txid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "TRANSCATION_ID");
			Utilities.addTxIdinMDCtologger(l_txid);
			String l_formater = String.format(
					"Received IMSI:[%s]ICCID[%s] MSISDN[%s] OVERRIDE_WPS_FLAG[%s] REQUESTED_OVERRIDE_WPS[%s] INITIATION_DATE[%s]",
					l_imsi, l_iccid, L_msisdn, l_overrideflag, l_requestedWps, l_initiateDate);

			log.debug(l_formater);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = new Date();
			String currentdate = sdf.format(d1);
			try {
				int datelocal = Utilities.compareDate(l_initiateDate, currentdate);
				log.debug("Date response is so:" + datelocal);
				if (datelocal < 0) {
					log.error("INITIATION_DATE should be grater then /equal to  current date");
					/*return o_customexception.riseexceptionwithoutrootcause(
							"INITIATION_DATE should be grater then /equal to  current date", 400);*/
					OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 456, l_imsi,
							"INITIATION_DATE should be grater then /equal to  current date");
					return Response.status(400).entity(o_OverRideWpsResponse).build();

				}
			} catch (ParseException e1) {

				e1.printStackTrace();
				/*	return o_customexception.riseexceptionwithoutrootcause("INITIATION_DATE should be in proper formate",
							400);*/
				OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 400, l_imsi,
						"INITIATION_DATE should be in proper formate");
				return Response.status(400).entity(o_OverRideWpsResponse).build();
			}

			if (!l_imsi.isEmpty() || !L_msisdn.isEmpty() || !l_iccid.isEmpty()) {

				log.debug("continuning with query");
				Connection l_imgdbcon = o_singleton.getL_imgdatabaseConnection();
				// Db query execution
				try {
					resultMap = ImgDbQueryExecutor.querySubscriberdetailsInDb(l_imgdbcon, l_imsi, L_msisdn, l_iccid);
					String db_Msisdn = resultMap.get("MSISDN");

					if (db_Msisdn != null) {

					} else {
						// return
						// o_customexception.riseexceptionwithoutrootcause("MSISDN
						// not Present in DataBase", 432);
						OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 459,
								l_imsi, "MSISDN not Present in DataBase");
						return Response.status(459).entity(o_OverRideWpsResponse).build();
					}

					// checking l_overrideflag is not empty in the request
					if (l_overrideflag.isEmpty()) {
						/*return o_customexception
								.riseexceptionwithoutrootcause("OVERRIDE_WPS_FLAG should be either 0 or 1", 400);*/

						OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 459,
								l_imsi, "OVERRIDE_WPS_FLAG should be either 0 or 1");
						return Response.status(459).entity(o_OverRideWpsResponse).build();
					}
					// checking l_overrideflag is 0/1 in the request
					if (Integer.parseInt(l_overrideflag.trim()) <= 1) {
						int intOvrRideFlag = Integer.parseInt(l_overrideflag.trim());
						if (intOvrRideFlag == 0) {

							log.debug("\n Roll back case");
							log.debug("Getting Override Status in DB");
							// Checking the subscriber is previously overriden
							// subscriber or not
							if ((Integer.parseInt(resultMap.get("OVERRIDE_WPS_FLAG"))) == 1) {
								log.debug("Subscriber Overridden in DB");

								String db_Imsi = resultMap.get("IMSI");

								ImgDbQueryExecutor.updateoverRideWPSInDb(l_imgdbcon, db_Imsi.trim(), intOvrRideFlag,
										l_requestedWps);

								log.debug("Roll back case sucess");
								// return
								// o_ResponseSender.Sucessresponsebulder(db_Imsi,
								// 0, "RollBack-Sucess");
								OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Success", l_txid,
										0, l_imsi, "");
								return Response.status(200).entity(o_OverRideWpsResponse).build();

							} else {
								log.error("Subscriber not belongs to Override");
								/*return o_customexception
										.riseexceptionwithoutrootcause("Subscriber not belongs to Override", 432);*/
								OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid,
										432, l_imsi, "Subscriber not belongs to Override");
								return Response.status(432).entity(o_OverRideWpsResponse).build();
							}

						} else {
							// Over ride case
							log.debug("Override case");
							// Wps is number or not
							if (!Utilities.isnumber(l_requestedWps.trim())) {
								log.error("Wps should be a number");
								// return
								// o_customexception.riseexceptionwithoutrootcause("Wps
								// should be a number", 207);

								OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid,
										431, l_imsi, "Wps should be a number");
								return Response.status(431).entity(o_OverRideWpsResponse).build();
							}

							// if (Utilities.isInRange(0, 100,
							// l_requestedWps.trim().length())) {

							// if Db wps isnot null check old WPS with new one
							// else continue
							String db_Wps = resultMap.get("OVERRIDE_WPS");
							if (db_Wps != null) {
								if (db_Wps.trim().equals(l_requestedWps.trim())) {
									log.error("Subscriber Present with Same WPS DB-WPS[" + db_Wps.trim()
											+ "] Requested [" + l_requestedWps.trim() + "]");
									/*return o_customexception.riseexceptionwithoutrootcause(
											"Subscriber Present with Same WPS DB-WPS", 206);*/
									OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure",
											l_txid, 206, l_imsi, "Subscriber Present with Same WPS DB-WPS");
									return Response.status(206).entity(o_OverRideWpsResponse).build();

								}
							}
							String currentPackage = "";
							try {

								currentPackage = resultMap.get("CURRENT_PACKAGE");
								if (currentPackage != null) {
									log.debug("currentPackage is :" + currentPackage);
								} else {
									currentPackage = "default";
									log.debug("currentPackage is null in dB continued with default package ["
											+ currentPackage + "]");
								}

								log.debug("Framing TMO Xml");
								TmoRequestXmlBody = SoapXmlFormater.GetUpdateSubscriberDetailsSOAPapi("12345",
										"UpdateWPS", resultMap.get("MSISDN"), resultMap.get("ICC_ID"), currentPackage,
										"true", l_requestedWps, "", "");

								log.debug("Framed XML Body is [" + TmoRequestXmlBody + "]");
								TmoResponseXmlBody = HttpClient.sendHttpRequest("http://192.168.151.52:7154",
										TmoRequestXmlBody);

								log.debug("Received  XML Body is [" + TmoResponseXmlBody + "]");

								log.debug("Parsing Tmo Response....--------->>>>");
								org.w3c.dom.Document l_tmoresponsedoc = l_XmlParser.Parser_xml(TmoResponseXmlBody);
								NodeList l_l_tmoresponsenodelist = l_XmlParser.get_Doc_to_NodeList(l_tmoresponsedoc,
										"*");

								String l_tmoresponsestatus = l_XmlParser.get_NodeList_to_elemet(l_l_tmoresponsenodelist,
										"ns0:status");
								Multimap<String, String> l_multimap = XmlParser.getxmlToMultMap(TmoResponseXmlBody, "");
								String db_Imsi = resultMap.get("IMSI");
								log.debug(" tmo response status " + l_tmoresponsestatus);

								//
								if (l_tmoresponsestatus.equals("SUCCESS")) {
									log.debug(" tmo response status sucess so sucess flow ");
									ImgDbQueryExecutor.updateoverRideWPSInDb(l_imgdbcon, db_Imsi.trim(), intOvrRideFlag,
											l_requestedWps);
									log.debug("Returining Sucess to client");

									// return
									// o_ResponseSender.Sucessresponsebulder(db_Imsi,
									// 0, "Sucess");
									OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Success",
											l_txid, 0, l_imsi, "");
									return Response.status(200).entity(o_OverRideWpsResponse).build();

								} //

								else {
									log.debug(" tmo response status Failed so Failure flow ");
									/*Collection<String> tmoresponsecollection = l_multimap.get("resultMsg");
									Object[] l_list = tmoresponsecollection.toArray();*/
									Iterator<String> l_listresultMsg = l_multimap.get("resultMsg").iterator();
									String resultMsg = "";
									if (l_listresultMsg.hasNext()) {
										resultMsg = l_listresultMsg.next();

									} else {
										// if Tmo not sending resultmsg
										resultMsg = "Failure record";
									}
									log.debug("resultMsg :" + resultMsg);
									// return
									// o_ResponseSender.Sucessresponsebulder(db_Imsi,
									// resultMsg, 600, "failure");

									OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure",
											l_txid, 600, l_imsi, resultMsg);
									return Response.status(600).entity(o_OverRideWpsResponse).build();
								}

							} catch (Exception e) {

								e.printStackTrace();
								log.error("Internal Server error");
								// return
								// o_customexception.riseexceptionwithoutrootcause("Internal
								// Server error", 500);

								OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid,
										500, l_imsi, "Internal Server error");
								return Response.status(500).entity(o_OverRideWpsResponse).build();
							}

							/*	} else {
							log.error("Invalid OVERRIDE_WPS legthshould be  0 or 100");
							return o_customexception.riseexceptionwithoutrootcause(
									"Invalid OVERRIDE_WPS legthshould be  0 or 100", 400);
							}*/
						}

					} else {
						log.error("OVERRIDE_WPS_FLAG should be either 0 or 1");
						/*return o_customexception
								.riseexceptionwithoutrootcause("OVERRIDE_WPS_FLAG should be either 0 or 1", 400);*/
						OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 448,
								l_imsi, "OVERRIDE_WPS_FLAG should be either 0 or 1");
						return Response.status(448).entity(o_OverRideWpsResponse).build();

					}

				} catch (SQLException e) {

					e.printStackTrace();
					// return
					// o_customexception.riseexceptionwithoutrootcause("Internal
					// server error", 900);
					OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 500, l_imsi,
							"Internal server error");
					return Response.status(500).entity(o_OverRideWpsResponse).build();

				}

			} else {
				log.error("Either IMSI/ICCID/MSISDN should be present");
				/*Response l_resp = o_customexception
						.riseexceptionwithoutrootcause("Either IMSI/ICCID/MSISDN should be present", 400);
				throw new WebApplicationException(l_resp);*/

				OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", l_txid, 400, l_imsi,
						"Either IMSI/ICCID/MSISDN should be present");
				return Response.status(400).entity(o_OverRideWpsResponse).build();

			}

		} else

		{
			log.debug("Not supported Request received");
			/*Response l_resp = o_customexception.riseexceptionwithoutrootcause("Not supported Request", 400);
			throw new WebApplicationException(l_resp);*/

			OverRideWpsResponse o_OverRideWpsResponse = new OverRideWpsResponse("Failure", "", 400, "",
					"Not supported Request");
			return Response.status(400).entity(o_OverRideWpsResponse).build();
		}

	}
}

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.NodeList;

import com.dob.base.ImgDbQueryExecutor;
import com.exception.handler.CustomException;
import com.utility.HttpClient;
import com.utility.ResponseSender;
import com.utility.Secured;
import com.utility.SingletonStorage;
import com.utility.SoapXmlFormater;
import com.utility.Utilities;
import com.utility.XmlParser;

@Path("ImgAPIGateway")
public class ImgAPIGateway {

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
		System.out.println("Thread current [" + Thread.currentThread().getName() + "]");
		System.out.println("Received Body [" + Received_Body + "] and length" + Received_Body.length());
		String rootElement = XmlParser.getXmlrootElement(Received_Body);
		System.out.println("Root element is :" + rootElement);
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
			String l_formater = String.format(
					"Received IMSI:[%s]ICCID[%s] MSISDN[%s] OVERRIDE_WPS_FLAG[%s] REQUESTED_OVERRIDE_WPS[%s] INITIATION_DATE[%s]",
					l_imsi, l_iccid, L_msisdn, l_overrideflag, l_requestedWps, l_initiateDate);

			System.out.println(l_formater);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = new Date();
			String currentdate = sdf.format(d1);
			try {
				int datelocal = Utilities.compareDate(l_initiateDate, currentdate);
				System.out.println("Date response is so:" + datelocal);
				if (datelocal < 0) {
					System.err.println("INITIATION_DATE should be grater then /equal to  current date");
					return o_customexception.riseexceptionwithoutrootcause(
							"INITIATION_DATE should be grater then /equal to  current date", 400);
				}
			} catch (ParseException e1) {

				e1.printStackTrace();
				return o_customexception.riseexceptionwithoutrootcause("INITIATION_DATE should be in proper formate",
						400);
			}

			if (!l_imsi.isEmpty() || !L_msisdn.isEmpty() || !l_iccid.isEmpty()) {

				System.out.println("continuning with query");
				Connection l_imgdbcon = o_singleton.getL_imgdatabaseConnection();
				// Db query execution
				try {
					resultMap = ImgDbQueryExecutor.querySubscriberdetailsInDb(l_imgdbcon, l_imsi, L_msisdn, l_iccid);
					String db_Msisdn = resultMap.get("MSISDN");

					if (db_Msisdn != null) {

					} else {
						return o_customexception.riseexceptionwithoutrootcause("MSISDN not Present in DataBase", 432);

					}

					// checking l_overrideflag is not empty in the request
					if (l_overrideflag.isEmpty()) {
						return o_customexception
								.riseexceptionwithoutrootcause("OVERRIDE_WPS_FLAG should be either 0 or 1", 400);
					}
					// checking l_overrideflag is 0/1 in the request
					if (Integer.parseInt(l_overrideflag.trim()) <= 1) {
						int intOvrRideFlag = Integer.parseInt(l_overrideflag.trim());
						if (intOvrRideFlag == 0) {

							System.out.println("\n Roll back case");
							System.out.println("Getting Override Status in DB");
							// Checking the subscriber is previously overriden
							// subscriber or not
							if ((Integer.parseInt(resultMap.get("OVERRIDE_WPS_FLAG"))) == 1) {
								System.out.println("Subscriber Overridden in DB");

								String db_Imsi = resultMap.get("IMSI");

								ImgDbQueryExecutor.updateoverRideWPSInDb(l_imgdbcon, db_Imsi.trim(), intOvrRideFlag,
										l_requestedWps);

								System.out.println("Roll back case sucess");
								return o_ResponseSender.Sucessresponsebulder(db_Imsi, 0, "RollBack-Sucess");

							} else {
								System.err.println("Subscriber not belongs to Override");
								return o_customexception
										.riseexceptionwithoutrootcause("Subscriber not belongs to Override", 432);
							}

						} else {
							// Over ride case
							System.out.println("\n Override case");
							// Wps is number or not
							if (!Utilities.isnumber(l_requestedWps.trim())) {
								System.err.println("Wps should be a number");
								return o_customexception.riseexceptionwithoutrootcause("Wps should be a number", 207);
							}

							// if (Utilities.isInRange(0, 100,
							// l_requestedWps.trim().length())) {

							// if Db wps isnot null check old WPS with new one
							// else continue
							String db_Wps = resultMap.get("OVERRIDE_WPS");
							if (db_Wps != null) {
								if (db_Wps.trim().equals(l_requestedWps.trim())) {
									System.err.println("Subscriber Present with Same WPS DB-WPS[" + db_Wps.trim()
											+ "] Requested [" + l_requestedWps.trim() + "]");
									return o_customexception.riseexceptionwithoutrootcause(
											"Subscriber Present with Same WPS DB-WPS", 206);

								}
							}
							String currentPackage = "";
							try {

								currentPackage = resultMap.get("CURRENT_PACKAGE");
								if (currentPackage != null) {
									System.out.println("currentPackage is :" + currentPackage);
								} else {
									currentPackage = "default";
									System.out.println("currentPackage is null in dB continued with default package ["
											+ currentPackage + "]");
								}

								System.out.println("Framing TMO Xml");
								TmoRequestXmlBody = SoapXmlFormater.GetUpdateSubscriberDetailsSOAPapi("12345",
										"UpdateWPS", resultMap.get("MSISDN"), resultMap.get("ICC_ID"), currentPackage,
										"true", l_requestedWps, "", "");

								System.out.println("Framed XML Body is [" + TmoRequestXmlBody + "]");
								TmoResponseXmlBody = HttpClient.sendHttpRequest("http://192.168.151.52:7154",
										TmoRequestXmlBody);

								System.out.println("Received  XML Body is [" + TmoResponseXmlBody + "]");

								System.out.println("Parsing Tmo Response....--------->>>>");
								org.w3c.dom.Document l_tmoresponsedoc = l_XmlParser.Parser_xml(TmoResponseXmlBody);
								NodeList l_l_tmoresponsenodelist = l_XmlParser.get_Doc_to_NodeList(l_tmoresponsedoc,
										"*");

								String l_tmoresponsestatus = l_XmlParser.get_NodeList_to_elemet(l_l_tmoresponsenodelist,
										"ns0:status");
								Multimap<String, String> l_multimap = XmlParser.getxmlToMultMap(TmoResponseXmlBody, "");
								String db_Imsi = resultMap.get("IMSI");
								System.out.println(" tmo response status " + l_tmoresponsestatus);

								//
								if (l_tmoresponsestatus.equals("SUCCESS")) {
									System.out.println(" tmo response status sucess so sucess flow ");
									ImgDbQueryExecutor.updateoverRideWPSInDb(l_imgdbcon, db_Imsi.trim(), intOvrRideFlag,
											l_requestedWps);
									System.out.println("Returining Sucess to client");

									return o_ResponseSender.Sucessresponsebulder(db_Imsi, 0, "Sucess");

								} //

								else {
									System.out.println(" tmo response status Failed so Failure flow ");
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
									System.out.println("resultMsg :" + resultMsg);
									return o_ResponseSender.Sucessresponsebulder(db_Imsi, resultMsg, 600, "failure");
								}

							} catch (Exception e) {

								e.printStackTrace();
								System.err.println("Internal Server error");
								return o_customexception.riseexceptionwithoutrootcause("Internal Server error", 500);
							}

							/*	} else {
							System.err.println("Invalid OVERRIDE_WPS legthshould be  0 or 100");
							return o_customexception.riseexceptionwithoutrootcause(
									"Invalid OVERRIDE_WPS legthshould be  0 or 100", 400);
							}*/
						}

					} else {
						System.err.println("OVERRIDE_WPS_FLAG should be either 0 or 1");
						return o_customexception
								.riseexceptionwithoutrootcause("OVERRIDE_WPS_FLAG should be either 0 or 1", 400);

					}

				} catch (SQLException e) {

					e.printStackTrace();
					return o_customexception.riseexceptionwithoutrootcause("Internal server error", 900);

				}

			} else {
				System.err.println("Either IMSI/ICCID/MSISDN should be present");
				Response l_resp = o_customexception
						.riseexceptionwithoutrootcause("Either IMSI/ICCID/MSISDN should be present", 400);
				throw new WebApplicationException(l_resp);

			}

		} else

		{
			System.out.println("Not supported Request received");
			Response l_resp = o_customexception.riseexceptionwithoutrootcause("Not supported Request", 400);
			throw new WebApplicationException(l_resp);
		}

	}
}

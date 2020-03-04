package demo1.date14;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.exception.handler.CustomException;
import com.response.GetSubscriberInfoResponse;
import com.utility.RandomString;
import com.utility.Secured;
import com.utility.Utilities;
import com.utility.XmlParser;

@Path("Getsubscriberinfo")
public class Getsubscriberinfo {
	static Logger log = Logger.getLogger(Getsubscriberinfo.class.getName());
	Getsubscriberinforepo l_Getsubscriberinforepo = new Getsubscriberinforepo();
	XmlParser l_XmlParser = new XmlParser();
	DirectResponse l_dir = new DirectResponse();
	CustomException o_customexception = new CustomException();
	HashMap<String, String> l_resultMap = null;
	@Context
	private ServletContext context;

	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public Response Getsubscriberinfopost(String Received_Body) {

		log.debug("Received Body [" + Received_Body + "] and length" + Received_Body.length());
		String Tx_id = "00000";
		String rootElement = "";
		try {
			rootElement = XmlParser.getXmlrootElement(Received_Body);
		} catch (Exception e2) {
			log.error("Exception !!!" + e2.getMessage());
			e2.printStackTrace();
			return responseBulder(Tx_id, 400, "Xml should be well formed", e2.getMessage(), "");

		}
		log.debug("Root element is :" + rootElement);

		if (rootElement.equalsIgnoreCase("GET_SUBSCRIBER_INFO")
				|| rootElement.equalsIgnoreCase("GET_SUBSCRIBER_INFO_REQUEST")) {
			Utilities.addAPIinMDCtologger(rootElement);
			// Parsing the Xml into document
			org.w3c.dom.Document l_doc;
			try {
				l_doc = l_XmlParser.Parser_xml(Received_Body);
			} catch (NullPointerException | ParserConfigurationException | SAXException | IOException e3) {
				log.error("Exception !!!" + e3.getMessage());
				e3.printStackTrace();
				return responseBulder(Tx_id, 400, "Xml should be well formed", e3.getMessage(), rootElement);

			}
			// to split into node list
			NodeList l_nodelist;
			try {
				l_nodelist = l_XmlParser.get_Doc_to_NodeList(l_doc, "*");
			} catch (XPathExpressionException e2) {
				log.error("Exception !!!" + e2.getMessage());
				e2.printStackTrace();
				return responseBulder(Tx_id, 400, "Xml should be well formed", e2.getMessage(), rootElement);

			}
			// Getting the particular Element in the Node list
			String l_imsi = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "IMSI");
			String L_msisdn = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "MSISDN");
			String l_iccid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "ICC_ID");
			try {
				Tx_id = XmlParser.getdocValuebyXpath(l_doc, "//HEADER/TRANSCATION_ID/text()").get(0);
			} catch (XPathExpressionException e1) {
				log.error("Transcation Id must be Present" + e1);
				e1.printStackTrace();
			}
			Utilities.addTxIdinMDCtologger(Tx_id);
			log.debug("IMSI [" + l_imsi + "] length " + l_imsi.length() + " MSISDN length " + L_msisdn.length() + "["
					+ L_msisdn + "] ICC_ID [" + l_iccid + "] length " + l_iccid.length() + "Tx_id" + Tx_id);
			if (!l_imsi.isEmpty()) {
				if (l_imsi.length() != 15) {
					log.debug("Imsi Length should be 15 digits");
					/*Response e_response = o_customexception.riseexception("IMSI Length should be 15", 400,
							"GET_LOCATION_INFO_REQUEST-->IMSI length is[" + l_imsi + "]");
					throw new WebApplicationException(e_response);*/

					return responseBulder(Tx_id, 400, "IMSI Length should be 15",
							"GET_LOCATION_INFO_REQUEST-->IMSI length is[" + l_imsi + "]", rootElement);
				}
				if (!Utilities.isnumber(l_imsi)) {
					log.debug("Imsi should be number");
					/*	Response e_response = o_customexception.riseexception("Imsi should be number", 400,
								"GET_LOCATION_INFO_REQUEST-->IMSI is[" + l_imsi + "]");
						throw new WebApplicationException(e_response);*/
					return responseBulder(Tx_id, 400, "Imsi should be number",
							"GET_LOCATION_INFO_REQUEST-->IMSI is[" + l_imsi + "]", rootElement);
				}
			} else if (!L_msisdn.isEmpty()) {

				if (!(L_msisdn.length() >= 10)) {
					log.debug("msisdn Length should grater then 10 digits");
					/*Response e_response = o_customexception.riseexception("msisdn Length should grater then 10 digits",
							400, "GET_LOCATION_INFO_REQUEST-->MSISDN length is[" + L_msisdn.length() + "]");
					throw new WebApplicationException(e_response);*/
					return responseBulder(Tx_id, 400, "msisdn Length should grater then 10 digits",
							"GET_LOCATION_INFO_REQUEST-->MSISDN length is[" + L_msisdn.length() + "]", rootElement);

				}
				if (!Utilities.isnumber(L_msisdn)) {
					log.debug("Imsi should be number");
					/*Response e_response = o_customexception.riseexception("msisdn should be number", 400,
							"GET_LOCATION_INFO_REQUEST-->MSISDN is[" + L_msisdn + "]");
					throw new WebApplicationException(e_response);*/

					return responseBulder(Tx_id, 400, "MSISDN should be number",
							"GET_LOCATION_INFO_REQUEST-->MSISDN is[" + L_msisdn + "]", rootElement);
				}
			} else if (!l_iccid.isEmpty()) {
				/*if (Utilities.isInRange(19, 20, l_iccid.length())) {
					log.debug("isInRange true");
				} else {
					log.debug("isInRange False");
				}*/

				if (!Utilities.isInRange(19, 20, l_iccid
						.length()))/*Utilities.isInRange will give true if the length is matched in limit so we need only false if ithe function ret  true then no issuse if it is rtn false then we neet to consider as true*/
				{

					log.debug("iccid Length should be 19 or 20");
					/*Response e_response = o_customexception.riseexception("iccid Length should be 19 or 20", 400,
							"GET_LOCATION_INFO_REQUEST-->ICC_ID length is[" + l_iccid.length() + "]");
					throw new WebApplicationException(e_response);*/

					return responseBulder(Tx_id, 400, "iccid Length should be 19 or 20",
							"GET_LOCATION_INFO_REQUEST-->ICC_ID length is[" + l_iccid.length() + "]", rootElement);
				}

			} else {
				log.debug("Either IMSI/MSISDN/ICCID should be Present");
				/*Response e_response = o_customexception.riseexception("Either IMSI/MSISDN/ICCID should be Present", 400,
						"GET_LOCATION_INFO_REQUEST-->Either IMSI/MSISDN/ICCID  is not present");
				throw new WebApplicationException(e_response);*/

				return responseBulder(Tx_id, 400, "Either IMSI/MSISDN/ICCID should be Present",
						"GET_LOCATION_INFO_REQUEST-->Either IMSI/MSISDN/ICCID  is not present", rootElement);

			}

			String l_type = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "TYPE");
			String l_reqdomine = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "REQ_DOMAIN");
			log.debug("IMSI is [" + l_imsi + "] MSISDN [" + L_msisdn + "] ICC_ID [" + l_iccid + "] TYPE [" + l_type
					+ "] REQ_DOMAIN [" + l_reqdomine + "]");

			if (l_doc != null) {
				log.debug("Getsubscriberinfo called");

				try {

					l_resultMap = l_dir.getItem(Received_Body, context);

					log.debug("Received map" + l_resultMap);

					/*String code = l_resultMap.get("error_code");
					String describ = l_resultMap.get("error_desc");
					String body = l_resultMap.get("Body");
					
					GetSubscriberInfoResponse o_GetSubscriberInfoResponse = new GetSubscriberInfoResponse(describ,
							Tx_id, Integer.parseInt(code), body);
					return Response.status(Integer.parseInt(code)).entity(o_GetSubscriberInfoResponse).build();*/
					return responseBulder(l_resultMap, Tx_id);

				} catch (Exception e) {

					e.printStackTrace();
					log.error("Exception ....." + e.getMessage());
					/*Response e_response = o_customexception.riseexception("internal server Error", 500,
							"Recevived body null");
					throw new WebApplicationException(e_response);*/

					return responseBulder(Tx_id, 400, "internal server Error",
							"Recevived body null...." + e.getMessage(), rootElement);
				}
			} else {

				/*	Response e_response = o_customexception.riseexception("Body should not be null", 400,
							"Recevived body null");
				
					throw new WebApplicationException(e_response);*/

				return responseBulder(Tx_id, 400, "Body should not be nullr", "Recevived body null....", rootElement);

			}
		} else {
			/*Response e_response = o_customexception.riseexceptionwithURI("API is not supported in This URI", 400,
					"Please Try different URI", "https.ptpl.com");
			
			throw new WebApplicationException(e_response);*/

			return responseBulder(Tx_id, 400, "Unsupported API", "API is not supported in This URI", rootElement);
		}

	}

	@Secured
	@GET
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	// @Path("/{imsi}")
	// @PathParam(value = "imsi") String Sub_identity
	public Response Getsubscriberinfoget(@QueryParam(value = "IMSI") long l_imsi,
			@QueryParam(value = "MSISDN") long l_msisdn, @QueryParam(value = "ICC_ID") long l_iccid,
			@QueryParam(value = "TYPE") String l_type, @QueryParam(value = "REQ_DOMAIN") int l_reqdomine,
			@QueryParam(value = "TRANSACTION_ID") String Tx_id)

	{
		Response e_response = null;

		String xml_ip = null;
		log.debug("GET Received IMSI [" + l_imsi + "]");
		log.debug("GET Received MSISDN [" + l_msisdn + "]");
		log.debug("GET Received ICCID [" + l_iccid + "]");
		log.debug("GET Received TYPE [" + l_type + "]");
		log.debug("GET Received DOMINE [" + l_reqdomine + "]");
		log.debug("GET Received TRANSACTION_ID [" + Tx_id + "]");
		if (Tx_id != null) {
		} else {
			RandomString l_randomstring = new RandomString(10);

			Tx_id = l_randomstring.nextString();
		}
		Utilities.addTxIdinMDCtologger(Tx_id);
		Utilities.addAPIinMDCtologger("GET:GET_SUBSCRIBER_INFO");

		/*log.debug(((l_imsi != null || l_msisdn != null || l_iccid != null)
				&& (l_imsi != "" || l_msisdn != "" || l_iccid != "")));
		if ((l_imsi != null || l_msisdn != null || l_iccid != null)
				&& (l_imsi != "" || l_msisdn != "" || l_iccid != "")) {
		
			if ((l_imsi != null) || (l_imsi != "")) {
				if (l_imsi.length() == 15) {
		
				} else {
					Responsestr = "<Response><ERROR>IMSI Length should be 15</ERROR></Response>";
				}
			}*/
		try {

			l_type = l_type.trim();
		} catch (NullPointerException e) {
			e_response = o_customexception.riseexception("Query param should not be null", 400,
					"Null value in Query Param");
			e.printStackTrace();
			throw new WebApplicationException(e_response);
		}

		if (Utilities.isInRange(1, 5, l_reqdomine)) {
			e_response = o_customexception.riseexception("REQ_DOMAIN should be 1-5", 400,
					"REQ_DOMAIN is :" + l_reqdomine);

			throw new WebApplicationException(e_response);
		}
		if (l_imsi != 0) {
			int l_imsilen = String.valueOf(l_imsi).length();
			if (l_imsilen == 15) {
				xml_ip = "<GET_SUBSCRIBER_INFO_REQUEST><IMSI>" + l_imsi + "</IMSI><TYPE>" + l_type
						+ "</TYPE><REQ_DOMAIN>" + l_reqdomine + "</REQ_DOMAIN></GET_SUBSCRIBER_INFO_REQUEST>";

				try {
					l_resultMap = l_dir.getItem(xml_ip, context);

					return responseBulder(l_resultMap, Tx_id);
				} catch (Exception e) {

					e.printStackTrace();
					log.error("Exception ....." + e.getMessage());
					e_response = o_customexception.riseexception("internal server Error", 500, "Recevived body null");
					throw new WebApplicationException(e_response);
				}
			} else {
				// Responsestr = "<RESPONSE><ERROR>IMSI Length Should be 15
				// digits</ERROR></RESPONSE>";
				/*ErrorMsgClass o_errmsg = new ErrorMsgClass("IMSI Length should be 15", 400,
						"GET_LOCATION_INFO_REQUEST-->IMSI ");
				Response l_res = Response.status(Status.BAD_REQUEST).entity(o_errmsg).build();*/
				e_response = o_customexception.riseexception("IMSI Length should be 15", 400,
						"GET_SUBSCRIBER_INFO_REQUEST-->IMSI length is[" + l_imsilen + "]");

				throw new WebApplicationException(e_response);
			}

		} else if (l_iccid != 0) {
			xml_ip = "<GET_SUBSCRIBER_INFO_REQUEST><ICC_ID>" + l_iccid + "</ICC_ID><TYPE>" + l_type
					+ "</TYPE><REQ_DOMAIN>" + l_reqdomine + "</REQ_DOMAIN></GET_SUBSCRIBER_INFO_REQUEST>";
			try {
				// return l_dir.getItem(xml_ip, context);
				l_resultMap = l_dir.getItem(xml_ip, context);

				return responseBulder(l_resultMap, Tx_id);
			} catch (Exception e) {

				e.printStackTrace();
				log.error("Exception ....." + e.getMessage());
				e_response = o_customexception.riseexception("internal server Error", 500, "Recevived body null");
				throw new WebApplicationException(e_response);
			}
		} else if (l_msisdn != 0) {
			xml_ip = "<GET_SUBSCRIBER_INFO_REQUEST><MSISDN>" + l_msisdn + "</MSISDN><TYPE>" + l_type
					+ "</TYPE><REQ_DOMAIN>" + l_reqdomine + "</REQ_DOMAIN></GET_SUBSCRIBER_INFO_REQUEST>";

			try {
				// return l_dir.getItem(xml_ip, context);
				l_resultMap = l_dir.getItem(xml_ip, context);

				return responseBulder(l_resultMap, Tx_id);
			} catch (Exception e) {

				e.printStackTrace();
				log.error("Exception ....." + e.getMessage());
				e_response = o_customexception.riseexception("internal server Error", 500, "Recevived body null");
				throw new WebApplicationException(e_response);
			}
		} else {
			e_response = o_customexception.riseexception("Either IMSI/ICCID/MSISDN Should be present", 400,
					"GET_SUBSCRIBER_INFO_REQUEST-->IMSI&&ICCID&&MSISDN is null at least one should present");

			throw new WebApplicationException(e_response);
		}
	}

	private static Response responseBulder(HashMap<String, String> l_resultMap, String Tx_id) {
		String code = l_resultMap.get("error_code");
		String describ = l_resultMap.get("error_desc");
		String body = l_resultMap.get("Body");
		String logger = String.format("error_code [%s]error_desc[%s] Body[%s]", code, describ, body);
		log.info(logger);
		GetSubscriberInfoResponse o_GetSubscriberInfoResponse = new GetSubscriberInfoResponse(describ, Tx_id,
				Integer.parseInt(code), body, "GET_SUBSCRIBER_INFO");
		Response l_resp = Response.status(200).entity(o_GetSubscriberInfoResponse).build();
		log.debug("l_resp " + l_resp);
		return l_resp;

	}

	private static Response responseBulder(String Tx_id, int code, String error_describ, String body,
			String request_type) {
		if (request_type.isEmpty()) {
			request_type = "GET_SUBSCRIBER_INFO";
		}

		String logger = String.format("error_code [%s]error_desc[%s] Body[%s]", code, error_describ, body);
		log.info(logger);
		GetSubscriberInfoResponse o_GetSubscriberInfoResponse = new GetSubscriberInfoResponse(error_describ, Tx_id,
				code, body, request_type);
		Response l_resp = Response.status(code).entity(o_GetSubscriberInfoResponse).build();
		log.debug("l_resp " + l_resp);
		return l_resp;

	}

}

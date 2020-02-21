package demo1.date14;

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

import org.w3c.dom.NodeList;

import com.exception.handler.CustomException;
import com.utility.Secured;
import com.utility.Utilities;
import com.utility.XmlParser;

@Path("Getsubscriberinfo")
public class Getsubscriberinfo {

	Getsubscriberinforepo l_Getsubscriberinforepo = new Getsubscriberinforepo();
	XmlParser l_XmlParser = new XmlParser();
	DirectResponse l_dir = new DirectResponse();
	CustomException o_customexception = new CustomException();
	@Context
	private ServletContext context;

	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public Response Getsubscriberinfopost(String Received_Body) {

		System.out.println("Received Body [" + Received_Body + "] and length" + Received_Body.length());

		String rootElement = XmlParser.getXmlrootElement(Received_Body);
		System.out.println("Root element is :" + rootElement);
		if (rootElement.equalsIgnoreCase("GET_SUBSCRIBER_INFO")
				|| rootElement.equalsIgnoreCase("GET_SUBSCRIBER_INFO_REQUEST")) {
			// Parsing the Xml into document
			org.w3c.dom.Document l_doc = l_XmlParser.Parser_xml(Received_Body);
			// to split into node list
			NodeList l_nodelist = l_XmlParser.get_Doc_to_NodeList(l_doc, "*");
			// Getting the particular Element in the Node list
			String l_imsi = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "IMSI");
			String L_msisdn = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "MSISDN");
			String l_iccid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "ICC_ID");

			System.out.println("IMSI [" + l_imsi + "] length " + l_imsi.length() + " MSISDN length " + L_msisdn.length()
					+ "[" + L_msisdn + "] ICC_ID [" + l_iccid + "] length " + l_iccid.length());
			if (!l_imsi.isEmpty()) {
				if (l_imsi.length() != 15) {
					System.out.println("Imsi Length should be 15 digits");
					Response e_response = o_customexception.riseexception("IMSI Length should be 15", 400,
							"GET_LOCATION_INFO_REQUEST-->IMSI length is[" + l_imsi + "]");
					throw new WebApplicationException(e_response);
				}
				if (!Utilities.isnumber(l_imsi)) {
					System.out.println("Imsi should be number");
					Response e_response = o_customexception.riseexception("Imsi should be number", 400,
							"GET_LOCATION_INFO_REQUEST-->IMSI is[" + l_imsi + "]");
					throw new WebApplicationException(e_response);
				}
			} else if (!L_msisdn.isEmpty()) {

				if (!(L_msisdn.length() >= 10)) {
					System.out.println("msisdn Length should grater then 10 digits");
					Response e_response = o_customexception.riseexception("msisdn Length should grater then 10 digits",
							400, "GET_LOCATION_INFO_REQUEST-->MSISDN length is[" + L_msisdn.length() + "]");
					throw new WebApplicationException(e_response);
				}
				if (!Utilities.isnumber(L_msisdn)) {
					System.out.println("Imsi should be number");
					Response e_response = o_customexception.riseexception("msisdn should be number", 400,
							"GET_LOCATION_INFO_REQUEST-->MSISDN is[" + L_msisdn + "]");
					throw new WebApplicationException(e_response);
				}
			} else if (!l_iccid.isEmpty()) {
				/*if (Utilities.isInRange(19, 20, l_iccid.length())) {
					System.out.println("isInRange true");
				} else {
					System.out.println("isInRange False");
				}*/

				if (!Utilities.isInRange(19, 20, l_iccid
						.length()))/*Utilities.isInRange will give true if the length is matched in limit so we need only false if ithe function ret  true then no issuse if it is rtn false then we neet to consider as true*/
				{

					System.out.println("iccid Length should be 19 or 20");
					Response e_response = o_customexception.riseexception("iccid Length should be 19 or 20", 400,
							"GET_LOCATION_INFO_REQUEST-->ICC_ID length is[" + l_iccid.length() + "]");
					throw new WebApplicationException(e_response);
				}

			} else {
				System.out.println("Either IMSI/MSISDN/ICCID should be Present");
				Response e_response = o_customexception.riseexception("Either IMSI/MSISDN/ICCID should be Present", 400,
						"GET_LOCATION_INFO_REQUEST-->Either IMSI/MSISDN/ICCID  is not present");
				throw new WebApplicationException(e_response);
			}

			String l_type = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "TYPE");
			String l_reqdomine = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "REQ_DOMAIN");
			System.out.println("IMSI is [" + l_imsi + "] MSISDN [" + L_msisdn + "] ICC_ID [" + l_iccid + "] TYPE ["
					+ l_type + "] REQ_DOMAIN [" + l_reqdomine + "]");

			if (l_doc != null) {
				System.out.println("Getsubscriberinfo called");

				return l_dir.getItem(Received_Body, context);
			} else {

				Response e_response = o_customexception.riseexception("Body should not be null", 400,
						"Recevived body null");

				throw new WebApplicationException(e_response);
			}
		} else {
			Response e_response = o_customexception.riseexceptionwithURI("API is not supported in This URI", 400,
					"Please Try different URI", "https.ptpl.com");

			throw new WebApplicationException(e_response);
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
			@QueryParam(value = "TYPE") String l_type, @QueryParam(value = "REQ_DOMAIN") int l_reqdomine) {
		Response e_response = null;

		String xml_ip = null;
		System.out.println("GET Received IMSI [" + l_imsi + "]");
		System.out.println("GET Received MSISDN [" + l_msisdn + "]");
		System.out.println("GET Received ICCID [" + l_iccid + "]");
		System.out.println("GET Received TYPE [" + l_type + "]");
		System.out.println("GET Received DOMINE [" + l_reqdomine + "]");
		/*System.out.println(((l_imsi != null || l_msisdn != null || l_iccid != null)
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
				return l_dir.getItem(xml_ip, context);
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
			return l_dir.getItem(xml_ip, context);
		} else if (l_msisdn != 0) {
			xml_ip = "<GET_SUBSCRIBER_INFO_REQUEST><MSISDN>" + l_msisdn + "</MSISDN><TYPE>" + l_type
					+ "</TYPE><REQ_DOMAIN>" + l_reqdomine + "</REQ_DOMAIN></GET_SUBSCRIBER_INFO_REQUEST>";

			return l_dir.getItem(xml_ip, context);
		} else {
			e_response = o_customexception.riseexception("Either IMSI/ICCID/MSISDN Should be present", 400,
					"GET_SUBSCRIBER_INFO_REQUEST-->IMSI&&ICCID&&MSISDN is null at least one should present");

			throw new WebApplicationException(e_response);
		}
	}

}

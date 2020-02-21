package demo1.date14;

import java.net.SocketTimeoutException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.exception.handler.CustomException;
import com.utility.Secured;
import com.utility.TcpConnecter;
import com.utility.Utilities;
import com.utility.UtilsVariable;
import com.utility.XmlParser;

@Path("ClientRequestGateway")
public class ClientRequestGateway {
	CustomException o_customexception = new CustomException();
	XmlParser l_XmlParser = new XmlParser();
	@Context
	private ServletContext context;

	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public Response TCPPostRequestGateway(String xmlIP) {
		System.out.println("ClientRequestGateway Received Request [" + xmlIP + "]");
		/*System.out.println("XmlIP.equals(null) , XmlIP.isEmpty() " + (XmlIP.equals(null)) + (XmlIP.isEmpty())
				+ "  -----[" + ((XmlIP.equals(null)) && (XmlIP.isEmpty())));*/
		if (!((xmlIP.equals(null)) || (xmlIP.isEmpty()))) {
			Multimap<String, String> l_HashMap = XmlParser.getxmlToMultMap(xmlIP, "");
			Document l_doc = l_XmlParser.Parser_xml(xmlIP);
			Element l_rootelement = l_doc.getDocumentElement();
			if (l_rootelement.getLocalName().equalsIgnoreCase("ADD_PRODUCT_REQUEST")
					|| l_rootelement.getLocalName().equalsIgnoreCase("ADD_PRODUCT")) {
				System.out.println("Request is ADD Product");
			}
			System.out.println("IMSI from map" + l_HashMap.get("IMSI"));
			System.out.println("processing Request from Client ");
			// TcpConnecter.getTcpClient("192.168.151.134", 8616, "HLR",
			// "TAMIL", null);
			UtilsVariable o_UtilsVariable = new UtilsVariable();
			o_UtilsVariable.setL_date(Utilities.getCurrentDateTime());
			try {
				String responsetoClient = TcpConnecter.sendTcpClientRequest("192.168.151.134", 6183, "HLR", "TAMIL",
						"<GET_SUBSCRIBER_INFO_REQUEST><HEADER><TRANSACTION_ID>2bFymfMA040220120913</TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER><BODY><PRIMARY_IMSI>234488420020121</PRIMARY_IMSI><TYPE>D</TYPE><REQ_DOMAIN>1</REQ_DOMAIN></BODY></GET_SUBSCRIBER_INFO_REQUEST>",
						true);

				Document l_xmldoc = l_XmlParser.Parser_xml(responsetoClient);

				if (responsetoClient.length() != 0) {
					return Response.ok(responsetoClient).language("en").build();
				} else {
					Response e_response = o_customexception.riseexception("Exception while processing", 500, "");
					throw new WebApplicationException(e_response);
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				Response e_response = o_customexception.riseexception("Internal server error", 900, e.getMessage());
				throw new WebApplicationException(e_response);

			} catch (Exception e) {
				e.printStackTrace();
				Response e_response = o_customexception.riseexception("Exception while processing", 500,
						e.getMessage());
				throw new WebApplicationException(e_response);

			}

		} else {
			Response e_response = o_customexception.riseexception("Bad request", 400, "null body Received");
			throw new WebApplicationException(e_response);
		}

	}

	@Secured
	@GET
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public Response TCPGetRequestGateway() {
		Response e_response = o_customexception.riseexception("Get Method will not be suppored..Under Process ", 405,
				"Please try with POST");
		throw new WebApplicationException(e_response);
	}
}
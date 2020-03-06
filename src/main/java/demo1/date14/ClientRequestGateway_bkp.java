package demo1.date14;

import javax.ws.rs.Path;

/*
import javax.ws.rs.Path;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

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
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.exception.handler.CustomException;
import com.utility.Secured;
import com.utility.TcpConnecter;
import com.utility.Utilities;
import com.utility.UtilsVariable;
import com.utility.XmlParser;*/

@Path("ClientRequestGateway")
public class ClientRequestGateway_bkp {
	/*
	CustomException o_customexception = new CustomException();
	static Logger log = Logger.getLogger(ClientRequestGateway_bkp.class.getName());
	public ExecutorService es = Executors.newFixedThreadPool(10);
	PriorityBlockingQueue<String> l_queue = new PriorityBlockingQueue<>();
	
	XmlParser l_XmlParser = new XmlParser();
	@Context
	private ServletContext context;
	
	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public Response TCPPostRequestGateway(String xmlIP) {
	
	log.debug("ClientRequestGateway Received Request [" + xmlIP + "]");
	log.debug("XmlIP.equals(null) , XmlIP.isEmpty() " + (XmlIP.equals(null)) + (XmlIP.isEmpty())
	+ "  -----[" + ((XmlIP.equals(null)) && (XmlIP.isEmpty())));
	if (!((xmlIP.equals(null)) || (xmlIP.isEmpty()))) {
	Multimap<String, String> l_HashMap = null;
	Document l_doc = null;
	try {
	l_HashMap = XmlParser.getxmlToMultMap(xmlIP, "");
	l_doc = l_XmlParser.Parser_xml(xmlIP);
	} catch (SAXException | IOException | ParserConfigurationException e1) {
	e1.printStackTrace();
	Response e_response = o_customexception.riseexception("XML should be well formaed", 400,
	e1.getMessage());
	throw new WebApplicationException(e_response);
	
	}
	
	Element l_rootelement = l_doc.getDocumentElement();
	// adding Api in Logger
	Utilities.addAPIinMDCtologger(l_rootelement.getLocalName());
	if (l_rootelement.getLocalName().equalsIgnoreCase("ADD_PRODUCT_REQUEST")
	|| l_rootelement.getLocalName().equalsIgnoreCase("ADD_PRODUCT")) {
	log.debug("Request is ADD Product");
	}
	log.debug("IMSI from map" + l_HashMap.get("IMSI"));
	log.debug("processing Request from Client ");
	// TcpConnecter.getTcpClient("192.168.151.134", 8616, "HLR",
	// "TAMIL", null);
	UtilsVariable o_UtilsVariable = new UtilsVariable();
	o_UtilsVariable.setL_date(Utilities.getCurrentDateTime());
	try {
	log.debug("calling TcpConnecter");
	String responsetoClient = TcpConnecter.sendTcpClientRequest("192.168.151.134", 6183, "HLR", "TAMIL",
	"<GET_SUBSCRIBER_INFO_REQUEST><HEADER><TRANSACTION_ID>2bFymfMA040220120913</TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER><BODY><PRIMARY_IMSI>234488420020121</PRIMARY_IMSI><TYPE>D</TYPE><REQ_DOMAIN>1</REQ_DOMAIN></BODY></GET_SUBSCRIBER_INFO_REQUEST>",
	true);
	
	l_XmlParser.Parser_xml(responsetoClient);
	
	if (responsetoClient.length() != 0) {
	return Response.ok(responsetoClient).language("en").build();
	} else {
	Response e_response = o_customexception.riseexception("Exception while processing", 500, "");
	throw new WebApplicationException(e_response);
	}
	} catch (SocketTimeoutException e) {
	log.error(e.getMessage());
	Response e_response = o_customexception.riseexception("Internal server error", 900, e.getMessage());
	throw new WebApplicationException(e_response);
	
	} catch (Exception e) {
	log.error(e.getMessage());
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
	log.warn("Get Method will not be suppored..Under Process");
	Response e_response = o_customexception.riseexception("Get Method will not be suppored..Under Process ", 405,
	"Please try with POST");
	throw new WebApplicationException(e_response);
	}
	*/}

package demo1.date14;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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
import com.utility.XmlParser;

@Path("Asyncgetsubscriber")

public class Asyncgetsubscriber {
	static Logger log = Logger.getLogger(Asyncgetsubscriber.class.getName());
	CustomException o_customexception = new CustomException();
	XmlParser l_XmlParser = new XmlParser();
	public ExecutorService es = Executors.newFixedThreadPool(10);
	PriorityBlockingQueue<String> l_queue = new PriorityBlockingQueue<>();

	@Secured
	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)
	public void getAllOrders(@Suspended final AsyncResponse ar, final String receiverdBody) {

		log.info("AsyncResponse#suspended=" + ar.isSuspended());

		l_queue.offer(receiverdBody);
		log.info("l_queue " + l_queue.size());

		es.execute(new Runnable() {
			public void run() {
				while (l_queue.size() > 0) {
					log.debug(" queue [" + l_queue + "]");
					String xmlIP = l_queue.poll();
					log.info("l_queue " + l_queue.size());

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
							// throw new WebApplicationException(e_response);
							log.error("XML should be well formaed");
							ar.resume(e_response);

						}

						Element l_rootelement = l_doc.getDocumentElement();

						ArrayList<String> ApiNameList = null;
						try {
							ApiNameList = XmlParser.getdocValuebyXpath(l_doc, "//REQUEST_TYPE/text()");
						} catch (Exception e1) {
							log.error("Exception while finding REQUEST_TYPE :" + e1.getMessage());
							e1.printStackTrace();
						}
						// adding Api in Logger
						if (ApiNameList.size() > 0) {

							String ApiName = ApiNameList.get(0);
							log.info("Founded request Type" + ApiName);
							Utilities.addAPIinMDCtologger(ApiName);

						} else {
							log.info("request Parent is Type :" + l_rootelement.getLocalName());
							Utilities.addAPIinMDCtologger(l_rootelement.getLocalName());
						}

						log.debug("IMSI from map :" + l_HashMap.get("IMSI"));
						log.debug("processing Request from Client ");
						// TcpConnecter.getTcpClient("192.168.151.134", 8616,
						// "HLR",
						// "TAMIL", null);
						UtilsVariable o_UtilsVariable = new UtilsVariable();
						o_UtilsVariable.setL_date(Utilities.getCurrentDateTime());
						try {
							log.debug("calling TcpConnecter");
							/*				String responsetoClient = TcpConnecter.sendTcpClientRequest("192.168.151.134", 6183, "HLR",
													"TAMIL",
													"<GET_SUBSCRIBER_INFO_REQUEST><HEADER><TRANSACTION_ID>2bFymfMA040220120913</TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER><BODY><PRIMARY_IMSI>234488420020121</PRIMARY_IMSI><TYPE>D</TYPE><REQ_DOMAIN>1</REQ_DOMAIN></BODY></GET_SUBSCRIBER_INFO_REQUEST>",
													true);*/

							// <REQUEST><HEADER><TRANSACTION_ID>MUJX9jiA060320114752</TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER><BODY><PRIMARY_IMSI>234488420020121</PRIMARY_IMSI><PRIMARY_MSISDN></PRIMARY_MSISDN><TYPE>D</TYPE><REQ_DOMAIN>1</REQ_DOMAIN></BODY></REQUEST>
							String responsetoClient = TcpConnecter.sendTcpClientRequest("192.168.151.134", 8616, "HLR",
									"TAMIL", xmlIP, true);

							l_XmlParser.Parser_xml(responsetoClient);

							if (responsetoClient.length() != 0) {
								// return
								// Response.ok(responsetoClient).language("en").build();
								log.info("responsetoClient :" + responsetoClient);
								ar.resume(Response.ok(responsetoClient).language("en").build());
							} else {
								Response e_response = o_customexception.riseexception("Internal server error", 500,
										"Socket time out");
								log.error("Exception while processing");
								// throw new
								// WebApplicationException(e_response);
								ar.resume(e_response);
							}
						} catch (SocketTimeoutException e) {
							log.error(e.getMessage());
							Response e_response = o_customexception.riseexception("Internal server error", 900,
									e.getMessage());
							log.error("Internal server error");
							// throw new WebApplicationException(e_response);
							ar.resume(e_response);

						} catch (Exception e) {
							log.error(e.getMessage());
							Response e_response = o_customexception.riseexception("Exception while processing", 500,
									e.getMessage());
							log.error("Exception while processing");
							// throw new WebApplicationException(e_response);
							ar.resume(e_response);

						}

					} else {
						Response e_response = o_customexception.riseexception("Bad request", 400, "null body Received");
						// throw new WebApplicationException(e_response);
						log.error("Null body received");
						ar.resume(e_response);
					}

				}
			}
		});

		log.error("####################Shuting down################");

	}

}

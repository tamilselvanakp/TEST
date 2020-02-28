package demo1.date14;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dob.base.GetauthTokenDob;
import com.response.GenerateAuthTokenResponse;
import com.utility.RandomString;
import com.utility.Utilities;
import com.utility.XmlParser;

@Path("GenerateAuthtoken")

public class GenerateAuthtoken extends Baseclass {
	public static Logger log = Logger.getLogger(GenerateAuthtoken.class.getName());

	@POST
	@Produces({ "application/xml" })
	@Consumes(MediaType.APPLICATION_XML)

	/*
	<GENERATE_AUTH_TOKEN>
	<TRANSCATION_ID>1111123</TRANSCATION_ID>
	<USER_NAME>TAMIL</USER_NAME>
	<PASSWORD>TAMIL</PASSWORD>
	<CLIENT_ID>2</CLIENT_ID>
	<PARTNER_ID>69</PARTNER_ID>
	<ENTITY>TAMIL</ENTITY>
	</GENERATE_AUTH_TOKEN>
	
	ENTITY-->mapped to Ntwork Id and Client id in ITConfig File
	USER_NAME & PASSWORD --> mapped to Network Id (HLR table)
	if any  thing goes Wrong Token will not generated
	*/
	public Response PostGenerateauthToken(String Received_Body) {
		Response l_resp = null;
		String authToken = "";
		ArrayList<String> listEntity = null;
		ArrayList<String> listNetwork = null;
		ArrayList<String> listNClinetid = null;
		int l_networkFromConfig = 0;
		log.info("GenerateauthToken called [" + Thread.currentThread().getName() + "]");
		log.info("Received Body [" + Received_Body + "] and length" + Received_Body.length());
		String rootElement = XmlParser.getXmlrootElement(Received_Body);

		log.debug("Root element in :" + rootElement);
		if (rootElement.equalsIgnoreCase("GENERATE_AUTH_TOKEN")
				|| rootElement.equalsIgnoreCase("GENERATE_AUTH_TOKEN_REQUEST")) {
			Utilities.addAPIinMDCtologger(rootElement);
			org.w3c.dom.Document l_doc = l_XmlParser.Parser_xml(Received_Body);
			// to split into node list
			NodeList l_nodelist = l_XmlParser.get_Doc_to_NodeList(l_doc, "*");
			// Getting the particular Element in the Node list
			String l_user = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "USER_NAME");

			String L_password = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "PASSWORD");
			String l_ip = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "IP");
			String l_clientid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "CLIENT_ID");
			String l_txid = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "TRANSCATION_ID");
			Utilities.addTxIdinMDCtologger(l_txid);
			int intClinetid = 0;
			try {
				intClinetid = Integer.parseInt(l_clientid);
			} catch (Exception e) {
				log.error("Client Id should be a number " + l_clientid);

				GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 464, "Failure",
						"Client Id should be a number", "");
				return Response.status(400).entity(o_respObj).build();
				// return l_resp =
				// o_customexception.riseexceptionwithoutrootcause("Invalid
				// Client Id", 467);
			}
			String l_partnerId = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "PARTNER_ID");
			String l_entity = l_XmlParser.get_NodeList_to_elemet(l_nodelist, "ENTITY");

			Document l_configdoc = o_singleton.getConfigReaderDocument();
			try {
				listEntity = XmlParser.getdocValuebyXpath(l_configdoc, "//ENTITY/NAME/text()");
				listNetwork = XmlParser.getdocValuebyXpath(l_configdoc, "//ENTITY/NETWORK/text()");
				listNClinetid = XmlParser.getdocValuebyXpath(l_configdoc, "//ENTITY/CLIENT_ID/text()");
				log.debug("Checking ");
				if (listEntity.contains(l_entity)) {
					int entityindex = listEntity.indexOf(l_entity);
					log.debug("Found Entity in config at :" + entityindex);
					String Network = listNetwork.get(entityindex);
					log.debug("Entity Network " + Network);
					l_networkFromConfig = Integer.parseInt(Network);
					String configclientid = listNClinetid.get(entityindex);
					log.debug("Client ID received [" + l_clientid + "] Config client Id [" + configclientid + "]");

					if (configclientid.equals(l_clientid.trim())) {
						log.debug("configclientid [" + configclientid + "] matched with received Client ID"
								+ l_clientid.trim() + "]");
					} else {
						log.error("MisMatching Client ID received [" + l_clientid + "] Config client Id ["
								+ configclientid + "]");
						GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 467, "Failure",
								"Mismatching Client id with Entity", "");
						return Response.status(400).entity(o_respObj).build();

					}

				} else {
					log.error("Entity Not found");
					GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 465, "Failure",
							"Entity Not found", "");
					return Response.status(400).entity(o_respObj).build();
				}

				// return l_resp =
				// o_customexception.riseexceptionwithoutrootcause("Entity
				// Not found", 468);

			} catch (Exception e) {
				log.error(e.getMessage());

				return l_resp = o_customexception.riseexception("Internal server Error", 500, e.getMessage());
			}
			String l_formater = String.format(
					"Received Values USER_NAME [%s],PASSWORD[%s],IP[%s],CLIENT_ID[%s],PARTNER_ID[%s],ENTITY[%s],network_id[%d]",
					l_user, L_password, l_ip, l_clientid, l_partnerId, l_entity, l_networkFromConfig);

			log.info(l_formater);
			Connection l_hlr = o_singleton.getL_databaseConnection();
			if (l_hlr != null) {
				Map<String, String> result;
				try {
					result = GetauthTokenDob.validate_user(l_hlr, l_networkFromConfig, l_user, L_password, null, 0, 0,
							0, 0, 1, "H");
					log.info("DB response for User Validation" + result);

					if (result.get("error_code").equals("0")) {

						log.info("Valid User Checking DB Network with CLint id and Client Network");

						log.info("Valid User so Creating Auth Token...... Getting auth token");

						// com.utility.RandomString gen = new
						// com.utility.RandomString(4, new Random(), "B@");
						// com.utility.RandomString gen = new
						// com.utility.RandomString(21);
						// RandomString gen = new RandomString(21, new
						// Random());
						RandomString o_RandomString = new RandomString(28);
						authToken = o_RandomString.nextString();
						log.info("Generated Auth token is [" + authToken + "]");
						Connection l_imgconn = o_singleton.getL_imgdatabaseConnection();
						boolean responseFromdb = GetauthTokenDob.updateAuthTokeninDb(l_imgconn, intClinetid,
								l_networkFromConfig, authToken);
						// log.error(result);
						if (responseFromdb == true) {
							GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 0, authToken,
									"sucess");
							return Response.ok().entity(o_respObj).build();
						} else {

						}

					} else {
						log.info("Invalid User ");
						log.error(result);
						GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 466, "Failure",
								result.get("error_msg"), "");
						return Response.status(400).entity(o_respObj).build();
					}
				} catch (SQLException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse(l_txid, 500, "Failure",
							e.getMessage(), "");
					return Response.status(500).entity(o_respObj).build();
					// return l_resp = o_customexception.riseexception("Internal
					// server Error", 500, e.getMessage());

				}

			}

		} else

		{
			log.debug("Not supported Request received");
			GenerateAuthTokenResponse o_respObj = new GenerateAuthTokenResponse("", 500, "Failure",
					"Not supported Request received", "");
			return Response.status(400).entity(o_respObj).build();
			// return l_resp =
			// o_customexception.riseexceptionwithoutrootcause("Not supported
			// Request", 400);

		}
		return l_resp;

	}

	@GET
	@Produces({ "application/xml" })

	public Response PostGenerateauthToken() {
		log.debug("Method Not Allowed");
		Response l_resp = o_customexception.riseexceptionwithoutrootcause(" Method Not Allowed", 405);
		throw new WebApplicationException(l_resp);
	}

}

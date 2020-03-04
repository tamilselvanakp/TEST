package demo1.date14;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.dob.base.GetSubscriberinfoDob;
import com.exception.handler.CustomException;
import com.utility.SingletonStorage;
import com.utility.XmlParser;

public class DirectResponse {
	GetSubscriberinfoDob o_GetSubscriberDate = new GetSubscriberinfoDob();
	SingletonStorage o_singleton = SingletonStorage.getSingletonInstances();
	static Logger log = Logger.getLogger(DirectResponse.class.getName());
	CustomException o_customexception = new CustomException();
	HashMap<String, String> Responsemap = null;

	public DirectResponse() {

	}

	public HashMap<String, String> getItem(String xmlbody, ServletContext ctx)
			throws XPathExpressionException, SQLException {

		// Getting the config value from memory
		// String configval = (String) ctx.getAttribute("StrconfigObj");
		Document l_conficDoc = o_singleton.getConfigReaderDocument();
		log.debug("configval is :" + l_conficDoc);
		// XmlParser l_parser = new XmlParser();
		// Document docForConfig = l_parser.Parser_xml(configval);
		// NodeList nodes = docForConfig.getElementsByTagName("*");

		// XmlParser o_XmlParser = new XmlParser();
		// NodeList nodes = o_XmlParser.get_Doc_to_NodeList(docForConfig,
		// "GET_LOCATION_INFO");

		// String l_Entity = o_XmlParser.get_NodeList_to_elemet(nodes,
		// "ENTITY");

		String l_Entity = XmlParser.getdocValuebyXpath(l_conficDoc, "//GET_LOCATION_INFO/ENTITY/text()").get(0);
		log.debug("l_Entity :" + l_Entity);

		Connection db_con = o_singleton.getL_databaseConnection();
		if (db_con != null) {

			// Connection db_con = (Connection) ctx.getAttribute("DB_conn");
			String RequestName = null;
			try {
				RequestName = XmlParser.getXmlrootElement(xmlbody);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				log.error("Exception !!!!!!!!!!!" + e.getMessage());
				e.printStackTrace();
			}
			log.debug("API Name Before:" + RequestName);
			if (RequestName.contains("REQUEST")) {
				RequestName = RequestName.replace("_REQUEST", "");
			}
			log.debug("IP API Name [" + RequestName.trim() + "]");
			log.debug("IP API XML [" + xmlbody + "]");
			String user = "192.168.151.134:ITG1008_00";
			log.debug("IP user[" + user + "]");
			String tx_id = "1234567890";
			log.debug("IP tx_id[" + tx_id + "]");
			String BulkTrans = "";
			log.debug("IP BulkTrans[" + BulkTrans + "]");

			return Responsemap = o_GetSubscriberDate.get_mboss_iface(db_con, RequestName, xmlbody, user, tx_id,
					BulkTrans);

			/*if (Responsestr == null || Responsestr == "") {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
			}
			log.debug("-------------------");
			return Response.status(Response.Status.OK).entity(Responsestr).type(MediaType.APPLICATION_XML).build();*/
		} /*else {
			return o_customexception.riseexception("Exception While processing", 500,
					"Db conn is null [" + Thread.currentThread().getStackTrace()[1].getFileName() + "] ["
							+ Thread.currentThread().getStackTrace()[1].getLineNumber() + "]");
			}*/
		return Responsemap;
	}

}

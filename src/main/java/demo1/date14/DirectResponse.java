package demo1.date14;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dob.base.GetSubscriberDate;
import com.exception.handler.CustomException;
import com.utility.SingletonStorage;
import com.utility.XmlParser;

public class DirectResponse {
	GetSubscriberDate o_GetSubscriberDate = new GetSubscriberDate();
	CustomException o_customexception = new CustomException();
	String Responsestr = "";

	public DirectResponse() {

	}

	public Response getItem(String xmlbody, ServletContext ctx) {

		// Getting the config value from memory
		String configval = (String) ctx.getAttribute("StrconfigObj");
		System.out.println("configval is :" + configval);
		XmlParser l_parser = new XmlParser();
		Document docForConfig = l_parser.Parser_xml(configval);
		// NodeList nodes = docForConfig.getElementsByTagName("*");

		XmlParser o_XmlParser = new XmlParser();
		NodeList nodes = o_XmlParser.get_Doc_to_NodeList(docForConfig, "GET_LOCATION_INFO");

		String l_Entity = o_XmlParser.get_NodeList_to_elemet(nodes, "ENTITY");
		System.out.println("l_Entity :" + l_Entity);
		SingletonStorage o_singleton = SingletonStorage.getSingletonInstances();

		Connection db_con = o_singleton.getL_databaseConnection();
		if (db_con != null) {

			// Connection db_con = (Connection) ctx.getAttribute("DB_conn");
			String RequestName = XmlParser.getXmlrootElement(xmlbody);
			System.out.println("API Name Before:" + RequestName);
			if (RequestName.contains("REQUEST")) {
				RequestName = RequestName.replace("_REQUEST", "");
			}
			System.out.println("IP API Name [" + RequestName.trim() + "]");
			System.out.println("IP API XML [" + xmlbody + "]");
			String user = "192.168.151.134:ITG1008_00";
			System.out.println("IP user[" + user + "]");
			String tx_id = "1234567890";
			System.out.println("IP tx_id[" + tx_id + "]");
			String BulkTrans = "";
			System.out.println("IP BulkTrans[" + BulkTrans + "]");

			Responsestr = o_GetSubscriberDate.get_mboss_iface(db_con, RequestName, xmlbody, user, tx_id, BulkTrans);

			if (Responsestr == null || Responsestr == "") {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).build();
			}
			System.out.println("-------------------");
			return Response.status(Response.Status.OK).entity(Responsestr).type(MediaType.APPLICATION_XML).build();
		} else {
			return o_customexception.riseexception("Exception While processing", 500,
					"Db conn is null [" + Thread.currentThread().getStackTrace()[1].getFileName() + "] ["
							+ Thread.currentThread().getStackTrace()[1].getLineNumber() + "]");
		}
	}

}

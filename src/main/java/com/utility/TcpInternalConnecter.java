package com.utility;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TcpInternalConnecter {

	public static void jsonParserNoReturn(String str) {
		// json node to json obj to traverse each element
		JsonElement rootNode = JsonParser.parseString(str);
		JsonObject o_JsonobjRootNode = rootNode.getAsJsonObject();

		Object l_mapKey = null;
		// parsing based on the key value
		for (Object o : o_JsonobjRootNode.keySet()) {
			String l_mapVlaue = (String) o;
			// Checking key value in map in obj map
			l_mapKey = o_JsonobjRootNode.get(l_mapVlaue);
			if (l_mapKey instanceof JsonObject) {
				System.out.println("Header: " + l_mapKey);
				// calling tcp connector when it is has been the object of
				// instances
				TcpInternalConnecter.jsonParserNoReturn(l_mapKey.toString());
			} else {
				System.out.println("key: " + l_mapVlaue + " value: " + l_mapKey);

			}
		}
	}

	static Object retrnVar;

	public static Object jsonParserWithParitcularElement(String str, String value) {
		// json node to json obj
		JsonElement rootNode = JsonParser.parseString(str);
		JsonObject o_JsonobjRootNode = rootNode.getAsJsonObject();

		Object l_mapKey = null;
		for (Object o : o_JsonobjRootNode.keySet()) {
			String l_mapVlaue = (String) o;

			l_mapKey = o_JsonobjRootNode.get(l_mapVlaue);
			if (l_mapKey instanceof JsonObject) {

				if (l_mapVlaue.equalsIgnoreCase(value)) {

					System.out.println("Comparing \"" + value + "\" with key: " + l_mapVlaue + " Map value: "
							+ l_mapKey.toString() + " is Json Elemnt with child node ");
					retrnVar = l_mapKey;
					l_mapKey = null;

				} else {
					// System.out.println("Header: " + l_mapKey);
					TcpInternalConnecter.jsonParserWithParitcularElement(l_mapKey.toString(), value);
				}

			} else {

				if (l_mapVlaue.equalsIgnoreCase(value)) {
					System.out.println("keystr is " + l_mapVlaue);
					System.out.println(
							"Comparing \"" + value + "\" with key: " + l_mapVlaue + " value: " + l_mapKey.toString());
					retrnVar = l_mapKey.toString();
					l_mapKey = null;
					break;

				}

			}
		}
		return retrnVar;
	}

	public static void main(String arg[]) throws Exception {
		/*
		Socket Client = null;
		SocketAddress sockaddr = null;
		boolean IsSocketCreated = false;
		String p_Response = "";
		OutputStream outToServer = null;
		InputStream in = null;
		String strRequestString = "";
		String newStringFormat = "";
		int retry = 1;
		String prefix = "";
		strRequestString = "<REGISTRATION_REQUEST><HEADER><ENTITY_NAME>Tamil1</ENTITY_NAME><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER></REGISTRATION_REQUEST>";
		int reqLength = strRequestString.length();
		String lenString = Utilities.XmlLengthFormater(reqLength);
		strRequestString = lenString + strRequestString;
		System.out.println("Request to:" + strRequestString);
		
		Client = new Socket();
		
		sockaddr = new InetSocketAddress("192.168.151.134", 8616);
		System.out.println("connecting to ..." + sockaddr.toString());
		
		try {
		Client.connect(sockaddr, 1000);
		if (Client.isConnected()) {
		IsSocketCreated = true;
		System.out.println("connection established");
		
		}
		} catch (Exception e) {
		System.out.println("Exception while creating socket,Reason is:" + e.getMessage());
		
		}
		
		System.out.println("Sending Req Len :" + strRequestString.getBytes().length);
		if (IsSocketCreated) {
		outToServer = Client.getOutputStream();
		for (int j = 0; j < 1; j++) {
		outToServer.write(strRequestString.getBytes());
		outToServer.flush();
		Client.setSoTimeout(10000);
		in = Client.getInputStream();
		InputStreamReader reader = new InputStreamReader(in);
		
		System.out.println("bef while" + Client.getReceiveBufferSize());
		char[] cbuf1 = new char[Client.getReceiveBufferSize() + 1];
		reader.read(cbuf1, 0, 9);
		
		String resLength = "";
		for (char a : cbuf1) {
		resLength += a;
		}
		int intResLength = Integer.parseInt(resLength.trim());
		
		System.out.println("resLength [" + intResLength + "]");
		
		char[] cbuf2 = new char[Client.getReceiveBufferSize() + 1];
		reader.read(cbuf2, 0, intResLength);
		int i = -1;
		* while ((i = in.read()) > -1) {
		System.out.println(i);
		p_Response += (char) i;
		
		}
		
		for (char a : cbuf2) {
		p_Response += a;
		
		}
		
		System.out.println(
		"DATA IS READ FROM CHANNELS SUCCESFULY,Actual response is : [" + p_Response.trim() + "]");
		}
		} else {
		System.out.println("connection not established");
		}
		
		*/

		// String getSubInfo =
		// "<REQUEST><HEADER><TRANSACTION_ID>2bFymfMA040220120913</TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><CONNECTION_TYPE>0</CONNECTION_TYPE></HEADER><BODY><PRIMARY_IMSI>234488420020121</PRIMARY_IMSI><TYPE>D</TYPE><REQ_DOMAIN>1</REQ_DOMAIN></BODY></REQUEST>";
		// String rn_req =
		// "<RNSPAReq><REQUESTTYPE>RN</REQUESTTYPE><IMSI>234064000100220</IMSI><MSISDN>12345678012</MSISDN><VLR>4528132801</VLR><NETWORK_ID>1</NETWORK_ID><TRANSACTIONID>42056425</TRANSACTIONID></RNSPAReq>";

		// SslConfigurator sslConfig =
		// SslConfigurator.newInstance().keyStoreFile("D:\\Software\\testkey.pem");

		String getsubres = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><ns0:updateSubscriberDetailsResponseV3 xmlns:ns0=\"http://T-Mobile/Wholesale/xml\"><ns0:headerResponse><ns0:senderID>XX</ns0:senderID><ns0:tranDateTime>2018-01-04T23:25:22.692-08:00</ns0:tranDateTime></ns0:headerResponse><ns0:body><ns0:response><ns0:partnerTransactionID>4d6a62cf-cc37-4017-93b3-1b4235de7977</ns0:partnerTransactionID><ns0:updateSubscriberDetails><ns0:status>SUCCESS</ns0:status><ns0:partnerID>XX</ns0:partnerID><ns0:action>UpdateWPS</ns0:action><ns0:MSISDN>7075340903</ns0:MSISDN><ns0:ICCID>8901260710040290066</ns0:ICCID><ns0:result><ns0:result>100</ns0:result><ns0:status>SUCCESS</ns0:status></ns0:result></ns0:updateSubscriberDetails></ns0:response><ns0:responseCount>1</ns0:responseCount></ns0:body></ns0:updateSubscriberDetailsResponseV3></SOAP-ENV:Body></SOAP-ENV:Envelope>";

		// String getsubres = "<SOAP-ENV:Envelope
		// xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><ns0:updateSubscriberDetailsResponseV3
		// xmlns:ns0=\"http://T-Mobile/Wholesale/xml\"><ns0:headerResponse><ns0:senderID>XX</ns0:senderID><ns0:tranDateTime>2018-01-04T23:25:22.692-08:00</ns0:tranDateTime></ns0:headerResponse><ns0:body><ns0:response><ns0:partnerTransactionID>4d6a62cf-cc37-4017-93b3-1b4235de7977</ns0:partnerTransactionID><ns0:updateSubscriberDetails><ns0:status>SUCCESS</ns0:status><ns0:partnerID>XX</ns0:partnerID><ns0:action>UpdateWPS</ns0:action><ns0:MSISDN>7075340903</ns0:MSISDN><ns0:ICCID>8901260710040290066</ns0:ICCID><ns0:result><ns0:result>100</ns0:result><ns0:status>SUCCESS</ns0:status></ns0:result></ns0:updateSubscriberDetails></ns0:response><ns0:responseCount>1</ns0:responseCount></ns0:body></ns0:updateSubscriberDetailsResponseV3></SOAP-ENV:Body></SOAP-ENV:Envelope>";
		// String getsubres =
		// "<RESPONSE><HEADER><TRANSACTION_ID><CHILD_TRANSACTION_ID>qws</CHILD_TRANSACTION_ID><CHILD_TRANSACTION_ID>123456</CHILD_TRANSACTION_ID></TRANSACTION_ID><REQUEST_TYPE>GET_SUBSCRIBER_INFO</REQUEST_TYPE><ERROR_CODE>255</ERROR_CODE><ERROR_DESC>Success</ERROR_DESC><CHILD><INNERCHILE>testing</INNERCHILE></CHILD></HEADER><TEST><REQUEST_TYPE>GET_SUBSCRIBER_DATA</REQUEST_TYPE><TRANSACTION_ID>123</TRANSACTION_ID><NW_ID>60</NW_ID><ACC_ID>1811724</ACC_ID><IMSI_TYPE>P</IMSI_TYPE><SEC_IMSIS><IMSI_01><IMSI>187412645030121</IMSI><FAD></FAD><BURNT_TYPE>1</BURNT_TYPE></IMSI_01></SEC_IMSIS></TEST></RESPONSE>";
		// String getsubres = "<a:RESPONSE xmnls:a=\"www.tamil.com\"
		// ><a:HEADER><a:TRANSACTION_ID><a:CHILD_TRANSACTION_ID>qws</a:CHILD_TRANSACTION_ID><a:CHILD_TRANSACTION_ID>123456</a:CHILD_TRANSACTION_ID></a:TRANSACTION_ID><a:REQUEST_TYPE>GET_SUBSCRIBER_INFO</a:REQUEST_TYPE><a:ERROR_CODE>255</a:ERROR_CODE><a:ERROR_DESC>Success</a:ERROR_DESC><CHILD><INNERCHILE>testing</INNERCHILE></CHILD></a:HEADER><TEST
		// xmlns=\"http://www.w3.org/TR/html4/\"><REQUEST_TYPE>GET_SUBSCRIBER_DATA</REQUEST_TYPE><TRANSACTION_ID>123</TRANSACTION_ID><NW_ID>60</NW_ID><ACC_ID>1811724</ACC_ID><IMSI_TYPE>P</IMSI_TYPE><SEC_IMSIS><IMSI_01><IMSI>187412645030121</IMSI><FAD></FAD><BURNT_TYPE>1</BURNT_TYPE></IMSI_01></SEC_IMSIS></TEST></a:RESPONSE>";
		/*Multimap<String, String> l_HashMap = XmlParser.getxmlToMultMap(getsubres, "");
		System.out.println("{\"message\":{\"header\":{\"error\":\"summa!\"}\"},\"body\":{\"name\":\"tamil!\"}}");
		System.out.println("-------------------------");
		System.out.println("CC is" + l_HashMap.get("CC"));
		System.out.println("IMSI is" + l_HashMap.get("IMSI"));
		System.out.println("IMSI is" + l_HashMap.get("APN_TYPE"));
		System.out.println("-------------------------");
		
		JSONObject soapDatainJsonObject = XML.toJSONObject(getsubres);
		String strSoapDatainJsonObject = soapDatainJsonObject.toString();
		System.out.println(strSoapDatainJsonObject);
		// SSLContext ssl = sslConfig.createSSLContext();
		// Client client = ClientBuilder.newBuilder().sslContext(ssl).build();
		Client client = ClientBuilder.newClient();
		WebTarget l_WebTarget = client.target("http://192.168.151.52:7154");
		System.out.println("Client request");
		Invocation.Builder l_Builder = l_WebTarget.request(MediaType.APPLICATION_JSON);
		Response response = l_Builder.post(Entity.entity(strSoapDatainJsonObject, MediaType.APPLICATION_JSON));
		String resstr = response.readEntity(String.class);
		System.out.println("Response is :" + resstr);
		
		System.out.println("==================");
		
		// String test =
		// "{\"message\":{\"header\":{\"error\":\"summa!\"},\"body\":{\"name\":\"tamil!\"}}}";
		// String test = "{\"message\":\"tamil!\"}";
		JsonElement jsonElement1 = JsonParser.parseString(resstr);
		// TcpInternalConnecter.jsonParserNoReturn(resstr);
		Object ll = TcpInternalConnecter.jsonParserWithParitcularElement(resstr, "ICCID");
		System.out.println("Return is" + ll);
		
		client.close();
		*/
		XmlParser l_XmlParser = new XmlParser();
		org.w3c.dom.Document l_doc = l_XmlParser.Parser_xml(getsubres);

		/*NodeList llst = l_XmlParser.get_Doc_to_NodeList(l_doc, "REQUEST");
		String a = l_XmlParser.get_NodeList_to_elemet(llst, "TRANSACTION_ID");
		System.out.println("node : " + a);
		l_doc.getDocumentElement().normalize();
		// to split into node list
		NodeList ll = l_doc.getElementsByTagName("TP_XML");
		
		System.out.println(" length" + ll.getLength());
		if (ll.getLength() > 0) {
			for (int i = 0; i < ll.getLength(); i++) {
				if (ll.item(i).hasChildNodes()) {
					System.out.print("Inner node Name  " + ll.item(i).getNodeName());
					System.out.print(" \t Inner node child  " + ll.item(i).getTextContent());
					System.out.println();
				}
				// System.out.println("ll " + ll.item(i).getNextSibling());
			}
		} else {
			System.out.println("sub doesnt have local imsi");
		}*/
		XPath l_xpath = XPathFactory.newInstance().newXPath();

		XPathExpression expr = l_xpath.compile("//updateSubscriberDetail/status/text()");
		Object result = expr.evaluate(l_doc, XPathConstants.NODESET);

		NodeList nodes = (NodeList) result;

		System.out.println("test " + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println("Val " + nodes.item(i).getNodeValue());
		}

	}

	public static Node node_Iterater(NodeList f_nodeList, String f_expression) {
		System.out.println("l_nodelist :" + f_nodeList.getLength() + " :" + f_nodeList);
		for (int i = 0; i < f_nodeList.getLength(); i++) {
			final Node l_node = f_nodeList.item(i);

			System.out.println("l_node :" + i + " :" + l_node.getNodeName() + " type :" + l_node.getNodeType()
					+ "Value :" + l_node.getNodeValue() + " text :" + l_node.getTextContent());

			if (f_expression.equals(l_node.getNodeName())) {
				return l_node;
			}

		}
		return null;
		/*} else {
			TcpInternalConnecter.node_Iterater((NodeList) l_node, f_expression);
		}*/

	}
}

// TcpConnecter.sendTcpClientRequest("192.168.151.134", 6183, "HLR",
// "TAMIL", getSubInfo, true);

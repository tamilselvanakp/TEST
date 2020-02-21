package com.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Logger;

public class SoapXmlFormater {
	static Logger log = Logger.getLogger(SoapXmlFormater.class.getName());
	public static String Prefix = "ns";

	private static SOAPMessage getEnvelop(String soapprefix) throws SOAPException {
		SOAPMessage message = MessageFactory.newInstance().createMessage();

		SOAPPart soapPart = message.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
		soapEnvelope.setPrefix(soapprefix);

		soapEnvelope.addNamespaceDeclaration(Prefix, "http://T-Mobile/Wholesale/xml");
		return message;
	}

	private static SOAPMessage bodyelementbulder(SOAPMessage message, SOAPBody saopBody, String BodyelementName,
			String apiPrefix) throws SOAPException {
		SOAPFactory soapFactory = SOAPFactory.newInstance();
		Name bodyName;
		bodyName = soapFactory.createName(BodyelementName);

		log.debug("Framing API for :" + BodyelementName);

		SOAPBodyElement apiBody = saopBody.addBodyElement(bodyName);
		// saopBody.addChildElement("v9:SomeElement");
		apiBody.setPrefix(apiPrefix);
		message.saveChanges();
		return message;
	}

	private static SOAPMessage getsoapbody(String prefix, String apiName) throws SOAPException {

		log.debug("prefix [" + prefix + "] apiName [" + apiName + "]");
		SOAPMessage message = SoapXmlFormater.getEnvelop(prefix);
		SOAPEnvelope soapEnvelope = message.getSOAPPart().getEnvelope();

		SOAPHeader header = message.getSOAPHeader();
		header.removeNamespaceDeclaration(header.getPrefix());
		header.setPrefix(prefix);

		SOAPBody saopBody = soapEnvelope.getBody();
		log.debug("soap message created");
		saopBody.setPrefix(prefix);
		saopBody.removeNamespaceDeclaration(saopBody.getPrefix());

		/*	log.debug("Creating body header [" + bodyHeaderName + "]");
			Name BodyHeadern = soapFactory.createName(bodyHeaderName);
			saopBody.addBodyElement(BodyHeadern).setPrefix(apiPrefix);*/

		/*Name bodyName;
		bodyName = soapFactory.createName(apiName);
		
		log.debug("Framing API for :" + apiName);
		
		SOAPBodyElement apiBody = saopBody.addBodyElement(bodyName);
		// saopBody.addChildElement("v9:SomeElement");
		apiBody.setPrefix(apiPrefix);
		message.saveChanges();
		return message;*/
		return message;

	}

	private static SOAPElement setchildForBodyElement(SOAPBodyElement Bodyelement, String childName, String Value)
			throws SOAPException {
		log.debug("Adding element \"" + childName + "\" with value : [" + Value + "]");
		return Bodyelement.addChildElement(childName).addTextNode(Value);
	}

	private static SOAPElement setchildElementForElement(SOAPElement Bodyelement, String childName, String Value)
			throws SOAPException {
		log.debug("Adding element \"" + childName + "\" with value : [" + Value + "]");
		return Bodyelement.addChildElement(childName).addTextNode(Value);
	}

	public static void main(String[] args)
			throws SOAPException, IOException, TransformerConfigurationException, TransformerFactoryConfigurationError {
		try {
			/*GetUpdateSubscriberDetailsSOAPapi("12345", "UpdateWPS", "45678954565", "3546548787", "packagetests", "true",
					"1234564", "", "");*/
			GetUpdateSubscriberDetailsSOAPapi("12345", "ChangeMSISDN", "45678954565", "3546548787", "", "", "1234564",
					"", "123456");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static String GetUpdateSubscriberDetailsSOAPapi(String partnerTransactionID, String action, String MSISDN,
			String ICCID, String Package, String productflagisbaseproduct, String WPS, String newICCID,
			String marketZip) throws Exception {
		SOAPMessage l_message = SoapXmlFormater.getsoapbody("soapenv", "updateProduct");

		SOAPBody l_body = l_message.getSOAPPart().getEnvelope().getBody();
		// l_body.setPrefix("tm");

		// creating header in body
		Name l_apivers3 = SOAPFactory.newInstance().createName("updateSubscriberDetailsRequestV3");
		SOAPBodyElement l_apiver3Element = l_body.addBodyElement(l_apivers3);
		l_apiver3Element.setPrefix("ns");

		SOAPElement l_requestheader = setchildForBodyElement(l_apiver3Element, "header", "");
		SOAPElement senderID = setchildElementForElement(l_requestheader, "senderID", "88");
		SOAPElement callBackLocation = setchildElementForElement(l_requestheader, "callBackLocation",
				"https://192.30.220.108:3005/lycaProvGateway/responseValidator.htm");
		SOAPElement tranDateTime = setchildElementForElement(l_requestheader, "tranDateTime",
				Utilities.getCurrentDateTime());

		// log.debug("Filling data : " + senderID.getLocalName() + " :
		// " + senderID.getTextContent() + ","
		// + callBackLocation.getLocalName() + " : " +
		// callBackLocation.getTextContent() + " , tranDateTime :"
		// + tranDateTime.getTextContent());
		/*Name l_requestbody = SOAPFactory.newInstance().createName("body");
		SOAPBodyElement l_requestBodyElm = l_body.addBodyElement(l_requestbody);
		l_requestBodyElm.setPrefix("tm");*/

		SOAPElement l_requestbdy = setchildForBodyElement(l_apiver3Element, "body", "");
		SOAPElement request = setchildElementForElement(l_requestbdy, "request", "");
		// SOAPElement partnerID =
		setchildElementForElement(request, "partnerID", "87");
		setchildElementForElement(request, "partnerTransactionID", partnerTransactionID);
		SOAPElement l_updateSubscriberDetailele = setchildElementForElement(request, "updateSubscriberDetail", "");
		SOAPElement l_action = setchildElementForElement(l_updateSubscriberDetailele, "action", action);
		SOAPElement l_ICCID = setchildElementForElement(l_updateSubscriberDetailele, "ICCID", ICCID);
		SOAPElement l_MSISDN = setchildElementForElement(l_updateSubscriberDetailele, "MSISDN", MSISDN);
		if (action.equals("UpdateWPS")) {

			SOAPElement l_product = setchildElementForElement(l_updateSubscriberDetailele, "product", "");
			SOAPElement l_productID = setchildElementForElement(l_product, "productID", Package);
			l_productID.setAttribute("isBaseProduct", productflagisbaseproduct);
			SOAPElement l_WPS = setchildElementForElement(l_product, "WPS", WPS);

		} else if (action.equals("ChangeSIM"))

		{

			// newICCID
			SOAPElement l_newICCID = setchildElementForElement(l_updateSubscriberDetailele, "newICCID", newICCID);
		}

		else if (action.equals("ChangeMSISDN")) {
			SOAPElement l_marketZip = setchildElementForElement(l_updateSubscriberDetailele, "marketZip", marketZip);

		} else {
			log.error("!!!!!!!!!! \"action\" should belongs to updateSubscriberDetailsRequestV3 ");
			return null;
		}
		l_message.saveChanges();
		l_message.toString();
		ByteArrayOutputStream l_byteArrayOutputStream = new ByteArrayOutputStream();
		l_message.writeTo(l_byteArrayOutputStream);
		String result = l_byteArrayOutputStream.toString();

		log.debug("Sopamsg [" + result + "]");
		l_byteArrayOutputStream.close();
		return result;
	}

}

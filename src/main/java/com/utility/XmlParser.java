package com.utility;

import java.io.StringReader;
import java.util.ArrayList;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.guava.HashMultimap;
import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.exception.handler.CustomException;

public class XmlParser {
	static Logger log = Logger.getLogger(XmlParser.class.getName());
	static CustomException o_customexception = new CustomException();

	public Document Parser_xml(String xml_body) {
		Document document = null;

		if (xml_body.length() == 0 || xml_body.equals("")) {
			log.debug("XML should not be a null value");
			Response e_response = o_customexception.riseexception("Body should not be null", 400,
					"Recevived body null");

			throw new WebApplicationException(e_response);
		}

		// log.debug("Inside Parser_xml["+xml_body+"]");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			Response e_response = o_customexception.riseexception(e1.getMessage(), 500, "Error while processing");
			e1.printStackTrace();
			throw new WebApplicationException(e_response);

		}

		// Build Document
		try {

			document = builder.parse(new InputSource(new StringReader(xml_body)));
		} catch (Exception e) {
			Response e_response = o_customexception.riseexception(e.getLocalizedMessage(), 400,
					"Recevived body should be a valid XML");
			e.printStackTrace();
			throw new WebApplicationException(e_response);

		}
		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();

		// Here comes the root node
		Element root = document.getDocumentElement();
		// log.debug(root.getNodeName());
		// log.debug("document"+document.getNodeValue());
		// log.debug("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// log.debug("Root Elemenmt Name is ["+name+"]");
		log.debug("Root Elemenmt Name is [" + name + "]");

		// log.debug("1st child element :" +
		// document.getChildNodes());
		NodeList nodes = document.getElementsByTagName("*");

		if (nodes.getLength() == 0) {

			log.debug("Nodes are empty" + nodes);
		}

		else {

			for (int i = 0; i < nodes.getLength(); i++) {

				Node l_crnt_node = nodes.item(i);
				// log.debug("\nCurrent Element :" +
				// nodes.item(i).getNodeName());
				if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
					if (l_crnt_node.getLocalName().contains("BODY")) {
						log.debug("BODY found continued");
						continue;
					}
					if (l_crnt_node.getLocalName().contains("HEADER")) {
						log.debug("HEADER found continued");
						continue;
					}

					// verify the tag has c
					if (l_crnt_node.getChildNodes().getLength() > 1) {
						/*
						 * log.debug("Node  [" +
						 * l_crnt_node.getLocalName() +
						 * "] has child nodes [" +
						 * l_crnt_node.getChildNodes().getLength() +
						 * "] so continued");
						 */
						continue;
					}
					// findind the values from map
					/*
					 * log.debug("Key:[" +
					 * l_crnt_node.getLocalName().trim() + "] Value [" +
					 * l_crnt_node.getTextContent().trim() + "]");
					 */

				}
			}

		}

		/*} catch (Exception e) {
			log.debug("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
			Response e_response = o_customexception.riseexception(e.getMessage(), 400, "Error while Processing XML");
		
			throw new WebApplicationException(e_response);
		
		}*/
		return document;
	}

	public String get_node_to_attribute(NodeList l_attrnodelst, String attribute_Name) {
		String l_renAttributeStr = "";
		if (l_attrnodelst.getLength() != 0) {
			// log.debug("NodeList length is not empty , continue");

			for (int i = 0; i < l_attrnodelst.getLength(); i++) {
				Node l_crrntNode = l_attrnodelst.item(i);
				// log.debug("\nCurrent Element :" +
				// l_crrntNode.getNodeName());

				if (l_crrntNode.getNodeType() == Node.ELEMENT_NODE) {

					Element Lcrnt_attrinode_Element = (Element) l_crrntNode;

					if (Lcrnt_attrinode_Element != null) {
						log.debug(" current attribute node Element is Not null");
					}

					// log.debug("\nCurrent ENTITY_NODE :"
					// +Lcrnt_node_Element+" :
					// "+Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent());
					try {
						l_renAttributeStr = Lcrnt_attrinode_Element.getAttribute(attribute_Name);
						if (l_renAttributeStr.isEmpty()) {
							log.debug("Retrived Attribute [" + attribute_Name + "] is Empty");
							return null;
						}
					} catch (NullPointerException nullpo) {
						log.debug("Null value in given attribute_Name [" + attribute_Name + "] so returning");
						return "";
					}

				}
			}
		} else {
			System.err.println("l_nodelst is null");
		}
		return l_renAttributeStr;

	}

	public String get_NodeList_to_elemet(NodeList l_nodelst, String Element_Name) {
		String l_renStr = "";
		if (l_nodelst.getLength() != 0) {
			// log.debug("NodeList length is not empty , continue");

			for (int i = 0; i < l_nodelst.getLength(); i++) {
				Node l_crrntNode = l_nodelst.item(i);
				// log.debug("\nCurrent Element :" +
				// l_crrntNode.getNodeName());

				if (l_crrntNode.getNodeType() == Node.ELEMENT_NODE) {

					Element Lcrnt_node_Element = (Element) l_crrntNode;

					if (Lcrnt_node_Element != null) {
						// log.debug("Not null");
					}

					// log.debug("\nCurrent ENTITY_NODE :"
					// +Lcrnt_node_Element+" :
					// "+Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent());
					try {
						int childlength = Lcrnt_node_Element.getElementsByTagName(Element_Name).getLength();
						// log.debug("childlength is :" + childlength);
						l_renStr = Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent();
						log.debug("Value in give tag [" + Element_Name + "] is [" + l_renStr + "]");
					} catch (NullPointerException nullpo) {

						return "";
					}

				}
			}
		} else {
			System.err.println("l_nodelst is null");
		}
		return l_renStr;
	}

	public NodeList get_Doc_to_NodeList(Document l_document, String expression) {
		NodeList f_nodeList = null;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();

			// = "/CONFIG";
			Object objnodeList = xPath.compile(expression).evaluate(l_document, XPathConstants.NODESET);
			f_nodeList = (NodeList) objnodeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f_nodeList;

	}

	public static String getXmlrootElement(String xml_body) {

		Document document = null;

		if (xml_body.length() == 0 || xml_body.equals("")) {
			log.debug("XML should not be a null value");
			Response e_response = o_customexception.riseexception("Body should not be null", 400,
					"Recevived body null");

			throw new WebApplicationException(e_response);
		}

		// log.debug("Inside Parser_xml["+xml_body+"]");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			Response e_response = o_customexception.riseexception(e1.getMessage(), 500, "Error while processing");
			e1.printStackTrace();
			throw new WebApplicationException(e_response);

		}

		// Build Document
		try {

			document = builder.parse(new InputSource(new StringReader(xml_body)));
		} catch (Exception e) {
			Response e_response = o_customexception.riseexception(e.getLocalizedMessage(), 400,
					"Recevived body should be a valid XML");
			e.printStackTrace();
			throw new WebApplicationException(e_response);

		}
		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();

		// Here comes the root node
		Element root = document.getDocumentElement();
		// log.debug(root.getNodeName());
		// log.debug("document"+document.getNodeValue());
		// log.debug("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// log.debug("Root Elemenmt Name is ["+name+"]");
		log.debug("Return root Elemrnt [" + name + "]");

		return name;

	}

	static Multimap<String, String> l_HashMap = HashMultimap.create();

	public static Multimap<String, String> getxmlToMultMap(String xml_body, String API_NAME) {
		try {
			l_HashMap.clear();
			if (xml_body.length() == 0 || xml_body.equals("")) {
				log.debug("XML should not be a null value");

				log.debug("Returning Null");
				log.debug("l_HashMap is " + l_HashMap);
				return l_HashMap;
			}

			// Empting the MAp

			// log.debug("Inside Parser_xml["+xml_body+"]");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Build Document
			Document document = builder.parse(new InputSource(new StringReader(xml_body)));

			// Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();

			// Here comes the root node
			Element root = document.getDocumentElement();
			// log.debug(root.getNodeName());
			// log.debug("document"+document.getNodeValue());
			// log.debug("Root element :" +
			// document.getDocumentElement());
			String name = root.getLocalName();
			// log.debug("Root Elemenmt Name is ["+name+"]");
			log.debug("Root Elemenmt Name is [" + name + "]");

			// log.debug("1st child element :" +
			// document.getChildNodes());
			NodeList nodes = document.getElementsByTagName("*");

			if (nodes.getLength() == 0) {
				log.debug("Nodes are enmpty" + nodes);

			}

			else {

				log.debug("Received API Name is " + API_NAME);

				for (int i = 0; i < nodes.getLength(); i++) {

					Node l_crnt_node = nodes.item(i);
					// log.debug("\nCurrent Element :" +
					// nodes.item(i).getNodeName());
					if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
						if (l_crnt_node.getLocalName().contains("BODY")) {
							log.debug("BODY found continued");
							continue;
						}
						if (!API_NAME.isEmpty()) {
							if (l_crnt_node.getLocalName().contains(API_NAME))

							{

								log.debug("API NAME is [" + API_NAME + "] found");
								continue;
							}
						}

						// verify the tag has c
						if (l_crnt_node.getChildNodes().getLength() > 1) {
							log.debug("Node  [" + l_crnt_node.getLocalName() + "] has child nodes ["
									+ l_crnt_node.getChildNodes().getLength() + "] so continued");
							continue;
						}
						// findind the values from map
						log.debug("Key:[" + l_crnt_node.getLocalName().trim() + "] Value ["
								+ l_crnt_node.getTextContent().trim() + "]");
						l_HashMap.put(l_crnt_node.getLocalName().trim(), l_crnt_node.getTextContent().trim());

					}
				}
			}

		} catch (Exception e) {
			log.debug("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!" + e);

		}
		return l_HashMap;
	}

	public static Document configReader(String xml_body) {
		Document document = null;

		if (xml_body.length() == 0 || xml_body.equals("")) {
			log.debug("XML should not be a null value");

		}

		// log.debug("Inside Parser_xml["+xml_body+"]");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		// Build Document
		try {

			document = builder.parse(new InputSource(new StringReader(xml_body)));
		} catch (Exception e) {

			e.printStackTrace();

		}
		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();

		// Here comes the root node
		Element root = document.getDocumentElement();
		// log.debug(root.getNodeName());
		// log.debug("document"+document.getNodeValue());
		// log.debug("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// log.debug("Root Elemenmt Name is ["+name+"]");
		log.debug("Root Elemenmt Name is from config [" + name + "]");

		// log.debug("1st child element :" +
		// document.getChildNodes());
		NodeList nodes = document.getElementsByTagName("*");

		if (nodes.getLength() == 0) {

			log.debug("Nodes are empty" + nodes);
		}

		else {

			for (int i = 0; i < nodes.getLength(); i++) {

				Node l_crnt_node = nodes.item(i);
				log.debug("Current Element :" + nodes.item(i).getNodeName());
				if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
					if (l_crnt_node.getLocalName().contains("BODY")) {
						log.debug("BODY found continued");
						continue;
					}
					if (l_crnt_node.getLocalName().contains("HEADER")) {
						log.debug("HEADER found continued");
						continue;
					}

					// verify the tag has c
					if (l_crnt_node.getChildNodes().getLength() > 1) {
						/*
						 * log.debug("Node  [" +
						 * l_crnt_node.getLocalName() +
						 * "] has child nodes [" +
						 * l_crnt_node.getChildNodes().getLength() +
						 * "] so continued");
						 */
						continue;
					}
					// findind the values from map

					log.debug("Key:[" + l_crnt_node.getLocalName().trim() + "] Value ["
							+ l_crnt_node.getTextContent().trim() + "]");

				}
			}

		}

		/*} catch (Exception e) {
			log.debug("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
			Response e_response = o_customexception.riseexception(e.getMessage(), 400, "Error while Processing XML");
		
			throw new WebApplicationException(e_response);
		
		}*/
		return document;
	}

	public static ArrayList<String> getdocValuebyXpath(Document xmlDocument, String xpathString)
			throws XPathExpressionException {
		ArrayList<String> l_list = new ArrayList<String>();
		// XmlParser l_XmlParser = new XmlParser();
		// org.w3c.dom.Document l_doc = l_XmlParser.Parser_xml(getsubres);
		XPath l_xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = null;
		if (xpathString.contains("text()")) {
			// chercking it is contains text()
			expr = l_xpath.compile(xpathString);
		} else {
			if (xpathString.endsWith("/")) {
				// Ends with backslass or not
				expr = l_xpath.compile(xpathString + "text()");
			} else {
				expr = l_xpath.compile(xpathString + "/text()");
			}
		}
		Object result = expr.evaluate(xmlDocument, XPathConstants.NODESET);

		NodeList nodes = (NodeList) result;

		for (int i = 0; i < nodes.getLength(); i++) {
			l_list.add(i, nodes.item(i).getNodeValue());
			log.debug("Value_" + i + " :" + nodes.item(i).getNodeValue());
		}
		return l_list;
	}

}

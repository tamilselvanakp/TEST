package com.utility;

import java.io.StringReader;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.internal.guava.HashMultimap;
import org.glassfish.jersey.internal.guava.Multimap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.exception.handler.CustomException;

public class XmlParser {
	static CustomException o_customexception = new CustomException();

	public Document Parser_xml(String xml_body) {
		Document document = null;

		if (xml_body.length() == 0 || xml_body.equals("")) {
			System.out.println("XML should not be a null value");
			Response e_response = o_customexception.riseexception("Body should not be null", 400,
					"Recevived body null");

			throw new WebApplicationException(e_response);
		}

		// System.out.println("Inside Parser_xml["+xml_body+"]");

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
		// System.out.println(root.getNodeName());
		// System.out.println("document"+document.getNodeValue());
		// System.out.println("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// System.out.println("Root Elemenmt Name is ["+name+"]");
		System.out.println("Root Elemenmt Name is [" + name + "]");

		// System.out.println("1st child element :" +
		// document.getChildNodes());
		NodeList nodes = document.getElementsByTagName("*");

		if (nodes.getLength() == 0) {

			System.out.println("Nodes are empty" + nodes);
		}

		else {

			for (int i = 0; i < nodes.getLength(); i++) {

				Node l_crnt_node = nodes.item(i);
				// System.out.println("\nCurrent Element :" +
				// nodes.item(i).getNodeName());
				if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
					if (l_crnt_node.getLocalName().contains("BODY")) {
						System.out.println("BODY found continued");
						continue;
					}
					if (l_crnt_node.getLocalName().contains("HEADER")) {
						System.out.println("HEADER found continued");
						continue;
					}

					// verify the tag has c
					if (l_crnt_node.getChildNodes().getLength() > 1) {
						/*
						 * System.out.println("Node  [" +
						 * l_crnt_node.getLocalName() +
						 * "] has child nodes [" +
						 * l_crnt_node.getChildNodes().getLength() +
						 * "] so continued");
						 */
						continue;
					}
					// findind the values from map
					/*
					 * System.out.println("Key:[" +
					 * l_crnt_node.getLocalName().trim() + "] Value [" +
					 * l_crnt_node.getTextContent().trim() + "]");
					 */

				}
			}

		}

		/*} catch (Exception e) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
			Response e_response = o_customexception.riseexception(e.getMessage(), 400, "Error while Processing XML");
		
			throw new WebApplicationException(e_response);
		
		}*/
		return document;
	}

	public String get_node_to_attribute(NodeList l_attrnodelst, String attribute_Name) {
		String l_renAttributeStr = "";
		if (l_attrnodelst.getLength() != 0) {
			// System.out.println("NodeList length is not empty , continue");

			for (int i = 0; i < l_attrnodelst.getLength(); i++) {
				Node l_crrntNode = l_attrnodelst.item(i);
				// System.out.println("\nCurrent Element :" +
				// l_crrntNode.getNodeName());

				if (l_crrntNode.getNodeType() == Node.ELEMENT_NODE) {

					Element Lcrnt_attrinode_Element = (Element) l_crrntNode;

					if (Lcrnt_attrinode_Element != null) {
						System.out.println(" current attribute node Element is Not null");
					}

					// System.out.println("\nCurrent ENTITY_NODE :"
					// +Lcrnt_node_Element+" :
					// "+Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent());
					try {
						l_renAttributeStr = Lcrnt_attrinode_Element.getAttribute(attribute_Name);
						if (l_renAttributeStr.isEmpty()) {
							System.out.println("Retrived Attribute [" + attribute_Name + "] is Empty");
							return null;
						}
					} catch (NullPointerException nullpo) {
						System.out.println("Null value in given attribute_Name [" + attribute_Name + "] so returning");
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
			// System.out.println("NodeList length is not empty , continue");

			for (int i = 0; i < l_nodelst.getLength(); i++) {
				Node l_crrntNode = l_nodelst.item(i);
				// System.out.println("\nCurrent Element :" +
				// l_crrntNode.getNodeName());

				if (l_crrntNode.getNodeType() == Node.ELEMENT_NODE) {

					Element Lcrnt_node_Element = (Element) l_crrntNode;

					if (Lcrnt_node_Element != null) {
						// System.out.println("Not null");
					}

					// System.out.println("\nCurrent ENTITY_NODE :"
					// +Lcrnt_node_Element+" :
					// "+Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent());
					try {
						int childlength = Lcrnt_node_Element.getElementsByTagName(Element_Name).getLength();
						// System.out.println("childlength is :" + childlength);
						l_renStr = Lcrnt_node_Element.getElementsByTagName(Element_Name).item(0).getTextContent();
						System.out.println("Value in give tag [" + Element_Name + "] is [" + l_renStr + "]");
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
			System.out.println("XML should not be a null value");
			Response e_response = o_customexception.riseexception("Body should not be null", 400,
					"Recevived body null");

			throw new WebApplicationException(e_response);
		}

		// System.out.println("Inside Parser_xml["+xml_body+"]");

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
		// System.out.println(root.getNodeName());
		// System.out.println("document"+document.getNodeValue());
		// System.out.println("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// System.out.println("Root Elemenmt Name is ["+name+"]");
		System.out.println("Return root Elemrnt [" + name + "]");

		return name;

	}

	static Multimap<String, String> l_HashMap = HashMultimap.create();

	public static Multimap<String, String> getxmlToMultMap(String xml_body, String API_NAME) {
		try {
			l_HashMap.clear();
			if (xml_body.length() == 0 || xml_body.equals("")) {
				System.out.println("XML should not be a null value");

				System.out.println("Returning Null");
				System.out.println("l_HashMap is " + l_HashMap);
				return l_HashMap;
			}

			// Empting the MAp

			// System.out.println("Inside Parser_xml["+xml_body+"]");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Build Document
			Document document = builder.parse(new InputSource(new StringReader(xml_body)));

			// Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();

			// Here comes the root node
			Element root = document.getDocumentElement();
			// System.out.println(root.getNodeName());
			// System.out.println("document"+document.getNodeValue());
			// System.out.println("Root element :" +
			// document.getDocumentElement());
			String name = root.getLocalName();
			// System.out.println("Root Elemenmt Name is ["+name+"]");
			System.out.println("Root Elemenmt Name is [" + name + "]");

			// System.out.println("1st child element :" +
			// document.getChildNodes());
			NodeList nodes = document.getElementsByTagName("*");

			if (nodes.getLength() == 0) {
				System.out.println("Nodes are enmpty" + nodes);

			}

			else {

				System.out.println("Received API Name is " + API_NAME);

				for (int i = 0; i < nodes.getLength(); i++) {

					Node l_crnt_node = nodes.item(i);
					// System.out.println("\nCurrent Element :" +
					// nodes.item(i).getNodeName());
					if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
						if (l_crnt_node.getLocalName().contains("BODY")) {
							System.out.println("BODY found continued");
							continue;
						}
						if (!API_NAME.isEmpty()) {
							if (l_crnt_node.getLocalName().contains(API_NAME))

							{

								System.out.println("API NAME is [" + API_NAME + "] found");
								continue;
							}
						}

						// verify the tag has c
						if (l_crnt_node.getChildNodes().getLength() > 1) {
							System.out.println("Node  [" + l_crnt_node.getLocalName() + "] has child nodes ["
									+ l_crnt_node.getChildNodes().getLength() + "] so continued");
							continue;
						}
						// findind the values from map
						System.out.println("Key:[" + l_crnt_node.getLocalName().trim() + "] Value ["
								+ l_crnt_node.getTextContent().trim() + "]");
						l_HashMap.put(l_crnt_node.getLocalName().trim(), l_crnt_node.getTextContent().trim());

					}
				}
			}

		} catch (Exception e) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!" + e);

		}
		return l_HashMap;
	}

	public static Document configReader(String xml_body) {
		Document document = null;

		if (xml_body.length() == 0 || xml_body.equals("")) {
			System.out.println("XML should not be a null value");

		}

		// System.out.println("Inside Parser_xml["+xml_body+"]");
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
		// System.out.println(root.getNodeName());
		// System.out.println("document"+document.getNodeValue());
		// System.out.println("Root element :" +
		// document.getDocumentElement());
		String name = root.getLocalName();
		// System.out.println("Root Elemenmt Name is ["+name+"]");
		System.out.println("Root Elemenmt Name is from config [" + name + "]");

		// System.out.println("1st child element :" +
		// document.getChildNodes());
		NodeList nodes = document.getElementsByTagName("*");

		if (nodes.getLength() == 0) {

			System.out.println("Nodes are empty" + nodes);
		}

		else {

			for (int i = 0; i < nodes.getLength(); i++) {

				Node l_crnt_node = nodes.item(i);
				System.out.println("\nCurrent Element :" + nodes.item(i).getNodeName());
				if (l_crnt_node.getNodeType() == Node.ELEMENT_NODE) {
					if (l_crnt_node.getLocalName().contains("BODY")) {
						System.out.println("BODY found continued");
						continue;
					}
					if (l_crnt_node.getLocalName().contains("HEADER")) {
						System.out.println("HEADER found continued");
						continue;
					}

					// verify the tag has c
					if (l_crnt_node.getChildNodes().getLength() > 1) {
						/*
						 * System.out.println("Node  [" +
						 * l_crnt_node.getLocalName() +
						 * "] has child nodes [" +
						 * l_crnt_node.getChildNodes().getLength() +
						 * "] so continued");
						 */
						continue;
					}
					// findind the values from map

					System.out.println("Key:[" + l_crnt_node.getLocalName().trim() + "] Value ["
							+ l_crnt_node.getTextContent().trim() + "]");

				}
			}

		}

		/*} catch (Exception e) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!!!!");
			e.printStackTrace();
			Response e_response = o_customexception.riseexception(e.getMessage(), 400, "Error while Processing XML");
		
			throw new WebApplicationException(e_response);
		
		}*/
		return document;
	}
}

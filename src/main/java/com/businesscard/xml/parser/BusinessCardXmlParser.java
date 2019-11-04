package com.businesscard.xml.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.businesscard.exception.BusinessCardNotFoundException;
import com.businesscard.exception.BusinessCardXmlParseException;
import com.businesscard.model.BusinessCard;

@Component
public class BusinessCardXmlParser {
	private static final Logger log = LoggerFactory.getLogger(BusinessCardXmlParser.class);

	private static final String SEARCH_FOR = "The search for ";
	private static final String NOT_FOUND = " not found.";
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String LANGUAGE = "language";
	private static final String REGEX = ":";

	@Value("${business.card.xml.file}")
	private String businessCardXmlFile;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public BusinessCard getById(String id) throws Exception{
		Node nodeById = getNodeById(id);
		if (nodeById == null) {
			log.debug(SEARCH_FOR + id + NOT_FOUND);
			throw new BusinessCardNotFoundException(id + NOT_FOUND);
		}
		
		return buildBusinessCardInfo(nodeById);
	}
	
	public BusinessCard getByName(String name) throws Exception{
		Node nodeByName = getNodeByName(name);
		if (nodeByName == null) {
			log.debug(SEARCH_FOR + name + NOT_FOUND);
			throw new BusinessCardNotFoundException(name + NOT_FOUND);
		}
			
		return buildBusinessCardInfo(nodeByName);
	}
	
	public List<BusinessCard> searchName(String name) {
		List<BusinessCard> cards = new ArrayList<>();
		NodeList nodes = searchNodeName(name);
		if (nodes == null) {
			log.debug(SEARCH_FOR + name + NOT_FOUND);
			throw new BusinessCardNotFoundException(name + NOT_FOUND);
		}
		
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i	);
			cards.add(buildBusinessCardInfo(node));
		}
		return cards;
	}
	
	public Node getNodeById(String id) throws BusinessCardXmlParseException {
		try {
			Document document = loadAndParseXml();
			XPath xpath = XPathFactory.newInstance().newXPath();

			String expr = "//businesscard/participant[@value='" + id + "']/parent::businesscard";
			return (Node) xpath.compile(expr).evaluate(document, XPathConstants.NODE);
		} catch(XPathExpressionException e) {
			log.error("Error searching for id {} with error {}", id, e.getMessage());
			throw new BusinessCardXmlParseException("Encountered error when searching for id." + e.getMessage());
		}
	}
	
	public Node getNodeByName(String name) throws BusinessCardXmlParseException {
		try {
			Document document = loadAndParseXml();
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			String expr = "//name[@name='" + name + "']/ancestor::businesscard";
			return (Node) xpath.compile(expr).evaluate(document, XPathConstants.NODE);
		} catch(XPathExpressionException e) {
			log.error("Error searching for name {} with error {}", name, e.getMessage());
			throw new BusinessCardXmlParseException("Encountered error when searching for name." + e.getMessage());
		}
	}

	public NodeList searchNodeName(String name) throws BusinessCardXmlParseException{
		try {
			Document document = loadAndParseXml();
			XPath xpath = XPathFactory.newInstance().newXPath();			

			String expr = "//name[contains(@name,'" + name + "')]/ancestor::businesscard";
			return (NodeList) xpath.compile(expr).evaluate(document, XPathConstants.NODESET);
		} catch(XPathExpressionException e) {
			log.error("Error searching for name {} with error {}", name, e.getMessage());
			throw new BusinessCardXmlParseException("Encountered error when searching for name." + e.getMessage());
		}
	}
	
	private Document loadAndParseXml() throws BusinessCardXmlParseException{
		try {
			log.debug("Reading xml file. " + businessCardXmlFile);
			Resource resouce = resourceLoader.getResource("classpath:" + businessCardXmlFile);
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(resouce.getInputStream());
		} catch(ParserConfigurationException | SAXException | IOException e) {
			log.error("Error parsing xml file. {}", e.getMessage());
			throw new BusinessCardXmlParseException("Error parsing xml file." + e.getMessage());
		}
	}
	
	private BusinessCard buildBusinessCardInfo(Node node) {
		BusinessCard card = new BusinessCard();
		if (node != null && node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i=0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if(childNode.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) childNode;
					extractElements(e, card);
				}
			}
		}
		return card;
	}

	private void extractElements(Element element, BusinessCard card) {
		if (element.hasChildNodes()) {
			//entity
			card.setCountryCode(element.getAttribute("countrycode"));

			NodeList childNodes = element.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) node;
					if ("name".equals(e.getNodeName())) {
						card.setName(e.getAttribute(NAME));
						card.setLanguage(e.getAttribute(LANGUAGE));
					}
				}
			}
		}else if("participant".equals(element.getNodeName())) {
			//participant
			String[] values = element.getAttribute(VALUE).split(REGEX);
			card.setIcd(values[0]);
			card.setEnterpriseNumber(values[1]);
		}
	}
}

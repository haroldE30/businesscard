package com.businesscard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.businesscard.model.BusinessCard;
import com.businesscard.xml.parser.BusinessCardXmlParser;

@Service
public class BusinessCardServiceImpl implements BusinessCardService {
	
	private BusinessCardXmlParser xmlParser;
	
	@Autowired
	public BusinessCardServiceImpl(BusinessCardXmlParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	@Override
	public BusinessCard findById(String icdAndEnterpriseNumber) throws Exception{
		return xmlParser.getById(icdAndEnterpriseNumber);
	}
	
	@Override
	public BusinessCard findByName(String name) throws Exception{
		return xmlParser.getByName(name);
	}

	@Override
	public List<BusinessCard> searchName(String name) throws Exception{
		return xmlParser.searchName(name);
	}
}

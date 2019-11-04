package com.businesscard.service;

import java.util.List;

import com.businesscard.model.BusinessCard;

public interface BusinessCardService {
	
	BusinessCard findById(String icdAndEnterpriseNumber) throws Exception; 
	
	BusinessCard findByName(String name) throws Exception;
	
	List<BusinessCard> searchName(String name) throws Exception;
	
}

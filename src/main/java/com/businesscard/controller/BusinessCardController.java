package com.businesscard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.businesscard.exception.BusinessCardNotFoundException;
import com.businesscard.exception.BusinessCardXmlParseException;
import com.businesscard.service.BusinessCardService;
import com.businesscard.util.JSONModel;

@RestController
public class BusinessCardController {
	
	@Autowired
	private BusinessCardService service;
	
	@GetMapping("/business-card/{icd}/{enum}")
	public JSONModel getById(@PathVariable("icd") String icd, @PathVariable("enum") String enterpriseNumber) throws Exception{
		String param = icd + ":" + enterpriseNumber;
		return new JSONModel.Builder<>(service.findById(param)).build();
	}
	
	@GetMapping("/business-card/search/{name}")
	public JSONModel searchByName(@PathVariable("name") String name) throws Exception{
		return new JSONModel.Builder<>(service.searchName(name)).build();
	}
	
	@GetMapping("/business-card/{name}")
	public JSONModel getByName(@PathVariable("name") String name) throws Exception{
		return new JSONModel.Builder<>(service.findByName(name)).build();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(BusinessCardNotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException() {
		return new ResponseEntity<>("Business card not found.", HttpStatus.NOT_FOUND);
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BusinessCardXmlParseException.class)
	public ResponseEntity<Object> handleXmlParseException() {
		return new ResponseEntity<>("Error parsing business card xml file.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

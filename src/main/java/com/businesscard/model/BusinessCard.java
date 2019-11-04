package com.businesscard.model;

public class BusinessCard {
	private String icd;
	private String enterpriseNumber;
	private String countryCode;
	private String name;
	private String language;
	
	public String getIcd() {
		return icd;
	}
	public void setIcd(String icd) {
		this.icd = icd;
	}
	public String getEnterpriseNumber() {
		return enterpriseNumber;
	}
	public void setEnterpriseNumber(String enterpriseNumber) {
		this.enterpriseNumber = enterpriseNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String counterCode) {
		this.countryCode = counterCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String toString() {
		return "ICD=" + getIcd() +
				", Enterprise Number=" + getEnterpriseNumber() +
				", Country Code=" + getCountryCode() + 
				", Name=" + getName() +
				", Language=" + getLanguage();
	}
}

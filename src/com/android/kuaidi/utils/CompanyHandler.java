package com.android.kuaidi.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.android.kuaidi.Company;

public class CompanyHandler extends DefaultHandler{

	private String tag;
	private Company company;
	private List<Company> companies;
	
	public List<Company> getCompanies() {
		return companies;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		
		if(!this.tag.equals("")) {
			String data = new String(ch, start, length);
			if(this.tag.equals("name"))
				company.setName(data);
			if(this.tag.equals("code"))
				company.setCode(data);
			if(this.tag.equals("image"))
				company.setIcon(data);
		}
	}	

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		
		if(localName.equals("company")) {
			companies.add(company);
		}
		this.tag = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		
		this.companies = new ArrayList<Company>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals("company")) {
			company = new Company();
		}
		this.tag = localName;
	}
	
}

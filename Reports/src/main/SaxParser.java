package main;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser extends DefaultHandler {
	
	String thisElement="campaignID";
	public String elementValue="";
	Boolean flag=false;
	private StringBuffer currentTagValue = new StringBuffer(1024);
	
	public String getElementValue()
	{
		return elementValue;
	}
	
	@Override 
	public void startDocument() throws SAXException { 
	//  System.out.println("Start parse XML..."); 
	} 
	
	@Override 
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException { 
		 
		if (qName.equals(thisElement))  
			{
		//	System.out.println("Start element XML..."); 
		//	 System.out.println("ThisElement is: "+thisElement); 
		//	 System.out.println("Qname is: "+qName); 
			 currentTagValue.setLength(0);
			flag= true;
			}
			} 
	
	@Override 
	public void characters(char[] ch, int start, int length) throws SAXException { 
		
		if (flag==true)
		{
		 currentTagValue.append(ch, start, length);
		 elementValue = currentTagValue.toString();
		// System.out.println("ElementValue is: "+elementValue); 
		 flag=false;
		 
		}
		
		
	}
	
	@Override 
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException { 
		
	//	System.out.println("ElementValue from endElement is: "+elementValue); 		
		flag=false;
			
		} 
	
	@Override 
	public void endDocument() { 
	 // System.out.println("Stop parse XML..."); 
	//  System.out.println("ElementValue from endDOCUMENT is: "+elementValue); 	
	} 
	
	public String getTagValue(String xml, String tag)
	{
		try{
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
	InputSource is = new InputSource(new StringReader(xml));

	SaxParser handler = new SaxParser();
	thisElement=tag;
	//System.out.println(thisElement);
	 saxParser.parse(is, handler);
	
	 
	 return handler.getElementValue();
	}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
}

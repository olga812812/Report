package main;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomParser {
	
	public Document parseXML(String xml)
	{
		try{
		InputSource is = new InputSource(new StringReader(xml));
		//is.setEncoding("windows-1251");
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		doc.getDocumentElement().normalize();
		return doc;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public String getTagValue(String xml, String tagName)
	{
		
			return parseXML(xml).getElementsByTagName(tagName).item(0).getTextContent();
			
	}
	
	public String  getNextTagValueByTextInTag(String xml, String tagName, String tagText, String nextTagName)
	{
	
		String result="";
		NodeList nList = parseXML(xml).getElementsByTagName(tagName);
		for (int i=0; i<nList.getLength(); i++)
		{
			
		if((nList.item(i).getTextContent()).equals(tagText)) 
			{
			NodeList childs=nList.item(i).getParentNode().getChildNodes();
			for(int j=0; j<childs.getLength(); j++)
			{
				if(childs.item(j).getNodeName().equals(nextTagName)) result=childs.item(j).getTextContent();
			}
			
			}
		}
		
		return result;
		
	}
	
	public ArrayList<String> getSeveralTagValues(String xml, String tagName)
	{
		    ArrayList<String> result = new ArrayList<String>();
		
			NodeList nList = parseXML(xml).getElementsByTagName(tagName);
			for (int i=0; i<nList.getLength(); i++)
			{
				result.add(nList.item(i).getTextContent());
				
			}
			return result;
			
	}

}

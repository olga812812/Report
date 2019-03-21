package main;

import java.net.HttpURLConnection;
import java.util.*;

public class ReportSMON implements GeneralReport{
	String reportSmonUrl="&objectName=owner&objectID=0&day=31.01.19&criteria=hour&adNetworkID=0&isXML=1";
	HttpURLConnection connection;
	Common commonMethods = new Common();
	DomParser domParser = new DomParser();
	Map<String,String> housrAndImpressions= new TreeMap<String, String>();

	@Override
	public void startReport() {
		
		connection = commonMethods.makeHttpRequest(adfox.getReportCommonURL()+getReportSmonUrl());
		String report=commonMethods.getResponse(connection);
		System.out.println(report);
		System.out.println(adfox.getReportCommonURL()+getReportSmonUrl());
		ArrayList<String> hours = domParser.getSeveralTagValues(report, "criteria");
		System.out.println(hours);
		for (int i=0; i<hours.size(); i++) {
			housrAndImpressions.put(hours.get(i), domParser.getNextTagValueByTextInTag(report, "criteria", hours.get(i), "impressions"));
		}
		System.out.println(housrAndImpressions);
		
		
	}
	
	public String getReportSmonUrl () {
		return reportSmonUrl;
		
	}

}

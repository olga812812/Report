package main;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdfoxData {
	Common com =  new Common();
	String response;
	DomParser domPars = new DomParser();
	SaxParser saxPars = new SaxParser();
	String pwd = com.loadProperties().getProperty("adfoxPwd");
	String login = com.loadProperties().getProperty("adfoxLogin");
	String apiCommonURL="https://api.adfox.ru/v1/API.php?loginAccount="+login+"&loginPassword="+com.getHashCode(pwd);
	String reportCommonURL="https://login.adfox.ru/commonReportsOutputForm.php?loginAccount="+login+"&loginPassword="+com.getHashCode(pwd);
	String urlBannerInfo = "&object=account&action=list&actionObject=banner&actionObjectID=2031551";
	String urlCampaignInfo= "&object=account&action=list&actionObject=campaign&superCampaignID=41601";
	String urlFlightTargetingInfo = "&object=campaign&action=info&actionObject=targeting&objectID=";
	String urlFlightPlacementInfo = "&object=campaign&action=info&actionObject=placement2&dataType=campaignSites&isOn=on&objectID=";
	
	public String getReportCommonURL() {
		return reportCommonURL;
	}
	
	public String getApiCommonURL(){
		return apiCommonURL;
	}
	
	
	public ArrayList<String> getCampaignFlights()
	{
		ArrayList<String> flights = new ArrayList<String>();
		HttpURLConnection connection = com.makeHttpRequest(apiCommonURL+urlCampaignInfo);
		response = com.getResponse(connection);
		int totalRows  = Integer.parseInt(domPars.getTagValue(response, "total_rows"));
		int rowsOnFirstPage  = Integer.parseInt(domPars.getTagValue(response, "rows"));
		
		com.closeConn(connection);
		
		for (int i=0; i<totalRows; i=i+rowsOnFirstPage)
		{
			if (i==0) flights.addAll(domPars.getSeveralTagValues(response, "ID"));
			else
			{
			connection = com.makeHttpRequest(apiCommonURL+urlCampaignInfo+"&offset="+i);
			response = com.getResponse(connection);
			flights.addAll(domPars.getSeveralTagValues(response, "ID"));
			com.closeConn(connection);
			}
		}
		
		
			
		return flights;
		
	}
	
	public Map<String, String> getFlightIPTargering(ArrayList<String> flights)
	{
		Map<String, String> mapFlightsIPTargeting = new HashMap<String, String>();
		HttpURLConnection connection;
		
		for(int i=0; i<flights.size(); i++)
	//	for(int i=0; i<3; i++)
		{
			connection = com.makeHttpRequest(apiCommonURL+urlFlightTargetingInfo+flights.get(i));
			response = com.getResponse(connection);
			mapFlightsIPTargeting.put(flights.get(i), domPars.getNextTagValueByTextInTag(response, "name", "ips", "description"));
			com.closeConn(connection);
		}
		
		System.out.println(mapFlightsIPTargeting);
		return mapFlightsIPTargeting;
		
	}
	
	public Map<String, ArrayList<String>> getFlightPlacement(ArrayList<String> flights)
	{
		Map<String, ArrayList<String>> mapFlightsPlacementTargeting = new HashMap<String, ArrayList<String>>();
		HttpURLConnection connection;
		
		for(int i=0; i<flights.size(); i++)
				//for(int i=0; i<3; i++)
				{
					connection = com.makeHttpRequest(apiCommonURL+urlFlightPlacementInfo+flights.get(i));
					response = com.getResponse(connection);
					mapFlightsPlacementTargeting.put(flights.get(i), domPars.getSeveralTagValues(response, "websiteName"));
					com.closeConn(connection);
				}
				
				System.out.println(mapFlightsPlacementTargeting);
				return mapFlightsPlacementTargeting;
				
		
	}

}

package main;

import java.net.HttpURLConnection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ReportCPMON implements GeneralReport{
	Common com = new Common();
	HttpURLConnection connection;
	String response;
	DomParser domPars = new DomParser();
	ArrayList<String> flightsOnOnePage;
	Map<String, ArrayList<String>> allFlights=new HashMap<>();
	String[] assistantsArray = com.loadProperties().getProperty("assistants").split(",");
	Integer priorityBorder = Integer.valueOf(com.loadProperties().getProperty("priority"));
	List<String> assistants = Arrays.asList(assistantsArray);
	HashSet<String> superCampaignIdSet = new HashSet<>();
	ArrayList<String> activeSuperCampaigns = new ArrayList<>();
	int numberOfFlightOnPage=100;
	String getAllCampaingsUrl = adfox.getApiCommonURL()+adfox.getAllCampaignUrl()+"&limit="+numberOfFlightOnPage;
	String getSuperCampaignInfoUrl = adfox.getApiCommonURL()+adfox.getSuperCampaignInfoUrl();
	
	private void makeConnectionAndGetResponce(String url){
		System.out.println(url);
		response="";
		connection=com.makeHttpRequest(url);
		response=com.getResponse(connection);
		
	}

	@Override
	public void startReport() {
		// TODO Auto-generated method stub		
		System.out.println("Start report: "+LocalTime.now());
		System.out.println(getAllCampaingsUrl);
		System.out.println(assistants);		
		makeConnectionAndGetResponce(getAllCampaingsUrl);	
		com.closeConnection(connection);
		int totalFlights  = Integer.parseInt(domPars.getTagValue(response, "total_rows"));		
		System.out.println(totalFlights);
		LocalTime  startTime= LocalTime.now();
		System.out.println("Start get flights: "+ startTime);
		for(int i=0; i<totalFlights; i=i+numberOfFlightOnPage){			
			makeConnectionAndGetResponce(getAllCampaingsUrl+"&offset="+i);	
			com.closeConnection(connection);
			System.out.println("i="+i);
			flightsOnOnePage=domPars.getSeveralTagValues(response, "ID");
			for (String flightID: flightsOnOnePage) {
				String flightStatus=domPars.getNextTagValueByTextInTag(response, "ID", flightID, "status");	
				String flightAssistantID=domPars.getNextTagValueByTextInTag(response, "ID", flightID, "assistantID");
				Integer flightPriority = Integer.valueOf(domPars.getNextTagValueByTextInTag(response, "ID", flightID, "level"));	
				
				if(flightStatus.equals("0")&&assistants.contains(flightAssistantID)&&flightPriority<=priorityBorder) {							
					String superCampaignID=domPars.getNextTagValueByTextInTag(response, "ID", flightID, "superCampaignID");																
					ArrayList<String> flightInfo= new ArrayList<>();
					flightInfo.add(superCampaignID);
					flightInfo.add(flightAssistantID);
					flightInfo.add(flightPriority.toString());						
					System.out.println(superCampaignID+" "+flightAssistantID+" "+flightPriority);
					allFlights.put(flightID, flightInfo);
					superCampaignIdSet.add(superCampaignID);
					
				}
				
			}			
			System.out.println(allFlights);
			System.out.println(superCampaignIdSet);						
			
		}
		System.out.println("Start get flights "+startTime+" End get flights: "+LocalTime.now());
		
		for (String superCampaignID: superCampaignIdSet) {
			makeConnectionAndGetResponce(getSuperCampaignInfoUrl+superCampaignID);	
			com.closeConnection(connection);
			String superCampaignStatus=domPars.getNextTagValueByTextInTag(response, "ID", superCampaignID, "status");	
			if (superCampaignStatus.equals("0")) activeSuperCampaigns.add(superCampaignID);
		}
		
		System.out.println(superCampaignIdSet);
		
		for(String flightId: allFlights.keySet()) {
			if (activeSuperCampaigns.contains((allFlights.get(flightId)).get(0))) System.out.println(flightId+" and info "+allFlights.get(flightId));
		}
		
		
		
		
	}

}

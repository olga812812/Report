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
	private Common com = new Common();
	private HttpURLConnection connection;
	private String response;
	private int totalFlights;
	private LocalTime  startGettingAllFlightsTime;
	private DomParser domPars = new DomParser();
	private ArrayList<String> flightsOnOnePage;
	private Map<String, ArrayList<String>> allFlights=new HashMap<>();
	private String[] assistantsArray = com.loadProperties().getProperty("assistants").split(",");
	private Integer priorityBorder = Integer.valueOf(com.loadProperties().getProperty("priority"));
	private List<String> assistants = Arrays.asList(assistantsArray);
	private HashSet<String> superCampaignIdSet = new HashSet<>();
	private ArrayList<String> activeSuperCampaigns = new ArrayList<>();
	private int numberOfFlightOnPage=100;
	private String getAllCampaingsUrl = adfox.getApiCommonURL()+adfox.getAllCampaignUrl()+"&limit="+numberOfFlightOnPage;
	private String getSuperCampaignInfoUrl = adfox.getApiCommonURL()+adfox.getSuperCampaignInfoUrl();
	
	private void print(Object str) {
		com.print(str);
	}
	
	private LocalTime getNowTime(){
		return LocalTime.now();
	}
	
	private void getTotalNumberOfFlights() {
		makeConnectionAndGetResponce(getAllCampaingsUrl);	
		closeConnection();
		totalFlights  = Integer.parseInt(domPars.getTagValue(response, "total_rows"));		
		print(totalFlights);
		
	}
	
	private void makeConnectionAndGetResponce(String url){
		System.out.println(url);
		response="";
		connection=com.makeHttpRequest(url);
		response=com.getResponse(connection);
		
	}
	
	private void closeConnection(){
		com.closeConnection(connection);
	}
	
	private void setStartTimeOfGettingAllFlights() {
		startGettingAllFlightsTime=getNowTime();
	}
	
	private void getActiveFlightsWithNeededPriorityAndAssistant(){
		for(int i=0; i<totalFlights; i=i+numberOfFlightOnPage){			
			makeConnectionAndGetResponce(getAllCampaingsUrl+"&offset="+i);	
			closeConnection();
			print("i="+i);
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
					print(superCampaignID+" "+flightAssistantID+" "+flightPriority);
					allFlights.put(flightID, flightInfo);
					superCampaignIdSet.add(superCampaignID);
					
				}
				
			}			
			print(allFlights);
			print(superCampaignIdSet);						
			
		}
	}
	
	private void getActiveSuperCampaigns(){
		for (String superCampaignID: superCampaignIdSet) {
			makeConnectionAndGetResponce(getSuperCampaignInfoUrl+superCampaignID);	
			closeConnection();
			String superCampaignStatus=domPars.getNextTagValueByTextInTag(response, "ID", superCampaignID, "status");	
			if (superCampaignStatus.equals("0")) activeSuperCampaigns.add(superCampaignID);
		}
	}
	
	private void getFlightsWithActiveSuperCampaign(){
		for(String flightId: allFlights.keySet()) {
			if (activeSuperCampaigns.contains((allFlights.get(flightId)).get(0))) print(flightId+" and info "+allFlights.get(flightId));
		}
	}

	@Override
	public void startReport() {
		// TODO Auto-generated method stub		
		print("Start report: "+getNowTime());
		print("Report url: "+getAllCampaingsUrl);
		print("Assistants from property file: "+assistants);	
		getTotalNumberOfFlights();		
		setStartTimeOfGettingAllFlights();		
		getActiveFlightsWithNeededPriorityAndAssistant();
		print("Start get flights "+startGettingAllFlightsTime+" End get flights: "+getNowTime());
		getActiveSuperCampaigns();		
		print(superCampaignIdSet);
		getFlightsWithActiveSuperCampaign();
		
	}

}

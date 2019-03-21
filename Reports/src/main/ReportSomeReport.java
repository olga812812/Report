package main;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

public class ReportSomeReport implements GeneralReport{
	
	public void checkString()
	{
		String test = "Tvigle.ru TEST tg  tyuj";
		if (test.toLowerCase().matches("(.*)(test|����)(.*)"))   System.out.println("It's test site");
	}
	
	public void checkFlightsForTargetingErrors(ArrayList<String> flights, Map<String, String> mapFlightsIPTargeting, Map<String, ArrayList<String>> mapFlightsPlacementTargeting)
	{
		ArrayList<String> flightSites;
		ArrayList<String> flightsWithWrongTargeting = new ArrayList<String>();
		
		System.out.println("Flights are: "+flights);
		for (int i=0; i<flights.size(); i++)
				{
					flightSites = mapFlightsPlacementTargeting.get(flights.get(i));
					for (int j=0; j<flightSites.size(); j++)
					{
						
						if (!flightSites.get(j).toLowerCase().matches("(.*)(test|����)(.*)")) 
							{
							if (!mapFlightsIPTargeting.get(flights.get(i)).matches("(.*)(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(.*)")) flightsWithWrongTargeting.add(flights.get(i));
							
							}
					}
				}
		
		System.out.println("Wrong flights are: "+flightsWithWrongTargeting);
		for (int i=0; i<flightsWithWrongTargeting.size(); i++)
		{
			System.out.println("Flight IP targeting is: "+mapFlightsIPTargeting.get(flightsWithWrongTargeting.get(i)));
			System.out.println("Flight Placement targeting is: "+mapFlightsPlacementTargeting.get(flightsWithWrongTargeting.get(i)));
		}
	}
	
	
	public void startReport()
	{
		AdfoxData aD = new AdfoxData();
		Common com =  new Common();
		DomParser domPars = new DomParser();
		SaxParser saxPars = new SaxParser();
		
	
		String urlBannerInfo = "&object=account&action=list&actionObject=banner&actionObjectID=2031551";
	//	String urlCampaignInfo= "&object=account&action=list&actionObject=campaign&superCampaignID=41601";
		
	
		HttpURLConnection connection = com.makeHttpRequest(adfox.getApiCommonURL()+urlBannerInfo);
		String response = com.getResponse(connection);
		System.out.println("From DOM campaignID is: "+domPars.getTagValue(response, "campaignID"));
		System.out.println("From SAX campaignID is: "+saxPars.getTagValue(response, "campaignID"));
		com.closeConn(connection);
		
		ArrayList<String> flights = new ArrayList<String>();
		flights = aD.getCampaignFlights();
		//aD.getFlightIPTargering(flights);
		//aD.getFlightPlacement(flights);
		checkFlightsForTargetingErrors(flights, aD.getFlightIPTargering(flights), aD.getFlightPlacement(flights));
	//	rM.checkString();
	}

}

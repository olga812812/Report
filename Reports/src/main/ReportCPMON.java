package main;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ReportCPMON implements GeneralReport{
	Common com = new Common();
	HttpURLConnection connection;
	String response;
	DomParser domPars = new DomParser();

	@Override
	public void startReport() {
		// TODO Auto-generated method stub
		String getAllCampaingsUrl = adfox.getApiCommonURL()+adfox.getAllCampaignUrl();
		System.out.println(getAllCampaingsUrl);
		connection=com.makeHttpRequest(getAllCampaingsUrl);
		response=com.getResponse(connection);
		int totalPages  = Integer.parseInt(domPars.getTagValue(response, "total_pages"));
		int numberOfFlightsOnPage  = Integer.parseInt(domPars.getTagValue(response, "limit"));
		System.out.println(totalPages+" "+numberOfFlightsOnPage);
		for(int i=0; i<totalPages; i=i+numberOfFlightsOnPage){
			
		}
		
		
		
	}

}

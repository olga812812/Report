package main;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ReportTVM implements GeneralReport {
	
	
	Common com = new Common();
	String reportTVMUrl;
	HttpURLConnection conn;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	DomParser domPars = new DomParser();
	
	public Date getDate(int daysAgo)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -daysAgo);
		return cal.getTime();
	}
	
	public String getReportTVMUrl(int daysAgo)
	{
		return "&period=&startDate="+dateFormat.format(getDate(daysAgo))+"&endDate="+dateFormat.format(getDate(daysAgo))+"&x=0&y=0&criteria=website&isExcel=&isExcel2007=&isNewSearch=on&objectName=owner&ignoreStartDate=off&isXML=1";
		
	}
	
	public Map<String, String> getImprForEachSite(ArrayList<String> sites, String report)
	{
		Map<String, String> mapSitePlusImpr = new TreeMap<String, String>();
		for (int i=0; i<sites.size(); i++)
		{
			mapSitePlusImpr.put(sites.get(i), domPars.getNextTagValueByTextInTag(report, "criteria", sites.get(i), "loads"));
		}
		return mapSitePlusImpr;
	}
	
	public void startReport()
	{
		
		ArrayList<String> sitesFromReportYesterday = new ArrayList<String>();
		ArrayList<String> sitesFromReportDayBeforeYesterday = new ArrayList<String>();
		Map<String, String> mapYesterday = new TreeMap<String, String>();
		Map<String, String> mapDayBeforeYesterday = new TreeMap<String, String>();
		int imprYesterday, imprDayBeforeYesterday, increase;
		int percent=50;
		int minValue=7;
		
		conn = com.makeHttpRequest(adfox.getReportCommonURL()+getReportTVMUrl(1));
		String reportYesterday = com.getResponse(conn);
		com.closeConnection(conn);
		conn = com.makeHttpRequest(adfox.getReportCommonURL()+getReportTVMUrl(2));
		String reportDayBeforeYesterday = com.getResponse(conn);
		com.closeConnection(conn);
		sitesFromReportYesterday=domPars.getSeveralTagValues(reportYesterday, "criteria");
		sitesFromReportDayBeforeYesterday=domPars.getSeveralTagValues(reportDayBeforeYesterday, "criteria");
		mapYesterday=getImprForEachSite(sitesFromReportYesterday, reportYesterday);
		mapDayBeforeYesterday=getImprForEachSite(sitesFromReportDayBeforeYesterday, reportDayBeforeYesterday);
		
		for (String mapYesterdayKey: mapYesterday.keySet())
		{
			for(String mapDayBeforeYesterdayKey: mapDayBeforeYesterday.keySet())
			{
				if(mapYesterdayKey.equals(mapDayBeforeYesterdayKey))
				{
					imprYesterday=Integer.parseInt(mapYesterday.get(mapYesterdayKey));
					imprDayBeforeYesterday=Integer.parseInt(mapDayBeforeYesterday.get(mapYesterdayKey));
					if (imprDayBeforeYesterday==0) increase=100; else increase=(int)Math.round((double)imprYesterday*100/(double)imprDayBeforeYesterday-100);
					
					if(imprYesterday>imprDayBeforeYesterday&&imprYesterday>minValue|imprDayBeforeYesterday>minValue&&increase>=percent)
					System.out.println(mapYesterdayKey+" "+imprDayBeforeYesterday+" "+imprYesterday+" "+increase);
				}
			}
		}
		
		
	}

}

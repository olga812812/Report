package test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

import main.DomParser;
import main.ReportTVM;

import static  org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomParser.class)



public class TestReportTVMMethod_getImprForEachSite {
	
	ReportTVM reportTVMObject = new ReportTVM();
	ArrayList<String> sites;
	Map<String,String> mapSample;
	String report="<xml>some report</xml>";
	
	@Test
	public void checkSeveralSites() {
		createArrayListSites();
		createMapSampleWithSitesAndImpressions();
		PowerMockito.replace(PowerMockito.method(DomParser.class, "getNextTagValueByTextInTag")).with(new InvocationHandler() {
			@Override 
			public Object invoke(Object proxy, Method getNextTagValueByTextInTag, Object[] args) throws Throwable {
				if (args[2].equals("site1")) return "1";
				if (args[2].equals("site2")) return "2";
				if (args[2].equals("site3")) return "3"; else return "5";	             	
			}
		});
		assertEquals(mapSample, reportTVMObject.getImprForEachSite(sites, report));				
	}
	
	@Test
	public void checkEmptySitesArray() {
		sites= new ArrayList<String>();
		assertEquals(new TreeMap<String,String>(), reportTVMObject.getImprForEachSite(sites, report));
	}
	
	@Test (expected=NullPointerException.class)
	
	public void checkNullInSites() {
		reportTVMObject.getImprForEachSite(null, report);
	}
	
	@Test (expected=NullPointerException.class)
	public void checkNullInReport() {
		createArrayListSites();
		reportTVMObject.getImprForEachSite(sites, null);
	}
	
	@Test (expected=NullPointerException.class)
	public void checkEmptyReport() {
		createArrayListSites();
		reportTVMObject.getImprForEachSite(sites, "");
	}
	
	
	private void createArrayListSites() {
		sites = new ArrayList<String>();
		sites.add("site1");
		sites.add("site2");
		sites.add("site3");
	}
	
	public void createMapSampleWithSitesAndImpressions() {
	    mapSample = new TreeMap<String,String>();
		mapSample.put("site1", "1");
		mapSample.put("site2", "2");
		mapSample.put("site3", "3");
		
	}

}

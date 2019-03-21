package main;


public class ReportMain {
	
	public static void createReport(String reportName) {
		// TODO Auto-generated method stub
		GeneralReport report;
		
		switch (reportName){
		case "TVM":        report=new  ReportTVM();
		                   break;  
		case "someReport": report=new ReportSomeReport();
		                   break; 
		case "SMON": 	   report=new ReportSMON();
						   break;
		case "CPMON": 	   report = new ReportCPMON();
						   break;
		default:           System.out.println("You didn't point report name. Try once again.");
						   report = null;
		                   break; 
		
		}
				
		report.startReport();
	
				
	}
		
	
	
	public static void main(String[] args) {
		
		createReport(args[0]);
		}		


}

package main;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Properties;
import java.util.Scanner;

public class Common {
	
	public  String getHashCode(String pwd)
	{
		StringBuffer hexString = new StringBuffer();
		MessageDigest md = null;
		
		try {
		    md = MessageDigest.getInstance("SHA-256");
		    byte[] hash=md.digest(pwd.getBytes());
		

		    for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
		    								}
          
			}
		catch (Exception ex) {ex.printStackTrace();}
		return hexString.toString();
	
	}
	
	public HttpURLConnection makeHttpRequest(String url)
	{
		try{
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		URL reqUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) reqUrl.openConnection();
		conn.setRequestMethod("GET");	
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
		conn.connect();
		return conn;
		}
		
		catch(Exception ex) 
		{ex.printStackTrace();
		 return null;
		}
	}
	
	public String getResponse(HttpURLConnection conn)
	{
		try{
			String resp="";
			Scanner in = new Scanner(conn.getInputStream());
			while(in.hasNextLine())
			{
				resp = resp + in.nextLine();
			}
			return resp;
		    }
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public void closeConn (HttpURLConnection conn)
	{
		conn.disconnect();
	}
	
	public Properties loadProperties() {
		Properties properties = new Properties();
		try {
		properties.load(new FileInputStream("reports.properties"));
		} 
		catch (Exception ex) {ex.printStackTrace();}
		return properties;
	}

}

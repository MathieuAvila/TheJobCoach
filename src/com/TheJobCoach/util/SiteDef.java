package com.TheJobCoach.util;

public class SiteDef {

	static String webAddress = "www.thejobcoach.fr:8080";
	
	static public String getAddress()
	{
		return webAddress;
	}
	
	static public void setAddress(String newAddress)
	{
		webAddress = newAddress;
	}
	
}

package com.TheJobCoach.webapp.util.client;

public interface IExtendedField 
{
	public boolean getIsDefault();	
	public boolean isValid();
	public void resetToDefault();
	public String getValue();
	public void setDefault(String value);
	public void setValue(String value);
}

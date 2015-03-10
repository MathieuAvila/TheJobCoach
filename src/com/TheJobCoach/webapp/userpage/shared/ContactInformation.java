package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;

public class ContactInformation implements Serializable {

	public static enum ContactStatus implements Serializable
	{
		 /** contact is accepted on both sides */
		CONTACT_OK,
		/** contact is requested to the other, and he/she has not yet accepted */
		CONTACT_REQUESTED,
		/** contact has requested me as a contact and is waiting my answer */
		CONTACT_AWAITING,
		/** no request nor requestor. Happens if not set yet */
		CONTACT_NONE ;

		private static final long serialVersionUID = 1115555324512443730L;
	}

	public static class Visibility implements Serializable
	{
		private static final long serialVersionUID = -8351118351873552581L;

		public Visibility(boolean document, boolean contact,
				boolean opportunity, boolean log)
		{
			super();
			this.document = document;
			this.contact = contact;
			this.opportunity = opportunity;
			this.log = log;
		}
		
		public Visibility()
		{
			this.document = false;
			this.contact = false;
			this.opportunity = false;
			this.log = false;
		}
		/** sharing documents */
		public boolean document;
		
		/** sharing external contacts */
		public boolean contact;
		
		/** sharing opportunities */
		public boolean opportunity;
		
		/** sharing logs, ONLY if opportunities is set */
		public boolean log;
	}
	
	private static final long serialVersionUID = 1115255324512443730L;

	public ContactStatus status;
	public String userName;
	public String firstName;
	public String lastName;
	
	public Visibility myVisibility;
	public Visibility hisVisibility;

	public boolean online;

	public ContactInformation(ContactStatus status, String userName,
			String firstName, String lastName, Visibility myVisibility,
			Visibility hisVisibility)
	{
		super();
		this.status = status;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.myVisibility = myVisibility;
		this.hisVisibility = hisVisibility;
	}

	public ContactInformation(String firstName, String lastName)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.myVisibility = new Visibility();
		this.hisVisibility = new Visibility();
		this.status = ContactStatus.CONTACT_NONE;
		this.userName = "";
	}

	public ContactInformation(String userName, String firstName, String lastName)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.myVisibility = new Visibility();
		this.hisVisibility = new Visibility();
		this.status = ContactStatus.CONTACT_NONE;
		this.userName = userName;
	}

	public ContactInformation()
	{
		this.myVisibility = new Visibility();
		this.hisVisibility = new Visibility();
		this.status = ContactStatus.CONTACT_NONE;
		this.userName = "";
		this.firstName = "";
		this.lastName = "";
	}
}


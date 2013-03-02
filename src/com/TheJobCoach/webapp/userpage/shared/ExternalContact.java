package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;

public class ExternalContact implements Serializable {

	private static final long serialVersionUID = 1115255124512443730L;

	public String ID;
	public String firstName;
	public String lastName;
	public String email;
	public String phone;
	public String personalNote;
	public String organization;
	public UpdatePeriod update;

	public ExternalContact(String iD, String firstName, String lastName,
			String email, String phone, String personalNote, String organization,
			UpdatePeriod update)
	{
		super();
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.personalNote = personalNote;
		this.organization = organization;
		this.update = update;
	}


	public ExternalContact()
	{
	}
	
	
}


package com.TheJobCoach.webapp.userpage.shared;

import java.io.Serializable;

import com.TheJobCoach.webapp.util.shared.SiteUUID;

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
		init(iD, firstName, lastName,
				email, phone, personalNote, organization,
				update);
	}

	public ExternalContact clone()
	{
		return new ExternalContact(ID, firstName, lastName,	email,  phone,  personalNote,  organization, update);
	}

	public ExternalContact()
	{
		init(SiteUUID.getDateUuid(), "", "",
				"", "", "", "",
				new UpdatePeriod());		
	}

	public ExternalContact(ExternalContact e)
	{
		this.init(e.ID, e.firstName, e.lastName,
				e.email, e.phone, e.personalNote, e.organization,
				new UpdatePeriod(e.update));
	}


	private void init(String iD, String firstName,
			String lastName, String email, String phone,
			String personalNote, String organization,
			UpdatePeriod updatePeriod) {
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.personalNote = personalNote;
		this.organization = organization;
		this.update = new UpdatePeriod(updatePeriod);
	}


}


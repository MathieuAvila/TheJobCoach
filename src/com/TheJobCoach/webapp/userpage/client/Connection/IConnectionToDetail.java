package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;

public interface IConnectionToDetail
{
	void toDetail(ContactInformation connectionUser);
	void toConnections();
}

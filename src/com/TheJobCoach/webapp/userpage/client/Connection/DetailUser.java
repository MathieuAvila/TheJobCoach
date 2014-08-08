package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class DetailUser extends DetailPanel {

	public DetailUser(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
		Grid grid = new Grid(10,2);
		grid.setWidget(0, 0, new Label(lang._TextName()));
		grid.setWidget(1, 0, new Label(lang._TextName()));
		grid.setWidget(2, 0, new Label(lang._TextName()));
		grid.setWidget(3, 0, new Label(lang._TextName()));
		add(grid);
	}

	void getAllContent()
	{
	}

	@Override
	public void showPanelDetail()
	{
		getAllContent();
	}
}

package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.Account.LangAccount;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class DetailUser extends DetailPanel implements ClientUserValuesUtils.ReturnValue {

	ClientUserValuesUtils values = null;
	final static LangAccount langAccount = GWT.create(LangAccount.class);

	Label labelTitle = new Label();
	Label labelStatus = new Label();
	Label labelKeywords = new Label();
	
	Label createLabel(String t)
	{
		Label result = new Label(t);
		result.setStyleName("label-status-ok-c");
		return result;
	}
	
	public DetailUser(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
		values = new ClientUserValuesUtils(RootPanel.get(), contactId);
		showPanelDetail();
	}
	
	boolean loaded = false;
	
	@Override
	public void showPanelDetail()
	{
		if (loaded) return;
		loaded = true;
		values.preloadValueList("ACCOUNT", this);
		Grid grid = new Grid(10,3);
		grid.setWidget(0, 0, createLabel(langConnection._TextFirstName()));
		grid.setWidget(1, 0, createLabel(langConnection._TextLastName()));
		grid.setWidget(2, 0, createLabel(langConnection.jobTitle()));
		grid.setWidget(3, 0, createLabel(langConnection.actualStatus()));
		grid.setWidget(4, 0, createLabel(langConnection.skills()));
		
		grid.setWidget(0, 1, new HorizontalSpacer("3em"));
		
		grid.setWidget(0, 2, new Label(connectionUser.firstName));
		grid.setWidget(1, 2, new Label(connectionUser.lastName));
		grid.setWidget(2, 2, labelTitle);
		grid.setWidget(3, 2, labelStatus);
		grid.setWidget(4, 2, labelKeywords);
		add(grid);
	}

	@Override
	public void notifyValue(boolean set, String key, String value)
	{
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_TITLE))
		{
			labelTitle.setText(value);
		}
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_STATUS))
		{
			labelStatus.setText(langAccount.accountStatusMap().get("accountStatusMap_" + value));
		}
		if (key.equals(UserValuesConstantsAccount.ACCOUNT_KEYWORDS))
		{
			labelKeywords.setText(value);
		}
	}
}

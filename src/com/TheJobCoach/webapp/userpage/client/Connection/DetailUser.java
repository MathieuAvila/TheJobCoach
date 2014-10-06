package com.TheJobCoach.webapp.userpage.client.Connection;

import com.TheJobCoach.webapp.userpage.client.Account.LangAccount;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.client.ClientUserValuesUtils;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.UserImageHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class DetailUser extends DetailPanel implements ClientUserValuesUtils.ReturnValue {

	ClientUserValuesUtils values = null;
	final static LangAccount langAccount = GWT.create(LangAccount.class);

	Label labelTitle = new Label();
	Label labelStatus = new Label();
	Label labelKeywords = new Label();
	Grid grid = new Grid(10,3);
	
	public static final int ROW_FIRSTNAME = 0; 
	public static final int ROW_LASTNAME = 1; 
	public static final int ROW_TITLE = 2; 
	public static final int ROW_STATUS = 3; 
	public static final int ROW_KEYWORDS = 4; 
	
	Label createLabel(String t)
	{
		Label result = new Label(t);
		result.setStyleName("label-status-ok-c");
		return result;
	}
	
	public DetailUser(UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
		values = ClientUserValuesUtils.getInstance(contactId);
		showPanelDetail();
	}
	
	boolean loaded = false;
	
	@Override
	public void showPanelDetail()
	{
		if (loaded) return;
		loaded = true;
		values.preloadValueList("ACCOUNT", this);
		grid.setWidget(ROW_FIRSTNAME, 0, createLabel(langConnection._TextFirstName()));
		grid.setWidget(ROW_LASTNAME, 0, createLabel(langConnection._TextLastName()));
		grid.setWidget(ROW_TITLE, 0, createLabel(langConnection.jobTitle()));
		grid.setWidget(ROW_STATUS, 0, createLabel(langConnection.actualStatus()));
		grid.setWidget(ROW_KEYWORDS, 0, createLabel(langConnection.skills()));
		
		grid.setWidget(0, 1, new HorizontalSpacer("3em"));
		
		grid.setWidget(ROW_FIRSTNAME, 2, new Label(connectionUser.firstName));
		grid.setWidget(ROW_LASTNAME, 2, new Label(connectionUser.lastName));
		grid.setWidget(ROW_TITLE, 2, labelTitle);
		grid.setWidget(ROW_STATUS, 2, labelStatus);
		grid.setWidget(ROW_KEYWORDS, 2, labelKeywords);
		add(UserImageHelper.getImage(user, contactId.userName, 256));
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

package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditUserSite implements EntryPoint {

	public interface EditLogEntryResult
	{
		public void setResult(UserJobSite result);
	}

	UserId user;

	TextBox textBoxName = new TextBox();
	RichTextArea textAreaDescription = new RichTextArea();
	TextBox textBoxUrl = new TextBox();
	TextBox textBoxLogin = new TextBox();
	TextBox textBoxPassword = new TextBox();
	DatePicker datePickerLastVisit = new DatePicker();

	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	Panel rootPanel;
	EditLogEntryResult result;
	UserJobSite currentUserSite;

	public void setRootPanel(Panel panel, UserJobSite _currentLogEntry, EditLogEntryResult _result)
	{
		rootPanel = panel;
		currentUserSite = _currentLogEntry;
		result = _result;
	}

	private void setUserJobSite(UserJobSite opp)
	{
		if (currentUserSite != null)
		{
			currentUserSite = opp;
			textBoxName.setText(opp.name);		
			textAreaDescription.setText(opp.description);
			textBoxUrl.setText(opp.URL);
			textBoxLogin.setText(opp.login);
			textBoxPassword.setText(opp.password);
			datePickerLastVisit.setValue(opp.lastVisit);
		}
	}

	private UserJobSite getUserJobSite()
	{
		String ID = new Date().toString();
		if (currentUserSite != null) ID = currentUserSite.ID;
		System.out.println("getUserJobSite -" + ID + "-" + textBoxName.getText()+ textBoxUrl.getText()+ textAreaDescription.getHTML()+ textBoxLogin.getValue());
		return new UserJobSite(ID,
				textBoxName.getText(), textBoxUrl.getText(), textAreaDescription.getHTML(), textBoxLogin.getValue(),
				textBoxPassword.getText(), datePickerLastVisit.getValue());
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		Lang lang = GWT.create(Lang.class);

		final DialogBox dBox = new DialogBox();
		dBox.setText("Edit LogEntry");
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(12, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(grid);		


		Label lblName = new Label(lang._TextName());
		grid.setWidget(0, 0, lblName);

		grid.setWidget(0, 1, textBoxName);
		textBoxName.setWidth("100%");

		Label lblDescription = new Label(lang._TextDescription());
		grid.setWidget(1, 0, lblDescription);

		grid.setWidget(1, 1, textAreaDescription);
		textAreaDescription.setSize("100%", "50px");

		Label lblUrl = new Label(lang._TextURL());
		grid.setWidget(2, 0, lblUrl);

		grid.setWidget(2, 1, textBoxUrl);

		Label lblLogin = new Label(lang._TextLogin());
		grid.setWidget(3, 0, lblLogin);

		grid.setWidget(3, 1, textBoxLogin);

		Label lblPassword = new Label(lang._TextPassword());
		grid.setWidget(4, 0, lblPassword);

		grid.setWidget(4, 1, textBoxPassword);

		Label lblLastvisit = new Label(lang._TextLastVisit());
		grid.setWidget(5, 0, lblLastvisit);

		grid.setWidget(5, 1, datePickerLastVisit);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(11, 1, horizontalPanel);

		Button btnOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
		horizontalPanel.add(btnOk);

		Button btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
		horizontalPanel.add(btnCancel);
		grid.getCellFormatter().setHorizontalAlignment(11, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		setUserJobSite(currentUserSite);

		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				result.setResult(getUserJobSite());				
			}
		});

		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				dBox.hide();
				result.setResult(null);				
			}
		});

		dBox.center();
	}
}

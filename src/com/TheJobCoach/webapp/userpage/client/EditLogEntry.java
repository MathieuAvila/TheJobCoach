package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditLogEntry implements EntryPoint {

	public interface EditLogEntryResult
	{
		public void setResult(UserLogEntry result);
	}
	
	UserId user;
	
	RichTextArea richTextAreaDescription = new RichTextArea();
	TextBox txtbxTitle = new TextBox();
	String id;
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	Panel rootPanel;
	EditLogEntryResult result;
	UserLogEntry currentLogEntry;
	
	public void setRootPanel(Panel panel, UserLogEntry _currentLogEntry, EditLogEntryResult _result, String text)
	{
		rootPanel = panel;
		currentLogEntry = _currentLogEntry;
		result = _result;
		txtbxTitle.setText(currentLogEntry.title);
		richTextAreaDescription.setHTML(currentLogEntry.description);		
	}

	public UserLogEntry getLogEntry()
	{
		return new UserLogEntry(currentLogEntry.ID, currentLogEntry.opportunityId,
				txtbxTitle.getText(), richTextAreaDescription.getHTML(), 
				new Date(), new Date(),
				UserLogEntry.entryTypeToString("NEW"),
				null, null);
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
		
		Grid grid = new Grid(7, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(grid);		
		grid.setSize("386px", "472px");
		
		Label lblTitle = new Label("Title");
		grid.setWidget(0, 0, lblTitle);		
		txtbxTitle.setText("Title");
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "100%");
		txtbxTitle.setWidth("100%");
		
		Label lblStatus = new Label("Status");
		grid.setWidget(1, 0, lblStatus);
		
		ListBox comboBoxStatus = new ListBox();
		grid.setWidget(1, 1, comboBoxStatus);
		for (LogEntryType e : UserLogEntry.getLogTypeTable())
		{
			comboBoxStatus.addItem(UserLogEntry.entryTypeToString(e), UserLogEntry.entryTypeToString(e));
		}
		
		Label lblDescription = new Label("Description");
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("100%");
		
		Label lblEventDate = new Label("Start date");
		grid.setWidget(3, 0, lblEventDate);
		
		DateBox dateBoxStart = new DateBox();
		grid.setWidget(3, 1, dateBoxStart);
		
		Label lblEndDate = new Label("End Date");
		grid.setWidget(4, 0, lblEndDate);
		
		DateBox dateBoxEndDate = new DateBox();
		grid.setWidget(4, 1, dateBoxEndDate);
		
		grid.getCellFormatter().setHorizontalAlignment(6, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(6, 1, horizontalPanel);
		
		Button btnOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
		horizontalPanel.add(btnOk);
		
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(getLogEntry());
				dBox.hide();
			}
		});
		
		Button btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
		horizontalPanel.add(btnCancel);
		
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(null);
				dBox.hide();
			}
		});
		
		dBox.center();
	}
}

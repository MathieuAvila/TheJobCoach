package com.TheJobCoach.webapp.userpage.client;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
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
	TextBox txtbxCompany = new TextBox();
	TextBox txtbxContractType = new TextBox();
	IntegerBox txtbxSalary = new IntegerBox();
	TextBox txtbxSource = new TextBox();
	TextBox txtbxUrl = new TextBox();
	TextBox txtbxLocation = new TextBox();
	String id;
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	Panel rootPanel;
	EditLogEntryResult result;
	UserLogEntry currentLogEntry;
	
	public void setRootPanel(Panel panel, UserLogEntry _currentLogEntry, EditLogEntryResult _result)
	{
		rootPanel = panel;
		currentLogEntry = _currentLogEntry;
		result = _result;
	}

	private void setLogEntry(UserLogEntry opp)
	{
		/*
		id = opp.ID;
		txtbxTitle.setText(opp.title);
		richTextAreaDescription.setHTML(opp.description);
		txtbxCompany.setText(opp.companyId);
		txtbxContractType.setText(opp.contractType);
		txtbxSalary.setValue(opp.salary);
		txtbxSource.setText(opp.source);
		txtbxUrl.setText(opp.url);
		txtbxLocation.setText(opp.location);
		*/
	}
	
	public UserLogEntry getLogEntry()
	{
		return currentLogEntry;
		/*
		return new UserLogEntry(id, new Date(), new Date(),
				txtbxTitle.getText(), richTextAreaDescription.getHTML(), txtbxCompany.getValue(),
				txtbxContractType.getText(), txtbxSalary.getValue(), new Date(), new Date(),
				false, txtbxSource.getText(), txtbxUrl.getText(), txtbxLocation.getText());*/
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		final DialogBox dBox = new DialogBox();
		dBox.setText("Edit LogEntry");
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);
		
		Grid grid = new Grid(12, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(grid);		
		
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
		
		Label lblCompany = new Label("Company");
		grid.setWidget(3, 0, lblCompany);		
		txtbxCompany.setText("Company");
		grid.setWidget(3, 1, txtbxCompany);
		grid.getCellFormatter().setWidth(3, 1, "100%");
		txtbxCompany.setWidth("100%");

		Label lblContractType = new Label("ContractType");
		grid.setWidget(4, 0, lblContractType);		
		txtbxContractType.setText("ContractType");
		grid.setWidget(4, 1, txtbxContractType);
		grid.getCellFormatter().setWidth(4, 1, "100%");
		txtbxContractType.setWidth("100%");
			
		Label lblSalary = new Label("Salary");
		grid.setWidget(5, 0, lblSalary);		
		txtbxSalary.setText("Salary");
		grid.setWidget(5, 1, txtbxSalary);
		grid.getCellFormatter().setWidth(5, 1, "100%");
		txtbxSalary.setWidth("100%");
		
		Label lblStartDate = new Label("Start date");
		grid.setWidget(6, 0, lblStartDate);
		
		DateBox dateBoxStart = new DateBox();
		grid.setWidget(6, 1, dateBoxStart);
				
		Label lblEndDate = new Label("End Date");
		grid.setWidget(7, 0, lblEndDate);
		
		DateBox dateBoxEndDate = new DateBox();
		grid.setWidget(7, 1, dateBoxEndDate);
		
		Label lblSource = new Label("Source");
		grid.setWidget(8, 0, lblSource);		
		txtbxSource.setText("Source");
		grid.setWidget(8, 1, txtbxSource);
		grid.getCellFormatter().setWidth(8, 1, "100%");
		txtbxSource.setWidth("100%");
				
		Label lblUrl = new Label("Url");
		grid.setWidget(9, 0, lblUrl);
		txtbxUrl.setWidth("100%");
		txtbxUrl.setText("Url");
		grid.setWidget(9, 1, txtbxUrl);
		grid.getCellFormatter().setWidth(9, 1, "100%");
			
		Label lblLocation = new Label("Location");
		grid.setWidget(10, 0, lblLocation);		
		txtbxLocation.setText("Location");
		grid.setWidget(10, 1, txtbxLocation);
		grid.getCellFormatter().setWidth(10, 1, "100%");
		txtbxLocation.setWidth("100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(11, 1, horizontalPanel);
		
		Button btnOk = new Button("OK");
		horizontalPanel.add(btnOk);
		
		Button btnCancel = new Button("Cancel");
		horizontalPanel.add(btnCancel);
		grid.getCellFormatter().setHorizontalAlignment(11, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		setLogEntry(this.currentLogEntry);
		
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(getLogEntry());
				dBox.hide();
			}
		});
		
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

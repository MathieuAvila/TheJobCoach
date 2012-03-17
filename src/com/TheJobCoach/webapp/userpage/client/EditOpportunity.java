package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity.ApplicationStatus;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditOpportunity implements EntryPoint {

	public interface EditOpportunityResult
	{
		public void setResult(UserOpportunity result);
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
	EditOpportunityResult result;
	UserOpportunity currentOpportunity;
	String boxName;
	
	public void setRootPanel(Panel panel, UserOpportunity _currentOpportunity, EditOpportunityResult _result, String _boxName)
	{
		rootPanel = panel;
		currentOpportunity = _currentOpportunity;
		result = _result;
		boxName = _boxName;
	}

	private void setOpportunity(UserOpportunity opp)
	{
		id = opp.ID;
		txtbxTitle.setText(opp.title);
		richTextAreaDescription.setHTML(opp.description);
		txtbxCompany.setText(opp.companyId);
		txtbxContractType.setText(opp.contractType);
		txtbxSalary.setValue(opp.salary);
		txtbxSource.setText(opp.source);
		txtbxUrl.setText(opp.url);
		txtbxLocation.setText(opp.location);
	}
	
	public UserOpportunity getOpportunity()
	{
		return new UserOpportunity(id, new Date(), new Date(),
				txtbxTitle.getText(), richTextAreaDescription.getHTML(), txtbxCompany.getValue(),
				txtbxContractType.getText(), txtbxSalary.getValue(), new Date(), new Date(),
				false, txtbxSource.getText(), txtbxUrl.getText(), txtbxLocation.getText());
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		Lang lang = GWT.create(Lang.class);
	    System.out.println("Locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				
	
		final DialogBox dBox = new DialogBox();
		dBox.setText(boxName);
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);
		
		Grid grid = new Grid(12, 2);
		grid.setBorderWidth(0);
		dBox.add(grid);		
		grid.setWidth("95%");
		
		Label lblTitle = new Label(lang._TextPositionName());
		grid.setWidget(0, 0, lblTitle);		
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "100%");
		txtbxTitle.setWidth("95%");
		
		Label lblStatus = new Label(lang._TextStatus());
		grid.setWidget(1, 0, lblStatus);
		
		ListBox comboBoxStatus = new ListBox();
		grid.setWidget(1, 1, comboBoxStatus);
		for (ApplicationStatus e : UserOpportunity.getApplicationStatusTable())
		{
			comboBoxStatus.addItem(lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(e)) ,UserOpportunity.applicationStatusToString(e));
		}
		
		Label lblDescription = new Label(lang._TextDescription());
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("95%");
		
		Label lblCompany = new Label(lang._TextCompany());
		grid.setWidget(3, 0, lblCompany);		
		grid.setWidget(3, 1, txtbxCompany);
		grid.getCellFormatter().setWidth(3, 1, "100%");
		txtbxCompany.setWidth("95%");

		Label lblContractType = new Label(lang._TextContractType());
		grid.setWidget(4, 0, lblContractType);		
		grid.setWidget(4, 1, txtbxContractType);
		grid.getCellFormatter().setWidth(4, 1, "100%");
		txtbxContractType.setWidth("95%");
			
		Label lblSalary = new Label(lang._TextSalary());
		grid.setWidget(5, 0, lblSalary);		
		grid.setWidget(5, 1, txtbxSalary);
		grid.getCellFormatter().setWidth(5, 1, "100%");
		txtbxSalary.setWidth("95%");
		
		Label lblStartDate = new Label(lang._TextStartDate());
		grid.setWidget(6, 0, lblStartDate);
		
		DateBox dateBoxStart = new DateBox();
		grid.setWidget(6, 1, dateBoxStart);
		dateBoxStart.setWidth("95%");
				
		Label lblEndDate = new Label(lang._TextEndDate());
		grid.setWidget(7, 0, lblEndDate);
		
		DateBox dateBoxEndDate = new DateBox();
		grid.setWidget(7, 1, dateBoxEndDate);
		dateBoxEndDate.setWidth("95%");
		
		Label lblSource = new Label(lang._TextSource());
		grid.setWidget(8, 0, lblSource);		
		grid.setWidget(8, 1, txtbxSource);
		grid.getCellFormatter().setWidth(8, 1, "100%");
		txtbxSource.setWidth("95%");
				
		Label lblUrl = new Label(lang._TextUrl());
		grid.setWidget(9, 0, lblUrl);
		txtbxUrl.setWidth("95%");
		grid.setWidget(9, 1, txtbxUrl);
		grid.getCellFormatter().setWidth(9, 1, "100%");
			
		Label lblLocation = new Label(lang._TextLocation());
		grid.setWidget(10, 0, lblLocation);		
		grid.setWidget(10, 1, txtbxLocation);
		grid.getCellFormatter().setWidth(10, 1, "100%");
		txtbxLocation.setWidth("95%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(11, 1, horizontalPanel);
		
		Button btnOk = new Button("OK");
		horizontalPanel.add(btnOk);
		
		Button btnCancel = new Button("Cancel");
		horizontalPanel.add(btnCancel);
		grid.getCellFormatter().setHorizontalAlignment(11, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		setOpportunity(this.currentOpportunity);
		
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(getOpportunity());
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

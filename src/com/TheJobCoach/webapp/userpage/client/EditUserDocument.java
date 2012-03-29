package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.google.gwt.core.client.EntryPoint;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditUserDocument implements EntryPoint {

	public interface EditUserDocumentResult
	{
		public void setResult(UserDocument result);
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
	EditUserDocumentResult result;
	UserDocument currentUserDocument;
	
	public void setRootPanel(Panel panel, UserDocument _currentUserDocument, EditUserDocumentResult _result, String text)
	{
		rootPanel = panel;
		currentUserDocument = _currentUserDocument;
		result = _result;
		txtbxTitle.setText(currentUserDocument.name);
		richTextAreaDescription.setHTML(currentUserDocument.description);		
	}

	public UserDocument getUserDocument()
	{
		return new UserDocument(currentUserDocument.ID,
				txtbxTitle.getText(), 
				richTextAreaDescription.getHTML(),
				new Date(),
				currentUserDocument.fileName,
				UserDocument.documentStatusToString("NEW"));
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		final DialogBox dBox = new DialogBox();
		dBox.setText("Edit UserDocument");
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
		Vector<UserDocument.DocumentStatus> tDocStatus = new Vector<UserDocument.DocumentStatus>();
		tDocStatus.copyInto(UserDocument.DocumentStatus.values());
		for (DocumentStatus e : tDocStatus)
		{
			comboBoxStatus.addItem(UserDocument.documentStatusToString(e), UserDocument.documentStatusToString(e));
		}
		
		Label lblDescription = new Label("Description");
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("100%");
		
		grid.getCellFormatter().setHorizontalAlignment(6, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(6, 1, horizontalPanel);
		
		Button btnOk = new Button("OK");
		horizontalPanel.add(btnOk);
		
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				result.setResult(getUserDocument());
				dBox.hide();
			}
		});
		
		Button btnCancel = new Button("Cancel");
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

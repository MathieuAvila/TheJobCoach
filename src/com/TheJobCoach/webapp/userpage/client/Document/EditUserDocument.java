package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IEditResult;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditUserDocument implements EntryPoint {

	private static final UserServiceAsync userService = GWT.create(UserService.class);
	final DialogBox dBox = new DialogBox();

	UserId user;

	final static Lang lang = GWT.create(Lang.class);
	final static LangDocument langDocument = GWT.create(LangDocument.class);
	
	Label lblType = new Label(langDocument._TextType());
	Label lblStatus = new Label(lang._TextStatus());
	Label lblDescription = new Label(lang._TextDescription());
	Label lblSelectFile = new Label(langDocument._TextSelectFile());

	RichTextArea richTextAreaDescription = new RichTextArea();
	TextBox txtbxTitle = new TextBox();
	ListBox comboBoxStatus = new ListBox();
	ListBox comboBoxType = new ListBox();
	
	public String fakeFileName = "";
	FileUpload upload = new FileUpload();
	
	FormPanel form = new FormPanel();
	IEditResult<UserDocument> resultInterface;
	Panel rootPanel;
	UserDocument currentUserDocument;
	DialogBlockOkCancel okCancel;
	
	public EditUserDocument(Panel panel, UserId _user, UserDocument _currentUserDocument, IEditResult<UserDocument> resultInterface)
	{
		user = _user;
		rootPanel = panel;
		currentUserDocument = _currentUserDocument;
		this.resultInterface = resultInterface;
	}
	
	private String stripUserName(String name)
	{
		String[] split0t = name.split("\\\\");
		String split0 = split0t[split0t.length -1];
		String[] split1t = split0.split("/");
		String split1 = split1t[split1t.length -1];
		return split1;
	}

	private UserDocument getDocument()
	{
		String iD = SiteUUID.getDateUuid();
		Date d = new Date();
		String stripUserName = "";
		if (currentUserDocument != null)
		{
			stripUserName = currentUserDocument.fileName;
			iD = currentUserDocument.ID;
		}
		String fileName = upload.getFilename();
		if (!fakeFileName.equals("")) fileName = fakeFileName;
		if (!"".equals(fileName))
		{
			stripUserName = stripUserName(fileName);
		}
		return new UserDocument(
				iD, 
				txtbxTitle.getValue(), richTextAreaDescription.getHTML(),
				d, stripUserName, 
				UserDocument.DocumentStatus.values()[comboBoxStatus.getSelectedIndex()],
				UserDocument.DocumentType.values()[comboBoxType.getSelectedIndex()], null);
	}

	public void commit()
	{
		if (currentUserDocument == null)
		{		
			if ("".equals(upload.getFilename()) && (fakeFileName.equals("")))
			{
				// This is not allowed: first insert must set a file.
				MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR,	langDocument._TextNeedFilename());
				return;
			}
		}
		final UserDocument ud = getDocument();
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.messageBoxException(rootPanel, caught);
			}
			@Override
			public void onSuccess(String result) {
			}
		};
		
		try {			
			userService.setUserDocument(user, ud, callback);
		} catch (CassandraException e) {
			MessageBox.messageBoxException(rootPanel, e);
		}

		// Now Upload file if necessary.
		if ("".equals(upload.getFilename()) || (!fakeFileName.equals("")))
		{
			dBox.hide();
			resultInterface.setResult(ud);			
			return;
		}		
		final String copyURL = GWT.getModuleBaseURL() + "UploadServlet?docid=" + URL.encodeQueryString(ud.ID) + "&userid=" + URL.encodeQueryString(user.userName)+ "&token=" + URL.encodeQueryString(user.token);
		form.setAction(copyURL);
		final MessageBox mb = MessageBox.messageBox(rootPanel, MessageBox.TYPE.WAIT, langDocument._TextUploadInProgress());
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() 
		{		      
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				mb.close();
				dBox.hide();
				resultInterface.setResult(ud);				
			}
		});
		form.submit();
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		dBox.setText(currentUserDocument == null ? langDocument._TextCreateANewUserDocument() : langDocument._TextUpdateANewUserDocument());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(5, 2);
		grid.setBorderWidth(0);
		grid.setSize("100%", "100%");

		Label lblTitle = new Label("Title");
		grid.setWidget(0, 0, lblTitle);		
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "90%");
		txtbxTitle.setWidth("90%");

		for (DocumentStatus e: UserDocument.DocumentStatus.values() )
		{
			comboBoxStatus.addItem(langDocument.documentStatusMap().get("documentStatusMap_" + UserDocument.documentStatusToString(e)), UserDocument.documentStatusToString(e));
		}
		for (UserDocument.DocumentType e: UserDocument.DocumentType.values() )
		{
			comboBoxType.addItem(langDocument.documentTypeMap().get("documentTypeMap_" + UserDocument.documentTypeToString(e)), UserDocument.documentTypeToString(e));
		}
		
		grid.setWidget(1, 0, lblType);
		grid.setWidget(1, 1, comboBoxType);
		
		grid.setWidget(2, 0, lblStatus);
		grid.setWidget(2, 1, comboBoxStatus);
		
		grid.setWidget(3, 0, lblDescription);		
		grid.setWidget(3, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("100%");

		grid.setWidget(4, 0, lblSelectFile);	
		grid.getCellFormatter().setWidth(3, 0, "30%");
		grid.getCellFormatter().setWidth(3, 1, "100%");

		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel holder = new HorizontalPanel();
		holder.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("demo-panel-padded");
		form.add(holder);
		upload.setName("upload");
		holder.add(upload);	
		upload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if 	(txtbxTitle.getValue() == "")
				{
					txtbxTitle.setValue(stripUserName(upload.getFilename()));
				}
			}			
		});
		grid.setWidget(4, 1, form);
		
		if (currentUserDocument != null)
		{
			txtbxTitle.setText(currentUserDocument.name);
			richTextAreaDescription.setHTML(currentUserDocument.description);
			comboBoxStatus.setItemSelected(currentUserDocument.status.ordinal(), true);
			comboBoxType.setItemSelected(currentUserDocument.type.ordinal(), true);
		}
		
		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				commit();
			}
		});		

		VerticalPanel vp = new VerticalPanel();
		dBox.setWidget(vp);	
		vp.add(grid);
		vp.add(okCancel);
		
		dBox.center();
	}
}

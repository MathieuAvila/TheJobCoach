package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.EasyCallback;
import com.TheJobCoach.webapp.util.client.GridHelper;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasAlignment;
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
		
		EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
			public void Run() throws CassandraException
			{
				userService.setUserDocument(user, ud, new EasyCallback<String>(rootPanel, new EasyCallback.SuccessRun<String>() {
					@Override
					public void onSuccess(String result)
					{
						if ("".equals(upload.getFilename()) && (fakeFileName.equals(""))) {
							resultInterface.setResult(ud);
							dBox.hide();
						}
					}}));
			}});
		
		// Now Upload file if necessary.
		if ("".equals(upload.getFilename()) && (fakeFileName.equals("")))
		{
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
				resultInterface.setResult(ud);				
				dBox.hide();
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

		GridHelper grid = new GridHelper(rootPanel);

		grid.addLine(0, new Label(langDocument._TextTitle()), txtbxTitle);
		
		for (DocumentStatus e: UserDocument.DocumentStatus.values() )
		{
			comboBoxStatus.addItem(langDocument.documentStatusMap().get("documentStatusMap_" + UserDocument.documentStatusToString(e)), UserDocument.documentStatusToString(e));
		}
		for (UserDocument.DocumentType e: UserDocument.DocumentType.values() )
		{
			comboBoxType.addItem(langDocument.documentTypeMap().get("documentTypeMap_" + UserDocument.documentTypeToString(e)), UserDocument.documentTypeToString(e));
		}
		grid.addLine(1, lblType, comboBoxType);		
		grid.addLine(2, lblStatus, comboBoxStatus);
		grid.addLine(3, lblDescription, richTextAreaDescription);

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
		grid.addLine(4, lblSelectFile, form);
		
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

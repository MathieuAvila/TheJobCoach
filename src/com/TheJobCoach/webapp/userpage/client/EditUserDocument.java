package com.TheJobCoach.webapp.userpage.client;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.userpage.shared.UserDocument.DocumentStatus;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasAlignment;
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

	private static final UserServiceAsync userService = GWT.create(UserService.class);
	final DialogBox dBox = new DialogBox();

	UserId user;

	RichTextArea richTextAreaDescription = new RichTextArea();
	TextBox txtbxTitle = new TextBox();
	ListBox comboBoxStatus = new ListBox();
	FileUpload upload = new FileUpload();
	FormPanel form = new FormPanel();
	EditUserDocumentResult resultInterface;

	public interface EditUserDocumentResult
	{
		public void setResult();
	}

	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	Panel rootPanel;
	UserDocument currentUserDocument;

	public void setRootPanel(Panel panel, UserDocument _currentUserDocument, String text, EditUserDocumentResult resultInterface)
	{
		rootPanel = panel;
		currentUserDocument = _currentUserDocument;
		if (currentUserDocument != null)
		{
			txtbxTitle.setText(currentUserDocument.name);
			richTextAreaDescription.setHTML(currentUserDocument.description);
		}
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
		String iD = new Date().toString();
		Date d = new Date();
		String stripUserName = "";
		if (currentUserDocument != null)
		{
			stripUserName = currentUserDocument.fileName;
			iD = currentUserDocument.ID;
		}
		if (!"".equals(upload.getFilename()))
		{
			stripUserName = stripUserName(upload.getFilename());
		}
		return new UserDocument(
				iD, 
				txtbxTitle.getValue(), richTextAreaDescription.getHTML(),
				d, stripUserName, 
				UserDocument.DocumentStatus.values()[comboBoxStatus.getSelectedIndex()]);
	}

	public void commit()
	{
		if (currentUserDocument == null)
		{		
			System.out.println("FileName '" + upload.getFilename()+ "'");
			if ("".equals(upload.getFilename()))
			{
				// This is not allowed: first insert must set a file.
				MessageBox mb = new MessageBox(rootPanel, true, false, MessageBox.TYPE.ERROR, 
						"Must set file name", 
						"When inserting a new document, you must provide a name",
						null);
				mb.onModuleLoad();
				return;
			}
		}
		UserDocument ud = getDocument();
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess(String result) {
				System.out.println(result);
			}
		};
		try {
			userService.setUserDocument(user, ud, callback);
		} catch (CassandraException e) {
			e.printStackTrace();
		}
		// Now Upload file if necessary.
		if ("".equals(upload.getFilename()))
		{
			dBox.hide();
			resultInterface.setResult();		
		}
		String copyURL = GWT.getModuleBaseURL() + "UploadServlet?docid=" + URL.encode(ud.ID);
		form.setAction(copyURL);
		final MessageBox mb = new MessageBox(dBox, false, false, MessageBox.TYPE.WAIT, "Upload in progress", "Please wait while upload is in progress", null);
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() 
		{		      
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				mb.close();
				dBox.hide();
				resultInterface.setResult();
				//Window.alert("Submit result " + event.getResults()  + " " + event.toDebugString());
			}
		});
		form.submit();
		mb.onModuleLoad();
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{					
		Lang lang = GWT.create(Lang.class);
		System.out.println("Load Edit User Document, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		dBox.setSize("500", "300");
		dBox.setText("Edit UserDocument");
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		Grid grid = new Grid(6, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(grid);		
		grid.setSize("100%", "100%");

		Label lblTitle = new Label("Title");
		grid.setWidget(0, 0, lblTitle);		
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "90%");
		txtbxTitle.setWidth("90%");

		Label lblStatus = new Label("Status");
		grid.setWidget(1, 0, lblStatus);

		grid.setWidget(1, 1, comboBoxStatus);
		int index = 0;
		for (DocumentStatus e: UserDocument.DocumentStatus.values() )
		{
			comboBoxStatus.addItem(UserDocument.documentStatusToString(e), lang.documentStatusMap().get(UserDocument.documentStatusToString(e)));
			if (currentUserDocument != null)
			{
				if (currentUserDocument.status == e)
				{
					comboBoxStatus.setItemSelected(index, true);
				}
			}
			index++;
		}

		Label lblDescription = new Label("Description");
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		grid.getCellFormatter().setWidth(2, 1, "90%");
		richTextAreaDescription.setWidth("100%");

		Label lblSelectFile = new Label("Select File");
		grid.setWidget(3, 0, lblSelectFile);	
		grid.getCellFormatter().setWidth(2, 0, "30%");
		grid.getCellFormatter().setWidth(2, 1, "100%");

		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(4, 1, horizontalPanel);

		Button btnOk = new ButtonImageText(ButtonImageText.Type.OK, lang._TextOk());
		horizontalPanel.add(btnOk);

		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				commit();
			}
		});

		Button btnCancel = new ButtonImageText(ButtonImageText.Type.CANCEL, lang._TextCancel());
		horizontalPanel.add(btnCancel);

		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				dBox.hide();
			}
		});		

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
		grid.setWidget(3, 1, form);
		grid.getCellFormatter().setHeight(5, 1, "0px");

		dBox.center();
	}
}

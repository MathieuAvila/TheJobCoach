package com.TheJobCoach.webapp.userpage.client.Account;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.Document.LangDocument;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.GridHelper;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

public class EditPhoto extends DialogBox implements IEditDialogModel<String> {


	DialogBlockOkCancel okCancel;
	UserId user;
	final static Lang lang = GWT.create(Lang.class);
	final static LangAccount langAccount = GWT.create(LangAccount.class);
	final static LangDocument langDocument = GWT.create(LangDocument.class);
	FileUpload upload = new FileUpload();
	FormPanel form = new FormPanel();
	Label lblSelectFile = new Label(langAccount.choosePhotoFile());
	IChooseResult<String> resultInterface;
	
	public void commit()
	{
		final DialogBox content = this;
		final String copyURL = GWT.getModuleBaseURL() + "UploadImage?type=photo&userid=" + URL.encodeQueryString(user.userName)+ "&token=" + URL.encodeQueryString(user.token);
		form.setAction(copyURL);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
		final MessageBox mb = MessageBox.messageBox(RootPanel.get(), MessageBox.TYPE.WAIT, langDocument._TextUploadInProgress());
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() 
		{		      
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				mb.close();
				resultInterface.setResult("");
				content.hide();
				if (event.getResults().contains("ERROR"))
				{
					MessageBox.messageBox(MessageBox.TYPE.ERROR, lang.checkSizeError());
				}
			}
		});
		form.submit();
	}

	public EditPhoto(UserId _user, IChooseResult<String> result)
	{
		user = _user;
		resultInterface = result;
		setText(langAccount.changeMyPhoto());
		setGlassEnabled(true);
		setAnimationEnabled(true);

		GridHelper grid = new GridHelper("70%", "30%");

		HorizontalPanel holder = new HorizontalPanel();
		holder.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("demo-panel-padded");
		form.add(holder);
		upload.setName("upload");
		holder.add(upload);	
		grid.addLine(0, lblSelectFile, form);
		
		okCancel = new DialogBlockOkCancel(null, this);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				commit();
			}
		});
		
		VerticalPanel vp = new VerticalPanel();
		setWidget(vp);	
		vp.add(grid);
		vp.add(okCancel);
		
		center();
	}

	@Override
	public IEditDialogModel<String> clone(Panel rootPanel, UserId userId,
			String edition, IChooseResult<String> result)
	{
		return new EditPhoto(userId, result);
	}

	@Override
	public void onModuleLoad()
	{
		// TODO Auto-generated method stub
		
	}

}

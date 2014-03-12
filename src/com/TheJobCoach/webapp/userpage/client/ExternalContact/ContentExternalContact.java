package com.TheJobCoach.webapp.userpage.client.ExternalContact;

import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentExternalContact implements EntryPoint {

	UserId user;

	// The list of data to display.
	private Vector<ExternalContact> externalContactList = new Vector<ExternalContact>();
	
	final ExtendedCellTable<ExternalContact> cellTable = new ExtendedCellTable<ExternalContact>(externalContactList);

	final static Lang lang = GWT.create(Lang.class);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);
	ButtonImageText buttonNewExternalContact;
	private IEditDialogModel<ExternalContact> editModel;
	
	Panel rootPanel;

	void init(Panel panel, UserId _user, IEditDialogModel<ExternalContact> editModel)
	{
		user = _user;
		rootPanel = panel;
		this.editModel = editModel;
	}
	
	public ContentExternalContact(Panel panel, UserId _user)
	{
		init(panel, _user, new EditExternalContact());		
	}
	
	public ContentExternalContact(Panel panel, UserId _user, IEditDialogModel<ExternalContact> editModel)
	{
		init(panel, _user,  editModel);
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	void getAllContent()
	{	
		ServerCallHelper<Vector<ExternalContact>> callback = new ServerCallHelper<Vector<ExternalContact>>(rootPanel) {
			@Override
			public void onSuccess(Vector<ExternalContact> result) {
				externalContactList.clear();
				externalContactList.addAll(result);
				cellTable.updateData();				
			}
		};
		userService.getExternalContactList(user, callback);
	}

	private void delete(final ExternalContact contact)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langExternalContact._TextDeleteTitle(), 
				langExternalContact._TextDeleteText() + contact.lastName + " " + contact.firstName, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							userService.deleteExternalContact(user, contact.ID, new ServerCallHelper<String>(rootPanel) {
								public void onSuccess(String result)
								{
									getAllContent();
								}
							});
						}}});
		mb.onModuleLoad();
	}

	class NewExternalContactHandler implements ClickHandler
	{		
		public void onClick(ClickEvent event)
		{
			 IEditDialogModel<ExternalContact> eus = editModel.clone(rootPanel, user, null, new IChooseResult<ExternalContact>() {

				@Override
				public void setResult(ExternalContact result) {

					if (result != null)
					{
						result.ID = SiteUUID.getDateUuid();
						userService.setExternalContact(user, result, new ServerCallHelper<String>(rootPanel) {								
							public void onSuccess(String result)
							{
								getAllContent();
							}
						});
					}			
				}
			});
			eus.onModuleLoad();
		}
	}

	private void updateExternalContact(ExternalContact contact)
	{	
		IEditDialogModel<ExternalContact> eus = editModel.clone(rootPanel, user, contact, new IChooseResult<ExternalContact>() {
			@Override
			public void setResult(ExternalContact result) {
				if (result != null)
				{
					userService.setExternalContact(user, result, new ServerCallHelper<String>(rootPanel) {
						public void onSuccess(String result)
						{
							getAllContent();
						}
					});
				}				
			}
		});
		eus.onModuleLoad();
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{			
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, langExternalContact._TextExternalContactTitle(), ClientImageBundle.INSTANCE.userExternalContactContent());


		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<ExternalContact, String>() {
			@Override
			public void update(int index, ExternalContact object, String value) {
				delete(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<ExternalContact, String>() {
			@Override
			public void update(int index, ExternalContact object, String value) {
				updateExternalContact(object);
			}});

		// Create first name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.firstName;
			}			
		},  langExternalContact._TextFirstName());
		
		// Create last name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.lastName;
			}			
		},  langExternalContact._TextLastName());
		
		// Create organization column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.organization;
			}			
		},  langExternalContact._Text_Organization());

		// Create phone column.
		cellTable.specialAddColumnSortableString(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{
				return contact.phone;
			}			
		},  langExternalContact._Text_Phone());

		 // email
		cellTable.addColumnEmail(new GetValue<String, ExternalContact>() {
			@Override
			public String getValue(ExternalContact contact)
			{				
				return contact.email;
			}			
		});

		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		buttonNewExternalContact = new ButtonImageText(ButtonImageText.Type.NEW, langExternalContact._TextNewExternalContact());
		horizontalPanel.add(buttonNewExternalContact);

		// Add a handler to the new button.
		NewExternalContactHandler newHandler = new NewExternalContactHandler();
		buttonNewExternalContact.addClickHandler(newHandler);

		getAllContent();		
	}
}

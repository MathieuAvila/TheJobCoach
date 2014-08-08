package com.TheJobCoach.webapp.userpage.client.Connection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.TheJobCoach.webapp.adminpage.shared.UserSearchEntry;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.LangExternalContact;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IconsCell.IGetIcons;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.MessageBoxTriState;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserId.UserType;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentConnection extends VerticalPanel {

	UserId user;

	// The list of connection data to display.
	private Vector<ContactInformation> connectionList = new Vector<ContactInformation>();
	private Set<String> excludeUserNameList = new HashSet<String>();

	// The list of search data to display.
	private Vector<UserSearchEntry> searchList = new Vector<UserSearchEntry>();

	final ExtendedCellTable<ContactInformation> cellTable = new ExtendedCellTable<ContactInformation>(connectionList);
	final ExtendedCellTable<UserSearchEntry> cellTableSearchResult = new ExtendedCellTable<UserSearchEntry>(searchList);

	final static Lang lang = GWT.create(Lang.class);
	final static LangConnection langConnection = GWT.create(LangConnection.class);
	final static LangExternalContact langExternalContact = GWT.create(LangExternalContact.class);
	ButtonImageText buttonRunSearch;
	final TextBox textBoxLastName = new TextBox();
	final TextBox textBoxFirstName = new TextBox();

	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static com.TheJobCoach.webapp.util.client.ClientImageBundle wpUtilImageBundle = (com.TheJobCoach.webapp.util.client.ClientImageBundle) GWT.create(com.TheJobCoach.webapp.util.client.ClientImageBundle.class);
	
	static ImageResource documentIcon = wpImageBundle.userDocumentContent_menu();
	static ImageResource opportunityIcon = wpImageBundle.opportunityContent_menu();
	static ImageResource addressIcon = wpImageBundle.userExternalContactContent_menu();
	static ImageResource logIcon = wpImageBundle.userLogContent_menu();
	
	static ImageResource voidIcon = wpImageBundle.void_24();

	static ImageResource documentIconThawed = wpImageBundle.userDocumentContent_thawed();
	static ImageResource opportunityIconThawed = wpImageBundle.opportunityContent_thawed();
	static ImageResource addressIconThawed = wpImageBundle.userExternalContactContent_thawed();
	static ImageResource logIconThawed = wpImageBundle.userLogContent_thawed();

	static ImageResource documentIconDisabled = wpImageBundle.userDocumentContent_disabled();
	static ImageResource opportunityIconDisabled = wpImageBundle.opportunityContent_disabled();
	static ImageResource addressIconDisabled = wpImageBundle.userExternalContactContent_disabled();
	static ImageResource logIconDisabled = wpImageBundle.userLogContent_disabled();

	static ImageResource messageIcon = wpImageBundle.userSendMail();
	
	static ImageResource rightIcon = wpUtilImageBundle.nextIcon();
	
	static ImageResource addIcon = wpUtilImageBundle.buttonAdd16();

	static ImageResource contactOk = wpImageBundle.userConnectionOk();
	static ImageResource contactNone = wpImageBundle.userConnectionNone();
	static ImageResource contactRequested = wpImageBundle.userConnectionRequested();
	static ImageResource contactAwaiting = wpImageBundle.userConnectionAwaiting();

	Panel rootPanel;
	ISendMessage sendMessage;
	IConnectionToDetail connectionToDetail;
	IEditDialogModel<ContactInformation> editShares = null;
	
	HorizontalPanel detailContainer = new HorizontalPanel();
	
	void init(UserId _user)
	{
		user = _user;
		rootPanel = RootPanel.get();
	}

	public ContentConnection(UserId _user, ISendMessage sendMessage, IConnectionToDetail connectionToDetail, IEditDialogModel<ContactInformation> editShares)
	{
		init(_user);
		this.sendMessage = sendMessage;
		this.connectionToDetail = connectionToDetail;
		this.editShares = editShares;
		onModuleLoad();
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	void getAllContent()
	{	
		ServerCallHelper<Vector<ContactInformation>> callback = new ServerCallHelper<Vector<ContactInformation>>(rootPanel) {
			@Override
			public void onSuccess(Vector<ContactInformation> result) {
				connectionList.clear();
				connectionList.addAll(result);
				cellTable.updateData();
				
				excludeUserNameList.clear();
				for (ContactInformation ci: result) excludeUserNameList.add(ci.userName);
				excludeUserNameList.add(user.userName);
			}
		};
		userService.getContactList(callback);
	}

	class RunSearchHandler implements ClickHandler
	{		
		public void onClick(ClickEvent event)
		{
			AsyncCallback<UserSearchResult> callback = new ServerCallHelper<UserSearchResult>(rootPanel) {
				@Override
				public void onSuccess(UserSearchResult result) {
					searchList.clear();
					searchList.addAll(result.entries);
					cellTableSearchResult.updateData();
					cellTableSearchResult.setVisible(true);
				}
			};
			cellTableSearchResult.setVisible(false);
			userService.searchUsers(user, textBoxFirstName.getValue(), textBoxLastName.getValue(), 10, 0, callback);
		}
	}

	private void message(ContactInformation object)
	{
		sendMessage.sendMessage(rootPanel, new UserId(object.userName, "", UserId.UserType.USER_TYPE_SEEKER),
				object.firstName, object.lastName);
	}

	private String messageReplace(String msg, ContactInformation object)
	{
		return msg
				.replace("%f", object.firstName)
				.replace("%l", object.lastName);
	}

	private void updateContactStatus(final ContactInformation object)
	{
		System.out.println("updateContactStatus " + object.userName);
		switch (object.status)
		{
		case CONTACT_OK: 
			MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, 
					messageReplace(langConnection.messageStatusOk(), object));
			break;
		case CONTACT_AWAITING: 
			MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, 
					messageReplace(langConnection.messageStatusAwaiting(), object));
			break;
		case CONTACT_NONE: 
			MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, 
					messageReplace(langConnection.messageStatusNone(), object));
			break;
		case CONTACT_REQUESTED: 
			new MessageBoxTriState(rootPanel, MessageBoxTriState.TYPE.QUESTION, "", messageReplace(langConnection.messageStatusRequested(), object),
					new ButtonImageText(ButtonImageText.Type.USER_ACCEPT, langConnection.acceptConnection()), 
					new ButtonImageText(ButtonImageText.Type.USER_REFUSE, langConnection.refuseConnection()), 
					new MessageBoxTriState.ICallback() {
				@Override
				public void complete(int ok)
				{
					// Call to server...
					ServerCallHelper<ContactInformation.ContactStatus> callback = new ServerCallHelper<ContactInformation.ContactStatus>(rootPanel) {
						@Override
						public void onSuccess(ContactInformation.ContactStatus result) {
							getAllContent();			
						}
					}; if (ok >= 0)							
						userService.updateContactRequest(new UserId(object.userName,  "", UserId.UserType.USER_TYPE_COACH), 
								ok == 0, callback);
				}});
			break;
		}
	}

	private void addConnection(UserSearchEntry newUser)
	{
		AsyncCallback<ContactInformation.ContactStatus> callback = new ServerCallHelper<ContactInformation.ContactStatus>(rootPanel) {
			@Override
			public void onSuccess(ContactInformation.ContactStatus result) {
				searchList.clear();
				cellTableSearchResult.setVisible(false);
				cellTableSearchResult.updateData();
				getAllContent();
			}
		};
		
		if (user.testAccount)
		{
			MessageBox.messageBox(rootPanel, MessageBox.TYPE.ERROR, langConnection.messageTestAccountDenied());
			return;
		}
		
		if (excludeUserNameList.contains(newUser.userName))
			return;
		cellTableSearchResult.setVisible(false);
		userService.updateContactRequest(new UserId(newUser.userName, "", UserType.USER_TYPE_COACH), true, callback);
		MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, 
				messageReplace(langConnection.messageStatusAwaiting(), new ContactInformation(newUser.firstName, newUser.lastName)));
	}

	void insertMiddle(HorizontalPanel p, Widget e)
	{
		p.add(e);
		p.setCellVerticalAlignment(e, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	class IGetIconsStatus implements IGetIcons<ContactInformation>
	{
		@Override
		public Vector<ImageResource> getIcons(ContactInformation element)
		{
			Vector<ImageResource> result = new Vector<ImageResource>();
			switch (element.status)
			{
			case CONTACT_OK: result.add(contactOk);break;
			case CONTACT_AWAITING: result.add(contactAwaiting);break;
			case CONTACT_NONE: result.add(contactNone);break;
			case CONTACT_REQUESTED: result.add(contactRequested);break;
			}
			return result;
		}

		@Override
		public boolean isClickable(ContactInformation element)
		{
			return element.status != ContactStatus.CONTACT_OK;
		}
	}

	class FieldUpdaterContactInformation implements FieldUpdater<ContactInformation, ContactInformation> 
	{
		@Override
		public void update(int index, ContactInformation object, ContactInformation value)
		{
			updateContactStatus(object);
		}
	}

	class GetIconsMessage implements IGetIcons<ContactInformation>
	{
		@Override
		public Vector<ImageResource> getIcons(ContactInformation element)
		{
			Vector<ImageResource> result = new Vector<ImageResource>();
			if (element.status == ContactStatus.CONTACT_OK)
			{
				result.add(messageIcon);
			}
			return result;
		}

		@Override
		public boolean isClickable(ContactInformation element)
		{
			return element.status == ContactStatus.CONTACT_OK;
		}
	}

	class FieldUpdaterSendMessage implements FieldUpdater<ContactInformation, ContactInformation>
	{
		@Override
		public void update(int index, ContactInformation object, ContactInformation value)
		{
			if (object.status == ContactStatus.CONTACT_OK)
			{
				message(object);
			}
		}
	}	

	class ComparatorContactInformationStatus implements Comparator<ContactInformation>
	{

		public int compare(ContactInformation o1,
				ContactInformation o2)
		{
			return o1.status.compareTo(o2.status);
		}
	}
	
	class FieldUpdaterAddConnection implements FieldUpdater<UserSearchEntry, UserSearchEntry> 
	{
		@Override
		public void update(int index, UserSearchEntry newUser, UserSearchEntry value) {
			addConnection(newUser);
		}
	};

	class FieldUpdaterUpdateShares implements FieldUpdater<ContactInformation, ContactInformation> 
	{
		@Override
		public void update(int index, ContactInformation newUser, ContactInformation value) 
		{
			if (newUser.status == ContactStatus.CONTACT_OK)
			{
				IEditDialogModel<ContactInformation> myEditShares = editShares.clone(null, null, value, new IChooseResult<ContactInformation>()	{
					@Override
					public void setResult(ContactInformation result)
					{
						AsyncCallback<Void> callback = new ServerCallHelper<Void>(rootPanel);
						userService.updateShares(user, result.myVisibility, callback);
						cellTable.redraw();
					}});
				myEditShares.onModuleLoad();
			}
		}
	};

	class GetIconsAdd implements IGetIcons<UserSearchEntry>
	{
		@Override
		public Vector<ImageResource> getIcons(UserSearchEntry element)
		{
			Vector<ImageResource> result = new Vector<ImageResource>();
			if (!excludeUserNameList.contains(element.userName))
				result.add(addIcon);
			return result;
		}

		@Override
		public boolean isClickable(UserSearchEntry element)
		{
			return !excludeUserNameList.contains(element.userName);
		}
	}
	
	class GetShares implements IGetIcons<ContactInformation>
	{
		@Override
		public Vector<ImageResource> getIcons(ContactInformation element)
		{
			Vector<ImageResource> result = new Vector<ImageResource>();
			if (element.status != ContactStatus.CONTACT_OK)
			{
				for (int i=0; i != 8; i++) result.add(voidIcon);
			}
			else 
			{
				result.add(element.myVisibility.opportunity ? opportunityIcon : opportunityIconDisabled);
				result.add(element.myVisibility.log ? logIcon : logIconDisabled);
				result.add(element.myVisibility.contact ? addressIcon : addressIconDisabled);
				result.add(element.myVisibility.document ? documentIcon : documentIconDisabled);

				result.add(element.hisVisibility.opportunity ? opportunityIconThawed : opportunityIconDisabled);
				result.add(element.hisVisibility.log ? logIconThawed : logIconDisabled);
				result.add(element.hisVisibility.contact ? addressIconThawed : addressIconDisabled);
				result.add(element.hisVisibility.document ? documentIconThawed : documentIconDisabled);
			}
			return result;
		}

		@Override
		public boolean isClickable(ContactInformation element)
		{
			return (element.status == ContactStatus.CONTACT_OK);
		}
	}
	
	class GetIconsToDetail implements IGetIcons<ContactInformation>
	{
		@Override
		public Vector<ImageResource> getIcons(ContactInformation element)
		{
			Vector<ImageResource> result = new Vector<ImageResource>();
			if (element.status == ContactStatus.CONTACT_OK)
			{
				result.add(rightIcon);
			}
			return result;
		}

		@Override
		public boolean isClickable(ContactInformation element)
		{
			return element.status == ContactStatus.CONTACT_OK;
		}
	}

	private void onModuleLoad()
	{
		setSize("100%", "100%");

		ContentHelper.insertTitlePanel(this, lang.connectionTitle(), ClientImageBundle.INSTANCE.userConnectionContent());

		ContentHelper.insertSubTitlePanel(this, langConnection.connectionsSubtitle());

		// Create status column.
		cellTable.addClickableIconsColumn(
				new IGetIconsStatus(), 
				new FieldUpdaterContactInformation(), 
				"", "3em",
				new ComparatorContactInformationStatus());

		// Create message column.
		cellTable.addClickableIconsColumn(
				new GetIconsMessage(), 
				new FieldUpdaterSendMessage(), 
				"", "3em", 
				null);

		// shares ?
		cellTable.addClickableIconsColumn(
				new GetShares(), 
				new FieldUpdaterUpdateShares(), 
				langConnection.shares(), "3em", 
				null);
	
		cellTable.specialAddColumnSortableString(new GetValue<String, ContactInformation>() {
			@Override
			public String getValue(ContactInformation contact)
			{
				return contact.lastName + " " + contact.firstName;
			}			
		}, langConnection._TextLastName() + " " + langConnection._TextFirstName());

		cellTable.addClickableIconsColumn(
				new GetIconsToDetail(), 
				new FieldUpdater<ContactInformation, ContactInformation>() {
			@Override
			public void update(int index, ContactInformation object, ContactInformation value) {
				if (object.status == ContactStatus.CONTACT_OK)
					connectionToDetail.toDetail(object);
			}},"", "3em", null);

		this.add(cellTable);
		cellTable.setSize("100%", "");		

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		this.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		ContentHelper.insertSubTitlePanel(this, langConnection.searchSubtitle());

		HorizontalPanel horizontalPanelSearch = new HorizontalPanel();
		insertMiddle(horizontalPanelSearch, new Label(langConnection._TextLastName()));
		insertMiddle(horizontalPanelSearch, textBoxLastName);
		insertMiddle(horizontalPanelSearch, new Label(langConnection._TextFirstName()));
		insertMiddle(horizontalPanelSearch, textBoxFirstName);
		buttonRunSearch = new ButtonImageText(ButtonImageText.Type.NEW, langConnection.runSearch());
		insertMiddle(horizontalPanelSearch, buttonRunSearch);
		RunSearchHandler runSearchHandler = new RunSearchHandler();
		buttonRunSearch.addClickHandler(runSearchHandler);

		this.add(horizontalPanelSearch);
		this.add(cellTableSearchResult);
		cellTableSearchResult.setVisible(false);
		cellTableSearchResult.setSize("100%", "");

		// name column.
		cellTableSearchResult.specialAddColumnSortableString(new GetValue<String, UserSearchEntry>() {
			@Override
			public String getValue(UserSearchEntry contact)
			{
				return contact.firstName + " " + contact.lastName + " (" + contact.userName + ")";
			}			
		},  langConnection._TextFirstName());

		// Create job title column.
		cellTableSearchResult.specialAddColumnSortableString(new GetValue<String, UserSearchEntry>() {
			@Override
			public String getValue(UserSearchEntry contact)
			{
				return contact.job;
			}
		},  langConnection.jobTitle());

		// Create add connection column.
		cellTableSearchResult.addClickableIconsColumn(
				new GetIconsAdd(), 
				new FieldUpdaterAddConnection(), 
				"", "3em",
				null);
		
		this.add(detailContainer);
		detailContainer.setSize("100%", "100%");
		detailContainer.setVisible(false);
		
		
		getAllContent();		
	}
}

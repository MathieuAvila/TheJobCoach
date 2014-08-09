package com.TheJobCoach.webapp.userpage.client.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.webapp.CatcherMessageBoxTriState;
import com.TheJobCoach.webapp.ErrorCatcherMessageBox;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchEntry;
import com.TheJobCoach.webapp.adminpage.shared.UserSearchResult;
import com.TheJobCoach.webapp.userpage.client.DefaultUserServiceAsync;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.IconsCell;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserId.UserType;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@GwtModule("com.TheJobCoach.webapp.userpage.UserPage")
public class AutoTestContentConnectionDetail extends GwtTest {

	UserId userId = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);
	UserId userIdTest = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER, true);

	ContactInformation ci1 = new ContactInformation(ContactStatus.CONTACT_OK, 
			"u1", "firstName1", "lastName1", new Visibility(), new Visibility());
	ContactInformation ci2 = new ContactInformation(ContactStatus.CONTACT_REQUESTED, 
			"u2", "firstName2", "lastName2", new Visibility(), new Visibility());
	ContactInformation ci3 = new ContactInformation(ContactStatus.CONTACT_AWAITING, 
			"u3", "firstName3", "lastName3", new Visibility(), new Visibility());

	// added after request
	ContactInformation ci_new = new ContactInformation(ContactStatus.CONTACT_REQUESTED, 
			"new_user", "firstName_new", "lastName_new", new Visibility(), new Visibility());

	Vector<ContactInformation> contactList =  new Vector<ContactInformation>(Arrays.asList(ci1, ci2, ci3));

	UserSearchEntry s_userId = new UserSearchEntry(userId.userName, "firstName", "lastName", "job", UserType.USER_TYPE_SEEKER);
	UserSearchEntry s_ci1 = new UserSearchEntry(ci1.userName, "firstName1", "lastName1", "job1", UserType.USER_TYPE_SEEKER);
	UserSearchEntry s_new = new UserSearchEntry("new_user", "firstName_new", "lastName_new", "job_new", UserType.USER_TYPE_SEEKER);

	Vector<UserSearchEntry> searchResultEntries = new Vector<UserSearchEntry>(Arrays.asList(s_userId, s_ci1, s_new));
	UserSearchResult searchResult = new UserSearchResult(searchResultEntries, 15);

	class SpecialUserServiceAsync extends DefaultUserServiceAsync
	{
		public int callsGet, callsMessage, callsUpdate, callsSearch, callsUpdateShares;
		public UserId userContact;
		public boolean ok;
		public String message;

		Logger logger = LoggerFactory.getLogger(SpecialUserServiceAsync.class);

		public void reset()
		{
			callsGet = callsMessage = callsUpdate = callsSearch = 0;
			ok = false;
			userContact = null;
			message = null;
		}

		@Override
		public void updateContactRequest(UserId userContact, boolean ok,
				AsyncCallback<ContactStatus> callback)
		{
			this.ok = ok;
			this.userContact = userContact;
			callsUpdate++;
			callback.onSuccess(ContactStatus.CONTACT_OK);
		}

		@Override
		public void getContactList(
				AsyncCallback<Vector<ContactInformation>> callback)
		{
			callsGet++;	
			logger.info("getContactList");
			callback.onSuccess(contactList);
		}
		@Override
		public void sendJobMail(UserId userContact, String message,
				AsyncCallback<Boolean> callback)
		{
			this.userContact = userContact;
			this.message = message;
			callsMessage++;
			callback.onSuccess(true);
		}

		@Override
		public void searchUsers(UserId id, String firstName, String lastName,
				int sizeRange, int startRange,
				AsyncCallback<UserSearchResult> callback)
		{
			logger.info("searchUsers " + id.userName + " firstName " + firstName + " lastName " + lastName + " sizeRange " + sizeRange + " startRange " + startRange);
			callsSearch++;
			assertEquals("first", firstName);
			assertEquals("last", lastName);
			assertEquals(10, sizeRange);
			assertEquals(0, startRange);
			callback.onSuccess(searchResult);
		}
		@Override
		public void updateShares(String userContact, Visibility contact,
				AsyncCallback<Void> callback)
		{
			logger.info("updateShares " + userContact);
			callsUpdateShares++;
			callback.onSuccess(null);
		}
	}

	SpecialUserServiceAsync userService = new SpecialUserServiceAsync();

	@Before
	public void beforeContentExternalContact()
	{
		addGwtCreateHandler(new GwtCreateHandler () {

			@Override
			public Object create(Class<?> arg0) throws Exception {
				if (arg0.getCanonicalName().equals("com.TheJobCoach.webapp.userpage.client.UserService"))
				{
					return userService;
				}
				return null;
			}}
				);
	}

	static final int COLUMN_STATUS        = 0;
	static final int COLUMN_MESSAGE       = 1;
	static final int COLUMN_SHARES        = 2;
	static final int COLUMN_NAME          = 3;
	static final int COLUMN_TODETAIL      = 4;

	class SendMessageTest implements ISendMessage
	{
		UserId contact; 
		String firstName;
		String lastName;
		int counter = 0;

		@Override
		public ISendMessage sendMessage(Panel panel, UserId contact, String firstName,
				String lastName)
		{
			this.contact = contact; 
			this.firstName = firstName;
			this.lastName = lastName;
			counter++;
			return null;
		}
	}

	static int COLUMN_SEARCH_NAME = 0;
	static int COLUMN_SEARCH_JOB = 1;
	static int COLUMN_SEARCH_ADD = 2;

	CatcherMessageBoxTriState mbTriStateCatcher = new CatcherMessageBoxTriState();
	ErrorCatcherMessageBox mbCatcher = new ErrorCatcherMessageBox();

	class ConnectionToDetailTest implements IConnectionToDetail
	{
		public int countToDetail = 0;
		public ContactInformation lastContact = null;
		
		@Override
		public void toDetail(ContactInformation connectionUser)
		{
			lastContact = connectionUser;
			countToDetail++;
		}
		@Override
		public void toConnections()
		{
			assertTrue(false);
		}				
	}

	public class TestEditShares implements IEditDialogModel<ContactInformation> 
	{
		public UserId userId;
		public ContactInformation edition;
		IChooseResult<ContactInformation> result;

		@Override
		public IEditDialogModel<ContactInformation> clone(Panel rootPanel,
				UserId userId, ContactInformation edition,
				IChooseResult<ContactInformation> result) {
			TestEditShares tes = new TestEditShares(userId, edition, result);
			vTestEditShares.add(tes);
			return tes;
		}

		TestEditShares(UserId userId, ContactInformation edition,
				IChooseResult<ContactInformation> result) {
			this.result = result;
			this.userId = userId;
			this.edition = edition;
		}
		
		TestEditShares() {}
		
				@Override
				public void onModuleLoad()
		{
		}	
	}

	Vector<TestEditShares> vTestEditShares = new Vector<TestEditShares>();

	@Test
	public void testAll() throws InterruptedException
	{
		if (mbCatcher.currentBox != null) mbCatcher.currentBox.close();
		if (mbTriStateCatcher.currentBox != null) mbTriStateCatcher.currentBox.close();

		SendMessageTest sendMessage = new SendMessageTest();
		ConnectionToDetailTest toDetail = new ConnectionToDetailTest();
		TestEditShares editShares = new TestEditShares();
		
		// create click event for further use.
		Event event = EventBuilder.create(Event.ONCLICK).build();

		ContentConnection cud = new ContentConnection(userId, sendMessage, toDetail, editShares);
		assertEquals(1, userService.callsGet);
		assertEquals(3, cud.cellTable.getRowCount());

		assertEquals(ci1.userName, cud.cellTable.getVisibleItem(0).userName);
		assertEquals(ci2.userName, cud.cellTable.getVisibleItem(1).userName);
		assertEquals(ci3.userName, cud.cellTable.getVisibleItem(2).userName);

		// Check columns values

		// Column name

		assertEquals(5, cud.cellTable.getColumnCount());
		assertEquals("lastName1 firstName1",           cud.cellTable.getColumn(COLUMN_NAME).getValue(ci1));

		// Column shares

		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> cShares = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_SHARES).getCell();
		// no click on unconnected contact.
		assertFalse(cShares.getIcons.isClickable(ci2));
		// click on connected contact.
		assertTrue(cShares.getIcons.isClickable(ci1));
		cud.cellTable.getColumn(COLUMN_SHARES).onBrowserEvent(new Cell.Context(1, COLUMN_SHARES, ci1), cud.cellTable.getElement(), ci1, EventBuilder.create(Event.ONCLICK).build());
		assertEquals(1, vTestEditShares.size());
		assertEquals(vTestEditShares.get(0).edition, ci1);
		// If we accept, it triggers a call to update the shares and a redraw without a GET call to the server.
		vTestEditShares.get(0).result.setResult(ci1);
		assertEquals(1, userService.callsGet);
		assertEquals(1, userService.callsUpdateShares);
		
		// C0 = status

		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> c0 = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_STATUS).getCell();
		Vector<ImageResource> v0 = c0.getIcons.getIcons(ci1);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactOk, v0.get(0));
		// Check clicking is useless
		assertFalse(c0.getIcons.isClickable(ci1));

		v0 = c0.getIcons.getIcons(ci2);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactRequested, v0.get(0));
		// Check clicking triggers a message box tri state
		assertTrue(c0.getIcons.isClickable(ci2));
		assertNull(mbTriStateCatcher.currentBox);
		cud.cellTable.getColumn(COLUMN_STATUS).onBrowserEvent(new Cell.Context(1, COLUMN_STATUS, ci2), cud.cellTable.getElement(), ci2, EventBuilder.create(Event.ONCLICK).build());
		assertNotNull(mbTriStateCatcher.currentBox);
		System.out.println(" TITLE " + mbTriStateCatcher.message);
		assertTrue(mbTriStateCatcher.message.contains(ci2.firstName));
		assertTrue(mbTriStateCatcher.message.contains(ci2.lastName));
		// 2 possibilities: either accept...
		userService.reset();
		cud.cellTable.getColumn(COLUMN_STATUS).onBrowserEvent(new Cell.Context(1, COLUMN_STATUS, ci2), cud.cellTable.getElement(), ci2, EventBuilder.create(Event.ONCLICK).build());
		assertNotNull(mbTriStateCatcher.currentBox);
		mbTriStateCatcher.currentBox.clickChoice(0); // accept
		assertEquals(1, userService.callsUpdate);
		assertEquals(ci2.userName, userService.userContact.userName);
		assertEquals(1, userService.callsGet); // updated with result
		assertEquals(true, userService.ok);
		// .. or refuse.
		userService.reset();
		mbTriStateCatcher.closeBox();
		cud.cellTable.getColumn(COLUMN_STATUS).onBrowserEvent(new Cell.Context(1, COLUMN_STATUS, ci2), cud.cellTable.getElement(), ci2, EventBuilder.create(Event.ONCLICK).build());
		assertNotNull(mbTriStateCatcher.currentBox);
		mbTriStateCatcher.currentBox.clickChoice(1); // refuse
		assertEquals(1, userService.callsUpdate);
		assertEquals(ci2.userName, userService.userContact.userName);
		assertEquals(1, userService.callsGet); // updated with result
		assertEquals(false, userService.ok);
		// or nothing
		userService.reset();
		mbTriStateCatcher.closeBox();
		cud.cellTable.getColumn(COLUMN_STATUS).onBrowserEvent(new Cell.Context(1, COLUMN_STATUS, ci2), cud.cellTable.getElement(), ci2, event);
		assertNotNull(mbTriStateCatcher.currentBox);
		mbTriStateCatcher.currentBox.clickChoice(-1); // cancel
		assertEquals(0, userService.callsUpdate);
		assertEquals(0, userService.callsGet); // updated with result

		v0 = c0.getIcons.getIcons(ci3);
		assertEquals(1, v0.size());
		assertEquals(ContentConnection.contactAwaiting, v0.get(0));
		// Check clicking triggers a message box info
		assertTrue(c0.getIcons.isClickable(ci3));

		ContentConnection.FieldUpdaterContactInformation fuCI = (ContentConnection.FieldUpdaterContactInformation)cud.cellTable.getColumn(0).getFieldUpdater();
		fuCI.update(0, ci2, ci2);

		// C1 = message
		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> c1 = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_MESSAGE).getCell();
		Vector<ImageResource> v1 = c1.getIcons.getIcons(ci1);
		// message box for OK contact
		assertEquals(1, v1.size());
		assertEquals(ContentConnection.messageIcon, v1.get(0));
		// no message box for NOK contact 1
		v1 = c1.getIcons.getIcons(ci2);
		assertEquals(0, v1.size());
		// no message box for NOK contact 2
		v1 = c1.getIcons.getIcons(ci3);
		assertEquals(0, v1.size());

		// Send message to ci2/ci3 => impossible
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci2), cud.cellTable.getElement(), ci2, event);
		assertEquals(0, sendMessage.counter);
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci3), cud.cellTable.getElement(), ci3, event);
		assertEquals(0, sendMessage.counter);
		// send message to ci1
		cud.cellTable.getColumn(COLUMN_MESSAGE).onBrowserEvent(new Cell.Context(1, COLUMN_MESSAGE, ci1), cud.cellTable.getElement(), ci1, event);
		assertEquals(1, sendMessage.counter);
		
		// Column TO_DETAIL

		@SuppressWarnings("unchecked")
		IconsCell<ContactInformation> cTodetail = (IconsCell<ContactInformation>)cud.cellTable.getColumn(COLUMN_TODETAIL).getCell();
		// no click on unconnected contact.
		assertFalse(cTodetail.getIcons.isClickable(ci2));
		// click on connected contact.
		assertTrue(cTodetail.getIcons.isClickable(ci1));
		cud.cellTable.getColumn(COLUMN_TODETAIL).onBrowserEvent(new Cell.Context(1, COLUMN_TODETAIL, ci1), cud.cellTable.getElement(), ci1, EventBuilder.create(Event.ONCLICK).build());
		assertEquals(1, toDetail.countToDetail);
		assertEquals(toDetail.lastContact, ci1);
		
		// Search engine
		// not visible
		assertFalse(cud.cellTableSearchResult.isVisible());
		cud.textBoxFirstName.setValue("first");
		cud.textBoxLastName.setValue("last");

		userService.reset();
		cud.buttonRunSearch.click();
		// service called, search result visible and with appropriate results: 
		// text + add button is present and clickable only for username not already listed, NOR myself 
		assertEquals(1, userService.callsSearch);
		assertTrue(cud.cellTableSearchResult.isVisible());

		assertEquals(searchResultEntries.size(), cud.cellTableSearchResult.getColumnCount());
		for (int i=0; i != searchResultEntries.size(); i++)
		{
			assertEquals(searchResultEntries.get(i), cud.cellTableSearchResult.getVisibleItem(i));
			UserSearchEntry sr = searchResultEntries.get(i);
			assertEquals(sr.firstName + " " + sr.lastName + " (" + sr.userName + ")", cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_NAME).getValue(searchResultEntries.get(i)));
			assertEquals(sr.job, cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_JOB).getValue(searchResultEntries.get(i)));
		}
		// check the "add" column
		@SuppressWarnings("unchecked")
		IconsCell<UserSearchEntry> c_add = (IconsCell<UserSearchEntry>)cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_ADD).getCell();
		// I cannot add myself
		Vector<ImageResource> v_search_add = c_add.getIcons.getIcons(s_userId);
		assertEquals(0, v_search_add.size());
		assertEquals(false, c_add.getIcons.isClickable(s_userId));
		// I cannot add a user already added
		v_search_add = c_add.getIcons.getIcons(s_ci1);
		assertEquals(0, v_search_add.size());
		assertEquals(false, c_add.getIcons.isClickable(s_ci1));
		// I can add another user
		v_search_add = c_add.getIcons.getIcons(s_new);
		assertEquals(1, v_search_add.size());
		assertEquals(ContentConnection.addIcon, v_search_add.get(0));
		assertEquals(true, c_add.getIcons.isClickable(s_new));

		// Add new user. Please do. I will have a message box, service is called and refreshed with new user, and search results are hidden.
		userService.reset();
		contactList.add(ci_new);
		cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_ADD).onBrowserEvent(new Cell.Context(1, COLUMN_SEARCH_ADD, s_new), cud.cellTableSearchResult.getElement(), s_new, event);
		assertEquals(1, userService.callsUpdate);
		assertEquals(1, userService.callsGet);
		assertFalse(cud.cellTableSearchResult.isVisible());
		assertNotNull(mbCatcher.currentBox);
		assertTrue(mbCatcher.message.contains(s_new.firstName));
		assertTrue(mbCatcher.message.contains(s_new.lastName));
		assertEquals(MessageBox.TYPE.INFO, mbCatcher.type);
		mbCatcher.currentBox.close();
		assertEquals(4, cud.cellTable.getRowCount()); // one more !
	}

	@Test
	public void testTestAccount() throws InterruptedException
	{
		if (mbCatcher.currentBox != null) mbCatcher.currentBox.close();
		if (mbTriStateCatcher.currentBox != null) mbTriStateCatcher.currentBox.close();

		SendMessageTest sendMessage = new SendMessageTest();

		// create click event for further use.
		Event event = EventBuilder.create(Event.ONCLICK).build();

		ContentConnection cud = new ContentConnection(userIdTest, sendMessage, new ConnectionToDetailTest(), new TestEditShares());

		// make a search, click on first result.
		// Search engine
		// not visible
		cud.textBoxFirstName.setValue("first");
		cud.textBoxLastName.setValue("last");
		userService.reset();
		cud.buttonRunSearch.click();
		@SuppressWarnings("unchecked")
		IconsCell<UserSearchEntry> c_add = (IconsCell<UserSearchEntry>)cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_ADD).getCell();
		assertEquals(true, c_add.getIcons.isClickable(s_new));
		cud.cellTableSearchResult.getColumn(COLUMN_SEARCH_ADD).onBrowserEvent(new Cell.Context(1, COLUMN_SEARCH_ADD, s_new), cud.cellTableSearchResult.getElement(), s_new, event);

		// Error message
		assertTrue(mbCatcher.hasError());
		assertEquals(MessageBox.TYPE.ERROR, mbCatcher.type);
		assertEquals("Vous ne pouvez pas envoyer de demande de connection dans un compte de test.", mbCatcher.message);
		mbCatcher.clearError();
	}


	@Test
	public void testSharesIcons()
	{
		ContentConnection cud = new ContentConnection(userIdTest, null, null, null);
		ContentConnection.GetShares getShares = cud.new GetShares();
		List<Boolean> bArray = Arrays.asList(true, false);
		for (ContactStatus s : ContactStatus.values())
		{
			for (boolean mo : bArray)
			{
				for (boolean ml : bArray)
				{
					for (boolean mc : bArray)
					{
						for (boolean md : bArray)
						{

							for (boolean ho : bArray)
							{
								for (boolean hl : bArray)
								{
									for (boolean hc : bArray)
									{
										for (boolean hd : bArray)
										{
											ContactInformation ci = new ContactInformation(
													s, "",
													"", "", 
													new Visibility(md, mc, mo, ml),
													new Visibility(hd, hc, ho, hl));
											Vector<ImageResource> vImage = getShares.getIcons(ci);
											assertEquals(vImage.size(), 8);
											if (ContactStatus.CONTACT_OK == s)
											{
												assertTrue(vImage.get(0) == (mo ? ContentConnection.opportunityIcon : ContentConnection.opportunityIconDisabled));
												assertTrue(vImage.get(1) == (ml ? ContentConnection.logIcon : ContentConnection.logIconDisabled));
												assertTrue(vImage.get(2) == (mc ? ContentConnection.addressIcon : ContentConnection.addressIconDisabled));
												assertTrue(vImage.get(3) == (md ? ContentConnection.documentIcon : ContentConnection.documentIconDisabled));

												assertTrue(vImage.get(4) == (ho ? ContentConnection.opportunityIconThawed : ContentConnection.opportunityIconDisabled));
												assertTrue(vImage.get(5) == (hl ? ContentConnection.logIconThawed : ContentConnection.logIconDisabled));
												assertTrue(vImage.get(6) == (hc ? ContentConnection.addressIconThawed : ContentConnection.addressIconDisabled));
												assertTrue(vImage.get(7) == (hd ? ContentConnection.documentIconThawed : ContentConnection.documentIconDisabled));
											}
											else
											{
												for (int i=0; i<8; i++)
												{
													assertTrue(vImage.get(i) == ContentConnection.voidIcon);

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

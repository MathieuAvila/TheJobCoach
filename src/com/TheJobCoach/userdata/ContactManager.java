package com.TheJobCoach.userdata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.util.CassandraAccessor;
import com.TheJobCoach.util.Convertor;
import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MailerInterface;
import com.TheJobCoach.util.ShortMap;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.Visibility;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class ContactManager implements IUserDataManager, UserValues.ValueCallback
{	
	private static Logger logger = LoggerFactory.getLogger(ContactManager.class);

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefDataName = null;
	static ColumnFamilyDefinition cfDefStatus = null;

	final static String COLUMN_FAMILY_NAME_CONTACTLIST = "contactlist";
	final static String COLUMN_FAMILY_NAME_CONTACTNAME = "contactname";
	final static String COLUMN_FAMILY_NAME_CONTACTSTATUS = "contactstatus";

	UserId user;
	UserValues userValues;
	AccountManager accountManager;

	public ContactManager(UserId user)
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTACTLIST, cfDef);
		cfDefDataName = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTACTNAME, cfDefDataName);
		cfDefStatus = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTACTSTATUS, cfDefStatus);
		this.user = user;
		userValues = new UserValues();
		accountManager = new AccountManager();
	}

	static {
		ContactManager voidCallback = new ContactManager();
		UserValues.registerCallback(UserValuesConstantsAccount.ACCOUNT_EMAIL, voidCallback);
		UserValues.registerCallback(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME, voidCallback);
		UserValues.registerCallback(UserValuesConstantsAccount.ACCOUNT_LASTNAME, voidCallback);
	}

	public ContactManager()
	{
	}

	static char contactStatusToChar(ContactInformation.ContactStatus status)
	{
		switch(status)
		{
		case CONTACT_AWAITING:
			return 'A';
		case CONTACT_OK:
			return 'O';
		case CONTACT_REQUESTED:
			return 'R';
		default:
			return ' ';
		}
	}

	/** Returns the rights "user" has granted to the other user */
	static ContactInformation.ContactStatus charToContactStatus(char status)
	{
		if (status == ' ') return ContactInformation.ContactStatus.CONTACT_NONE;
		if (status == 'A') return ContactInformation.ContactStatus.CONTACT_AWAITING;
		if (status == 'O') return ContactInformation.ContactStatus.CONTACT_OK;
		if (status == 'R') return ContactInformation.ContactStatus.CONTACT_REQUESTED;
		return ContactInformation.ContactStatus.CONTACT_NONE;
	}

	/** Serialize the rights "user" has granted to the other user */
	static String serializeContactInformation(ContactInformation contact)
	{
		return 
				contactStatusToChar(contact.status) + 
				Convertor.toString(contact.myVisibility.document) +
				Convertor.toString(contact.myVisibility.contact) + 
				Convertor.toString(contact.myVisibility.opportunity) + 
				Convertor.toString(contact.myVisibility.log) +
				Convertor.toString(contact.hisVisibility.document) +
				Convertor.toString(contact.hisVisibility.contact) + 
				Convertor.toString(contact.hisVisibility.opportunity) + 
				Convertor.toString(contact.hisVisibility.log);
	}

	/** deserialize the rights as "user" has granted them to the other user */
	static ContactInformation deserializeContactInformation(String contact)
	{
		if (contact == null)
			return new ContactInformation();
		ContactInformation result = new ContactInformation(
				charToContactStatus(contact.charAt(0)),
				contact, "", "",
				new ContactInformation.Visibility(
						Convertor.toBoolean(String.valueOf(contact.charAt(1))),
						Convertor.toBoolean(String.valueOf(contact.charAt(2))),
						Convertor.toBoolean(String.valueOf(contact.charAt(3))),
						Convertor.toBoolean(String.valueOf(contact.charAt(4)))
						),
						new ContactInformation.Visibility(
								Convertor.toBoolean(String.valueOf(contact.charAt(5))),
								Convertor.toBoolean(String.valueOf(contact.charAt(6))),
								Convertor.toBoolean(String.valueOf(contact.charAt(7))),
								Convertor.toBoolean(String.valueOf(contact.charAt(8)))
								)
				);
		return result;
	}

	void updateOneContactClearance(String currentUser, ContactInformation info) throws CassandraException
	{
		logger.info("Update connection clearance for user: " + currentUser + " about user: " + info.userName + " to status " + info.status);
		Map<String, String> mapUpdate = new HashMap<String, String>();
		mapUpdate.put(info.userName, serializeContactInformation(info));
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTLIST, currentUser, mapUpdate);
	}

	void updateOneContactStatusOnOneSide(String currentUser, ContactInformation info) throws CassandraException
	{
		logger.info("Update connection status for user: " + currentUser + " about user: " + info.userName + " to status " + info.status);
		updateOneContactClearance(currentUser, info);

		Map<String, String> mapNameUpdate = new HashMap<String, String>();
		mapNameUpdate.put("f#" + info.userName, info.firstName);
		mapNameUpdate.put("l#" + info.userName, info.lastName);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTNAME, currentUser, mapNameUpdate);
	}

	void deleteOneContactStatusOnOneSide(String currentUser, String otherUser) throws CassandraException
	{
		logger.info("Delete connection for user: " + currentUser + " about user: " + otherUser);
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_CONTACTLIST, currentUser, otherUser);
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_CONTACTNAME, currentUser, "f#" + otherUser);
		CassandraAccessor.deleteColumn(COLUMN_FAMILY_NAME_CONTACTNAME, currentUser, "l#" + otherUser);
	}


	/** Requesting a contact to a user OR accept contact request. Adds to the list of contacts. Sends a request mail if necessary
	 * @param userContact User to request contact to.
	 * @param ok In case we are in CONTACT_REQUESTED, true means accept, false means refuse
	 * @return The request status.
	 * @throws CassandraException 
	 */
	public ContactInformation.ContactStatus updateContactRequest(UserId userContact, boolean ok) throws CassandraException
	{
		// Check whether we have requested such contact.
		String connectInfoStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName, userContact.userName);
		ContactInformation contactInfo = deserializeContactInformation(connectInfoStr);
		contactInfo.userName = userContact.userName;

		UserInformation myInfo  = new UserInformation();
		boolean hasUserInfo = accountManager.getUserInformation(user, myInfo);
		if (!hasUserInfo)
		{
			logger.warn("User " + user.userName + " has no user information.");
			return ContactInformation.ContactStatus.CONTACT_NONE;
		}
		UserInformation contactUserInfo = new UserInformation();
		boolean contactHasUserInfo = accountManager.getUserInformation(userContact, contactUserInfo);
		if (!contactHasUserInfo)
		{
			logger.warn("Requested contact name " + userContact.userName + " has no user information.");
			return ContactInformation.ContactStatus.CONTACT_NONE;
		}
		contactInfo.firstName = contactUserInfo.firstName;
		contactInfo.lastName = contactUserInfo.name;

		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));
		String lang = null;		
		try { 
			Map<String, String> langMap = userValues.getValues(user, UserValuesConstantsAccount.ACCOUNT_LANGUAGE); 
			lang = langMap.get(UserValuesConstantsAccount.ACCOUNT_LANGUAGE);
		}
		catch (SystemException e) {} // this cannot happen

		switch (contactInfo.status)
		{
		case CONTACT_OK: // already connected. No need to go further.
			logger.info("" + user.userName + " is already connected to " + userContact.userName + ". Ignore request");
			return ContactInformation.ContactStatus.CONTACT_OK;

		case CONTACT_REQUESTED: // received a connection request, accept.
		{
			if (ok)
			{
				logger.info(user.userName + " is accepting connection request from: " + userContact.userName);

				contactInfo.status = ContactStatus.CONTACT_OK;
				// me
				updateOneContactStatusOnOneSide(user.userName, contactInfo);
				// him
				ContactInformation hisContactInfo = new ContactInformation(
						ContactStatus.CONTACT_OK,
						user.userName, myInfo.firstName, myInfo.name,
						// by default, share nothing with my contacts
						new ContactInformation.Visibility(false, false, false, false),
						new ContactInformation.Visibility(false, false, false, false));
				updateOneContactStatusOnOneSide(userContact.userName, hisContactInfo);

				// send him an email about our new partnership
				String body = Lang.connectionGranted(lang, contactUserInfo.firstName, contactUserInfo.name, user.userName, myInfo.firstName, myInfo.name);
				MailerFactory.getMailer().sendEmail(contactUserInfo.email, Lang.connectionGrantedSubject(lang), body, "noreply@www.thejobcoach.fr", parts);

				return ContactInformation.ContactStatus.CONTACT_OK;
			}
			else
			{
				logger.info(user.userName + " is refusing connection request from: " + userContact.userName);

				// remove both request and result
				deleteOneContactStatusOnOneSide(userContact.userName, user.userName);
				deleteOneContactStatusOnOneSide(user.userName, userContact.userName);

				// send him an email about rejection
				String body = Lang.connectionRefused(lang, contactUserInfo.firstName, contactUserInfo.name, user.userName, myInfo.firstName, myInfo.name);
				MailerFactory.getMailer().sendEmail(contactUserInfo.email, Lang.connectionRefusedSubject(lang), body, "noreply@www.thejobcoach.fr", parts);

				return ContactInformation.ContactStatus.CONTACT_NONE;				
			}
		}
		case CONTACT_NONE: // send request
		{
			if (user.testAccount)
			{
				// Refuse. This is server security, the client part is less violent.
				// If we arrive here, it is a hand-crafted request.
				logger.error("SECURITY: Forbid test account: " + user.userName + " is requesting connection request to: " + userContact.userName);
				return ContactStatus.CONTACT_NONE;
			}

			logger.info(user.userName + " is requesting connection request to: " + userContact.userName);

			// me
			contactInfo.status = ContactStatus.CONTACT_AWAITING;
			logger.info(user.userName + " is requesting connection request to: " + contactInfo.userName);
			updateOneContactStatusOnOneSide(user.userName, contactInfo);

			// him
			ContactInformation hisContactInfo = new ContactInformation(
					ContactStatus.CONTACT_REQUESTED,
					user.userName, myInfo.firstName, myInfo.name,
					new ContactInformation.Visibility(false, false, false, false),
					new ContactInformation.Visibility(false, false, false, false));
			updateOneContactStatusOnOneSide(userContact.userName, hisContactInfo);

			// send him an email about partnership request
			String body = Lang.requestConnection(lang, myInfo.firstName, myInfo.name, user.userName, contactUserInfo.firstName, contactUserInfo.name);
			if (false == MailerFactory.getMailer().sendEmail(contactUserInfo.email, Lang.requestConnectionSubject(lang), body, "noreply@www.thejobcoach.fr", parts))
				logger.error("Unexpected error in sending email: ");

			return ContactInformation.ContactStatus.CONTACT_REQUESTED;
		}
		case CONTACT_AWAITING: // already requested. What should we do ? Resend request ? We don't, this could cause spam.
			logger.info(user.userName + " is requesting AGAIN a connection to: " + userContact.userName + ". Ignore");

			return ContactInformation.ContactStatus.CONTACT_NONE;
		}
		return ContactInformation.ContactStatus.CONTACT_NONE;
	}

	/**
	 * Get all the list of contacts and associated statuses
	 * @return list of contacts
	 * @throws CassandraException 
	 */
	public Vector<ContactInformation> getContactList() throws CassandraException
	{
		Vector<ContactInformation> result = new Vector<ContactInformation>();
		Map<String, String> contactTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName);
		Map<String, String> contactNameTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTNAME, user.userName);
		Map<String, String> contactNameStatus = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTSTATUS, user.userName);

		if ((contactTable == null) || (contactNameTable == null)) return result;
		for (String contactUsername: contactTable.keySet())
		{
			ContactInformation contact = deserializeContactInformation(contactTable.get(contactUsername));
			contact.userName = contactUsername;
			contact.firstName = contactNameTable.get("f#" + contactUsername);
			contact.lastName = contactNameTable.get("l#" + contactUsername);
			if (contactNameStatus != null)
			{
				System.out.println("from " + user.userName + " status for:" + contactUsername +  " = " + contactNameStatus.get(contactUsername));
				String st = contactNameStatus.get(contactUsername);
				if (st != null)
					contact.online = ("i".equals(st));
			}
			result.add(contact);
		}
		return result;
	}

	/**
	 * Get all the list of contacts and associated statuses
	 * @return list of contacts
	 * @throws CassandraException 
	 */
	public Vector<ContactInformation> getUpdatedContactList() throws CassandraException
	{
		Vector<ContactInformation> result = new Vector<ContactInformation>();
		Map<String, String> contactTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName);
		Map<String, String> contactNameTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTNAME, user.userName);
		
		if ((contactTable == null) || (contactNameTable == null)) return result;
		for (String contactUsername: contactTable.keySet())
		{
			String lastContact = contactNameTable.get("last#" + contactUsername);
			String newContact = contactTable.get(contactUsername);
			ContactInformation contact = deserializeContactInformation(newContact);
			System.out.println("check user: " + user.userName + " with: " +contactUsername+ " " + contact.status);
			if (contact.status == ContactStatus.CONTACT_OK)
			{
				if ((lastContact == null) || (!lastContact.equals(newContact)))
				{
					// check something is MORE shared. Don't advert for loss of share, that would create him a nuclear psychologic attack.
					// Always take care of your user. He needs being comforted, not frustrated. Really.
					ContactInformation lastDataContact = (lastContact == null) ? new ContactInformation() : deserializeContactInformation(lastContact);
					if ( 
							(!lastDataContact.hisVisibility.document && contact.hisVisibility.document) ||
							(!lastDataContact.hisVisibility.opportunity && contact.hisVisibility.opportunity) ||
							(!lastDataContact.hisVisibility.log && contact.hisVisibility.log) ||
							(!lastDataContact.hisVisibility.contact && contact.hisVisibility.contact)
							)
					{				
						contact.userName = contactUsername;
						contact.firstName = contactNameTable.get("f#" + contactUsername);
						contact.lastName = contactNameTable.get("l#" + contactUsername);
						result.add(contact);
						// update to latest. theoretically we should update after sending the email, for coherence reasons.
						logger.info("Update last connection clearance for user: " + user.userName + " about user: " + contact.userName);
						Map<String, String> mapUpdate = new HashMap<String, String>();
						mapUpdate.put("last#" + contactUsername, newContact);
						CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTNAME, user.userName, mapUpdate);
					}
				}
			}
		}
		return result;
	}

	/** Send a mail to another user
	 * @param userContact User to request contact to.
	 * @return TRUE if everything is fine
	 * @throws CassandraException 
	 * @throws SystemException 
	 */
	public boolean sendJobMail(UserId userContact, String message) throws CassandraException, SystemException
	{
		// Check we are connected. Otherwise this is a security error.
		String connectInfoStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName, userContact.userName);
		ContactInformation contactInfo = deserializeContactInformation(connectInfoStr);
		if (contactInfo.status != ContactInformation.ContactStatus.CONTACT_OK)
			throw new SystemException();

		// using recipient's language
		String lang = accountManager.getUserLanguage(userContact);

		// get information about both participants
		UserInformation myInfo  = new UserInformation();
		boolean hasUserInfo = accountManager.getUserInformation(user, myInfo);
		if (!hasUserInfo)
		{
			logger.warn("User " + user.userName + " has no user information.");
			return false;
		}
		UserInformation contactUserInfo = new UserInformation();
		boolean contactHasUserInfo = accountManager.getUserInformation(userContact, contactUserInfo);
		if (!contactHasUserInfo)
		{
			logger.warn("Requested contact name " + userContact.userName + " has no user information.");
			return false;
		}

		// prepare mail
		Map<String, MailerInterface.Attachment> parts = new HashMap<String, MailerInterface.Attachment>();
		parts.put("thejobcoachlogo", new MailerInterface.Attachment("/com/TheJobCoach/webapp/mainpage/client/thejobcoach-icon.png", "image/png", "img_logo.png"));

		// send email
		String body = Lang.jobMail(lang, contactUserInfo.firstName, contactUserInfo.name, user.userName, myInfo.firstName, myInfo.name, message);
		boolean sent = MailerFactory.getMailer().sendEmail(contactUserInfo.email, Lang.jobMailSubject(lang), body, "noreply@www.thejobcoach.fr", parts);
		if (!sent)
			logger.warn("Failed to send email to "+contactUserInfo.email + ", to user: " + userContact.userName);
		return sent;
	}

	public ContactInformation getUserClearance(UserId userContact) throws CassandraException, SystemException
	{
		if (userContact.userName.equals(user.userName))
		{
			// allow full clearance to myself.
			return new ContactInformation(ContactStatus.CONTACT_OK , userContact.userName, "","",
					new Visibility(true, true, true, true), new Visibility(true, true, true, true));
		}
		String connectInfoStr = CassandraAccessor.getColumn(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName, userContact.userName);
		if (connectInfoStr == null)
			// WTF ? Security alert.
		{
			logger.warn("Security alert. User " + user.userName + " trying to access invalid register to " + userContact.userName);
			return new ContactInformation(ContactStatus.CONTACT_NONE , userContact.userName, "","",
					new Visibility(false, false, false, false), new Visibility(false, false, false, false));
		}
		ContactInformation contactInfo = deserializeContactInformation(connectInfoStr);
		if (contactInfo.status != ContactStatus.CONTACT_OK)
			// WTF ? Security alert.
		{
			logger.warn("Security alert. User " + user.userName + " trying to access clearance register to " + userContact.userName + " although not yet connected");
			return new ContactInformation(ContactStatus.CONTACT_NONE , userContact.userName, "","",
					new Visibility(false, false, false, false), new Visibility(false, false, false, false));
		}
		contactInfo.userName = userContact.userName;
		return contactInfo;
	}

	public void setUserClearance(String contactName, Visibility visibility) throws CassandraException, SystemException
	{
		if (contactName.equals(user.userName))
			return;
		// Can't trust source information for other user. Need to retrieve it.
		ContactInformation sourceClearance = getUserClearance(new UserId(contactName));
		// Can't trust status other than the one set by the system. Check this.
		if (sourceClearance.status != ContactStatus.CONTACT_OK)
		{
			logger.warn("Security alert. User " + user.userName + " trying to access clearance register to " + contactName + " although not yet connected");
			throw new SystemException();
		}
		// me
		ContactInformation information = new ContactInformation(
				ContactStatus.CONTACT_OK,
				contactName, "","",
				visibility,
				sourceClearance.hisVisibility);
		updateOneContactClearance(user.userName, information);
		// him
		ContactInformation hisContactInfo = new ContactInformation(
				ContactStatus.CONTACT_OK,
				user.userName, "","",
				sourceClearance.hisVisibility,
				information.myVisibility);
		updateOneContactClearance(information.userName, hisContactInfo);
	}

	@Override
	public void deleteUser(UserId user) throws CassandraException
	{
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTACTNAME, user.userName);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTACTSTATUS, user.userName);
		// XXX: delete associated accounts.
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{
	}

	@Override
	public void createUserDefaults(UserId user, String lang)
	{
	}

	@Override
	public void notify(UserId id, String key, String value)
	{
		if ((key.equals(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME)) ||
				(key.equals(UserValuesConstantsAccount.ACCOUNT_LASTNAME)))
		{

			String colkey = null;
			if (key.equals(UserValuesConstantsAccount.ACCOUNT_FIRSTNAME)) colkey = "f#" + id.userName;
			if (key.equals(UserValuesConstantsAccount.ACCOUNT_LASTNAME)) colkey = "l#" + id.userName;

			Map<String, String> contactTable;
			try
			{
				contactTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTLIST, id.userName);
			}
			catch (CassandraException e)
			{
				logger.warn("Unexpected exception in notify for User " + id.userName);
				return;
			}
			if (contactTable == null) return;
			for (String contactUsername: contactTable.keySet())
			{
				try
				{
					CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTNAME, contactUsername, 
							(new ShortMap())
							.add(colkey, value).get());
				}
				catch (CassandraException e)
				{
					logger.warn("Unexpected exception in notify for User " + id.userName + " when updating "+ contactUsername);
					return;
				}
				logger.info("changed user key " + colkey + " to "  + value + " from user " + id.userName + " when updating "+ contactUsername);
			}
		}
	}

	public void changeConnectStatus(boolean connected)
	{
		UserChatManager cm = new UserChatManager(user);
		try
		{
			Map<String, String> contactTable = CassandraAccessor.getRow(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName);
			if (contactTable == null) return;
			Date d = new Date();
			for (String contactUsername: contactTable.keySet())
			{
				cm.hasChangedStatus(contactUsername, d, connected ? ChatInfo.UserStatus.ONLINE : ChatInfo.UserStatus.OFFLINE);
				CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTSTATUS, contactUsername, 
						(new ShortMap())
						.add(user.userName, connected ? "i":"o").get());
			}
		}
		catch (CassandraException e)
		{
			logger.error(e.toString());
		}
	}
}

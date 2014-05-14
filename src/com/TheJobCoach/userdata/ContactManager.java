package com.TheJobCoach.userdata;

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
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsAccount;

public class ContactManager implements IUserDataManager
{	
	private static Logger logger = LoggerFactory.getLogger(ContactManager.class);

	static ColumnFamilyDefinition cfDef = null;
	static ColumnFamilyDefinition cfDefDataName = null;

	final static String COLUMN_FAMILY_NAME_CONTACTLIST = "contactlist";
	final static String COLUMN_FAMILY_NAME_CONTACTNAME = "contactname";

	UserId user;
	UserValues userValues;
	AccountManager accountManager;
	
	public ContactManager(UserId user)
	{
		cfDef = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTACTLIST, cfDef);
		cfDefDataName = CassandraAccessor.checkColumnFamilyAscii(COLUMN_FAMILY_NAME_CONTACTNAME, cfDefDataName);
		this.user = user;
		userValues = new UserValues();
		accountManager = new AccountManager();
	}

	char contactStatusToChar(ContactInformation.ContactStatus status)
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
	ContactInformation.ContactStatus charToContactStatus(char status)
	{
		if (status == ' ') return ContactInformation.ContactStatus.CONTACT_NONE;
		if (status == 'A') return ContactInformation.ContactStatus.CONTACT_AWAITING;
		if (status == 'O') return ContactInformation.ContactStatus.CONTACT_OK;
		if (status == 'R') return ContactInformation.ContactStatus.CONTACT_REQUESTED;
		return ContactInformation.ContactStatus.CONTACT_NONE;
	}
	
	/** Serialize the rights "user" has granted to the other user */
	String serializeContactInformation(ContactInformation contact)
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
	ContactInformation deserializeContactInformation(String contact)
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
	
	void updateOneContactStatusOnOneSide(String currentUser, ContactInformation info) throws CassandraException
	{
		logger.info("Update connection status for user: " + currentUser + " about user: " + info.userName + " to status " + info.status);
		Map<String, String> mapUpdate = new HashMap<String, String>();
		mapUpdate.put(info.userName, serializeContactInformation(info));
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTLIST, currentUser, mapUpdate);

		Map<String, String> mapNameUpdate = new HashMap<String, String>();
		mapNameUpdate.put("f#" + info.userName, info.firstName);
		mapNameUpdate.put("l#" + info.userName, info.lastName);
		CassandraAccessor.updateColumn(COLUMN_FAMILY_NAME_CONTACTNAME, currentUser, mapNameUpdate);
	}
	
	/** Requesting a contact to a user OR accept contact request. Adds to the list of contacts. Sends a request mail if necessary
	 * @param userContact User to request contact to.
	 * @return The request status.
	 * @throws CassandraException 
	 */
	public ContactInformation.ContactStatus updateContactRequest(UserId userContact) throws CassandraException
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
			logger.info(user.userName + " is accepting connection request from: " + userContact.userName);
			
			contactInfo.status = ContactStatus.CONTACT_OK;
			// me
			updateOneContactStatusOnOneSide(user.userName, contactInfo);
			// him
			ContactInformation hisContactInfo = new ContactInformation(
					ContactStatus.CONTACT_OK,
					user.userName, myInfo.firstName, myInfo.name,
					// by default, share everything with my contacts
					new ContactInformation.Visibility(true, true, true, true),
					new ContactInformation.Visibility(true, true, true, true));
			updateOneContactStatusOnOneSide(userContact.userName, hisContactInfo);

			// send him an email about our new partnership
			String body = Lang.connectionGranted(lang, contactUserInfo.firstName, contactUserInfo.name, user.userName, myInfo.firstName, myInfo.name);
			MailerFactory.getMailer().sendEmail(contactUserInfo.email, Lang.connectionGrantedSubject(lang), body, "noreply@www.thejobcoach.fr", parts);
			
			return ContactInformation.ContactStatus.CONTACT_OK;
		}
		case CONTACT_NONE: // send request
		{
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
		//System.out.println(contactTable);
		//System.out.println(contactNameTable);
		if ((contactTable == null) || (contactNameTable == null)) return result;
		for (String contactUsername: contactTable.keySet())
		{
			ContactInformation contact = deserializeContactInformation(contactTable.get(contactUsername));
			contact.userName = contactUsername;
			contact.firstName = contactNameTable.get("f#" + contactUsername);
			contact.lastName = contactNameTable.get("l#" + contactUsername);
			result.add(contact);
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
		
		// using recipent's language
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

	@Override
	public void deleteUser(UserId user) throws CassandraException
	{
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTACTLIST, user.userName);
		CassandraAccessor.deleteKey(COLUMN_FAMILY_NAME_CONTACTNAME, user.userName);
	}

	@Override
	public void createTestUser(UserId user, String lang)
	{
	}

	@Override
	public void createUserDefaults(UserId user, String lang)
	{
	}

}

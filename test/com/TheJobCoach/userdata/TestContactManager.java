package com.TheJobCoach.userdata;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.util.MailerFactory;
import com.TheJobCoach.util.MockMailer;
import com.TheJobCoach.webapp.mainpage.shared.UserInformation;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.CreateAccountStatus;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode.ValidateAccountStatus;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation.ContactStatus;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.SystemException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class TestContactManager
{
	Logger logger = LoggerFactory.getLogger(TestContactManager.class);

	UserId id = new UserId("user", "token", UserId.UserType.USER_TYPE_SEEKER);

	UserId contact_id_1 = new UserId("user1", "token1", UserId.UserType.USER_TYPE_SEEKER);
	UserId contact_id_2 = new UserId("user2", "token2", UserId.UserType.USER_TYPE_SEEKER);

	ContactManager contactManager = new ContactManager(id);
	ContactManager contactManager1 = new ContactManager(contact_id_1);
	ContactManager contactManager2 = new ContactManager(contact_id_2);
	
	MockMailer mockMail = new MockMailer();
	
	AccountManager account = new AccountManager();
	
	void createOneAccount(UserId user) throws CassandraException
	{
		UserInformation info = new UserInformation(
				"lastName" + user.userName, 
				user.userName + "@toto.com", 
				"",
				"firstName" + user.userName);
		account.deleteAccount(user.userName);
		CreateAccountStatus status = account.createAccountWithToken(user, info, "en");
		assertEquals(CreateAccountStatus.CREATE_STATUS_OK, status);
		ValidateAccountStatus validated = account.validateAccount(user.userName, user.token);
		assertEquals(ValidateAccountStatus.VALIDATE_STATUS_OK, validated);
	}
	
	public void setTest() throws CassandraException, SystemException
	{
		contactManager.deleteUser(id);
		contactManager.deleteUser(contact_id_1);
		contactManager.deleteUser(contact_id_2);
		
		MailerFactory.setMailer(mockMail);
		
		// create user accounts
		createOneAccount(id);
		createOneAccount(contact_id_1);
		createOneAccount(contact_id_2);
	}
	
	@Test
	public void testConnectionRequestProcedure() throws CassandraException, SystemException
	{
		setTest();
		
		// Starting with a void list of contacts
		Vector<ContactInformation> contactList = contactManager.getContactList();
		assertEquals(0, contactList.size());
		
		// Send request to contact_id_1
		ContactInformation.ContactStatus newStatus = contactManager.updateContactRequest(contact_id_1);
		assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
		
		// Now list contains 1 contact : contact_id_1
		contactList = contactManager.getContactList();
		assertEquals(1, contactList.size());
		assertEquals("firstNameuser1", contactList.get(0).firstName);
		assertEquals("lastNameuser1", contactList.get(0).lastName);
		assertEquals(ContactStatus.CONTACT_AWAITING, contactList.get(0).status);
		// Also 1 contact on the other side
		Vector<ContactInformation> contactList1 = contactManager1.getContactList();
		assertEquals(1, contactList1.size());
		assertEquals("firstNameuser", contactList1.get(0).firstName);
		assertEquals("lastNameuser", contactList1.get(0).lastName);
		assertEquals(ContactStatus.CONTACT_REQUESTED, contactList1.get(0).status);
		
		// Check contact_id_1 received a request through mail 
		String mail = mockMail.lastBody;
		String subject = mockMail.lastSubject;
		//System.out.println(mail);
		//System.out.println(subject);
		assertTrue(mail.contains("firstNameuser lastNameuser (user)"));
		assertTrue(mail.contains("Hello firstNameuser1 lastNameuser1"));
		assertEquals(1, mockMail.lastParts.size()); // image header
		assertEquals("user1@toto.com", mockMail.lastDst);
		assertTrue(subject.contains("You have received a connection request"));
		
		// resend request. This must be ignored.
		mockMail.reset();
		newStatus = contactManager.updateContactRequest(contact_id_1);
		assertEquals(ContactInformation.ContactStatus.CONTACT_NONE, newStatus);
		// Check same as before
		contactList = contactManager.getContactList();
		assertEquals(1, contactList.size());
		assertEquals(ContactStatus.CONTACT_AWAITING, contactList.get(0).status);
		contactList1 = contactManager1.getContactList();
		assertEquals(1, contactList1.size());
		assertEquals(ContactStatus.CONTACT_REQUESTED, contactList1.get(0).status);
		// no mail
		assertNull(mockMail.lastBody);
		
		// Now accept connection on the other-side
		newStatus = contactManager1.updateContactRequest(id);
		assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);
		// Check both are connected
		contactList = contactManager.getContactList();
		assertEquals(1, contactList.size());
		assertEquals(ContactStatus.CONTACT_OK, contactList.get(0).status);
		contactList1 = contactManager1.getContactList();
		assertEquals(1, contactList1.size());
		assertEquals(ContactStatus.CONTACT_OK, contactList1.get(0).status);
		// check original user has received a notification through mail
		mail = mockMail.lastBody;
		subject = mockMail.lastSubject;
		System.out.println(mail);
		System.out.println(subject);
		assertTrue(mail.contains("Hello firstNameuser lastNameuser"));
		assertTrue(mail.contains("Your connection request to firstNameuser1 lastNameuser1 (user1) has been accepted."));
		assertEquals(1, mockMail.lastParts.size()); // image header
		assertEquals("user@toto.com", mockMail.lastDst);
		assertTrue(subject.contains("You have a new connection."));

		// 2nd user request
		// Send request to contact_id_2
		newStatus = contactManager.updateContactRequest(contact_id_2);
		assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);

		// Now list contains 2 contact : contact_id_1, contact_id_2
		contactList = contactManager.getContactList();
		assertEquals(2, contactList.size());
		assertEquals("firstNameuser1", contactList.get(0).firstName);
		assertEquals("lastNameuser1", contactList.get(0).lastName);
		assertEquals(ContactStatus.CONTACT_OK, contactList.get(0).status);
		assertEquals("firstNameuser2", contactList.get(1).firstName);
		assertEquals("lastNameuser2", contactList.get(1).lastName);
		assertEquals(ContactStatus.CONTACT_AWAITING, contactList.get(1).status);
		// Also 1 contact on the other side
		Vector<ContactInformation> contactList2 = contactManager2.getContactList();
		assertEquals(1, contactList2.size());
		assertEquals("firstNameuser", contactList2.get(0).firstName);
		assertEquals("lastNameuser", contactList2.get(0).lastName);
		assertEquals(ContactStatus.CONTACT_REQUESTED, contactList2.get(0).status);
	}
	
	@Test
	public void testConnectionMail() throws CassandraException, SystemException
	{
		setTest();
		// connect 2 users
		ContactInformation.ContactStatus newStatus = contactManager.updateContactRequest(contact_id_1);
		assertEquals(ContactInformation.ContactStatus.CONTACT_REQUESTED, newStatus);
		newStatus = contactManager1.updateContactRequest(id);
		assertEquals(ContactInformation.ContactStatus.CONTACT_OK, newStatus);
		
		// now user talks to user1
		mockMail.reset();
		boolean sent = contactManager.sendJobMail(contact_id_1, "petit papa noël, quand tu descendras du ciel");
		assertTrue(sent);
		
		// Check mail content
		String mail = mockMail.lastBody;
		String subject = mockMail.lastSubject;
		System.out.println(mail);
		System.out.println(subject);
		assertTrue(mail.contains("Hello firstNameuser1 lastNameuser1"));
		assertTrue(mail.contains("petit papa noël, quand tu descendras du ciel"));
		assertTrue(mail.contains("You have received a new message from firstNameuser lastNameuser (user)"));
		assertEquals(1, mockMail.lastParts.size()); // image header
		assertEquals("user1@toto.com", mockMail.lastDst);
		assertTrue(subject.contains("You have received an email from your connection"));
	}
}

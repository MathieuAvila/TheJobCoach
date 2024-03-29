package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.Coach.MessagePipe;
import com.TheJobCoach.webapp.userpage.client.Document.ComponentDocumentList;
import com.TheJobCoach.webapp.userpage.client.ExternalContact.ComponentExternalContactList;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry.LogEntryType;
import com.TheJobCoach.webapp.util.client.DialogBlockOkCancel;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.TheJobCoach.webapp.util.shared.UserValuesConstantsCoachMessages;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditLogEntry implements EntryPoint, IEditLogEntry {
	
	private final static UserServiceAsync userService = GWT.create(UserService.class);

	private static final Lang lang = GWT.create(Lang.class);
	private static final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);

	UserId user;

	RichTextArea richTextAreaDescription = new RichTextArea();
	RichTextArea richTextAreaNote = new RichTextArea();
	TextBox txtbxTitle = new TextBox();
	ListBox comboBoxStatus = new ListBox();
	DateBox dateBoxEvent = new DateBox();	
	CheckBox doneBox = new CheckBox();
	ComponentDocumentList cdl;
	ComponentExternalContactList ecl;
	DialogBlockOkCancel okCancel;
	
	HorizontalPanel eventInfoPanel = new HorizontalPanel();
	HorizontalPanel eventDonePanel = new HorizontalPanel();

	String id;

	Panel rootPanel;
	IChooseResult<UserLogEntry> result;
	UserLogEntry currentLogEntry;
	String oppId;
	Vector<UserDocumentId> userDocumentList;
	Vector<ExternalContact> contactList = new Vector<ExternalContact>();
	
	public EditLogEntry(Panel panel, UserLogEntry _currentLogEntry, String _oppId, UserId _user, IChooseResult<UserLogEntry> editLogEntryResult)
	{
		user = _user;
		rootPanel = panel;
		currentLogEntry = _currentLogEntry;
		result = editLogEntryResult;
		oppId = _oppId;
	}

	public EditLogEntry()
	{
	}

	public UserLogEntry getLogEntry()
	{
		String id = SiteUUID.getDateUuid();
		if (currentLogEntry != null)
		{
			id = currentLogEntry.ID;
			oppId = currentLogEntry.opportunityId;
		}
		Date eventDate = dateBoxEvent.getValue();
		if (eventDate == null) eventDate = new Date();
		return new UserLogEntry(
				oppId,
				id, 
				txtbxTitle.getText(), richTextAreaDescription.getHTML(), 
				eventDate,
				UserLogEntry.entryTypeToString(comboBoxStatus.getValue(comboBoxStatus.getSelectedIndex())),
				contactList, userDocumentList, richTextAreaNote.getHTML(), doneBox.getValue().booleanValue());
	}

	private void commit()
	{
		final UserLogEntry log = getLogEntry();
		userService.setUserLogEntry(user, log, new ServerCallHelper<String>(rootPanel) {
			public void onSuccess(String tmp)
			{
				result.setResult(log);			
			}
		});
	};

	// this is the list of event types that can have a "done" field.
	final static Set<LogEntryType> validEventType 
	= new HashSet<LogEntryType>(Arrays.asList(
			LogEntryType.INTERVIEW, 
			LogEntryType.RECALL
			));

	void updateDoneField()
	{
		int index = comboBoxStatus.getSelectedIndex();
		String valueStr = "";
		try { valueStr = comboBoxStatus.getValue(index); } catch (Exception e) {};
		eventInfoPanel.setVisible(validEventType.contains(UserLogEntry.entryTypeToString(valueStr)));
	}
	
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{	
		final DialogBox dBox = new DialogBox();
		dBox.setText(currentLogEntry == null ? lang._TextNewLogEntry(): lang._TextUpdateLogEntry());
		dBox.setGlassEnabled(true);
		dBox.setAnimationEnabled(true);

		VerticalPanel hp = new VerticalPanel();		
		Grid grid = new Grid(5, 2);
		grid.setBorderWidth(0);
		dBox.setWidget(hp);		
		grid.setSize("100%", "100%");

		hp.add(grid);
		
		Label lblTitle = new Label(langLogEntry._TextUserLogTitle());
		grid.setWidget(0, 0, lblTitle);		
		grid.setWidget(0, 1, txtbxTitle);
		grid.getCellFormatter().setWidth(0, 1, "50%");
		grid.getCellFormatter().setWidth(0, 0, "50%");
		txtbxTitle.setWidth("100%");
		lblTitle.setWidth("100%");
		
		Label lblStatus = new Label("Status");
		grid.setWidget(1, 0, lblStatus);
		grid.setWidget(1, 1, comboBoxStatus);
		
		for (LogEntryType e : UserLogEntry.getLogTypeTable())
		{
			comboBoxStatus.addItem(langLogEntry.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(e)), UserLogEntry.entryTypeToString(e));
			if ((currentLogEntry != null) && (currentLogEntry.type == e))
			{
				comboBoxStatus.setSelectedIndex(comboBoxStatus.getItemCount()-1);
			}
		}
		
		Label lblDescription = new Label("Description");
		grid.setWidget(2, 0, lblDescription);		
		grid.setWidget(2, 1, richTextAreaDescription);
		
		grid.getCellFormatter().setWidth(2, 1, "100%");
		richTextAreaDescription.setWidth("100%");
		richTextAreaDescription.setHeight("5em");
		
		Label lblEndDate = new Label(langLogEntry._TextCreated());

		grid.setWidget(3, 0, lblEndDate);
		eventDonePanel.add(dateBoxEvent);
		Label doneText = new Label(langLogEntry._TextDone());

		eventDonePanel.add(doneText);
		eventDonePanel.add(eventInfoPanel);
		eventInfoPanel.add(doneText);
		eventInfoPanel.add(doneBox);
		
		comboBoxStatus.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event)
			{
				updateDoneField();
			}
		});
		
		grid.setWidget(3, 1, eventDonePanel);
		eventDonePanel.setCellVerticalAlignment(eventInfoPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		eventDonePanel.setCellVerticalAlignment(doneText, HasVerticalAlignment.ALIGN_MIDDLE);
		eventDonePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		eventInfoPanel.setCellVerticalAlignment(dateBoxEvent, HasVerticalAlignment.ALIGN_MIDDLE);
		eventInfoPanel.setCellVerticalAlignment(doneBox, HasVerticalAlignment.ALIGN_MIDDLE);
		eventInfoPanel.setCellVerticalAlignment(doneText, HasVerticalAlignment.ALIGN_MIDDLE);
		eventInfoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		grid.setWidget(4, 0, new Label(lang._TextPersonalNote()));
		grid.setWidget(4, 1, richTextAreaNote);
		richTextAreaNote.setHeight("5em");
		
		okCancel = new DialogBlockOkCancel(null, dBox);
		okCancel.getOk().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				okCancel.setEnabled(false);
				commit();
				dBox.hide();
			}
		});
		if (currentLogEntry != null)
		{
			userDocumentList = currentLogEntry.attachedDocumentId;
			for (ExternalContact ec: currentLogEntry.linkedExternalContact) 
				contactList.add(ec);
		}
		else
		{
			userDocumentList = new Vector<UserDocumentId>();
		}
		cdl = new ComponentDocumentList(userDocumentList, rootPanel, user);
		cdl.onModuleLoad();
		hp.add(cdl);
		
		ecl = new ComponentExternalContactList(contactList, rootPanel, user);
		ecl.onModuleLoad();
		hp.add(ecl);		
		hp.add(new VerticalSpacer("10px"));
		hp.add(okCancel);
		
		if (currentLogEntry != null)
		{
			txtbxTitle.setText(currentLogEntry.title);
			richTextAreaDescription.setHTML(currentLogEntry.description);
			richTextAreaNote.setHTML(currentLogEntry.note);
			dateBoxEvent.setValue(currentLogEntry.eventDate);
		}
		else
		{
			dateBoxEvent.setValue(new Date());
		}
		
		dBox.center();

		// Inform user about personal note
		MessagePipe.getMessagePipe(user, rootPanel).addMessage(UserValuesConstantsCoachMessages.COACH_PERSONAL_NOTE);
		
		updateDoneField();
	}

	@Override
	public IEditLogEntry clone(Panel panel, UserLogEntry _currentLogEntry,
			String _oppId, UserId _user,
			IChooseResult<UserLogEntry> editLogEntryResult)
	{
		return new EditLogEntry(panel, _currentLogEntry, _oppId, _user, editLogEntryResult);
	}
}

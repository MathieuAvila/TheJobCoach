package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.ExternalContact;
import com.TheJobCoach.webapp.userpage.shared.UserDocumentId;
import com.TheJobCoach.webapp.userpage.shared.UserLogEntry;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserLog implements EntryPoint, IContentUserLog {

	UserId user;

	final ExtendedCellTable<UserLogEntry> cellTable = new ExtendedCellTable<UserLogEntry>();

	final Lang lang = GWT.create(Lang.class);
	final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);

	private UserOpportunity editedOpportunity;

	private IEditLogEntry editModel;
	private IContentUserOpportunity opportunityContent;

	ButtonImageText buttonBack;
	Button buttonNewLogEntry;

	public ContentUserLog(Panel panel, UserId _user, UserOpportunity opp)
	{
		rootPanel = panel;
		editedOpportunity = opp;
		user = _user;
		editModel = new EditLogEntry();
		opportunityContent = new ContentUserOpportunity();
	}

	public ContentUserLog(Panel panel, UserId _user, UserOpportunity opp, IEditLogEntry editModel, IContentUserOpportunity opportunityContent)
	{
		rootPanel = panel;
		editedOpportunity = opp;
		user = _user;
		this.editModel = editModel;
		this.opportunityContent = opportunityContent;
	}

	public ContentUserLog()
	{
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	// The list of data to display.
	private Vector<UserLogEntry> UserLogEntryList = new Vector<UserLogEntry>();

	// Create a data provider.
	AsyncDataProvider<UserLogEntry> dataProvider = new AsyncDataProvider<UserLogEntry>() {
		@Override
		protected void onRangeChanged(HasData<UserLogEntry> display) 
		{
			final com.google.gwt.view.client.Range range = display.getVisibleRange();
			int start = range.getStart();
			int end = start + range.getLength();
			if (end >= UserLogEntryList.size() ) end = UserLogEntryList.size();
			if (UserLogEntryList.size() != 0)
			{
				List<UserLogEntry> dataInRange = UserLogEntryList.subList(start, end);
				// Push the data back into the list.
				cellTable.setRowData(start, dataInRange);
			}
		}
	};

	void getAllContent()
	{
		EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
			public void Run() throws CassandraException
			{
				userService.getUserLogEntryList(user, editedOpportunity.ID, new ServerCallHelper<Vector<UserLogEntry>>(rootPanel)
						{	
					@Override
					public void onSuccess(Vector<UserLogEntry> r)
					{
						UserLogEntryList.clear();
						UserLogEntryList.addAll(r);
						dataProvider.updateRowCount(UserLogEntryList.size(), true);
						dataProvider.updateRowData(0, UserLogEntryList.subList(0, UserLogEntryList.size()));
						cellTable.redraw();
					}
						});
			}});
	}

	Map<UserLogEntry.LogEntryType, UserOpportunity.ApplicationStatus> changeOpportunityStatus = new HashMap<UserLogEntry.LogEntryType, UserOpportunity.ApplicationStatus>()
			{
		private static final long serialVersionUID = 5182652491874042551L;
		{
			/*EVENT,
			RECALL,
			INTERVIEW,
			PROPOSAL,
			CLOSED*/
			put(UserLogEntry.LogEntryType.APPLICATION, UserOpportunity.ApplicationStatus.APPLIED);
		}
			};


			protected void checkOpportunityChange(UserLogEntry log)
			{
				// Only if relevant
				if (!changeOpportunityStatus.containsKey(log.type))
				{
					return;
				}
				UserLogEntry lastLog = null;
				UserLogEntry lastLogNotSame = null;
				for (UserLogEntry currentLog : UserLogEntryList)
				{
					// Only if relevant
					if (changeOpportunityStatus.containsKey(currentLog.type))
					{
						if (lastLog == null || currentLog.eventDate.after(lastLog.eventDate))
						{
							lastLog = currentLog;
						}
						if ((lastLogNotSame == null) || ((currentLog.eventDate.after(lastLog.eventDate))) && (!currentLog.ID.equals(log.ID)))
						{
							lastLogNotSame = currentLog;
						}
					}
				}
				// Check if we have changed the latest one.
				boolean isLast = false;
				if (lastLog != null)
				{
					// Is it the last one that was changed, and is it still the lastOne ?
					if (log.ID.equals(lastLog.ID)) // Yes
					{
						// If there's no last, the edited one is the only one.
						if (lastLogNotSame == null)
						{
							isLast= true;					
						}
						// otherwise check that the edited is the same OR after the last
						else
						{
							if (log.ID.equals(lastLogNotSame.ID) || (log.eventDate.after(lastLogNotSame.eventDate)))
							{
								isLast= true;		
							}
						}
					}
				}
				else 
				{
					isLast= true; // It's the only one.
				}

				// We can propose to change this.
				if (isLast)
				{
					// Only if it's different than current status
					if (!changeOpportunityStatus.get(log.type).equals(editedOpportunity.status))
					{

					}
				}
			}

			void deleteLogEntry(final UserLogEntry currentLogEntry)
			{
				MessageBox mb = new MessageBox(
						rootPanel, true, true, MessageBox.TYPE.QUESTION, langLogEntry._Text_DeleteEditLogTitle(), 
						langLogEntry._Text_DeleteEditLog() + currentLogEntry.title + " ? ", new MessageBox.ICallback() {
							@Override
							public void complete(boolean ok) {
								if (ok == true)
								{
									EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
										public void Run() throws CassandraException
										{
											userService.deleteUserLogEntry(user, currentLogEntry.ID, new ServerCallHelper<String>(rootPanel) {	
												@Override
												public void onSuccess(String r)
												{
													getAllContent();
												}
											});
										}});							
								}
							}});
				mb.onModuleLoad();
			}


			class NewLogEntryHandler implements ClickHandler
			{
				public void onClick(ClickEvent event)
				{
					IEditLogEntry eus = editModel.clone(rootPanel, null, editedOpportunity.ID, user, new IChooseResult<UserLogEntry>() 
							{
						@Override
						public void setResult(UserLogEntry result) {
							if (result != null)
							{
								checkOpportunityChange(result);
								getAllContent();
							}
						}
							});
					eus.onModuleLoad();
				}
			}


			void updateLogEntry(UserLogEntry currentLogEntry)
			{
				IEditLogEntry eus = editModel.clone(rootPanel, currentLogEntry, editedOpportunity.ID, user, new IChooseResult<UserLogEntry>() {
					@Override
					public void setResult(UserLogEntry result) {
						if (result != null)
						{					
							getAllContent();
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
				rootPanel.setSize("100%", "100%");
				rootPanel.clear();

				VerticalPanel simplePanelCenter = new VerticalPanel();
				simplePanelCenter.setSize("100%", "");
				rootPanel.add(simplePanelCenter);

				ContentHelper.insertTitlePanel(simplePanelCenter, langLogEntry._Text_EditLog(), ClientImageBundle.INSTANCE.userLogContent());

				// Create title column.
				TextColumn<UserLogEntry> titleColumn = new TextColumn<UserLogEntry>() 	{
					@Override
					public String getValue(UserLogEntry userLog) 
					{
						return userLog.title;
					}
				};

				// Create status column.
				TextColumn<UserLogEntry> statusColumn = new TextColumn<UserLogEntry>() {
					@Override
					public String getValue(UserLogEntry userLog) 
					{
						return langLogEntry.logEntryStatusMap().get("logEntryStatus_" + UserLogEntry.entryTypeToString(userLog.type));
					}
				};

				// Create event column.
				TextColumn<UserLogEntry> createdColumn = new TextColumn<UserLogEntry>() {
					@Override
					public String getValue(UserLogEntry userLog) 
					{
						return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(userLog.eventDate);
					}
				};

				cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserLogEntry, String>() {
					@Override
					public void update(int index, UserLogEntry object, String value) {
						deleteLogEntry(object);
					}});

				cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserLogEntry, String>() {
					@Override
					public void update(int index, UserLogEntry object, String value) {
						updateLogEntry(object);
					}});

				titleColumn.setSortable(true);
				statusColumn.setSortable(true);
				createdColumn.setSortable(true);
				cellTable.setStyleName("filecelltable");
				cellTable.addColumn(titleColumn, lang._TextName());
				cellTable.addColumn(statusColumn, lang._TextStatus());
				cellTable.addColumn(createdColumn, langLogEntry._TextCreated());

				cellTable.addColumnHtml(new FieldUpdater<UserLogEntry, String>() {
					@Override
					public void update(int index, UserLogEntry object, String value) {				
					}}, 
					new GetValue<String, UserLogEntry>(){

						@Override
						public String getValue(UserLogEntry log)
						{
							String result = "";
							String brk = "";
							for (UserDocumentId doc : log.attachedDocumentId)
							{
								result = result + brk + doc.fileName;
								brk = "<br/>";
							}
							return result;
						}}, langLogEntry._TextFiles());

				cellTable.addColumnHtml(new FieldUpdater<UserLogEntry, String>() {
					@Override
					public void update(int index, UserLogEntry object, String value) {				
					}}, 
					new GetValue<String, UserLogEntry>(){

						@Override
						public String getValue(UserLogEntry log)
						{
							String result = "";
							String brk = "";
							for (ExternalContact contact : log.linkedExternalContact)
							{
								result = result + brk + contact.firstName + " " + contact.lastName;
								brk = "<br/>";
							}
							return result;
						}}, langLogEntry._Text_Contacts());

				dataProvider.addDataDisplay(cellTable);

				AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
				cellTable.setRowData(0, UserLogEntryList);
				cellTable.setRowCount(UserLogEntryList.size(), true);
				cellTable.setVisibleRange(0, 20);
				cellTable.addColumnSortHandler(columnSortHandler);

				SimplePanel simplePanel_1 = new SimplePanel();
				simplePanel_1.setStyleName("#closeButton");
				simplePanelCenter.add(simplePanel_1);
				simplePanel_1.setHeight("10px");

				Grid grid_1 = new Grid(3, 2);
				simplePanelCenter.add(grid_1);

				Label lblTitle = new Label(lang._TextName());
				lblTitle.setStyleName("summary-title");
				grid_1.setWidget(0, 0, lblTitle);

				Label lblTitleContent = new Label(editedOpportunity.title);
				lblTitleContent.setStyleName("summary-text");
				grid_1.setWidget(0, 1, lblTitleContent);

				Label labelStatus = new Label(lang._TextStatus());
				labelStatus.setStyleName("summary-title");
				grid_1.setWidget(1, 0, labelStatus);

				Label label_1 = new Label(lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(editedOpportunity.status)));
				label_1.setStyleName("summary-text");
				grid_1.setWidget(1, 1, label_1);

				Label labelCompany = new Label(lang._TextCompany());
				labelCompany.setStyleName("summary-title");
				grid_1.setWidget(2, 0, labelCompany);

				Label labelCompanyContent = new Label(editedOpportunity.companyId);
				labelCompanyContent.setStyleName("summary-text");
				grid_1.setWidget(2, 1, labelCompanyContent);

				SimplePanel simplePanel = new SimplePanel();
				simplePanelCenter.add(simplePanel);
				simplePanel.setHeight("20px");

				buttonBack = new ButtonImageText(ButtonImageText.Type.BACK, langLogEntry._Text_BackToOpportunityList());
				buttonBack.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						IContentUserOpportunity contentUserOpportunity = opportunityContent.clone(rootPanel, user);
						contentUserOpportunity.onModuleLoad();		
					}

				});

				simplePanelCenter.add(buttonBack);

				SimplePanel simplePanel_2 = new SimplePanel();
				simplePanelCenter.add(simplePanel_2);
				simplePanel_2.setHeight("20px");

				simplePanelCenter.add(cellTable);
				cellTable.setSize("100%", "");		

				HorizontalPanel horizontalPanel = new HorizontalPanel();
				simplePanelCenter.add(horizontalPanel);
				horizontalPanel.setWidth("100%");

				buttonNewLogEntry = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewLogEntry());	
				horizontalPanel.add(buttonNewLogEntry);

				HTML htmlDescriptionhtml = new HTML("", true);
				simplePanelCenter.add(htmlDescriptionhtml);
				htmlDescriptionhtml.setSize("100%", "100%");

				// Add a handler to the new button.
				NewLogEntryHandler newHandler = new NewLogEntryHandler();
				buttonNewLogEntry.addClickHandler(newHandler);

				getAllContent();		
			}


			@Override
			public IContentUserLog clone(Panel panel, UserId _user, UserOpportunity opp)
			{
				return new ContentUserLog(panel, _user, opp);
			}
}

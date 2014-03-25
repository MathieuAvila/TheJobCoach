package com.TheJobCoach.webapp.userpage.client.Opportunity;

import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserOpportunity;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserOpportunity implements EntryPoint, IContentUserOpportunity {

	UserId user;
	
	// The list of data to display.
	private Vector<UserOpportunity> userOpportunityList = new Vector<UserOpportunity>();

	final ExtendedCellTable<UserOpportunity> cellTable = new ExtendedCellTable<UserOpportunity>(userOpportunityList);
	UserOpportunity currentOpportunity = null;
	final HTML panelDescriptionContent = new HTML("");
	final Label labelPubDate = new Label();
	final Label labelStartDate = new Label();

	final Lang lang = GWT.create(Lang.class);
	final LangLogEntry langLogEntry = GWT.create(LangLogEntry.class);

	ButtonImageText buttonNewOpportunity;
	ButtonImageText buttonFeedOpportunity;

	private IEditDialogModel<UserOpportunity> editModel;
	private IContentUserLog logContent;

	private void setUserOpportunity(UserOpportunity opp)
	{
		ServerCallHelper<UserOpportunity>callback = new ServerCallHelper<UserOpportunity>(rootPanel) {
			@Override
			public void onSuccess(UserOpportunity result) {
				currentOpportunity = result;
				panelDescriptionContent.setHTML(currentOpportunity.description);
				labelPubDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(currentOpportunity.pubDate));
				labelStartDate.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(currentOpportunity.startDate));
				cellTable.redraw();				
			}
		};
		userService.getUserOpportunity(user, opp.ID, callback);	
	}

	public ContentUserOpportunity(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
		this.editModel = new EditOpportunity();
		this.logContent = new ContentUserLog(); 
	}

	public ContentUserOpportunity(Panel panel, UserId _user, IEditDialogModel<UserOpportunity> editModel, IContentUserLog logContent)
	{
		rootPanel = panel;
		user = _user;
		this.editModel = editModel; 
		this.logContent = logContent;
	}

	public ContentUserOpportunity()
	{
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	void getAllContent()
	{		
		ServerCallHelper<Vector<UserOpportunity>>callback = new ServerCallHelper<Vector<UserOpportunity>>(rootPanel) {
			@Override
			public void onSuccess(Vector<UserOpportunity> result) {
				userOpportunityList.clear();
				userOpportunityList.addAll(result);
				cellTable.updateData();
				cellTable.redraw();
			}
		};
		userService.getUserOpportunityList(user, "managed", callback);
	}

	private void delete(final UserOpportunity opp)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langLogEntry._Text_DeleteOpportunityTitle(), 
				langLogEntry._Text_DeleteOpportunity() + opp.title, new MessageBox.ICallback() {
					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{
							userService.deleteUserOpportunity(user, opp.ID, new ServerCallHelper<String>(rootPanel) {
								public void onSuccess(String result)
								{
									getAllContent();
								}
							});
						}
					}});
		mb.onModuleLoad();
	}

	class localChooseResult implements IChooseResult<UserOpportunity>
	{
		public void setResult(UserOpportunity result) {
			if (result != null)
			{
				result.ID = new Date().toString();
				userService.setUserOpportunity(user, "managed", result, new ServerCallHelper<String>(rootPanel) {
					public void onSuccess(String result)
					{
						getAllContent();
					}
				});
			}
		}
	}
	
	class NewOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			IEditDialogModel<UserOpportunity> eus = editModel.clone(rootPanel, user, null, new localChooseResult());
			eus.onModuleLoad();
		}
	}
	
	class LocalIChooseResult implements IChooseResult<UserOpportunity> 
	{
		@Override
		public void setResult(UserOpportunity result) {
			if (result != null)
			{
				userService.setUserOpportunity(user, "managed", result, new ServerCallHelper<String>(rootPanel) {
					public void onSuccess(String result)
					{
						getAllContent();
					}
				});
			}
		}
	};
	
	class FeedOpportunityHandler implements ClickHandler
	{
		public void onClick(ClickEvent event)
		{
			AutoFeed eus = new AutoFeed(rootPanel, user, new LocalIChooseResult(), 
					editModel.clone(rootPanel, user, null, new localChooseResult()));
			eus.onModuleLoad();
		}
	}

	private void updateUserOpportunity(UserOpportunity opp)
	{
		IEditDialogModel<UserOpportunity> eus = editModel.clone(rootPanel, user, opp, new LocalIChooseResult());
		eus.onModuleLoad();
	}

	void editLog(UserOpportunity opp)
	{
		IContentUserLog cul = logContent.clone(rootPanel, user, opp);		
		cul.onModuleLoad();
	}

	public void onModuleLoad()
	{			
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, lang.lblOpportunities_text(), ClientImageBundle.INSTANCE.opportunityContent());


		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				delete(object);
			}});

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				updateUserOpportunity(object);
			}});

		cellTable.addColumnUrl(new ExtendedCellTable.GetValue<String, UserOpportunity>() {
			public String getValue(UserOpportunity contact) {
				return contact.url;
			}
		});

		// Create title column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.title;
			}			
		},  lang._TextName());
		
		// Create company column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.companyId;
			}			
		},  lang._TextCompany());

		// Create status column.
		cellTable.specialAddColumnSortableWithComparator(new GetValue<String, UserOpportunity>() {

			@Override
			public String getValue(UserOpportunity userOpportunity)
			{
				return lang.applicationStatusMap().get("ApplicationStatus_" + UserOpportunity.applicationStatusToString(userOpportunity.status));
			}}
		, new Comparator<UserOpportunity>(){
			@Override
			public int compare(UserOpportunity o1, UserOpportunity o2)
			{
				return o1.status.compareTo(o2.status);
			}}, lang._TextStatus());

		// Create location column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.location;
			}			
		},  lang._TextLocation());

		// Create salary column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.salary;
			}			
		},  lang._TextSalary());

		// Create contract type column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserOpportunity>() {
			@Override
			public String getValue(UserOpportunity opp)
			{
				return opp.contractType;
			}			
		},  lang._TextContractType());

		// Create creation date column.
		cellTable.specialAddColumnSortableDate(new GetValue<Date, UserOpportunity>() {
			@Override
			public Date getValue(UserOpportunity opp)
			{
				return opp.lastUpdate;
			}
		},  lang._TextCreationDate());

		cellTable.addColumnWithIcon(IconCellSingle.IconType.RIGHT, new FieldUpdater<UserOpportunity, String>() {
			@Override
			public void update(int index, UserOpportunity object, String value) {
				editLog(object);
			}});

		// Add a selection model to handle user selection.
		final SingleSelectionModel<UserOpportunity> selectionModel = new SingleSelectionModel<UserOpportunity>();
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				UserOpportunity selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					setUserOpportunity(selected);					
				}
			}
		});

		simplePanelCenter.add(cellTable);
		cellTable.setSize("100%", "");	
		
		// Create a Pager to control the table.		
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(cellTable);
	    simplePanelCenter.add(pager);
	    
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		simplePanelCenter.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		buttonNewOpportunity = new ButtonImageText(ButtonImageText.Type.NEW, lang._TextNewOpportunity());
		horizontalPanel.add(buttonNewOpportunity);

		buttonFeedOpportunity = new ButtonImageText(ButtonImageText.Type.FEED, langLogEntry._Text_feed_from());
		horizontalPanel.add(buttonFeedOpportunity);

		HTML htmlDescriptionhtml = new HTML("", true);
		simplePanelCenter.add(htmlDescriptionhtml);
		htmlDescriptionhtml.setSize("100%", "100%");

		SimplePanel simplePanel = new SimplePanel();
		simplePanelCenter.add(simplePanel);
		simplePanel.setHeight("10px");

		Grid grid_1 = new Grid(2, 2);
		simplePanelCenter.add(grid_1);

		Label lblPubDate = new Label(lang._TextPubDate());
		lblPubDate.setStyleName("summary-title");
		grid_1.setWidget(0, 0, lblPubDate);

		labelPubDate.setStyleName("summary-text");
		grid_1.setWidget(0, 1, labelPubDate);

		Label lblStartDate = new Label(lang._TextStartDate());
		lblStartDate.setStyleName("summary-title");
		grid_1.setWidget(1, 0, lblStartDate);

		labelStartDate.setStyleName("summary-text");
		grid_1.setWidget(1, 1, labelStartDate);

		Label labelDescription = new Label(lang._TextDescription());
		labelDescription.setStyleName("summary-title");
		simplePanelCenter.add(labelDescription);

		simplePanelCenter.add(panelDescriptionContent);	

		// Add a handler to the new button.
		NewOpportunityHandler newHandler = new NewOpportunityHandler();
		buttonNewOpportunity.addClickHandler(newHandler);

		// Add a handler to the feed button.
		FeedOpportunityHandler feedHandler = new FeedOpportunityHandler();
		buttonFeedOpportunity.addClickHandler(feedHandler);

		getAllContent();		
	}

	@Override
	public IContentUserOpportunity clone(Panel panel, UserId _user)
	{
		return new ContentUserOpportunity(panel, _user);
	}
}

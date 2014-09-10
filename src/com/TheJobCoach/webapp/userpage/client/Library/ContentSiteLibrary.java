package com.TheJobCoach.webapp.userpage.client.Library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserJobSite;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.TheJobCoach.webapp.util.client.IChooseResult;
import com.TheJobCoach.webapp.util.client.IEditDialogModel;
import com.TheJobCoach.webapp.util.client.IconCellSingle;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.SiteUUID;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.TheJobCoach.webapp.userpage.client.UserSite.EditUserSite;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentSiteLibrary extends VerticalPanel {

	final LangLibrary langLibrary = GWT.create(LangLibrary.class);
	final Lang lang = GWT.create(Lang.class);

	UserId user;

	// The list of data to display.
	private List<WebSiteDefinition> webSiteList = new ArrayList<WebSiteDefinition>();
	
	final ExtendedCellTable<WebSiteDefinition> cellTable = new ExtendedCellTable<WebSiteDefinition>(webSiteList);
	SimplePager pager;
	
	ASiteLibrary library;

	Panel rootPanel;
	
	TextBox nameBox = new TextBox();
	ListBox comboBoxLocation = new ListBox();
	ListBox comboBoxTarget = new ListBox();
	ListBox comboBoxSector = new ListBox();
	ListBox comboBoxType = new ListBox();
	
	WebSiteDefinition filter = new WebSiteDefinition();
	IEditDialogModel<UserJobSite> editSiteInterface;
	
	void init(Panel rootPanel, UserId user, ASiteLibrary library, IEditDialogModel<UserJobSite> editSiteInterface)
	{
		this.user = user;
		this.rootPanel = rootPanel;
		this.library = library;
		this.editSiteInterface = editSiteInterface;
		onModuleLoad();
	}
	
	public ContentSiteLibrary(Panel rootPanel, UserId user) {
		super();
		init(rootPanel, user, new SiteLibraryData(), new EditUserSite());
	}
	
	public ContentSiteLibrary(Panel rootPanel, UserId user, ASiteLibrary library, IEditDialogModel<UserJobSite> editSiteInterface)
	{
		super();
		init(rootPanel, user, library, editSiteInterface);
	}
	
	private final UserServiceAsync userService = GWT.create(UserService.class);

	private void addSite(WebSiteDefinition object)
	{
		UserJobSite site = new UserJobSite(SiteUUID.getDateUuid(), object.name, object.url);
		IEditDialogModel<UserJobSite> eus = editSiteInterface.clone(rootPanel, user, site, new IChooseResult<UserJobSite>() {
			@Override
			public void setResult(UserJobSite result) {}
		});
		eus.onModuleLoad();
	}
	
	private void appendList(ListBox element, Map<String, String> list, String trim)
	{
		element.addItem(langLibrary.selectNone(), "");
		for (String key: list.keySet())
		{
			element.addItem(list.get(key), key.substring(trim.length()));
		}
	}
	
	void addElementToFilter(HorizontalPanel filterPanel, String text, Widget filterElement)
	{
		if (filterPanel.getWidgetCount() != 0)
			filterPanel.add(new HorizontalSpacer("1em"));
		filterPanel.add(new Label(text));
		filterPanel.add(filterElement);
	}
	
	void onModuleLoad()
	{
		ContentHelper.insertTitlePanel(this, lang._TextSiteLibrary(), ClientImageBundle.INSTANCE.siteLibraryContent());

		HorizontalPanel filterPanel = new HorizontalPanel();
		filterPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		addElementToFilter(filterPanel, langLibrary.selectName(), nameBox);
		addElementToFilter(filterPanel, langLibrary.selectLocation(), comboBoxLocation);
		//addElementToFilter(filterPanel, langLibrary.selectPeople(), comboBoxTarget);
		addElementToFilter(filterPanel, langLibrary.selectSector(), comboBoxSector);
		addElementToFilter(filterPanel, langLibrary.selectType(), comboBoxType);

		//appendList(comboBoxTarget, langLibrary.publicTargetMap(), "publictargetMap_");
		appendList(comboBoxSector, langLibrary.sectorMap(), "sectorMap_");
		appendList(comboBoxType, langLibrary.siteTypeMap(), "sitetypeMap_");
		appendList(comboBoxLocation, langLibrary.locationMap(), "locationMap_");
		
		nameBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				filter.name = nameBox.getValue();
				updateContent();
			}			
		});
		
		comboBoxSector.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				int index = comboBoxSector.getSelectedIndex();
				String v = comboBoxSector.getValue(index);
				filter.sectorId = Arrays.asList(v);
				updateContent();
			}			
		});

		comboBoxType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				int index = comboBoxType.getSelectedIndex();
				String v = comboBoxType.getValue(index);
				filter.typeId = v;
				updateContent();
			}			
		});
		comboBoxLocation.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event)
			{
				int index = comboBoxLocation.getSelectedIndex();
				String v = comboBoxLocation.getValue(index);
				filter.locationId = Arrays.asList(v);
				updateContent();
			}			
		});
		
		// Create URL column.
		cellTable.addColumnWithIcon(IconCellSingle.IconType.URL, new FieldUpdater<WebSiteDefinition, String>() {
			@Override
			public void update(int index, final WebSiteDefinition object, String value) {
				final Anchor link = new Anchor("", object.url);
				add(link);
				link.setTarget("_blank");
				clickElement(link.getElement());
			}});

		// Create name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, WebSiteDefinition>() {
			@Override
			public String getValue(WebSiteDefinition site)
			{
				return site.name;
			}			
		},  lang._TextName());

		// Create location column.
		cellTable.specialAddColumnSortableString(new GetValue<String, WebSiteDefinition>() {
			@Override
			public String getValue(WebSiteDefinition site)
			{
				String result = "";
				if (site.locationId != null)
				{
					for (String s: site.locationId)
					 result += (result.equals("") ? "":",") +  langLibrary.locationMap().get("locationMap_" + s);
				}
				return result;
			}
		},  lang._TextLocation());

		// Create target column.
		/*
		cellTable.specialAddColumnSortableString(new GetValue<String, WebSiteDefinition>() {
			@Override
			public String getValue(WebSiteDefinition site)
			{
				return site.peopleTarget;
			}			
		},  langLibrary.publicTarget());
		*/
		
		// Create type column.
		cellTable.specialAddColumnSortableString(new GetValue<String, WebSiteDefinition>() {
			@Override
			public String getValue(WebSiteDefinition site)
			{
				return langLibrary.siteTypeMap().get("siteTypeMap_" + site.typeId);
			}			
		},  langLibrary.siteType());

		// Create sector column.
		cellTable.specialAddColumnSortableString(new GetValue<String, WebSiteDefinition>() {
			@Override
			public String getValue(WebSiteDefinition site)
			{
				String result = "";
				if (site.sectorId != null)
				{
					for (String s: site.sectorId)
						result += (result.equals("") ? "":", ") +  langLibrary.sectorMap().get("sectorMap_" + s);
				}
				return result;
			}			
		},  langLibrary.sector());

		cellTable.addColumnWithIcon(IconCellSingle.IconType.ADD, new FieldUpdater<WebSiteDefinition, String>() {
			@Override
			public void update(int index, WebSiteDefinition object, String value) {
				addSite(object);
			}});

	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(cellTable);
	    cellTable.setRowCount(webSiteList.size());
	    
	    
		updateContent();
		
		add(filterPanel);
		add(cellTable);
		add(pager);
		
		setWidth("100%");
	}
	
	private void updateContent()
	{
		webSiteList.clear();
		webSiteList.addAll(library.getSiteFilter(filter));
		cellTable.updateData();
	}

	/* Helper to simulate JS click */
	public static native void clickElement(Element elem) /*-{
    elem.click();
	}-*/;

}

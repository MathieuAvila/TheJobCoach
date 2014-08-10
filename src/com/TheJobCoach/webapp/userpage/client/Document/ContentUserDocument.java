package com.TheJobCoach.webapp.userpage.client.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.DownloadIFrame;
import com.TheJobCoach.webapp.userpage.client.Lang;
import com.TheJobCoach.webapp.userpage.client.UserService;
import com.TheJobCoach.webapp.userpage.client.UserServiceAsync;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.EasyAsync;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.client.IEditResult;
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
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentUserDocument implements EntryPoint, IEditResult<UserDocument> {

	final static Lang lang = GWT.create(Lang.class);
	final static LangDocument langDocument = GWT.create(LangDocument.class);

	UserId user;
	// The list of data to display.
	private List<UserDocument> userDocumentList = new ArrayList<UserDocument>();
	final ExtendedCellTable<UserDocument> cellTable = new ExtendedCellTable<UserDocument>(userDocumentList);
	UserDocument currentSite = null;
	private final UserServiceAsync userService = GWT.create(UserService.class);
	Panel rootPanel;

	public ContentUserDocument(Panel _rootPanel, UserId userId)
	{
		user = userId;
		rootPanel = _rootPanel;
	}

	void getAllContent()
	{
		EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
			public void Run() throws CassandraException
			{
				userService.getUserDocumentList(user, new ServerCallHelper<Vector<UserDocument>>(rootPanel) {	
					@Override
					public void onSuccess(Vector<UserDocument> r)	{
						userDocumentList.clear();
						userDocumentList.addAll(r);				
						cellTable.updateData();
					}});
			}});
	}

	void updateDoc(final UserDocument object)
	{
		EditUserDocument eud = new EditUserDocument(rootPanel, user, object, this);
		eud.onModuleLoad();
	}

	void deleteDoc(final UserDocument object)
	{
		MessageBox mb = new MessageBox(
				rootPanel, true, true, MessageBox.TYPE.QUESTION, langDocument._TextConfirmDeleteUserDocumentTitle(), 
				langDocument._TextConfirmDeleteUserDocument() + object.fileName, new MessageBox.ICallback() {

					@Override
					public void complete(boolean ok) {
						if (ok == true)
						{	
							EasyAsync.serverCall(rootPanel, new EasyAsync.ServerCallRun() {
								public void Run() throws CassandraException
								{
									userService.deleteUserDocument(user, object.ID, new ServerCallHelper<String>(rootPanel) {	
										@Override
										public void onSuccess(String r)	{
											getAllContent();
										}});
								}});
						}
					}
				});
		mb.onModuleLoad();
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		final VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, langDocument._TextUserDocument(), ClientImageBundle.INSTANCE.userDocumentContent());

		cellTable.addColumnWithIcon(IconCellSingle.IconType.DELETE, new FieldUpdater<UserDocument, String>() {
			@Override
			public void update(int index, UserDocument object, String value) {
				deleteDoc(object);
			}}	
				);

		cellTable.addColumnWithIcon(IconCellSingle.IconType.UPDATE, new FieldUpdater<UserDocument, String>() {
			@Override
			public void update(int index, UserDocument object, String value) {
				updateDoc(object);
			}}	
				);

		// Create name column.
		cellTable.specialAddColumnSortableString(new GetValue<String, UserDocument>() {
			@Override
			public String getValue(UserDocument doc)
			{
				return doc.name;
			}			
		},  lang._TextName());

		// file name + link
		cellTable.addColumnWithIconCellFile(
				new FieldUpdater<UserDocument, String>() {
					@Override
					public void update(int index, UserDocument object, String value) {
						if (object.revisions.size() != 0)
						{
							String id = object.revisions.get(object.revisions.size() - 1).ID;
							String copyURL = GWT.getModuleBaseURL() 
									+ "DownloadServlet?docid=" 
									+ URL.encodeQueryString(id) 
									+ "&userid=" 
									+ URL.encodeQueryString(user.userName)
									+ "&fromuserid="
									+ URL.encodeQueryString(user.userName)
									+ "&token=" 
									+ URL.encodeQueryString(user.token);
							DownloadIFrame iframe = new DownloadIFrame(copyURL);
							simplePanelCenter.add(iframe);
						}
					}},
					new GetValue<String, UserDocument>() {
						@Override
						public String getValue(UserDocument contact) {
							return contact.fileName;
						}},
						langDocument._TextFilename()
				);

		// Create type column.
		cellTable.specialAddColumnSortableWithComparator(new GetValue<String, UserDocument>() {
			@Override
			public String getValue(UserDocument document)
			{
				return langDocument.documentTypeMap().get("documentTypeMap_" + UserDocument.documentTypeToString(document.type));
			}}
		, new Comparator<UserDocument>(){
			@Override
			public int compare(UserDocument o1, UserDocument o2)
			{
				return o1.type.compareTo(o2.type);
			}}, langDocument._TextType());

		// Create status column.
		cellTable.specialAddColumnSortableWithComparator(new GetValue<String, UserDocument>() {

			@Override
			public String getValue(UserDocument document)
			{
				return langDocument.documentStatusMap().get("documentStatusMap_" + UserDocument.documentStatusToString(document.status));
			}}
		, new Comparator<UserDocument>(){
			@Override
			public int compare(UserDocument o1, UserDocument o2)
			{
				return o1.status.compareTo(o2.status);
			}}, lang._TextStatus());

		// Create lastUpdate column.
		cellTable.specialAddColumnSortableDate(new GetValue<Date, UserDocument>() {
			@Override
			public Date getValue(UserDocument document)
			{
				return document.lastUpdate;
			}			
		},  langDocument._TextLastUpdate());
		simplePanelCenter.add(cellTable);		

		ButtonImageText button = new ButtonImageText(ButtonImageText.Type.NEW, langDocument._TextNewUserDocument());
		final IEditResult<UserDocument> iEditResult = this;
		button.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event) {
				EditUserDocument eud = new EditUserDocument(rootPanel, user, null, iEditResult);
				eud.onModuleLoad();
			}
		});
		simplePanelCenter.add(button);
		
		cellTable.setRowData(0, userDocumentList);
		cellTable.setRowCount(userDocumentList.size(), true);
		cellTable.setVisibleRange(0, 20);
		
		getAllContent();
	}

	@Override
	public void setResult(UserDocument result)
	{
		getAllContent();
	}
}

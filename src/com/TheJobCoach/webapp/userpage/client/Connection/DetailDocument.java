package com.TheJobCoach.webapp.userpage.client.Connection;

import java.util.Comparator;
import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.DownloadIFrame;
import com.TheJobCoach.webapp.userpage.client.Document.LangDocument;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.client.ExtendedCellTable.GetValue;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class DetailDocument extends DetailPanel {

	// The list of opportunity data to display.
	private Vector<UserDocument> documentList = new Vector<UserDocument>();

	final ExtendedCellTable<UserDocument> cellTable = new ExtendedCellTable<UserDocument>(documentList);
	
	final static LangDocument langDocument = GWT.create(LangDocument.class);

	public DetailDocument(final UserId user, ContactInformation connectionUser)
	{
		super(user, connectionUser);
		add(cellTable);// file name + link
		cellTable.addColumnWithIconCellFile(
				new FieldUpdater<UserDocument, String>() {
					@Override
					public void update(int index, UserDocument object, String value) {
						if (object.revisions.size() != 0)
						{
							String id = object.revisions.get(object.revisions.size() - 1).ID;
							String copyURL = GWT.getModuleBaseURL() + "DownloadServlet?docid=" + URL.encodeQueryString(id) + "&userid=" + URL.encodeQueryString(contactId.userName)+ "&token=" + URL.encodeQueryString(contactId.token);
							DownloadIFrame iframe = new DownloadIFrame(copyURL);
							RootPanel.get().add(iframe);
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

	}

	boolean loaded = false;

	@Override
	public void showPanelDetail()
	{
		if (loaded) return;
		loaded = true;
		AsyncCallback<Vector<UserDocument>>callback = new ServerCallHelper<Vector<UserDocument>>(RootPanel.get()) {
			@Override
			public void onSuccess(Vector<UserDocument> result) {
				documentList.clear();
				documentList.addAll(result);
				cellTable.updateData();
				cellTable.redraw();
			}
		};
		userService.getUserDocumentList(contactId, callback);
		setSize("100%", "100%");
	}
}

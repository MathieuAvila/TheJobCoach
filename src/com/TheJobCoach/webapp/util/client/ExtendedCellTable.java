package com.TheJobCoach.webapp.util.client;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.TheJobCoach.webapp.util.client.ClientImageBundle;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class ExtendedCellTable<DocType> extends CellTable<DocType> {

	enum COLUMN_TYPE {URL, DELETE, UPDATE, RIGHT, EMAIL};

	static final int COUNT_VISIBLE_ROWS = 20;
	
	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);

	private List<DocType> list = null;

	public interface GetValue<C, D> {
		C getValue(D element);
	}
	
	public interface TableRes extends CellTable.Resources {
		@Source({CellTable.Style.DEFAULT_CSS, "com/TheJobCoach/webapp/util/client/CellTable.css"})
		TableStyle cellTableStyle();
		 
		interface TableStyle extends CellTable.Style {}
		}
	
	HashMap<Column<DocType, String>, Comparator<DocType>> compareMethodColumn = new HashMap<Column<DocType, String>, Comparator<DocType>>();

	// Create a data provider.
	AsyncDataProvider<DocType> dataProvider = new AsyncDataProvider<DocType>() {

		@Override
		protected void onRangeChanged(HasData<DocType> display) {
			// Get the ColumnSortInfo from the table.
			final ColumnSortList sortList = getColumnSortList();

			// This sorting code is here so the example works. In practice, you
			// would sort on the server.
			java.util.Collections.sort(list, new Comparator<DocType>() 
					{
				@Override
				public int compare(DocType o1, DocType o2) 
				{
					if (o1 == o2) 
					{
						return 0;
					}
					int diff = 0;
					if ((o1 != null) && (o2 != null))
					{
						Comparator<DocType> comp = null;
						if (sortList.size() != 0)
							comp = compareMethodColumn.get(sortList.get(0).getColumn());
						if (comp != null)
						{
							diff = comp.compare(o1, o2);
							return sortList.get(0).isAscending() ? diff : -diff;
						}
					}
					return 0;
				}
					});
			// Push the data back into the list.
			setRowData(0, list);
			//setVisibleRange(0, list.size());
			redraw();
		}
	};

	public Column<DocType, String> specialAddColumnSortableString(final GetValue<String, DocType> getter, String title) 
	{
		return specialAddColumnSortableWithComparator(getter, new Comparator<DocType>() {

			@Override
			public int compare(DocType o1, DocType o2)
			{
				String v1 = getter.getValue(o1);
				String v2 = getter.getValue(o1);
				if (v1 != null && v2 != null)
					return getter.getValue(o1).compareTo(getter.getValue(o2));
				return 0;
			}}, title);
	}

	public Column<DocType, String> specialAddColumnSortableDate(final GetValue<Date, DocType> getter, String title)
	{
		return specialAddColumnSortableWithComparator(new GetValue<String, DocType>(){

			@Override
			public String getValue(DocType doc)
			{
				return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(getter.getValue(doc));
			}
		}, new Comparator<DocType>() {

			@Override
			public int compare(DocType o1, DocType o2)
			{
				return getter.getValue(o1).before(getter.getValue(o2)) ? -1:1;
			}}, title);
	}

	public Column<DocType, String> specialAddColumnSortableWithComparator(final GetValue<String, DocType> getter, Comparator<DocType> comparator, String title) 
	{
		Column<DocType, String> column = new TextColumn<DocType>() {
			@Override
			public String getValue(DocType doc) 
			{
				return getter.getValue(doc);
			}
		};
		addColumn(column, title);
		compareMethodColumn.put(column, comparator);
		column.setSortable(true);
		return column;
	}


	private <C> Column<DocType, C> specialAddColumn(Cell<C> cell, final GetValue<C, DocType> getter, FieldUpdater<DocType, C> fieldUpdater) 
	{
		Column<DocType, C> column = new Column<DocType, C>(cell) {

			@Override
			public C getValue(DocType object) 
			{
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);

		return column;
	}

	public void addColumnUrl(GetValue<String, DocType> getter)
	{
		// Create URL column.
		ClickableTextCell anchorcolumn = new ClickableTextCell()
		{			
		};
		IconCellUrl iconCellUrl = new IconCellUrl(anchorcolumn);
		Column<DocType, String> columnUrl = specialAddColumn(iconCellUrl, getter,
				new FieldUpdater<DocType, String>() {
			public void update(int index, DocType object, String value) {				
			}
		});
		addColumn(columnUrl, "");
		setColumnWidth(columnUrl, "20px");
	}

	public void addColumnEmail(GetValue<String, DocType> getter)
	{
		// Create URL column.
		ClickableTextCell anchorcolumn = new ClickableTextCell()
		{			
		};
		IconCellUrl iconCellEmail = new IconCellUrl(anchorcolumn, new IconCellUrl.IUrlBuilder()
		{			
			@Override
			public String getUrlFromValue(String value)
			{
				return "mailto:" + value;
			}

			@Override
			public String getTextFromValue(String value)
			{				
				return value;
			}
		}, wpImageBundle.emailLink24());
		Column<DocType, String> columnUrl = specialAddColumn(iconCellEmail, getter,
				new FieldUpdater<DocType, String>() {
			public void update(int index, DocType object, String value) {				
			}
		});
		addColumn(columnUrl, "");
		setColumnWidth(columnUrl, "20px");
	}

	public void addColumnWithIcon(IconCellSingle.IconType type, FieldUpdater<DocType, String> fieldUpdater)
	{		
		IconCellSingle iconCell = new IconCellSingle(type);
		Column<DocType, String> column = specialAddColumn(
				iconCell,
				new GetValue<String, DocType>() {
					public String getValue(DocType contact) {
						return "&nbsp;";
					}
				},
				fieldUpdater);		
		addColumn(column, "");
		setColumnWidth(column, "20px");		
	}

	public void addColumnWithIconCellFile(FieldUpdater<DocType, String> fieldUpdater, GetValue<String, DocType> getter, String cName)
	{
		ClickableTextCell anchorcolumn = new ClickableTextCell()
		{
			@Override
			protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant(
							"<div class=\"clickableText\">" + 
							"<a style=\"clickableText\">");
					sb.append(value);
					sb.appendHtmlConstant("</a></div>");
				}
			}
		};
		IconCellFile iconCellFile = new IconCellFile(anchorcolumn);
		Column<DocType, String> column = specialAddColumn(
				iconCellFile,
				getter,
				fieldUpdater);
		addColumn(column, cName);
		//this.(column, "20px");
	}

	public void addColumnHtml(FieldUpdater<DocType, String> fieldUpdater, final GetValue<String, DocType> getter, String cName)
	{
		ClickableTextCell anchorcolumn = new ClickableTextCell()
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void render(Context context, com.google.gwt.safehtml.shared.SafeHtml value, com.google.gwt.safehtml.shared.SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant(getter.getValue((DocType)context.getKey()));
				}
			}
		};
		Column<DocType, String> column = specialAddColumn(
				anchorcolumn,
				getter,
				fieldUpdater);
		addColumn(column, cName);
	}

	private void init()
	{
		setVisibleRange(0, COUNT_VISIBLE_ROWS);
		setPageSize(COUNT_VISIBLE_ROWS);
		setStyleName("filecelltable");
		setSize("100%", "");
		AsyncHandler columnSortHandler = new AsyncHandler(this);
		addColumnSortHandler(columnSortHandler);
	}
	
	private static CellTable.Resources tableRes = GWT.create(TableRes.class);
	
	public ExtendedCellTable()
	{
		super(COUNT_VISIBLE_ROWS, tableRes);
		init();
	}

	public void updateData()
	{
		dataProvider.updateRowCount(list.size(), true);
		dataProvider.updateRowData(0, list.subList(0, list.size()));
		redraw();			
	}

	public ExtendedCellTable(List<DocType> list)
	{
		super(COUNT_VISIBLE_ROWS, tableRes);
		this.list = list;
		dataProvider.addDataDisplay(this);
		init();
	}
}

package com.TheJobCoach.webapp.util.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;

public class ExtendedCellTable<DocType> extends CellTable<DocType> {

	enum COLUMN_TYPE {URL, DELETE, UPDATE, RIGHT};

	public interface GetValue<C, D> {
		C getValue(D contact);
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
			protected void render(Context context, com.google.gwt.safehtml.shared.SafeHtml value, com.google.gwt.safehtml.shared.SafeHtmlBuilder sb) {
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

	public ExtendedCellTable()
	{
		super();
		setVisibleRange(0, 20);
		setStyleName("filecelltable");
		setSize("100%", "");
	}
}
package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class GridHelper extends Grid
{
	String defaultCol1Size = "30%";
	String defaultCol2Size = "70%";
	
	void init(int w, int h)
	{
		super.setBorderWidth(0);
		super.setSize("100%", "100%");
	}

	public GridHelper(int w, int h)
	{
		super(w, h);
		init( w, h);
	}

	public GridHelper(int h)
	{
		super(0, h);
		init(0, h);
	}
	
	public GridHelper()
	{
		super(0, 0);
		init(0, 0);
	}

	public GridHelper(String s1, String s2)
	{
		super(0, 0);
		init(0, 0);
		defaultCol1Size = s1;
		defaultCol2Size = s2;
	}
	
	public void addLine(int line, Widget title, Widget content)
	{
		if (super.numRows <= line) super.resizeRows(line + 1);
		if (super.numColumns < 2) super.resizeColumns(2);
		super.setWidget(line, 0, title);		
		if (content != null) super.setWidget(line, 1, content);
		super.getCellFormatter().setWidth(line, 0, defaultCol1Size);
		title.setWidth("100%");
		super.getCellFormatter().setWidth(line, 1, defaultCol2Size);
		content.setWidth("100%");
	}
	
	public void addLine(Widget title, Widget content)
	{
		addLine(super.numRows, title, content);
	}
	
	public void addLineTitle(int line, Widget title)
	{
		addLine(line, title, null);
	}
}

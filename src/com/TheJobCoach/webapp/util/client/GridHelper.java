package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class GridHelper extends Grid
{
	void init(Panel root, int w, int h)
	{
		super.setBorderWidth(0);
		super.setSize("100%", "100%");
	}

	public GridHelper(Panel root, int w, int h)
	{
		super(w, h);
		init(root, w, h);
	}

	public GridHelper(Panel root, int h)
	{
		super(2, h);
		init(root, 2, h);
	}
	
	public GridHelper(Panel root)
	{
		super(2, 0);
		init(root, 2, 0);
	}
	
	public void addLine(int line, Widget title, Widget content)
	{
		if (super.numRows <= line) super.resizeRows(line + 1);
		if (super.numColumns < 2) super.resizeColumns(2);
		super.setWidget(line, 0, title);		
		if (content != null) super.setWidget(line, 1, content);
		super.getCellFormatter().setWidth(line, 1, "90%");
		title.setWidth("90%");
	}
	
	public void addLineTitle(int line, Widget title)
	{
		addLine(line, title, null);
	}
}

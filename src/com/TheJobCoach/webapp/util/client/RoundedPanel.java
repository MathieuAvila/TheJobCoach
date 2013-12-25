package com.TheJobCoach.webapp.util.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoundedPanel extends VerticalPanel
{
	SimplePanel header = new SimplePanel();
	SimplePanel headerInside = new SimplePanel();
	SimplePanel content = new SimplePanel();
	Panel contentInside;
	
	public RoundedPanel(Panel p)
	{
		super();
		init(null, p);
	}
	
	public RoundedPanel(Panel p, Widget title)
	{
		super();
		init(null, p);
		this.setTitleWidget(title);
	}
	
	public RoundedPanel()
	{
		super();
		init(null, new SimplePanel());
	}
	
	public RoundedPanel(String additionalStyle, Panel p)
	{	
		super();
		init(additionalStyle, p);
	}
	
	void init(String additionalStyle, Panel contentObject)
	{
		contentInside = contentObject;
		headerInside.addStyleName("subbox");
		headerInside.setWidth("100%");
		header.setWidth("100%");
		super.add(header);
		header.add(headerInside);
		super.add(content);
		content.add(contentInside);
		super.setSize("100%", "100%");
		header.addStyleName("coachboxtitle");
		content.addStyleName("coachboxcontent");
		if (additionalStyle != null)
		{
			header.addStyleName(additionalStyle + "title");
			content.addStyleName(additionalStyle + "content");
		}
		contentInside.addStyleName("subbox");
		contentInside.setWidth("100%");
		content.setWidth("100%");
	}
	
	@Override
	public void add(Widget child)
	{
		//subSp.add(child);
		contentInside.add(child);
	}
	
	public void setTitleWidget(Widget child)
	{
		//subSp.add(child);
		headerInside.add(child);
	}
}

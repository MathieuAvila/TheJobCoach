package com.TheJobCoach.webapp.util.client;

import com.TheJobCoach.webapp.util.client.EasyAsync.ToRun;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestCheckedComponents implements EntryPoint, IChanged {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		final RootPanel root = RootPanel.get("testcheckedcomponent");
		final IChanged listener  = this;
		if (root != null)
		{
			EasyAsync.Check(root, new ToRun() {
				@Override
				public void Open()
				{	
					root.setStyleName("mainpage-content");		
					HorizontalPanel hp = new HorizontalPanel();
					hp.setStyleName("mainpage-content");
					root.add(hp);
					hp.setSize("100%", "100%");
					CheckedTime checkedTime = new CheckedTime();					
					CheckedLabel lblTime = new CheckedLabel("Test CheckedTime", true, checkedTime);
					
					Grid grid = new Grid(6, 2);
					grid.setWidget(0,0, lblTime);
					grid.setWidget(0,1, checkedTime.getItem());
					hp.add(grid);
					checkedTime.getItem().setWidth("100%");
					lblTime.setWidth("500px");
					checkedTime.registerListener(listener);
					
					checkedTime.setDefault((String.valueOf((long)(1000*60*60*1.5))));
					checkedTime.setValue(String.valueOf((long)(1000*60*60*1.5)));
				}
			});
		}}

	@Override
	public void changed(boolean ok, boolean changed, boolean init)
	{
				
	}

}

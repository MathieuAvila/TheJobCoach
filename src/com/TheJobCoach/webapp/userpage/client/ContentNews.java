package com.TheJobCoach.webapp.userpage.client;

import java.util.Vector;

import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.client.ServerCallHelper;
import com.TheJobCoach.webapp.util.shared.UserId;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentNews implements EntryPoint {

	UserId user;
	VerticalPanel simplePanelCenter = new VerticalPanel();
	final Lang lang = GWT.create(Lang.class);
	
	public ContentNews(Panel panel, UserId _user)
	{
		rootPanel = panel;
		user = _user;
	}
	

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	private void getNews()
	{
		ServerCallHelper<Vector<NewsInformation> > callback =  new ServerCallHelper<Vector<NewsInformation>>(rootPanel){
			@Override
			public void onSuccess(Vector<NewsInformation> result)
			{
				for (NewsInformation news: result)
				{
					ContentHelper.insertSubTitlePanel(simplePanelCenter, DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(news.created) + " - " + news.title);
					HTML text = new HTML(news.text);
					text.setStyleName("label-status-ok-nc");
					simplePanelCenter.add(text);
				}				
			}
		};		
		userService.getNews(user, callback);		
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		simplePanelCenter.setWidth("100%");
		rootPanel.add(simplePanelCenter);
		
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextNews(), ClientImageBundle.INSTANCE.newsContent());

		getNews();
	}
}

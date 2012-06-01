package com.TheJobCoach.webapp.userpage.client;

import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;

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
		AsyncCallback<Vector<NewsInformation>> callback = new AsyncCallback<Vector<NewsInformation>>()	{
			@Override
			public void onFailure(Throwable caught)
			{
				Window.alert(caught.getMessage());
			}
			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(Vector<NewsInformation> result)
			{
				System.out.println(result);
				for (NewsInformation news: result)
				{
					ContentHelper.insertSubTitlePanel(simplePanelCenter, DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(news.created) + " - " + news.title);
					HTML text = new HTML(news.text);
					text.setStyleName("label-status-ok-nc");
					simplePanelCenter.add(text);
				}				
			}
		};
		try {
			userService.getNews(user, callback);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{		
		System.out.println("Load News, locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				

		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		simplePanelCenter.setWidth("100%");
		rootPanel.add(simplePanelCenter);
		
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextNews(), ClientImageBundle.INSTANCE.newsContent());

		getNews();
	}
}

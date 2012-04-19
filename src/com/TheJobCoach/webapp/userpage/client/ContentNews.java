package com.TheJobCoach.webapp.userpage.client;

import java.util.Vector;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.NewsInformation;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentNews implements EntryPoint {

	UserId user;
	HTML htmlNews = new HTML("News", true);
	final Lang lang = GWT.create(Lang.class);
	
	public void setUserParameters(UserId _user)
	{
		user = _user;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

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
				String html = "";
				for (NewsInformation news: result)
				{
					System.out.println(news.created);
					int year = news.created.getYear()+1900;
					int month = news.created.getMonth() + 1;
					html += "<hr/><h2>" + news.created.getDate() + "/" + month + "/" + year + " - " + news.title + "</h2></br>" + news.text + "</br></br></br>";
				}
				htmlNews.setHTML(html);
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

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel rootPanel = RootPanel.get("centercontent");
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "100%");
		rootPanel.add(simplePanelCenter);
		
		ContentHelper.insertTitlePanel(simplePanelCenter, lang._TextNews(), ClientImageBundle.INSTANCE.newsContent());

		simplePanelCenter.add(htmlNews);
		simplePanelCenter.setCellWidth(htmlNews, "100%");
		simplePanelCenter.setCellHeight(htmlNews, "100%");
		htmlNews.setSize("100%", "100%");

		getNews();
	}
}

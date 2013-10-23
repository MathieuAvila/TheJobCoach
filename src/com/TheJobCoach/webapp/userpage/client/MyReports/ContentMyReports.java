package com.TheJobCoach.webapp.userpage.client.MyReports;

import java.util.Date;

import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.DownloadIFrame;
import com.TheJobCoach.webapp.userpage.client.images.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.ButtonImageText;
import com.TheJobCoach.webapp.util.client.CheckedCheckBox;
import com.TheJobCoach.webapp.util.client.CheckedDate;
import com.TheJobCoach.webapp.util.client.CheckedExtendedDropListField;
import com.TheJobCoach.webapp.util.client.ContentHelper;
import com.TheJobCoach.webapp.util.shared.ConstantsMyReports;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentMyReports implements EntryPoint {

	final LangMyReports langMyReports = GWT.create(LangMyReports.class);
	
	UserId user;


	final CheckedExtendedDropListField tfPeriod = new CheckedExtendedDropListField(
			ConstantsMyReports.PERIOD_LIST, langMyReports.periodMap(), "period_");
	Label clPeriod = new Label(langMyReports.period());
		
	final CheckedExtendedDropListField tfFormat = new CheckedExtendedDropListField(
			ConstantsMyReports.FORMAT_LIST, langMyReports.formatMap(), "format_");
	Label clFormat = new Label(langMyReports.format());

	final CheckedCheckBox tfDetailOpp = new CheckedCheckBox(FormatUtil.trueString);
	Label clDetailOpp = new Label(langMyReports.includeOpportunityDetail());

	final CheckedCheckBox tfDetailLog = new CheckedCheckBox(FormatUtil.trueString);
	Label clDetailLog = new Label(langMyReports.includeLogDetail());

	final CheckedCheckBox tfLogPeriod = new CheckedCheckBox(FormatUtil.trueString);
	Label clLogPeriod = new Label(langMyReports.onlyLogPeriod());

	final CheckedDate tfStartDate = new CheckedDate();
	Label clStartDate = new Label(langMyReports.startDate());

	final CheckedDate tfEndDate = new CheckedDate();
	Label clEndDate = new Label(langMyReports.endDate());
	
	ButtonImageText buttonActivityReport = new ButtonImageText(ButtonImageText.Type.NEW, langMyReports.getActivityReport());

	final CheckedExtendedDropListField tfFormatContact = new CheckedExtendedDropListField(
			ConstantsMyReports.FORMAT_LIST, langMyReports.formatMap(), "format_");
	Label clFormatContact = new Label(langMyReports.format());

	final CheckedCheckBox tfDetailContact = new CheckedCheckBox(FormatUtil.trueString);
	Label clDetailContact = new Label(langMyReports.includeContactDetail());

	ButtonImageText buttonContactReport = new ButtonImageText(ButtonImageText.Type.NEW, langMyReports.getContactReport());

	public ContentMyReports(Panel rootPanel, UserId user) {
		this.user = user;
		this.rootPanel = rootPanel;
	}

	//private final UserServiceAsync userService = GWT.create(UserService.class);

	Panel rootPanel;

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	public String getStartDate()
	{
		if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_LAST_WEEK))
		{
			Date d = new Date();
			CalendarUtil.addDaysToDate(d, -7);
			return FormatUtil.getDateString(FormatUtil.startOfTheDay(d));			
		} 
		else if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_LAST_2WEEKS))
		{
			Date d = new Date();
			CalendarUtil.addDaysToDate(d, -7 * 2);
			return FormatUtil.getDateString(FormatUtil.startOfTheDay(d));			
		}
		else if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_LAST_MONTH))
		{
			Date d = new Date();
			CalendarUtil.addMonthsToDate(d, -1);
			return FormatUtil.getDateString(FormatUtil.startOfTheDay(d));			
		}
		else if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_LAST_2MONTHS))
		{
			Date d = new Date();
			CalendarUtil.addMonthsToDate(d, -2);
			return FormatUtil.getDateString(FormatUtil.startOfTheDay(d));			
		}
		else if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_SET))
		{
			return tfStartDate.getValue();	
		};		
		//else (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_ALL)
		return FormatUtil.getDateString(FormatUtil.startOfTheUniverse());	
	}
	
	public String getEndDate()
	{
		if (tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_SET))
		{
			return FormatUtil.getDateString(FormatUtil.endOfTheDay(tfEndDate.getItem().getValue()));	
		};
		Date d = new Date();
		return FormatUtil.getDateString(FormatUtil.endOfTheDay(d));		
	}

	private void updateDateSelector()
	{
		boolean enable = tfPeriod.getValue().equals(ConstantsMyReports.PERIOD_SET);
		tfStartDate.getItem().setEnabled(enable);
		tfEndDate.getItem().setEnabled(enable);
		tfStartDate.setValue(getStartDate());
		tfEndDate.setValue(getEndDate());
	}
	
	public String getActivityURL()
	{
		String cookie = LocaleInfo.getLocaleCookieName();
		String cookieLang = com.google.gwt.user.client.Cookies.getCookie(cookie);
		if (cookieLang == null) cookieLang="FR";
		String copyURL = GWT.getModuleBaseURL() + "DownloadReport?reporttype="+URL.encodeQueryString("reportactivity")
				+ "&format=" + URL.encodeQueryString(tfFormatContact.getValue())
				+ "&detail=" + URL.encodeQueryString(tfDetailContact.getValue())
				+ "&lang=" + URL.encodeQueryString(cookieLang)
				+ "&userid=" + URL.encodeQueryString(user.userName)
				+ "&token=" + URL.encodeQueryString(user.token);
		return copyURL;
	}
	/**
	 * This is the entry point method.
	 * @wbp.parser.entryPoint
	 */
	public void onModuleLoad()
	{
		rootPanel.setSize("100%", "100%");
		rootPanel.clear();

		final VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setSize("100%", "");
		rootPanel.add(simplePanelCenter);

		ContentHelper.insertTitlePanel(simplePanelCenter, "Mes rapports", ClientImageBundle.INSTANCE.userMyReportsContent());
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langMyReports.activityReport());

		Grid grid0 = new Grid(7, 2);
		simplePanelCenter.add(grid0);
		
		grid0.setWidget(0,0, clPeriod);
		grid0.setWidget(0,1, tfPeriod.getItem());

		grid0.setWidget(1,0, clStartDate);
		grid0.setWidget(1,1, tfStartDate.getItem());
		
		grid0.setWidget(2,0, clEndDate);
		grid0.setWidget(2,1, tfEndDate.getItem());

		grid0.setWidget(3,0, clFormat);
		grid0.setWidget(3,1, tfFormat.getItem());

		grid0.setWidget(4,0, clDetailOpp);
		grid0.setWidget(4,1, tfDetailOpp.getItem());

		grid0.setWidget(5,0, clDetailLog);
		grid0.setWidget(5,1, tfDetailLog.getItem());

		grid0.setWidget(6,0, clLogPeriod);
		grid0.setWidget(6,1, tfLogPeriod.getItem());
		
		tfStartDate.getItem().setValue(new Date());
		tfEndDate.getItem().setValue(new Date());
		
		tfPeriod.getItem().addChangeHandler(new ChangeHandler()
		{
			@Override
			public void onChange(ChangeEvent event)
			{
				updateDateSelector();
			}			
		});
		
		buttonActivityReport.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event)
			{
				String cookie = LocaleInfo.getLocaleCookieName();
				String cookieLang = com.google.gwt.user.client.Cookies.getCookie(cookie);
				String copyURL = GWT.getModuleBaseURL() + "DownloadReport?reporttype="+URL.encodeQueryString("reportaction")
						+ "&detailopp=" + URL.encodeQueryString(tfDetailOpp.getValue())
						+ "&detaillog=" + URL.encodeQueryString(tfDetailLog.getValue())
						+ "&logperiod=" + URL.encodeQueryString(tfLogPeriod.getValue())
						+ "&start=" + URL.encodeQueryString(getStartDate()) 
						+ "&end=" + URL.encodeQueryString(getEndDate())
						+ "&format=" + URL.encodeQueryString(tfFormat.getValue())
						+ "&lang=" + URL.encodeQueryString(cookieLang)
						+ "&userid=" + URL.encodeQueryString(user.userName)
						+ "&token=" + URL.encodeQueryString(user.token);
				DownloadIFrame iframe = new DownloadIFrame(copyURL);
				simplePanelCenter.add(iframe);
			}
		});
		simplePanelCenter.add(buttonActivityReport);
		tfPeriod.setValue(ConstantsMyReports.PERIOD_LAST_WEEK);
		updateDateSelector();
		
		ContentHelper.insertSubTitlePanel(simplePanelCenter, langMyReports.contactReport());
		Grid grid1 = new Grid(2, 2);
		simplePanelCenter.add(grid1);
		grid1.setWidget(0,0, clFormatContact);
		grid1.setWidget(0,1, tfFormatContact.getItem());

		grid1.setWidget(1,0, clDetailContact);
		grid1.setWidget(1,1, tfDetailContact.getItem());

		buttonContactReport.addClickHandler(new ClickHandler()
		{			
			public void onClick(ClickEvent event)
			{				
				DownloadIFrame iframe = new DownloadIFrame(getActivityURL());
				simplePanelCenter.add(iframe);
			}
		});
		simplePanelCenter.add(buttonContactReport);

	}
}

package com.TheJobCoach.webapp.userpage.client.Connection.Chat;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import sun.java2d.Surface;

import com.TheJobCoach.webapp.userpage.client.Connection.LangConnection;
import com.TheJobCoach.webapp.userpage.client.Connection.Chat.IChatService.GetChatHistoryResult;
import com.TheJobCoach.webapp.userpage.client.Connection.Chat.IChatService.GetChatInformationResult;
import com.TheJobCoach.webapp.userpage.shared.ContactInformation;
import com.TheJobCoach.webapp.util.client.VerticalSpacer;
import com.TheJobCoach.webapp.util.shared.ChatInfo;
import com.TheJobCoach.webapp.util.shared.ChatInfo.MsgType;
import com.TheJobCoach.webapp.util.shared.ChatInfo.UserStatus;
import com.TheJobCoach.webapp.util.shared.FormatUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A widget to display the collection of ChatEvents that are on a particular
 * {@link Surface}.
 *
 */
public class ChatContainer extends FlowPanel implements IChatContainer {

	final static LangConnection langConnection = GWT.create(LangConnection.class);

	private class ChatEventView extends SimplePanel implements
	MouseUpHandler, MouseDownHandler, MouseMoveHandler,
	ValueChangeHandler<String>, IChatInfoHandler, KeyDownHandler
	{
		ContactInformation info;
		
		private final int MINIMUM_SIZE_W = 100;
		private final int MINIMUM_SIZE_H = 100;

		private final DivElement titleElement;
		private final DivElement done;

		private final TextBox content = new TextBox();

		VerticalPanel chatZonePanel = new VerticalPanel();

		private boolean dragging, colorChange;

		private int dragOffsetX, dragOffsetY;

		private int org_x, org_y, org_w, org_h;

		HTML typingInfo = new HTML("is typing");
		
		private boolean sentIsTyping = true;
		
		private IChatService chatService;
		
		private ScrollPanel scroll = new ScrollPanel();

		private Date firstDate = new Date();
		
		private void insertNewHistory(final boolean goDown)
		{
			chatService.getChatHistory(info.userName, firstDate, new GetChatHistoryResult()
			{
				@Override
				public void Run(Vector<ChatInfo> history)
				{
					int size = scroll.getMaximumVerticalScrollPosition();
					Vector<ChatInfo> newList = new Vector<ChatInfo>();
					for (ChatInfo elem: history)
					{
						newList.insertElementAt(elem, 0);
					}
					for (ChatInfo elem: newList)
					{
						receiveChatInfo(elem, true);
						if (elem.eventTime.before(firstDate))
						{
							firstDate.setTime(elem.eventTime.getTime() - 1);
						}
					}
					if (goDown)
						scroll.setVerticalScrollPosition(1000);
					else
					{
						int newSize = scroll.getMaximumVerticalScrollPosition();
						System.out.println(newSize-size);
						scroll.setVerticalScrollPosition(newSize-size);
					}
				}
			});
		}

		/**
		 * @param info 
		 * @param ChatEvent
		 *          the ChatEvent to render
		 */
		public ChatEventView(ContactInformation info, ChatContainer surface, IChatService chatService) 
		{
			this.info = info;
			this.chatService = chatService;

			setStyleName("ChatWindow");

			final Element elem = getElement();
			elem.getStyle().setProperty("position", "absolute");
			getElement().getStyle().setBackgroundColor("lightblue");

			HorizontalPanel hp = new HorizontalPanel();
			SimplePanel titlePanel = new SimplePanel();
			titlePanel.setWidth("100%");
			hp.add(titlePanel);
			hp.setWidth("100%");
			elem.appendChild(hp.getElement());

			titleElement = titlePanel.getElement().appendChild(Document.get().createDivElement());
			titleElement.setClassName("ChatWindow-title");
			if (info.firstName.equals("") || info.lastName.equals(""))
			{
				titleElement.setInnerHTML(info.userName);
				chatService.getInformation(info.userName, new GetChatInformationResult()
				{
					@Override
					public void Run(ContactInformation info)
					{
						titleElement.setInnerHTML(info.userName + " (" + info.firstName + " " + info.lastName + ")");
					}
				});
			}
			else
			{
				titleElement.setInnerHTML(info.userName + " (" + info.firstName + " " + info.lastName + ")");
			}
			
			content.setStyleName("ChatWindow-typeZone");
			content.addValueChangeHandler(this);
			content.addMouseUpHandler(this);

			typingInfo.setStyleName("ChatWindow-isTyping");
			typingInfo.setHTML("<br/>");
			
			SimplePanel sp = new SimplePanel();
			sp.setSize("100%", "300px");
			//sp.addStyleName("coachblabla");
			
			chatZonePanel.setWidth("100%");
			chatZonePanel.setHeight("300px");
			//DOM.setStyleAttribute(chatZonePanel.getElement(), "overflow", "hidden");
			//DOM.setStyleAttribute(sp.getElement(), "overflow", "hidden");
			//DOM.setStyleAttribute(sp.getElement(), "position", "relative");
			//DOM.setStyleAttribute(chatZonePanel.getElement(), "position", "absolute");

			//DOM.setStyleAttribute(chatZonePanel.getElement(), "bottom", "5px");
			//DOM.setStyleAttribute(chatZonePanel.getElement(), "left", "15px");

			VerticalPanel contentPanel = new VerticalPanel();
			
			sp.add(chatZonePanel);
			
			contentPanel.add(new VerticalSpacer("0.5em"));

			//contentPanel.add(sp);
			contentPanel.add(scroll);
			scroll.add(sp);
			//scroll.setAlwaysShowScrollBars(true);
			sp.setHeight("300px");
			
			contentPanel.add(typingInfo);
			contentPanel.add(content);
			this.setWidget(contentPanel);
			
			setPixelPosition(100, 100);
			setPixelSize(200, 15);

			addDomHandler(this, MouseDownEvent.getType());
			addDomHandler(this, MouseMoveEvent.getType());
			addDomHandler(this, MouseUpEvent.getType());
			
			content.addKeyDownHandler(this);
			checkForChange();

			SimplePanel donePanel = new SimplePanel();
			donePanel.setWidth("2em");
			hp.add(donePanel);
			hp.setCellWidth(donePanel, "2em");
			done = donePanel.getElement().appendChild(Document.get().createDivElement());
			done.setClassName("TodoEvent-title");
			done.setInnerHTML("X");
			donePanel.getElement().appendChild(done);

			elem.getStyle().setProperty("height", "400px");
			
			// load history
			insertNewHistory(true);
			chatZonePanel.setHeight("100px");

			scroll.addScrollHandler(new ScrollHandler()
			{
				@Override
				public void onScroll(ScrollEvent event)
				{
					System.out.println(scroll.getVerticalScrollPosition());
					if (scroll.getVerticalScrollPosition() == 0)
					{
						insertNewHistory(false);
					}
				}
			});
		}

		private void addEvent(String text, String style, boolean left, Date d, boolean top)
		{
			Date today = new Date();
			@SuppressWarnings("deprecation")
			boolean sameDay = 
					(today.getDate() == d.getDate() ) && 
					(today.getMonth() == d.getMonth() ) && 
					(today.getYear() == d.getYear() ) ;
			String timeString = sameDay ? FormatUtil.getFormattedTime(d) :  FormatUtil.getFormattedDate(d);
			HTML chatEvent = new HTML(text);
			HorizontalPanel hp = new HorizontalPanel();
			if (!top)
				chatZonePanel.add(hp);
			else
				chatZonePanel.insert(hp, 0);
			chatZonePanel.setCellHorizontalAlignment(hp, left ? HasHorizontalAlignment.ALIGN_LEFT : HasHorizontalAlignment.ALIGN_RIGHT);
			
			hp.setHorizontalAlignment(left ? HasHorizontalAlignment.ALIGN_LEFT : HasHorizontalAlignment.ALIGN_RIGHT);
			chatEvent.setStyleName(style);
			
			Label timeLabel = new Label(timeString);
			timeLabel.setStyleName("ChatWindow-time");
			if (!left) hp.add(timeLabel);
			hp.add(chatEvent);
			if (left) hp.add(timeLabel);
			
			if (!top)
				scroll.setVerticalScrollPosition(scroll.getMaximumVerticalScrollPosition());
		}
		
		@Override
		public void receiveChatInfo(ChatInfo event, boolean history)
		{	
			// TODO Auto-generated method stub
			switch (event.type)
			{
			case IS_TYPING:
				System.out.println("Received is typing from info.userName");
				typingInfo.setHTML(ChatContainer.langConnection.isTyping().replace("%s", info.userName));
				break;
			case MSG_FROM:
			{
				typingInfo.setHTML("<br/>");
				addEvent(event.msg, "ChatWindow-histext-" + (history ? "passive":"active"), false, event.eventTime, history);
			}
			break;
			case MSG_TO:
			{
				addEvent(event.msg, "ChatWindow-mytext-" + (history ? "passive":"active"), true, event.eventTime, history);
			}
			break;
			case STATUS_CHANGE:
				HTML chatEvent = new HTML(((event.status == UserStatus.ONLINE) ? 
						ChatContainer.langConnection.isOnline() : ChatContainer.langConnection.isOffline()).replace("%s", info.userName));
				chatZonePanel.add(chatEvent);
				chatEvent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				chatZonePanel.setCellHorizontalAlignment(chatEvent, HasHorizontalAlignment.ALIGN_LEFT);
				chatEvent.setStyleName("ChatWindow-status");
				scroll.setVerticalScrollPosition(scroll.getMaximumVerticalScrollPosition());
				break;
			default:
				break;
			}
			DOM.setStyleAttribute(chatZonePanel.getElement(), "bottom", "0px");
		}

		private boolean checkForChange()
		{
			final Style style = getElement().getStyle();
			int new_org_x = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
			int new_org_y = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));

			int new_org_w = content.getOffsetWidth() - 2;
			int new_org_h = content.getOffsetHeight() - 2;

			boolean result = (new_org_x != org_x) || (new_org_y != org_y) || (new_org_h != org_h) || (new_org_w != org_w);

			org_x = new_org_x;
			org_y = new_org_y;
			org_w = new_org_w;
			org_h = new_org_h;

			if (org_w < MINIMUM_SIZE_W) org_w = MINIMUM_SIZE_W;
			if (org_h < MINIMUM_SIZE_H) org_h = MINIMUM_SIZE_H;

			return result;
		}

		public void onMouseDown(MouseDownEvent event) {
			ChatContainer.this.select(this);

			final EventTarget target = event.getNativeEvent().getEventTarget();
			assert Element.is(target);
			if (!Element.is(target)) {
				return;
			}
			colorChange = false;				

			if (done.isOrHasChild(Element.as(target))) 
			{
				this.setVisible(false);
				event.preventDefault();
				return;
			}
			if (titleElement.isOrHasChild(Element.as(target))) {
				dragging = true;
				final Element elem = getElement().cast();
				dragOffsetX = event.getX();
				dragOffsetY = event.getY();
				DOM.setCapture(elem);
				event.preventDefault();
			}
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (dragging)
			{	    	  
				final Style style = getElement().getStyle();
				int orgX = Integer.parseInt(style.getLeft().substring(0, (style.getLeft().length() - 2)));
				int orgY = Integer.parseInt(style.getTop().substring(0, (style.getTop().length() - 2)));
				setPixelPosition(event.getX() + orgX - dragOffsetX,
						event.getY() + orgY - dragOffsetY);
				event.preventDefault();
			}
		}

		public void onMouseUp(MouseUpEvent event) 
		{
			if (colorChange)
			{
				colorChange = false;
				event.preventDefault();
				return;
			}
			if (dragging)
			{
				dragging = false;
				DOM.releaseCapture(getElement());
				event.preventDefault();
				// XXX
				return;
			}				
			// Also check for resize.
			if (checkForChange())
			{
				// XXX
			}				
		}

		public void setPixelPosition(int x, int y) {
			final Style style = getElement().getStyle();
			style.setPropertyPx("left", x);
			style.setPropertyPx("top", y);
		}

		public void setPixelSize(int width, int height) {
			content.setPixelSize(width, height);
		}

		@Override
		public void onKeyDown(KeyDownEvent event) 
		{
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
			{
				// add fake message
				ChatInfo msg = new ChatInfo(new Date(), info.userName, MsgType.MSG_TO, content.getText());
				receiveChatInfo(msg, false);
				// make server call
				chatService.sendMsg(info.userName, content.getText());
				sentIsTyping = false;
				// void text zone
				content.setText("");
			}
			else
			{
				System.out.println(" isTyping " + sentIsTyping);
				if (sentIsTyping == false)
				{
					System.out.println("Sent isTyping to: " + info.userName);
					chatService.sendIsTyping(info.userName);
					sentIsTyping = true;
				}
			}
		}

		private void select(int zIndex) {
			getElement().getStyle().setProperty("zIndex", "" + zIndex );
		}

		@Override
		public void onValueChange(ValueChangeEvent<String> event)
		{
			// XXX
		}
	}

	private ChatEventView selectedChatEventView;

	private HashMap<String, ChatEventView> content = new HashMap<String, ChatEventView>();

	private IChatService chatService;

	@Override
	public IChatInfoHandler getChatFromUser(ContactInformation info, boolean create)
	{
		ChatEventView handler = null;
		if (content.containsKey(info.userName))
			handler = content.get(info.userName);
		else
		{
			if (!create)
				return null;
			handler = new ChatEventView(info, this, chatService);
			add(handler);
			content.put(info.userName, handler);
		}
		select(handler);
		handler.setVisible(true);
		return handler;
	}

	public ChatContainer(IChatService chatService) 
	{
		this.chatService = chatService;
		final Element elem = getElement();
		elem.setId("surface");
		elem.getStyle().setProperty("position", "absolute");
	}

	private void select(ChatEventView ChatEventView) 
	{			
		assert ChatEventView != null;
		if (selectedChatEventView != ChatEventView)
		{
			selectedChatEventView = ChatEventView;
			for (ChatEventView view: content.values())
			{
				view.select(ChatEventView == view ? 1 : 0);				
			}
		}
	}
	
	@Override
	public void updateInfo(Vector<ChatInfo> chatInfo)
	{
		for (ChatInfo ci: chatInfo)
		{
			System.out.println("received message from: " + ci.dst + " msg: " + ci.msg + " type "+ ci.type);
			IChatInfoHandler handler = getChatFromUser(new ContactInformation(ci.dst, "",""), ci.type == MsgType.MSG_FROM);
			if (handler != null)
				handler.receiveChatInfo(ci, false);
		}
	}

}
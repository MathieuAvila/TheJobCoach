package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.footer.client.Footer;
import com.TheJobCoach.webapp.mainpage.client.Lang;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Validate implements EntryPoint {

	Panel rootPanel;
	final static Lang lang = GWT.create(Lang.class);
	
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public void setRootPanel(Panel panel)
	{
		rootPanel = panel;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		rootPanel.clear();
		rootPanel.setStyleName("footer-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		verticalPanel.add(verticalPanel_3);
		verticalPanel.setCellHeight(verticalPanel_3, "50");
		verticalPanel_3.setHeight("50");

		Image image = new Image(ClientImageBundle.INSTANCE.coach_logo());
		verticalPanel.add(image);
		verticalPanel.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);

		Label label = new Label("The Job Coach");
		label.setStyleName("mainpage-title");
		verticalPanel.add(label);
		verticalPanel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel.add(verticalPanel_1);
		verticalPanel.setCellHeight(verticalPanel_1, "50");
		verticalPanel_1.setHeight("50");

		final Label panel = new Label(lang._TextValidateLoginWait());
		verticalPanel.add(panel);
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel.add(verticalPanel_2);
		verticalPanel.setCellHeight(verticalPanel_2, "50");
		verticalPanel_2.setHeight("50");

		Button btnNewButton = new Button(lang._TextValidateGoToLogin());
		verticalPanel.add(btnNewButton);
		
		VerticalPanel verticalPanel_5 = new VerticalPanel();
		verticalPanel_5.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.add(verticalPanel_5);
		verticalPanel.setCellVerticalAlignment(verticalPanel_5, HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.setCellHeight(verticalPanel_5, "200");
		verticalPanel_5.setHeight("200");
		
		SimplePanel simplePanel = new SimplePanel();
		verticalPanel.add(simplePanel);
		verticalPanel.setCellHeight(simplePanel, "100%");
		simplePanel.setHeight("100%");
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		verticalPanel_4.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel_4.setStyleName("mainpage-content");
		verticalPanel_4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(verticalPanel_4);
		verticalPanel.setCellVerticalAlignment(verticalPanel_4, HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel_4.setSize("", "");
		Footer footerPanel = new Footer();
		footerPanel.setRootPanel(verticalPanel_4);	
		footerPanel.onModuleLoad();
		
		
		String username = com.google.gwt.user.client.Window.Location.getParameter("username");
		String token = com.google.gwt.user.client.Window.Location.getParameter("token");

		loginService.validateAccount(username, token, new AsyncCallback<MainPageReturnCode.ValidateAccountStatus>() {
			public void onFailure(Throwable caught) {				
				panel.setText("Failure");
			}

			public void onSuccess(MainPageReturnCode.ValidateAccountStatus result)
			{
				switch (result)
				{
				case VALIDATE_STATUS_ERROR:
					panel.setText(lang._TextValidateLoginUnexpectedError());
					break;
				case VALIDATE_STATUS_UNKNOWN:
					panel.setText(lang._TextValidateLoginNoSuchLogin());
					break;
				case VALIDATE_STATUS_OK:
					panel.setText(lang._TextValidateSuccess());
					break;
				}
			}
		});
		
		class ConnectHandlerLoginPage implements ClickHandler {

			public void onClick(ClickEvent event) {
				UrlBuilder urlB = Window.Location.createUrlBuilder();
				urlB.removeParameter("action");
				String url = urlB.buildString();
				Window.Location.assign(url);		
				Window.Location.replace(url);
			}
		};
		// Add a handler to the connect button clicker.
		ConnectHandlerLoginPage goToLoginHandler = new ConnectHandlerLoginPage();
		btnNewButton.addClickHandler(goToLoginHandler);

	}
}

package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.adminpage.client.AdminPage;
import com.TheJobCoach.webapp.footer.client.Footer;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.userpage.client.UserPage;
import com.TheJobCoach.webapp.util.client.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainPage implements EntryPoint {

	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	RootPanel rootPanel = null;

	void messageLogin(String title, String message)
	{
		MessageBox mb = new MessageBox(rootPanel, true, false, MessageBox.TYPE.ERROR, 
				title, 
				message,
				null);
		mb.onModuleLoad();
	}

	public void onModuleLoad()
	{	
		final Lang lang = GWT.create(Lang.class);
		
		rootPanel = RootPanel.get("content");
		rootPanel.clear();
		rootPanel.setStyleName("mainpage-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");
		
		Image image_FR = new Image(ClientImageBundle.INSTANCE.flag_FR());
		Image image_EN = new Image(ClientImageBundle.INSTANCE.flag_EN());
		Image image_coach = new Image(ClientImageBundle.INSTANCE.coach_logo());
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.setStyleName("mainpage-content");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(verticalPanel, 0, 0);
		verticalPanel.setSize("100%", Window.getClientHeight() - 20 + "px");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);
		verticalPanel.setCellVerticalAlignment(horizontalPanel_1, HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanel_1.setHeight("50px");
		horizontalPanel_1.setStyleName("mainpage-content");

		verticalPanel.add(image_coach);

		Label lblTheJobCoach = new Label("The Job Coach");
		lblTheJobCoach.setStyleName("mainpage-title");
		verticalPanel.add(lblTheJobCoach);

		Label lblNewLabel = new Label(lang._TextSlogan());
		lblNewLabel.setStyleName("mainpage-label-slogan");
		verticalPanel.add(lblNewLabel);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setStyleName("mainpage-content");
		verticalPanel.add(horizontalPanel_2);
		horizontalPanel_2.setHeight("10px");

		HorizontalPanel horizontalPanel_12 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_12);

		horizontalPanel_12.add(image_FR);
		horizontalPanel_12.setCellHeight(image_FR, "20");
		horizontalPanel_12.setCellWidth(image_FR, "20");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		horizontalPanel_12.add(verticalPanel_3);
		horizontalPanel_12.setCellWidth(verticalPanel_3, "50px");
		verticalPanel_3.setSize("50px", "");

		horizontalPanel_12.add(image_EN);
		horizontalPanel_12.setCellHeight(image_EN, "20");
		horizontalPanel_12.setCellWidth(image_EN, "20");
		
		HorizontalPanel horizontalPanel_11 = new HorizontalPanel();
		horizontalPanel_11.setStyleName("mainpage-content");
		verticalPanel.add(horizontalPanel_11);
		horizontalPanel_11.setHeight("10px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("mainpage-content");
		verticalPanel.add(horizontalPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setSize("", "30px");

		Label lblNomDutilisateur = new Label(lang._TextUserName());
		lblNomDutilisateur.setStyleName("mainpage-label-userpass");
		horizontalPanel.add(lblNomDutilisateur);
		horizontalPanel.setCellVerticalAlignment(lblNomDutilisateur, HasVerticalAlignment.ALIGN_MIDDLE);
		final TextBox nameField = new TextBox();
		horizontalPanel.add(nameField);
		horizontalPanel.setCellVerticalAlignment(nameField, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(nameField, HasHorizontalAlignment.ALIGN_CENTER);

		Label lblMotDePasse = new Label(lang._TextUserPassword());
		lblMotDePasse.setStyleName("mainpage-label-userpass");
		horizontalPanel.add(lblMotDePasse);
		horizontalPanel.setCellVerticalAlignment(lblMotDePasse, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(lblMotDePasse, HasHorizontalAlignment.ALIGN_CENTER);

		final TextBox passwordField = new PasswordTextBox();
		horizontalPanel.add(passwordField);
		horizontalPanel.setCellVerticalAlignment(passwordField, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(passwordField, HasHorizontalAlignment.ALIGN_CENTER);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		final Button connectButton = new Button(lang._TextLogin());
		connectButton.setStyleName("mainpage-connect");
		horizontalPanel.add(connectButton);
		verticalPanel.setCellHorizontalAlignment(connectButton, HasHorizontalAlignment.ALIGN_CENTER);

		// We can add style names to widgets
		connectButton.addStyleName("sendButton");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel.add(verticalPanel_1);
		verticalPanel.setCellHeight(verticalPanel_1, "20px");
		verticalPanel_1.setHeight("20px");

		Label lblLostCredentials = new Label(lang._TextLostCredentials());
		verticalPanel.add(lblLostCredentials);
		lblLostCredentials.setStyleName("mainpage-label-clickable");
		// Create a handler for the lost credentials
		class LostCredentialsHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				LostCredentials lc = new LostCredentials(rootPanel);
				lc.onModuleLoad();
			}
		}
		// Add a handler to the the lost credentials button clicker.
		LostCredentialsHandler lostCredentialsHandler = new LostCredentialsHandler();
		lblLostCredentials.addClickHandler(lostCredentialsHandler);

		VerticalPanel verticalPanel_space = new VerticalPanel();
		verticalPanel.add(verticalPanel_space);
		verticalPanel.setCellHeight(verticalPanel_space, "20px");
		verticalPanel_space.setHeight("20px");
		
		
		Label lblPasEncoreInscrit = new Label(lang._TextNotYesRegistered());
		verticalPanel.add(lblPasEncoreInscrit);
		lblPasEncoreInscrit.setStyleName("mainpage-label-clickable");

		// Create a handler for the not subscribed button
		class NotSubscribedHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				CreateAccount ca = new CreateAccount(rootPanel);
				ca.onModuleLoad();
			}
		}
		// Add a handler to the not subscribed button clicker.
		NotSubscribedHandler notSubscribedHandler = new NotSubscribedHandler();
		lblPasEncoreInscrit.addClickHandler(notSubscribedHandler);

		
		
		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		simplePanelCenter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Footer footerPanel = new Footer();
		footerPanel.setRootPanel(simplePanelCenter);	
		footerPanel.onModuleLoad();
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.add(verticalPanel_2);
		verticalPanel.setCellVerticalAlignment(verticalPanel_2, HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel.setCellHeight(verticalPanel_2, "100%");
		verticalPanel_2.setHeight("100%");
		verticalPanel.add(simplePanelCenter);
		verticalPanel.setCellHorizontalAlignment(simplePanelCenter, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setCellVerticalAlignment(simplePanelCenter, HasVerticalAlignment.ALIGN_BOTTOM);
		simplePanelCenter.setSize("", "");
		class ConnectHandler implements ClickHandler {

			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			private void sendNameToServer() {						
				// Then, we send the input to the server.
				connectButton.setEnabled(false);
				loginService.connect(nameField.getText(), passwordField.getText(), new AsyncCallback<MainPageReturnLogin>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user						
						connectButton.setEnabled(true);
					}

					public void onSuccess(MainPageReturnLogin result)
					{
						connectButton.setEnabled(true);
						System.out.println("Login returned result is:" + result.getLoginStatus());
						if (result.id != null)
							System.out.println("Login returned: " + result.id.token + " result is:" + result.getLoginStatus());
						switch (result.getLoginStatus())
						{ 
						case CONNECT_STATUS_UNKNOWN_USER:
							MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Error", lang._TextLoginNoSuchLoginPassword());
							break;
						case CONNECT_STATUS_PASSWORD:		
							MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Error", lang._TextLoginNoSuchLoginPassword());
							break;
						case CONNECT_STATUS_NOT_VALIDATED:
							MessageBox.messageBox(rootPanel, MessageBox.TYPE.INFO, "Error", lang._TextLoginNotValidated());
							break;
						case CONNECT_STATUS_OK:
							switch (result.id.type)
							{
							case USER_TYPE_SEEKER:
								UserPage uP = new UserPage();
								uP.setUser(result.id);
								uP.onModuleLoad();
								break;
							case USER_TYPE_COACH:
								UserPage cP = new UserPage();
								cP.setUser(result.id);
								cP.onModuleLoad();
								break;
							case USER_TYPE_ADMIN:
								AdminPage aP = new AdminPage();
								aP.setUser(result.id);
								aP.onModuleLoad();
								break;
							}
							return;
						}
					}
				});
			}
		}

		// Add a handler to the connect button clicker.
		ConnectHandler handler = new ConnectHandler();
		connectButton.addClickHandler(handler);

		// Create a handler for the Language click button
		class CreateLangHandler implements ClickHandler
		{

			String langName;

			public CreateLangHandler(String _langName)
			{			
				langName = _langName;
			}

			public void onClick(ClickEvent event)
			{				
				System.out.println("Setting is: " + LocaleInfo.getCurrentLocale().getLocaleName());

				System.out.println("Click on " + langName);
				String cookie = LocaleInfo.getLocaleCookieName();
				com.google.gwt.user.client.Cookies.setCookie(cookie, langName);
				System.out.println("Applied locale "+langName);
				Window.Location.reload();
			}
		}
		image_FR.addClickHandler(new CreateLangHandler("fr"));
		image_EN.addClickHandler(new CreateLangHandler("en"));		

	}

}

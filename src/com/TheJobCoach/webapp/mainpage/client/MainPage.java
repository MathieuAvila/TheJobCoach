package com.TheJobCoach.webapp.mainpage.client;

import com.TheJobCoach.webapp.footer.client.Footer;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnCode;
import com.TheJobCoach.webapp.mainpage.shared.MainPageReturnLogin;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.userpage.client.UserPage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainPage implements EntryPoint {
	
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{		
		Lang lang = GWT.create(Lang.class);
		System.out.println("Locale is: " + LocaleInfo.getCurrentLocale().getLocaleName());				
		
		UserPage uP = new UserPage();
		uP.setUser(new UserId("user", "user", UserId.UserType.USER_TYPE_SEEKER));
		uP.onModuleLoad();
		return;
		/*
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("content");
		rootPanel.clear();
		rootPanel.setStyleName("mainpage-content");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.setSize("100%", "100%");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setStyleName("mainpage-content");
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(verticalPanel, 0, 0);
		verticalPanel.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.setHeight("100px");
		horizontalPanel_1.setStyleName("mainpage-content");
		
		Image image = new Image("jobcoach.gif");
		verticalPanel.add(image);
		
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
		
		Image imageFr = new Image("drapeau_francais.gif");
		horizontalPanel_12.add(imageFr);
		horizontalPanel_12.setCellHeight(imageFr, "20");
		horizontalPanel_12.setCellWidth(imageFr, "20");
		imageFr.setSize("30px", "30px");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		horizontalPanel_12.add(verticalPanel_3);
		horizontalPanel_12.setCellWidth(verticalPanel_3, "50px");
		verticalPanel_3.setSize("50px", "");
		
		Image imageEn = new Image("drapeau_anglais.gif");
		horizontalPanel_12.add(imageEn);
		horizontalPanel_12.setCellHeight(imageEn, "20");
		horizontalPanel_12.setCellWidth(imageEn, "20");
		imageEn.setSize("30px", "30px");
		
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
		nameField.setText("mathieu");
		horizontalPanel.add(nameField);
		horizontalPanel.setCellVerticalAlignment(nameField, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(nameField, HasHorizontalAlignment.ALIGN_CENTER);
		
		Label lblMotDePasse = new Label(lang._TextUserPassword());
		lblMotDePasse.setStyleName("mainpage-label-userpass");
		horizontalPanel.add(lblMotDePasse);
		horizontalPanel.setCellVerticalAlignment(lblMotDePasse, HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(lblMotDePasse, HasHorizontalAlignment.ALIGN_CENTER);
		
		final TextBox passwordField = new TextBox();
		passwordField.setText("mathieu");
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
		verticalPanel.setCellHeight(verticalPanel_1, "50px");
		verticalPanel_1.setHeight("50px");

		Label lblPasEncoreInscrit = new Label(lang._TextNotYesRegistered());
		verticalPanel.add(lblPasEncoreInscrit);
		lblPasEncoreInscrit.setStyleName("mainpage-label-clickable");				
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel.add(verticalPanel_2);
		verticalPanel.setCellHeight(verticalPanel_2, "50px");
		verticalPanel_2.setHeight("50px");
		
		HorizontalPanel horizontalPanel_8 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_8);
		verticalPanel.setCellWidth(horizontalPanel_8, "378");
		verticalPanel.setCellHeight(horizontalPanel_8, "178");
		horizontalPanel_8.setSize("378", "178");

		final VerticalPanel verticalPanelSubscribe = new VerticalPanel();
		horizontalPanel_8.add(verticalPanelSubscribe);
		verticalPanelSubscribe.setStyleName("mainpage-create_account-panel");
		verticalPanelSubscribe.setSize("267px", "178px");
		
		HorizontalPanel horizontalPanel_10 = new HorizontalPanel();
		verticalPanelSubscribe.add(horizontalPanel_10);
		horizontalPanel_10.setWidth("267px");
		
		Label lblUserName = new Label(lang._TextUserName());
		horizontalPanel_10.add(lblUserName);
		
		final TextBox textBoxUserName = new TextBox();
		textBoxUserName.setText("atila");
		horizontalPanel_10.add(textBoxUserName);
		horizontalPanel_10.setCellHorizontalAlignment(textBoxUserName, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		verticalPanelSubscribe.add(horizontalPanel_5);
		horizontalPanel_5.setWidth("267px");

		Label label = new Label(lang._TextUserEMail());
		horizontalPanel_5.add(label);

		final TextBox textBoxMail = new TextBox();
		textBoxMail.setText("mathieu.avila@laposte.net");
		horizontalPanel_5.add(textBoxMail);
		horizontalPanel_5.setCellHorizontalAlignment(textBoxMail, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel_7 = new HorizontalPanel();
		verticalPanelSubscribe.add(horizontalPanel_7);
		horizontalPanel_7.setWidth("267px");

		Label label_2 = new Label("Nom");
		horizontalPanel_7.add(label_2);

		final TextBox textBoxCreateName = new TextBox();
		textBoxCreateName.setText("Avila");
		horizontalPanel_7.add(textBoxCreateName);
		horizontalPanel_7.setCellHorizontalAlignment(textBoxCreateName, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
		verticalPanelSubscribe.add(horizontalPanel_6);
		horizontalPanel_6.setWidth("267px");

		Label label_1 = new Label("Prénom");
		horizontalPanel_6.add(label_1);

		final TextBox textBoxFirstName = new TextBox();
		textBoxFirstName.setText("Mathieu");
		horizontalPanel_6.add(textBoxFirstName);
		horizontalPanel_6.setCellHorizontalAlignment(textBoxFirstName, HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		verticalPanelSubscribe.add(horizontalPanel_4);
		horizontalPanel_4.setWidth("267px");

		Label lblNewLabel_1 = new Label("Mot de passe");
		horizontalPanel_4.add(lblNewLabel_1);

		final TextBox newUserEmailBox = new TextBox();
		newUserEmailBox.setText("mathieu");
		horizontalPanel_4.add(newUserEmailBox);
		horizontalPanel_4.setCellHorizontalAlignment(newUserEmailBox, HasHorizontalAlignment.ALIGN_RIGHT);

		Button btnCreateAccount = new Button(lang._TextCreateAccount());
		verticalPanelSubscribe.add(btnCreateAccount);
		verticalPanelSubscribe.setCellHorizontalAlignment(btnCreateAccount, HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanelSubscribe.setVisible(false);

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setStyleName("mainpage-content");
		verticalPanel.add(horizontalPanel_3);
		verticalPanel.setCellVerticalAlignment(horizontalPanel_3, HasVerticalAlignment.ALIGN_BOTTOM);
		horizontalPanel_3.setHeight("100px");
		
		VerticalPanel simplePanelCenter = new VerticalPanel();
		simplePanelCenter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Footer footerPanel = new Footer();
		footerPanel.setRootPanel(simplePanelCenter);	
		footerPanel.onModuleLoad();
		verticalPanel.add(simplePanelCenter);
		verticalPanel.setCellHorizontalAlignment(simplePanelCenter, HasHorizontalAlignment.ALIGN_CENTER);
		simplePanelCenter.setStyleName("mainpage-content");	
		simplePanelCenter.setSize("", "");
		// Create a handler for the connect button
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
						//dialogBox.setText("Remote Procedure Call");
						//serverResponseLabel.setHTML(result);
						//dialogBox.center();
						connectButton.setEnabled(true);
						System.out.println("Login returned: " + result.getToken()+ " result is:" + result.getLoginStatus());
						switch (result.getLoginStatus())
						{ 
						case CONNECT_STATUS_UNKNOWN_USER:
							Window.alert("Error at login: this user is unknown");
							break;
						case CONNECT_STATUS_PASSWORD:		
							Window.alert("Error at login: wrong password");
							break;
						case CONNECT_STATUS_NOT_VALIDATED:
							Window.alert("Error at login: this account has not been validated. Please check your email for validation");
							break;
						case CONNECT_STATUS_OK:
							UserPage uP = new UserPage();
							uP.setUser(new UserId(nameField.getText(), result.getToken()));
							uP.onModuleLoad();
							return;
						}
					}
				});
			}
		}

		// Add a handler to the connect button clicker.
		ConnectHandler handler = new ConnectHandler();
		connectButton.addClickHandler(handler);

		// Create a handler for the connect button
		class CreateAccountHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				verticalPanelSubscribe.setVisible(!verticalPanelSubscribe.isVisible());
				System.out.println(textBoxUserName.getText());
				System.out.println(passwordField.getText());
				System.out.println(textBoxMail.getText());
				loginService.createAccount(
						textBoxUserName.getText(), 
						textBoxCreateName.getText(), 
						textBoxFirstName.getText(),
						passwordField.getText(), 
						textBoxMail.getText(),
						LocaleInfo.getCurrentLocale().getLocaleName(),
						new AsyncCallback<MainPageReturnCode.CreateAccountStatus>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						connectButton.setFocus(true);
					}

					public void onSuccess(MainPageReturnCode.CreateAccountStatus result) {
						//dialogBox.setText("Remote Procedure Call");
						//serverResponseLabel.setHTML(result);
						//dialogBox.center();
						connectButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler to the connect button clicker.
		CreateAccountHandler createAccountHandler = new CreateAccountHandler();
		lblPasEncoreInscrit.addClickHandler(createAccountHandler);
			
		
		
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
		imageFr.addClickHandler(new CreateLangHandler("fr"));
		imageEn.addClickHandler(new CreateLangHandler("en"));
		*/
	}
	
}

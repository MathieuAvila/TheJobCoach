package com.TheJobCoach.webapp;

import com.TheJobCoach.webapp.util.client.MessageBoxTriState;
import com.TheJobCoach.webapp.util.client.MessageBoxTriState.TYPE;

public class CatcherMessageBoxTriState implements MessageBoxTriState.Catcher
{
	public MessageBoxTriState currentBox;
	public TYPE type;
	public String title;
	public String message;
	
	@Override
	public void event(MessageBoxTriState box, TYPE type, String title, String message)
	{
		currentBox = box;
		this.type = type;
		this.title = title;
		this.message = message;
	}
	
	public boolean isTriggered()
	{
		return currentBox != null;	
	}
	
	public void closeBox()
	{
		if (currentBox != null) currentBox.close();
		currentBox = null;
	}
	
	public MessageBoxTriState getCurrentMessageBox()
	{
		return currentBox;
	}
	
	public TYPE getCurrentType()
	{
		return type;
	}
	
	public CatcherMessageBoxTriState()
	{
		// Set-up message catcher.
		MessageBoxTriState.registerErrorCatcher(this);
	}
}

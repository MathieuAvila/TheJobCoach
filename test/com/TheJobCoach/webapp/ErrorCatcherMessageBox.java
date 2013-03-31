package com.TheJobCoach.webapp;

import com.TheJobCoach.webapp.util.client.MessageBox;
import com.TheJobCoach.webapp.util.client.MessageBox.TYPE;

public class ErrorCatcherMessageBox implements MessageBox.ErrorCatcher
{
	public MessageBox currentBox;
	public TYPE type;

	@Override
	public void errorEvent(MessageBox error, TYPE type)
	{
		currentBox = error;
		this.type = type;
	}
	
	public boolean hasError()
	{
		return currentBox != null;	
	}
	
	public void clearError()
	{
		currentBox.close();
		currentBox = null;
	}
	
	public MessageBox getCurrentMessageBox()
	{
		return currentBox;
	}
	
	public TYPE getCurrentType()
	{
		return type;
	}
	
	public ErrorCatcherMessageBox()
	{
		// Set-up message catcher.
		MessageBox.registerErrorCatcher(this);
	}
	
}

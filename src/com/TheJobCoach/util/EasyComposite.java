package com.TheJobCoach.util;

import java.util.Date;
import me.prettyprint.hector.api.beans.Composite;

public class EasyComposite extends Composite
{		
	public EasyComposite easyAdd(String k)
	{
		add(k);
		return this;
	}

	public EasyComposite easyAdd(Date k)
	{
		add(k);
		return this;
	}
	
}

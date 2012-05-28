package com.TheJobCoach.webapp.util.client;

import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class DynamicImage extends Image implements IChanged {
	
	Map<String, ImageResource> images;
	
	public void setValue()
	{
		if (from != null)
		{
			String value = from.getValue();
			if ((value != null) && (images.get(value) !=null))
				super.setResource(images.get(value));
		}
	}
	
	@Override
	public void changed(boolean ok, boolean isDefault, boolean init) 
	{
		setValue();
	}
	
	IExtendedField from = null;
	
	void init(boolean initial, IExtendedField from, Map<String, ImageResource> images)
	{
		this.from = from;
		this.images = images;
	}
	
	public DynamicImage(IExtendedField from, Map<String, ImageResource> images)
	{
		super("");
		init(false, from, images);
		setSource(from);
	}

	public void setSource(IExtendedField from)
	{
		this.from = from;
		from.registerListener(this);
		setValue();
	}

	public DynamicImage(Map<String, ImageResource> images)
	{
		super("");
		init(false, null, images);
	}

	
}

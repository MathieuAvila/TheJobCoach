package com.TheJobCoach.webapp.userpage.client.MyGoals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.TheJobCoach.webapp.util.client.ClientImageBundle;
import com.TheJobCoach.webapp.util.client.HorizontalSpacer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ResultEvaluation extends HorizontalPanel
{
	int minimum;
	int value;
	boolean current;
	
	Set<String> isSet = new HashSet<String>();
	Map<Boolean, String> remarks;
	static ClientImageBundle wpImageBundle = (ClientImageBundle) GWT.create(ClientImageBundle.class);
	static ImageResource success = wpImageBundle.success_24();
	static ImageResource failure = wpImageBundle.failure_24();
	static ImageResource unknown = wpImageBundle.unknown_24();
	static Map<Boolean, Map<Boolean, ImageResource>> images = new HashMap<Boolean, Map<Boolean, ImageResource>>();
	static Map<Boolean, ImageResource> imagesCurrent = new HashMap<Boolean, ImageResource>();
	Label actualPerformance = new Label();
	Image img = new Image();
	Label successOrFailure = new Label();
	Label remark = new Label();

	final static LangGoals langGoals = GWT.create(LangGoals.class);
	
	{
		Map<Boolean, ImageResource> current = new HashMap<Boolean, ImageResource>();
		current.put(new Boolean(true), success);
		current.put(new Boolean(false), unknown);
		
		Map<Boolean, ImageResource> past = new HashMap<Boolean, ImageResource>();
		past.put(new Boolean(true), success);
		past.put(new Boolean(false), failure);
		
		images.put(new Boolean(true), current);
		images.put(new Boolean(false), past);
	}
	
	public void setResource(ImageResource imageResource)
	{
		DOM.setImgSrc(img.getElement(), imageResource.getSafeUri().asString());
	}

	
	ResultEvaluation()
	{
		super();
		add(actualPerformance);
		add(new HorizontalSpacer("2em"));
		add(img);
		add(new HorizontalSpacer("1em"));
		add(successOrFailure);
		add(remark);
	}
	
	void update()
	{
		if (isSet.size() != 3) return;
		boolean success = value >= minimum;
		setResource(images.get(new Boolean(current)).get(new Boolean(success)));
		successOrFailure.setText(success ? langGoals.success() : (current ? langGoals.unknown() : langGoals.failure()));
		actualPerformance.setText(String.valueOf(value));
	}
	
	public void setValue(int value)
	{
		this.value = value;
		isSet.add("v");
		update();
	}
	
	public void setMinimum(int minimum)
	{
		this.minimum = minimum;
		isSet.add("m");
		update();
	}
	
	public void setCurrent(boolean current)
	{
		this.current = current;
		isSet.add("c");
		update();
	}
	
}

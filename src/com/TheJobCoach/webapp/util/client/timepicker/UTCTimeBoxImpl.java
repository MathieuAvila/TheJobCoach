package com.TheJobCoach.webapp.util.client.timepicker;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface UTCTimeBoxImpl extends IsWidget, HasValue<Long>, HasValueChangeHandlers<Long>, HasText {
 
    public void setTimeFormat(DateTimeFormat timeFormat);
   
    public void setVisibleLength(int length);    

    public void validate();

    public void setTabIndex(int tabIndex);
       
}

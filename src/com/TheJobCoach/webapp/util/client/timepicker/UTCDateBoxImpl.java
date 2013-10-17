package com.TheJobCoach.webapp.util.client.timepicker;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.datepicker.client.DateBox;

public interface UTCDateBoxImpl extends IsWidget, HasValue<Long>, HasValueChangeHandlers<Long>, HasText, HasEnabled {
   
    public void setDateFormat(DateTimeFormat dateFormat);

    public void setVisibleLength(int length);    

    public void setTabIndex(int tabIndex);

    public DateBox getDateBox();

}
 
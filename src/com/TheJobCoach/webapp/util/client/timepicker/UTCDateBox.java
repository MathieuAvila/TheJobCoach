package com.TheJobCoach.webapp.util.client.timepicker;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class UTCDateBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long>, HasText, HasEnabled {

    private UTCDateBoxImpl impl;

    public UTCDateBox() {
        this(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM));
    }

    public UTCDateBox(DateTimeFormat format) {
        impl = GWT.create(UTCDateBoxImpl.class);
        impl.setDateFormat(format);
        initWidget(impl.asWidget());
    }

    @Deprecated
    public UTCDateBox(DatePicker picker, long date, DateBox.Format format) {
        this();
        impl.setValue(date);
    }
   
    @Override
    public String getText() {
        return impl.getText();
    }
   
    @Override
    public void setText(String text) {
        impl.setText(text);
    }

    @Override
    public Long getValue() {
        return impl.getValue();
    }

    @Override
    public void setValue(Long value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        impl.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return impl.addValueChangeHandler(handler);
    }

    @Override
    public boolean isEnabled() {
        return impl.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        impl.setEnabled(enabled);
    }

    public void setVisibleLength(int length) {
        impl.setVisibleLength(length);
    }

    public void setTabIndex(int tabIndex) {
        impl.setTabIndex(tabIndex);
    }

    public DateBox getDateBox() {
        return impl.getDateBox();
    }

    public static final long DAY_IN_MS = 24L*60L*60L*1000L;
   
    public static final long trimTimeToMidnight(long time) {
        // first trim to midnight
        return time - time % DAY_IN_MS;
    }

    public static final Date utc2date(Long time) {

        // don't accept negative values
        if (time == null || time < 0) return null;
       
        // add the timezone offset
        time += timezoneOffsetMillis(new Date(time));

        return new Date(time);
    }

    public static final Long date2utc(Date date) {

        // use null for a null date
        if (date == null) return null;
       
        long time = date.getTime();
       
        // remove the timezone offset        
        time -= timezoneOffsetMillis(date);
       
        return time;
    }

    public static final Long getValueForToday() {
        return trimTimeToMidnight(date2utc(new Date()));
    }

    @SuppressWarnings("deprecation")
    public static final long timezoneOffsetMillis(Date date) {
        return date.getTimezoneOffset()*60*1000;        
    }

}


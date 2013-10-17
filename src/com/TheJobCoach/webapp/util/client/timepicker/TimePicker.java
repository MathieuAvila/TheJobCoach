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

public class TimePicker extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long>, HasText, HasEnabled {

    public UTCTimeBoxImpl impl;
   
    public TimePicker() {
        this(DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT));
    }
   
    public TimePicker(DateTimeFormat timeFormat) {
        // used deferred binding for the implementation
        impl = GWT.create(UTCTimeBoxImpl.class);
        impl.setTimeFormat(timeFormat);
        initWidget(impl.asWidget());
    }
   
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return impl.addValueChangeHandler(handler);
    }

    @Override
    public Long getValue() {
        return impl.getValue();
    }

    @Override
    public void setValue(Long value) {
        impl.setValue(value);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        impl.setValue(value, fireEvents);
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
    public boolean isEnabled() {
        return DomUtils.isEnabled(getElement());
    }

    @Override
    public void setEnabled(boolean enabled) {
        DomUtils.setEnabled(getElement(), enabled);
    }    
   
    public void setVisibleLength(int length) {
        impl.setVisibleLength(length);
    }

    public void setTabIndex(int tabIndex) {
        impl.setTabIndex(tabIndex);
    }
   
    public void validate() {
        impl.validate();
    }

    public static final Long getValueForNextHour() {
        Date date = new Date();
        long value = UTCDateBox.date2utc(date);

        // remove anything after an hour and add an hour
        long hour = 60 * 60 * 1000;
        value = value % UTCDateBox.DAY_IN_MS;
        return value - (value % hour) + hour;
    }

}

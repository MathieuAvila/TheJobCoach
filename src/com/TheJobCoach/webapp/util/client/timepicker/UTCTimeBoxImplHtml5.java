package com.TheJobCoach.webapp.util.client.timepicker;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;

public class UTCTimeBoxImplHtml5 extends UTCTimeBoxImplShared {
   
    private static final DateTimeFormat timeInputFormat = DateTimeFormat.getFormat("HH:mm");
   
    private InputWidget widget;
   
    public UTCTimeBoxImplHtml5() {
        widget = new InputWidget("time");
        setTimeFormat(timeInputFormat);
       
        widget.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                fireValueChangeEvent(getValue());
            }
           
        });
       
        initWidget(widget);
    }
   
    @Override
    public Long getValue() {
        return string2long(widget.getValue());
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        widget.setValue(long2string(value), fireEvents);
    }

     @Override
    public String getText() {
        return value2text(getValue());
    }

    @Override
    public void setText(String text) {
        setValue(text2value(text), true);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Long> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void fireValueChangeEvent(Long value) {
        ValueChangeEvent.fire(this, value);        
    }

    @Override
    public void setTabIndex(int tabIndex) {
        widget.setTabIndex(tabIndex);
    }

    private Long string2long(String value) {
        return parseUsingFormat(value, timeInputFormat);
    }
   
    private String long2string(Long value) {
        return formatUsingFormat(value, timeInputFormat);
    }    
   
}

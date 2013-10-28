package com.TheJobCoach.webapp.util.client.timepicker;

import java.util.Date;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class UTCTimeBoxImplHtml4 extends UTCTimeBoxImplShared {

    private static final String CLASSNAME_INVALID = "invalid";

    private TextBox textbox;
    private TimeBoxMenu menu;
    private Long lastKnownValue;

    private class TextBoxHandler implements KeyDownHandler, BlurHandler, ClickHandler {

        @Override
        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_UP:
                menu.adjustHighlight(-1);
                break;
            case KeyCodes.KEY_DOWN:
                menu.adjustHighlight(1);
                break;
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
                // accept current value
                if (menu.isShowing()) {
                    menu.acceptHighlighted();
                    hideMenu();
                    clearInvalidStyle();
                }
                else {
                   
                    // added a sync here because this is when we
                    // accept the value we've typed if we're typing in
                    // a new value.
                    syncValueToText();
                   
                    validate();
                }
                break;
            case KeyCodes.KEY_ESCAPE:
                validate();
                hideMenu();
                break;
            default:
                hideMenu();
                clearInvalidStyle();
                break;
            }
        }

        @Override
        public void onBlur(BlurEvent event) {
            validate();
        }

        @Override
        public void onClick(ClickEvent event) {
            showMenu();
        }
    }

    private class TimeBoxMenuOption extends SimplePanel {

        private long offsetFromMidnight;
        private String value;

        public TimeBoxMenuOption(long offsetFromMidnight) {
            this.offsetFromMidnight = offsetFromMidnight;

            // format the time in the local time zone
            long time = TimeUtils.timezoneOffsetMillis(new Date(0)) + offsetFromMidnight;
            value = timeFormat.format(new Date(time));

            Anchor anchor = new Anchor(value);
            anchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    acceptValue();
                    hideMenu();
                    clearInvalidStyle();
                }

            });
            setWidget(anchor);
        }

        public void acceptValue() {
            setText(value);
        }

        public void setSelected(boolean isSelected) {
            setStyleName("selected", isSelected);
        }

        public void setHighlighted(boolean isHighlighted) {
            setStyleName("highlighted", isHighlighted);
        }

        private long getTime() {
            return offsetFromMidnight;
        }

        public boolean isTimeEqualTo(long time) {
            long compare = getTime();
            return compare == time;
        }

        public boolean isTimeLessThan(long time) {
            return getTime() < time;
        }

    }

    private class TimeBoxMenu extends PopupPanel {

        private static final long INTERVAL = 30 * 60 * 1000L;
        private static final long DAY = 24 * 60 * 60 * 1000L;

        private TimeBoxMenuOption[] options;
        private int highlightedOptionIndex = -1;

        public TimeBoxMenu() {
            super(true);
            setStyleName("gwt-TimeBox-menu");
            addAutoHidePartner(textbox.getElement());

            FlowPanel container = new FlowPanel();

            int numOptions = (int) (DAY / INTERVAL);
            options = new TimeBoxMenuOption[numOptions];

            // we need to use times for formatting, but we don't keep
            // them around. the purpose is only to generate text to
            // insert into the textbox.
            for (int i = 0; i < numOptions; i++) {
                options[i] = new TimeBoxMenuOption(i * INTERVAL);
                container.add(options[i]);
            }

            add(container);
        }

        public void adjustHighlight(int distance) {

            // make the list of times visible if it isn't
            if (!isShowing()) {
                showTimePicker();
            }

            // highlight the new value
            int index = normalizeOptionIndex(highlightedOptionIndex + distance);
            setHighlightedIndex(index);
            scrollToIndex(index);
        }

        public void acceptHighlighted() {
            if (hasHighlightedOption()) {
                options[highlightedOptionIndex].acceptValue();
            }
        }

        private boolean hasHighlightedOption() {
            return (highlightedOptionIndex != -1);
        }

        private int normalizeOptionIndex(int index) {
            if (index < 0) {
                return 0;
            }
            else if (index >= options.length) {
                return options.length - 1;
            }
            else {
                return index;
            }
        }

        public void scrollToIndex(int index) {
            options[normalizeOptionIndex(index)].getElement().scrollIntoView();
            getContainerElement().setScrollLeft(0);
        }

        public void setHighlightedIndex(int index) {
            if (index != highlightedOptionIndex) {
                if (hasHighlightedOption()) {
                    options[highlightedOptionIndex].setHighlighted(false);
                }
                highlightedOptionIndex = index;
                options[index].setHighlighted(true);
                scrollToIndex(index);
            }
        }

        public void showTimePicker() {

            showRelativeTo(textbox);

            int lastOptionLessThanCurrentTime = 0;

            // reset while we try to find an option to highlight
            highlightedOptionIndex = -1;

            Long currentTime = getValue();
            for (int i = 0; i < options.length; i++) {
                TimeBoxMenuOption option = options[i];

                boolean isEqual = currentTime != null && option.isTimeEqualTo(currentTime);
                if (isEqual) {
                    highlightedOptionIndex = i;
                }
                option.setSelected(isEqual);
                option.setHighlighted(isEqual);

                if (currentTime != null && option.isTimeLessThan(currentTime)) {
                    lastOptionLessThanCurrentTime = i;
                }
            }

            int index;
            if (hasHighlightedOption()) {
                index = highlightedOptionIndex;
            }
            else {
                index = normalizeOptionIndex(lastOptionLessThanCurrentTime);
            }
            // include a little extra to center the current time
            setHighlightedIndex(index);
            scrollToIndex(index + 6);
        }

    }

    public UTCTimeBoxImplHtml4() {
        this.textbox = new TextBox();

        TextBoxHandler handler = new TextBoxHandler();
        textbox.addKeyDownHandler(handler);
        textbox.addBlurHandler(handler);
        textbox.addClickHandler(handler);

        textbox.setStyleName("gwt-TimeBox");
       
        initWidget(textbox);
    }
   
    @Override
    public void setTimeFormat(DateTimeFormat timeFormat) {
        super.setTimeFormat(timeFormat);
        this.menu = new TimeBoxMenu();
    }    

    public TextBox getTextBox() {
        return textbox;
    }

    public void showMenu() {
        // make the menu visible and select the appropriate value
        menu.showTimePicker();
    }

    public void hideMenu() {
        menu.hide();
    }

    public boolean hasValue() {
        return getText().trim().length() > 0;
    }

    public boolean hasValidValue() {
        return !hasValue() || getValue() != null;
    }

    @Override
    public Long getValue() {
        return text2value(getText());
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        setValue(value, true, fireEvents);
    }

    protected void setValue(Long value, boolean updateTextBox, boolean fireEvents) {

        if (updateTextBox) {
            syncTextToValue(value);
        }

        // keep track of the last known value so that we only fire
        // when it's different.
        Long oldValue = lastKnownValue;
        lastKnownValue = value;

        if (fireEvents && !isSameValue(oldValue, value)) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    protected boolean isSameValue(Long a, Long b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public String getText() {
        return textbox.getValue();
    }

    @Override
    public void setText(String text) {
        textbox.setValue(text);
        syncValueToText();
    }

    protected void syncTextToValue(Long value) {
        textbox.setValue(value2text(value));
    }

    protected void syncValueToText() {
        setValue(text2value(getText()), false, true);
    }

    @Override
    public void validate() {
        boolean valid = true;
        if (hasValue()) {
            Long value = getValue();
            if (value != null) {
                // scrub the value to format properly
                setText(value2text(value));
            }
            else {
                // empty is ok and value != null ok, this is invalid
                valid = false;
            }
        }
        setStyleName(CLASSNAME_INVALID, !valid);
    }

    @Override
    public void setVisibleLength(int length) {
        textbox.setVisibleLength(length);
    }

    @Override
    public void setTabIndex(int tabIndex) {
        textbox.setTabIndex(tabIndex);
    }

    public void clearInvalidStyle() {
        removeStyleName(CLASSNAME_INVALID);
    }

}

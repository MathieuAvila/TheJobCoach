package com.TheJobCoach.webapp.util.client.timepicker;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class DomUtils extends UIObject {
   
    private DomUtils() {}
   
    public static void setEnabled(Element element, boolean enabled) {
        element.setPropertyBoolean("disabled", !enabled);
        setStyleName(element, "disabled", !enabled);
    }

    public static boolean isEnabled(Element element) {
        return element.getPropertyBoolean("disabled");        
    }
   
}

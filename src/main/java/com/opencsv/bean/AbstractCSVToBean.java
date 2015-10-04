package com.opencsv.bean;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

public abstract class AbstractCSVToBean {

    abstract protected PropertyEditor getPropertyEditor(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException;

    protected String checkForTrim(String s, PropertyDescriptor prop) {
        return trimmableProperty(prop) ? s.trim() : s;
    }

    protected boolean trimmableProperty(PropertyDescriptor prop) {
        return !prop.getPropertyType().getName().contains("String");
    }

    /**
     * Convert a string value to its Object value.
     *
     * @param value - String value
     * @param prop  - PropertyDescriptor
     * @return The object set to value (i.e. Integer).  Will return String if no PropertyEditor is found.
     * @throws InstantiationException - Thrown on error getting the property editor from the property descriptor.
     * @throws IllegalAccessException - Thrown on error getting the property editor from the property descriptor.
     */
    protected Object convertValue(String value, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
        PropertyEditor editor = getPropertyEditor(prop);
        Object obj = value;
        if (null != editor) {
            editor.setAsText(value);
            obj = editor.getValue();
        }
        return obj;
    }

}

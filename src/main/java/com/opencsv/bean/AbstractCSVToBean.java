package com.opencsv.bean;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts CSV data to objects.
 */
public abstract class AbstractCSVToBean {

    private Map<Class<?>, PropertyEditor> editorMap = null;
    
    /**
     * Attempt to find custom property editor on descriptor first, else try the
     * propery editor manager.
     *
     * @param desc PropertyDescriptor.
     * @return The PropertyEditor for the given PropertyDescriptor.
     * @throws InstantiationException Thrown when getting the PropertyEditor for the class.
     * @throws IllegalAccessException Thrown when getting the PropertyEditor for the class.
     */
    abstract protected PropertyEditor getPropertyEditor(PropertyDescriptor desc)
            throws InstantiationException, IllegalAccessException;

    /**
     * Returns the trimmed value of the string only if the property the string
     * is describing should be trimmed to be converted to that type.
     *
     * @param s    String describing the value.
     * @param prop Property descriptor of the value.
     * @return The string passed in if the property is a string, otherwise it
     * will return the string with the beginning and end whitespace removed.
     */
    protected String checkForTrim(String s, PropertyDescriptor prop) {
        return trimmableProperty(prop) ? s.trim() : s;
    }

    private boolean trimmableProperty(PropertyDescriptor prop) {
        return !prop.getPropertyType().getName().contains("String");
    }

    /**
     * Convert a string value to its Object value.
     *
     * @param value String value
     * @param prop  PropertyDescriptor
     * @return The object set to value (i.e. Integer).  Will return String if
     * no PropertyEditor is found.
     * @throws InstantiationException Thrown on error getting the property
     * editor from the property descriptor.
     * @throws IllegalAccessException Thrown on error getting the property
     * editor from the property descriptor.
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

    /**
     * Returns the PropertyEditor for the given class.
     * Should be more efficient if used often, because it caches PropertyEditors.
     *
     * @param cls The class for which the property editor is desired
     * @return The PropertyEditor for the given class
     */
    protected PropertyEditor getPropertyEditorValue(Class<?> cls) {
        if (editorMap == null) {
            editorMap = new HashMap<Class<?>, PropertyEditor>();
        }

        PropertyEditor editor = editorMap.get(cls);

        if (editor == null) {
            editor = PropertyEditorManager.findEditor(cls);
            addEditorToMap(cls, editor);
        }

        return editor;
    }

    private void addEditorToMap(Class<?> cls, PropertyEditor editor) {
        if (editor != null) {
            editorMap.put(cls, editor);
        }
    }


}

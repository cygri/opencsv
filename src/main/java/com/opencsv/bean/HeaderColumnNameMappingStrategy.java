package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvBadConverterException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;

/*
 * Copyright 2007 Kyle Miller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Maps data to objects using the column names in the first row of the CSV file
 * as reference. This way the column order does not matter.
 *
 * @param <T> Type of the bean to be returned
 */
public class HeaderColumnNameMappingStrategy<T> implements MappingStrategy<T> {

    // header and indexLookup should be replaced with a BidiMap from Apache
    // Commons Collections once Apache Commons BeanUtils supports Collections
    // version 4.0 or newer. Until then I don't like BidiMaps, because they
    // aren't done with Generics, meaning everything is an Object and there is
    // no type safety.
    /**
     * An ordered array of the headers for the columns of a CSV input.
     * When reading, this array is automatically populated from the input source.
     * When writing, it is guessed from annotations, or, lacking any annotations,
     * from the names of the variables in the bean to be written.
     */
    protected String[] header;
    
    /** This map makes finding the column index of a header name easy. */
    protected Map<String, Integer> indexLookup = new HashMap<String, Integer>();
    
    /**
     * Given a header name, this map allows one to find the corresponding
     * property descriptor.
     */
    protected Map<String, PropertyDescriptor> descriptorMap = null;
    
    /**
     * Given a header name, this map allows one to find the corresponding
     * {@link BeanField}.
     */
    protected Map<String, BeanField> fieldMap = null;
    
    /** This is the class of the bean to be manipulated. */
    protected Class<? extends T> type;
    
    /**
     * Whether or not annotations were found and should be used for determining
     * the binding between columns in a CSV source or destination and fields in
     * a bean.
     */
    protected boolean annotationDriven;
    
    /** An error message that is used when a custom converter cannot be instantiated. */
    private static final String CANNOT_INSTANTIATE = "There was a problem instantiating the custom converter ";

    /**
     * Default constructor.
     */
    public HeaderColumnNameMappingStrategy() {
    }

    @Override
    public void captureHeader(CSVReader reader) throws IOException {
        header = reader.readNext();
    }
    
    /**
     * This method generates a header that can be used for writing beans of the
     * type provided back to a file.
     * The ordering of the headers is alphabetically ascending.
     * @return An array of header names for the output file, or an empty array
     *   if no header should be written
     */
    @Override
    public String[] generateHeader() {
        // Always take what's been given or previously determined first.
        if(header == null) {

            // We will have to generate a new header, which means this instance
            // has not been used for importing data.
            if(fieldMap == null) {
                loadFields(type);
            }
            
            // To make testing simpler and because not all receivers are
            // guaranteed to be as flexible with column order as opencsv,
            // make the column ordering deterministic by sorting the column
            // headers alphabetically.
            SortedSet<String> set = new TreeSet(fieldMap.keySet());
            header = set.toArray(new String[fieldMap.size()]);
        }
        
        // Clone so no one has direct access to internal data structures
        return ArrayUtils.clone(header);
    }

    /**
     * Creates an index map of column names to column position.
     *
     * @param values Array of header values.
     */
    protected void createIndexLookup(String[] values) {
        if (indexLookup.isEmpty()) {
            for (int i = 0; i < values.length; i++) {
                indexLookup.put(values[i], i);
            }
        }
    }

    /**
     * Resets index map of column names to column position.
     */
    protected void resetIndexMap() {
        indexLookup.clear();
    }

    @Override
    public Integer getColumnIndex(String name) {
        if (null == header) {
            throw new IllegalStateException("The header row hasn't been read yet.");
        }

        createIndexLookup(header);

        return indexLookup.get(name);
    }

    @Override
    public PropertyDescriptor findDescriptor(int col)
            throws IntrospectionException {
        String columnName = getColumnName(col);
        return (StringUtils.isNotBlank(columnName)) ? findDescriptor(columnName) : null;
    }

    @Override
    public BeanField findField(int col) throws CsvBadConverterException {
        String columnName = getColumnName(col);
        return (StringUtils.isNotBlank(columnName)) ?
                fieldMap.get(columnName.toUpperCase().trim()) :
                null;
    }
    
    @Override
    public int findMaxFieldIndex() {
        return header == null ? -1 : header.length-1;
    }

    /**
     * Get the column name for a given column position.
     *
     * @param col Column position.
     * @return The column name or null if the position is larger than the
     * header array or there are no headers defined.
     */
    public String getColumnName(int col) {
        return (null != header && col < header.length) ? header[col] : null;
    }

    /**
     * Find the property descriptor for a given column.
     *
     * @param name Column name to look up.
     * @return The property descriptor for the column.
     * @throws IntrospectionException Thrown on error loading the property
     *                                descriptors.
     */
    protected PropertyDescriptor findDescriptor(String name) throws IntrospectionException {
        if (null == descriptorMap) {
            descriptorMap = loadDescriptorMap(); //lazy load descriptors
        }
        return descriptorMap.get(name.toUpperCase().trim());
    }

    /**
     * Builds a map of property descriptors for the bean.
     *
     * @return Map of property descriptors
     * @throws IntrospectionException Thrown on error getting information
     *                                about the bean.
     */
    protected Map<String, PropertyDescriptor> loadDescriptorMap() throws IntrospectionException {
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();

        PropertyDescriptor[] descriptors = loadDescriptors(getType());
        for (PropertyDescriptor descriptor : descriptors) {
            map.put(descriptor.getName().toUpperCase(), descriptor);
        }

        return map;
    }

    /**
     * Attempts to instantiate the class of the custom converter specified.
     *
     * @param converter The class for a custom converter
     * @return The custom converter
     * @throws CsvBadConverterException If the class cannot be instantiated
     */
    protected BeanField instantiateCustomConverter(Class<? extends AbstractBeanField> converter)
            throws CsvBadConverterException {
        try {
            return converter.newInstance();
        } catch (IllegalAccessException oldEx) {
            // Combine this block with the next one as soon as Java 7
            // is the minimum supported version.
            CsvBadConverterException newEx =
                    new CsvBadConverterException(converter,
                            CANNOT_INSTANTIATE + converter.getCanonicalName());
            newEx.initCause(oldEx);
            throw newEx;
        } catch (InstantiationException oldEx) {
            CsvBadConverterException newEx =
                    new CsvBadConverterException(converter,
                            CANNOT_INSTANTIATE + converter.getCanonicalName());
            newEx.initCause(oldEx);
            throw newEx;
        }
    }

    /**
     * Builds a map of fields for the bean.
     *
     * @throws CsvBadConverterException If there is a problem instantiating the
     *                                  custom converter for an annotated field
     */
    protected void loadFieldMap() throws CsvBadConverterException {
        fieldMap = new HashMap<String, BeanField>();

        for (Field field : loadFields(getType())) {
            String columnName;
            String locale;

            // Always check for a custom converter first.
            if (field.isAnnotationPresent(CsvCustomBindByName.class)) {
                columnName = field.getAnnotation(CsvCustomBindByName.class).column().toUpperCase().trim();
                if(StringUtils.isEmpty(columnName)) {
                    columnName = field.getName().toUpperCase();
                }
                Class<? extends AbstractBeanField> converter = field
                        .getAnnotation(CsvCustomBindByName.class)
                        .converter();
                BeanField bean = instantiateCustomConverter(converter);
                bean.setField(field);
                if(StringUtils.isEmpty(columnName)) {
                    fieldMap.put(field.getName().toUpperCase(), bean);
                }
                else {
                    fieldMap.put(columnName, bean);
                }
            }

            // Then check for CsvBindByName.
            else if (field.isAnnotationPresent(CsvBindByName.class)) {
                boolean required = field.getAnnotation(CsvBindByName.class).required();
                columnName = field.getAnnotation(CsvBindByName.class).column().toUpperCase().trim();
                locale = field.getAnnotation(CsvBindByName.class).locale();
                if (field.isAnnotationPresent(CsvDate.class)) {
                    String formatString = field.getAnnotation(CsvDate.class).value();
                    if (StringUtils.isEmpty(columnName)) {
                        fieldMap.put(field.getName().toUpperCase(),
                                new BeanFieldDate(field, required, formatString, locale));
                    } else {
                        fieldMap.put(columnName, new BeanFieldDate(field, required, formatString, locale));
                    }
                } else {
                    if (StringUtils.isEmpty(columnName)) {
                        fieldMap.put(field.getName().toUpperCase(),
                                new BeanFieldPrimitiveTypes(field, required, locale));
                    } else {
                        fieldMap.put(columnName, new BeanFieldPrimitiveTypes(field, required, locale));
                    }
                }
            }

            // And only check for CsvBind if nothing else is there, because
            // CsvBind is deprecated.
            else {
                boolean required = field.getAnnotation(CsvBind.class).required();
                fieldMap.put(field.getName().toUpperCase(),
                        new BeanFieldPrimitiveTypes(field, required, null));
            }
        }
    }

    private PropertyDescriptor[] loadDescriptors(Class<? extends T> cls) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(cls);
        return beanInfo.getPropertyDescriptors();
    }

    private List<Field> loadFields(Class<? extends T> cls) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : FieldUtils.getAllFields(cls)) {
            if (field.isAnnotationPresent(CsvBind.class)
                    || field.isAnnotationPresent(CsvBindByName.class)
                    || field.isAnnotationPresent(CsvCustomBindByName.class)) {
                fields.add(field);
            }
        }
        annotationDriven = !fields.isEmpty();
        return fields;
    }

    @Override
    public T createBean() throws InstantiationException, IllegalAccessException, IllegalStateException {
        if(type == null) {
            throw new IllegalStateException("The type has not been set in the MappingStrategy.");
        }
        return type.newInstance();
    }

    /**
     * Get the class type that the Strategy is mapping.
     *
     * @return Class of the object that mapper will create.
     */
    public Class<? extends T> getType() {
        return type;
    }

    /**
     * Sets the class type that is being mapped.
     * Also initializes the mapping between column names and bean fields.
     *
     * @param type Class type.
     * @throws CsvBadConverterException If a field in the bean is annotated
     *                                  with a custom converter that cannot be initialized. If you are not
     *                                  using custom converters that you have written yourself, it should be
     *                                  safe to catch this exception and ignore it.
     */
    public void setType(Class<? extends T> type) throws CsvBadConverterException {
        this.type = type;
        loadFieldMap();
    }

    /**
     * Determines whether the mapping strategy is driven by annotations.
     * For this mapping strategy, the supported annotations are:
     * <ul><li>{@link com.opencsv.bean.CsvBindByName}</li>
     * <li>{@link com.opencsv.bean.CsvCustomBindByName}</li>
     * <li>{@link com.opencsv.bean.CsvBind}</li>
     * </ul>
     *
     * @return Whether the mapping strategy is driven by annotations
     */
    @Override
    public boolean isAnnotationDriven() {
        return annotationDriven;
    }
}

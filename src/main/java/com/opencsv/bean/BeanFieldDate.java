/*
 * Copyright 2016 Andrew Rucker Jones.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opencsv.bean;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlDateLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimeLocaleConverter;
import org.apache.commons.beanutils.locale.converters.SqlTimestampLocaleConverter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class converts an input to a date type.
 *
 * @author Andrew Rucker Jones
 * @see com.opencsv.bean.CsvDate
 */
public class BeanFieldDate extends AbstractBeanField {

    private final boolean required;
    private final String formatString;
    private final String locale;

    /**
     * @param field        A {@link java.lang.reflect.Field} object.
     * @param required     True if the field is required to contain a value, false
     *                     if it is allowed to be null or a blank string.
     * @param formatString The string to use for formatting the date. See
     *                     {@link com.opencsv.bean.CsvDate#value()}
     * @param locale       If not null or empty, specifies the locale used for
     *                     converting locale-specific data types
     */
    public BeanFieldDate(Field field, boolean required, String formatString, String locale) {
        super(field);
        this.required = required;
        this.formatString = formatString;
        this.locale = locale;
    }

    /**
     * Converts the input string to a locale-specific date/time object.
     * This is used only on destination objects that support locale-specific
     * conversions and only when the locale has been specified.
     *
     * @param value     The string to be converted into a date/time type
     * @param fieldType The class of the destination field
     * @return The date/time object resulting from the conversion
     * @throws CsvDataTypeMismatchException If a non-convertable type is
     *                                      passed in, or if the conversion fails
     */
    private Object convertLocaleSpecific(String value, Class fieldType)
            throws CsvDataTypeMismatchException {
        DateLocaleConverter c;
        Object o;
        Locale l = new Locale(locale);

        // Get the proper converter
        if (fieldType == Date.class) {
            c = new DateLocaleConverter(l, formatString);
        } else if (fieldType == java.sql.Date.class) {
            c = new SqlDateLocaleConverter(l, formatString);
        } else if (fieldType == Time.class) {
            c = new SqlTimeLocaleConverter(l, formatString);
        } else if (fieldType == Timestamp.class) {
            c = new SqlTimestampLocaleConverter(l, formatString);
        } else {
            throw new CsvDataTypeMismatchException(value, field.getType(),
                    "@CsvDate annotation used on non-date field.");
        }

        // Convert with respect to format string and locale
        try {
            o = c.convert(fieldType, (Object) value);
        } catch (ConversionException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, fieldType);
            csve.initCause(e);
            throw csve;
        }

        return o;
    }

    /**
     * Converts the input string to a date/time object.
     * This is used on destination objects that don't support locale-specific
     * conversions or when the locale hasn't been specified.
     *
     * @param value     The string to be converted into a date/time type
     * @param fieldType The class of the destination field
     * @return The date/time object resulting from the conversion
     * @throws CsvDataTypeMismatchException If a non-convertable type is
     *                                      passed in, or if the conversion fails
     */
    private Object convertLocaleInspecific(String value, Class fieldType)
            throws CsvDataTypeMismatchException {
        DateTimeConverter c;
        Object o;
        Class conversionType = fieldType;
        
        // Get the proper converter
        if (fieldType == Date.class) {
            c = new DateConverter();
        } else if (fieldType == Calendar.class
                || fieldType == GregorianCalendar.class
                || fieldType == XMLGregorianCalendar.class) {
            conversionType = Calendar.class;
            c = new CalendarConverter();
        } else if (fieldType == java.sql.Date.class) {
            c = new SqlDateConverter();
        } else if (fieldType == Time.class) {
            c = new SqlTimeConverter();
        } else if (fieldType == Timestamp.class) {
            c = new SqlTimestampConverter();
        } else {
            throw new CsvDataTypeMismatchException(value, field.getType(),
                    "@CsvDate annotation used on non-date field.");
        }

        // Convert with respect to format string
        c.setPattern(formatString);
        try {
            o = c.convert(conversionType, value);
        } catch (ConversionException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(value, fieldType);
            csve.initCause(e);
            throw csve;
        }

        // Postprocess for special types
        if (fieldType == GregorianCalendar.class
                || fieldType == XMLGregorianCalendar.class) {
            if (!(o instanceof GregorianCalendar)) {
                Calendar cal = (Calendar) o;
                o = new GregorianCalendar(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND)
                );
            }
            if (fieldType == XMLGregorianCalendar.class) {
                try {
                    o = DatatypeFactory
                            .newInstance()
                            .newXMLGregorianCalendar((GregorianCalendar) o);
                } catch (DatatypeConfigurationException e) {
                    // I've never known how to handle this exception elegantly,
                    // especially since I can't conceive of the circumstances
                    // under which it is thrown.
                    CsvDataTypeMismatchException ex = new CsvDataTypeMismatchException(
                            "It was not possible to initialize an XMLGregorianCalendar.");
                    ex.initCause(e);
                    throw ex;
                }
            }
        }

        return o;
    }

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (required && StringUtils.isEmpty(value)) {
            throw new CsvRequiredFieldEmptyException();
        }

        Class fieldType = field.getType();
        Object o;

        // Send to the proper submethod
        Collection<Class> localeFields = Arrays.<Class>asList(
                Date.class, java.sql.Date.class, Time.class, Timestamp.class);
        Collection<Class> nonlocaleFields = Arrays.<Class>asList(
                Calendar.class, GregorianCalendar.class, XMLGregorianCalendar.class);
        if (localeFields.contains(fieldType) && StringUtils.isNotEmpty(locale)) {
            o = convertLocaleSpecific(value, fieldType);
        } else if (nonlocaleFields.contains(fieldType) || localeFields.contains(fieldType)) {
            o = convertLocaleInspecific(value, fieldType);
        } else {
            throw new CsvDataTypeMismatchException(value, field.getType(),
                    "@CsvDate annotation used on non-date field.");
        }

        return o;
    }
}

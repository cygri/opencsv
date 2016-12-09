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

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class writes beans out in CSV format to a {@link java.io.Writer},
 * keeping state information and making an intelligent guess at the mapping
 * strategy to be applied.
 * 
 * @param <T> Type of the bean to be written
 * @author Andrew Rucker Jones
 * @see MappingUtils#determineMappingStrategy(java.lang.Class) 
 * @since 3.9
 */
public class StatefulBeanToCsv<T> {
    /** The beans being written are counted in the order they are written. */
    private int lineNumber = 0;
    
    private Character separator = null;
    private Character quotechar = null;
    private Character escapechar = null;
    private String lineEnd = null;
    private boolean headerWritten = false;
    private MappingStrategy<T> mappingStrategy = null;
    private final Writer writer;
    private CSVWriter csvwriter;
    private boolean throwExceptions = true;
    private List<CsvException> capturedExceptions = new ArrayList<CsvException>();
    private static final String INTROSPECTION_ERROR = "There was an error while manipulating the bean to be written.";
    
    /** The nullary constructor should never be used. */
    private StatefulBeanToCsv() {
        throw new IllegalStateException("This class may never be instantiated with the nullary constructor.");
    }
    
    /**
     * The only constructor that should be used.
     * @param writer A {@link java.io.Writer} for writing the beans as a CSV to
     */
    public StatefulBeanToCsv(Writer writer) {
        this.writer = writer;
    }
    
    /**
     * Sets the mapping strategy for writing beans to a CSV destination.
     * <p>If the mapping strategy is set this way, it will always be used instead
     * of automatic determination of an appropriate mapping strategy.</p>
     * <p>It is perfectly legitimate to read a CSV source, take the mapping
     * strategy from the read operation, and pass it in to this method for a
     * write operation. This conserves some processing time, but, more
     * importantly, preserves header ordering.</p>
     * 
     * @param mappingStrategy The mapping strategy to be used for write operations
     */
    public void setMappingStrategy(MappingStrategy<T> mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
    }
    
    /**
     * Custodial tasks that must be performed before beans are written to a CSV
     * destination for the first time.
     * @param bean Any bean to be written. Used to determine the mapping
     *   strategy automatically.
     */
    private void beforeFirstWrite(T bean) {
        
        // Determine mapping strategy
        if(mappingStrategy == null) {
            mappingStrategy = MappingUtils.<T>determineMappingStrategy(bean.getClass());
        }
        
        // Build CSVWriter
        if(separator == null) {separator = CSVWriter.DEFAULT_SEPARATOR;}
        if(quotechar == null) {quotechar = CSVWriter.DEFAULT_QUOTE_CHARACTER;}
        if(escapechar == null) {escapechar = CSVWriter.DEFAULT_ESCAPE_CHARACTER;}
        if(lineEnd == null) {lineEnd = CSVWriter.DEFAULT_LINE_END;}
        csvwriter = new CSVWriter(writer, separator, quotechar, escapechar, lineEnd);
        
        // Write the header
        if(!headerWritten) {
            String[] header = mappingStrategy.generateHeader();
            if(header.length > 0) {
                csvwriter.writeNext(header);
            }
            headerWritten = true;
        }
    }
    
    /**
     * Writes a bean out to the {@link java.io.Writer} provided to the
     * constructor.
     * 
     * @param bean A bean to be written to a CSV destination
     * @throws CsvDataTypeMismatchException If a field of the bean is
     *   annotated improperly or an unsupported data type is supposed to be
     *   written
     * @throws CsvRequiredFieldEmptyException If a field is marked as required,
     *   but the source is null
     */
    public void write(T bean) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        if(bean != null) {
            ++lineNumber;
            beforeFirstWrite(bean);
            List<String> contents = new ArrayList<String>();
            int numColumns = mappingStrategy.findMaxFieldIndex();
            if(mappingStrategy.isAnnotationDriven()) {
                BeanField beanField;
                for(int i = 0; i <= numColumns; i++) {
                    beanField = mappingStrategy.findField(i);
                    try {
                        String s = beanField != null ? beanField.write(bean) : "";
                        contents.add(StringUtils.defaultString(s));
                    }
                    // Combine to a multi-catch once we support Java 7
                    catch(CsvDataTypeMismatchException e) {
                        e.setLineNumber(lineNumber);
                        if(throwExceptions) {throw e;}
                        else {capturedExceptions.add(e);}
                    }
                    catch(CsvRequiredFieldEmptyException e) {
                        e.setLineNumber(lineNumber);
                        if(throwExceptions) {throw e;}
                        else {capturedExceptions.add(e);}
                    }
                }
            }
            else {
                PropertyDescriptor desc;
                for(int i = 0; i <= numColumns; i++) {
                    try {
                        desc = mappingStrategy.findDescriptor(i);
                        Object o = desc != null ? desc.getReadMethod().invoke(bean, (Object[]) null) : null;
                        contents.add(ObjectUtils.toString(o, ""));
                        // Once we support Java 7
//                        contents.add(Objects.toString(o, ""));
                    }
                    // Combine in a multi-catch with Java 7
                    catch(IntrospectionException e) {
                        CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(
                                bean, null, INTROSPECTION_ERROR);
                        csve.initCause(e);
                        throw csve;
                    }
                    catch(IllegalAccessException e) {
                        CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(
                                bean, null, INTROSPECTION_ERROR);
                        csve.initCause(e);
                        throw csve;
                    }
                    catch(InvocationTargetException e) {
                        CsvBeanIntrospectionException csve = new CsvBeanIntrospectionException(
                                bean, null, INTROSPECTION_ERROR);
                        csve.initCause(e);
                        throw csve;
                    }
                }
            }
            csvwriter.writeNext(contents.toArray(new String[contents.size()]));
        }
    }
    
    /**
     * Writes a list of beans out to the {@link java.io.Writer} provided to the
     * constructor.
     * 
     * @param beans A list of beans to be written to a CSV destination
     * @throws CsvDataTypeMismatchException If a field of the beans is
     *   annotated improperly or an unsupported data type is supposed to be
     *   written
     * @throws CsvRequiredFieldEmptyException If a field is marked as required,
     *   but the source is null
     */
    public void write(List<T> beans) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {
        if(CollectionUtils.isNotEmpty(beans)) {
            for(T bean : beans) {write(bean);}
        }
    }

    /**
     * @return Whether or not exceptions are thrown. If they are not thrown,
     *   they are captured and returned later via {@link #getCapturedExceptions()}.
     */
    public boolean isThrowExceptions() {
        return throwExceptions;
    }

    /**
     * @param throwExceptions Sets whether exceptions should be thrown during
     *   writing of beans to a CSV destination. If they are not thrown, they are
     *   captured and returned later via {@link #getCapturedExceptions()}.
     */
    public void setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
    }
    
    /**
     * Any exceptions captured during writing of beans to a CSV destination can
     * be retrieved through this method.
     * <p><em>Reads from the list are destructive!</em> Calling this method will
     * clear the list of captured exceptions. However, calling
     * {@link #write(java.util.List)} or {@link #write(java.lang.Object)}
     * multiple times with no intervening call to this method will not clear the
     * list of captured exceptions, but rather add to it if further exceptions
     * are thrown.</p>
     * @return A list of exceptions that would have been thrown during any and
     *   all read operations since the last call to this method
     */
    public List<CsvException> getCapturedExceptions() {
        List<CsvException> intermediate = capturedExceptions;
        capturedExceptions = new ArrayList<CsvException>();
        return intermediate;
    }
    
    /**
     * @see com.opencsv.CSVWriter#separator
     * @param separator Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public StatefulBeanToCsv withSeparator(char separator) {
        this.separator = separator;
        return this;
    }
    
    /**
     * @see com.opencsv.CSVWriter#quotechar
     * @param quotechar Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public StatefulBeanToCsv withQuotechar(char quotechar) {
        this.quotechar = quotechar;
        return this;
    }
    
    /**
     * @see com.opencsv.CSVWriter#escapechar
     * @param escapechar Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public StatefulBeanToCsv withEscapechar(char escapechar) {
        this.escapechar = escapechar;
        return this;
    }
    
    /**
     * @see com.opencsv.CSVWriter#lineEnd
     * @param lineEnd Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public StatefulBeanToCsv withLineEnd(String lineEnd) {
        this.lineEnd = lineEnd;
        return this;
    }
}

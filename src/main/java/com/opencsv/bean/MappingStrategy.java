package com.opencsv.bean;


/*
 Copyright 2007 Kyle Miller.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvBadConverterException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;

/**
 * The interface for the classes that handle translating between the columns in
 * the CSV file to an actual object.
 *
 * @param <T> Type of object you are converting the data to.
 */
public interface MappingStrategy<T> {

    /**
     * Gets the property descriptor for a given column position.
     *
     * @param col The column to find the description for
     * @return The property descriptor for the column position or null if one
     * could not be found.
     * @throws IntrospectionException Thrown on error retrieving the property
     *                                description.
     */
   PropertyDescriptor findDescriptor(int col) throws IntrospectionException;

    /**
     * Gets the field for a given column position.
     *
     * @param col The column to find the field for
     * @return BeanField containing the field for a given column position, or
     * null if one could not be found
     * @throws CsvBadConverterException If a custom converter for a field cannot
     *                                  be initialized
     */
    BeanField findField(int col) throws CsvBadConverterException;
    
    /**
     * Finds and returns the highest index in this mapping.
     * This is especially important for writing, since position-based mapping
     * can ignore some columns that must be included in the output anyway.
     * {@link #findField(int) } will return null for these columns, so we need
     * a way to know when to stop writing new columns.
     * @return The highest index in the mapping. If there are no columns in the
     *   mapping, returns -1.
     * @since 3.9
     */
    int findMaxFieldIndex();

    /**
     * Implementation will return a bean of the type of object you are mapping.
     *
     * @return A new instance of the class being mapped.
     * @throws InstantiationException Thrown on error creating object.
     * @throws IllegalAccessException Thrown on error creating object.
     */
    T createBean() throws InstantiationException, IllegalAccessException;

    /**
     * Implementation of this method can grab the header line before parsing
     * begins to use to map columns to bean properties.
     *
     * @param reader The CSVReader to use for header parsing
     * @throws java.io.IOException If parsing fails
     */
   void captureHeader(CSVReader reader) throws IOException;
   
   /**
    * Implementations of this method must return an array of column headers
    * based on the contents of the mapping strategy.
    * If no header can or should be generated, an array of zero length should
    * be returned, and not null.
    * @return An array of column names for a header
    * @since 3.9
    */
   String[] generateHeader();

    /**
     * Gets the column index that corresponds to a specific column name.
     * If the CSV file doesn't have a header row, this method will always return
     * null.
     *
     * @param name The column name
     * @return The column index, or null if the name doesn't exist
     */
    Integer getColumnIndex(String name);

    /**
     * Determines whether the mapping strategy is driven by annotations.
     *
     * @return Whether the mapping strategy is driven by annotations
     */
   boolean isAnnotationDriven();
}
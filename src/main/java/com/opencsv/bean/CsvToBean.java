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
import com.opencsv.exceptions.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converts CSV data to objects.
 *
 * @param <T> Class to convert the objects to.
 */
public class CsvToBean<T> extends AbstractCSVToBean {
   
   /** A list of all exceptions during parsing and mapping of the input. */
   private List<CsvException> capturedExceptions = null;
   
   /** The mapping strategy to be used by this CsvToBean. */
   private MappingStrategy<T> mappingStrategy;
   
   /** The reader this class will use to access the data to be read. */
   private CSVReader csvReader;
   
   /** The filter this class will use on the beans it reads. */
   private CsvToBeanFilter filter = null;
   
   /**
    * Determines whether or not exceptions should be thrown during parsing or
    * collected for later examination through {@link #getCapturedExceptions()}.
    */
   private boolean throwExceptions = true;

   /**
    * Default constructor.
    */
   public CsvToBean() {
   }

   /**
    * Parse the values from a CSVReader constructed from the Reader passed in.
    * @param mapper Mapping strategy for the bean.
    * @param reader Reader used to construct a CSVReader
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, Reader reader) {
        setMappingStrategy(mapper);
        setCsvReader(new CSVReader(reader));
      return parse();
   }

   /**
    * Parse the values from a CSVReader constructed from the Reader passed in.
    * @param mapper Mapping strategy for the bean.
    * @param reader Reader used to construct a CSVReader
    * @param throwExceptions If false, exceptions internal to opencsv will not
    *   be thrown, but can be accessed after processing is finished through
    *   {@link #getCapturedExceptions()}.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, Reader reader, boolean throwExceptions) {
        setMappingStrategy(mapper);
        setCsvReader(new CSVReader(reader));
        this.setThrowExceptions(throwExceptions);
      return parse();
   }

   /**
    * Parse the values from a CSVReader constructed from the Reader passed in.
    *
    * @param mapper Mapping strategy for the bean.
    * @param reader Reader used to construct a CSVReader
    * @param filter CsvToBeanFilter to apply - null if no filter.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, Reader reader, CsvToBeanFilter filter) {
        setMappingStrategy(mapper);
        setCsvReader(new CSVReader(reader));
        this.setFilter(filter);
      return parse();
   }

   /**
    * Parse the values from a CSVReader constructed from the Reader passed in.
    * @param mapper Mapping strategy for the bean.
    * @param reader Reader used to construct a CSVReader
    * @param filter CsvToBeanFilter to apply - null if no filter.
    * @param throwExceptions If false, exceptions internal to opencsv will not
    *   be thrown, but can be accessed after processing is finished through
    *   {@link #getCapturedExceptions()}.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, Reader reader,
                        CsvToBeanFilter filter, boolean throwExceptions) {
        setMappingStrategy(mapper);
        setCsvReader(new CSVReader(reader));
        this.setFilter(filter);
        this.setThrowExceptions(throwExceptions);
      return parse();
   }

   /**
    * Parse the values from the CSVReader.
    * @param mapper Mapping strategy for the bean.
    * @param csv CSVReader
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, CSVReader csv) {
      setMappingStrategy(mapper);
      setCsvReader(csv);
      return parse();
   }

   /**
    * Parse the values from the CSVReader.
    * @param mapper Mapping strategy for the bean.
    * @param csv CSVReader
    * @param throwExceptions If false, exceptions internal to opencsv will not
    *   be thrown, but can be accessed after processing is finished through
    *   {@link #getCapturedExceptions()}.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, CSVReader csv, boolean throwExceptions) {
        setMappingStrategy(mapper);
        setCsvReader(csv);
        this.setThrowExceptions(throwExceptions);
      return parse();
   }

   /**
    * Parse the values from the CSVReader.
    * Throws exceptions for bad data and other sorts of problems relating
    * directly to opencsv, as well as general exceptions from external code
    * used.
    *
    * @param mapper Mapping strategy for the bean.
    * @param csv    CSVReader
    * @param filter CsvToBeanFilter to apply - null if no filter.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, CSVReader csv,
                        CsvToBeanFilter filter) {
        setMappingStrategy(mapper);
        setCsvReader(csv);
        this.setFilter(filter);
      return parse();
   }

   /**
    * Parse the values from the CSVReader.
    * Only throws general exceptions from external code used. Problems related
    * to opencsv and the data provided to it are captured for later processing
    * by user code and can be accessed through {@link #getCapturedExceptions()}.
    *
    * @param mapper          Mapping strategy for the bean.
    * @param csv             CSVReader
    * @param filter          CsvToBeanFilter to apply - null if no filter.
    * @param throwExceptions If false, exceptions internal to opencsv will not
    *                        be thrown, but can be accessed after processing is finished through
    *                        {@link #getCapturedExceptions()}.
    * @return List of Objects.
    */
   public List<T> parse(MappingStrategy<T> mapper, CSVReader csv,
                        CsvToBeanFilter filter, boolean throwExceptions) {
        setMappingStrategy(mapper);
        setCsvReader(csv);
        this.setFilter(filter);
        this.setThrowExceptions(throwExceptions);
       return parse();
   }
   
   /**
    * Parses the input based on parameters already set through other methods.
    * @return A list of populated beans based on the input
    * @throws IllegalStateException If either MappingStrategy or CSVReader is
    *   not specified
    */
   public List<T> parse() throws IllegalStateException {
      // First verify that the user hasn't failed to give us the information
      // we need to do his or her work for him or her.
      if(mappingStrategy == null || csvReader == null) {
          throw new IllegalStateException("Both mapping strategy and CSVReader/Reader must be specified!");
      }
      
      long lineProcessed = 0;
      String[] line = null;

      try {
         mappingStrategy.captureHeader(csvReader);
      } catch (Exception e) {
         throw new RuntimeException("Error capturing CSV header!", e);
      }

      try {
         List<T> list = new ArrayList<T>();
         while (null != (line = csvReader.readNext())) {
            lineProcessed++;
            try {
               processLine(mappingStrategy, filter, line, list);
            } catch (CsvException e) {
               CsvException csve = (CsvException) e;
               csve.setLineNumber(lineProcessed);
               if (throwExceptions) {
                  throw csve;
               } else {
                    getCapturedExceptions().add(csve);
                }
            }
         }
         return list;
      } catch (Exception e) {
         throw new RuntimeException("Error parsing CSV line: " + lineProcessed + " values: " + Arrays.toString(line), e);
      }
   }

   private void processLine(MappingStrategy<T> mapper, CsvToBeanFilter filter, String[] line, List<T> list)
           throws IllegalAccessException, InvocationTargetException,
           InstantiationException, IntrospectionException,
           CsvBadConverterException, CsvDataTypeMismatchException,
           CsvRequiredFieldEmptyException, CsvConstraintViolationException {
      if (filter == null || filter.allowLine(line)) {
         T obj = processLine(mapper, line);
         list.add(obj);
      }
   }

   /**
    * Creates a single object from a line from the CSV file.
    * @param mapper MappingStrategy
    * @param line  Array of Strings from the CSV file.
    * @return Object containing the values.
    * @throws IllegalAccessException Thrown on error creating bean.
    * @throws InvocationTargetException Thrown on error calling the setters.
    * @throws InstantiationException Thrown on error creating bean.
    * @throws IntrospectionException Thrown on error getting the PropertyDescriptor.
    * @throws CsvBadConverterException If a custom converter cannot be
    *   initialized properly
    * @throws CsvDataTypeMismatchException If the source data cannot be converted
    *   to the type of the destination field
    * @throws CsvRequiredFieldEmptyException If a mandatory field is empty in
    *   the input file
    * @throws CsvConstraintViolationException When the internal structure of
    *   data would be violated by the data in the CSV file
    */
   protected T processLine(MappingStrategy<T> mapper, String[] line)
           throws IllegalAccessException, InvocationTargetException,
           InstantiationException, IntrospectionException,
           CsvBadConverterException, CsvDataTypeMismatchException,
           CsvRequiredFieldEmptyException, CsvConstraintViolationException {
      T bean = mapper.createBean();
      for (int col = 0; col < line.length; col++) {
         if (mapper.isAnnotationDriven()) {
            processField(mapper, line, bean, col);
         } else {
            processProperty(mapper, line, bean, col);
         }
      }
      return bean;
   }

   private void processProperty(MappingStrategy<T> mapper, String[] line, T bean, int col)
           throws IntrospectionException, InstantiationException,
           IllegalAccessException, InvocationTargetException, CsvBadConverterException {
      PropertyDescriptor prop = mapper.findDescriptor(col);
      if (null != prop) {
         String value = checkForTrim(line[col], prop);
         Object obj = convertValue(value, prop);
         prop.getWriteMethod().invoke(bean, obj);
      }
   }

   private void processField(MappingStrategy<T> mapper, String[] line, T bean, int col)
           throws CsvBadConverterException, CsvDataTypeMismatchException,
           CsvRequiredFieldEmptyException, CsvConstraintViolationException {
      BeanField beanField = mapper.findField(col);
      if (beanField != null) {
         String value = line[col];
         beanField.setFieldValue(bean, value);
      }
   }

   @Override
   protected PropertyEditor getPropertyEditor(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException {
      Class<?> cls = desc.getPropertyEditorClass();
      if (null != cls) {
          return (PropertyEditor) cls.newInstance();
      }
      return getPropertyEditorValue(desc.getPropertyType());
   }

   /**
    * Returns the list of all exceptions that would have been thrown during the
    * import, but were suppressed by setting {@link #throwExceptions} to {@code false}.
    * @return The list of exceptions captured while processing the input file
    */
   public List<CsvException> getCapturedExceptions() {
      if (capturedExceptions == null) {
         capturedExceptions = new ArrayList<CsvException>();
        }
        return capturedExceptions;
    }

    /**
     * Sets the mapping strategy to be used by this bean.
     * @param mappingStrategy Mapping strategy to convert CSV input to a bean
     */
    public void setMappingStrategy(MappingStrategy<T> mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
    }

    /**
     * Sets the reader to be used to read in the information from the CSV input.
     * @param csvReader Reader for input
     */
    public void setCsvReader(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    /**
     * Sets a filter to selectively remove some lines of input before they
     * become beans.
     * @param filter A class that filters the input lines
     */
    public void setFilter(CsvToBeanFilter filter) {
        this.filter = filter;
    }

    /**
     * Determines whether errors during import should be thrown or kept in a
     * list for later retrieval via {@link #getCapturedExceptions()}.
     * @param throwExceptions Whether or not to throw exceptions during processing
     */
    public void setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
    }
}

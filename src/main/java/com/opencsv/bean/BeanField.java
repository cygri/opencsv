package com.opencsv.bean;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.lang.reflect.Field;

/**
 * Used to extend the {@link java.lang.reflect.Field} class to include
 * functionality that opencsv requires.
 * This includes a required flag and a {@link #write(java.lang.Object)} method
 * for writing beans back out to a CSV file. The required flag determines if the
 * field has to be non-empty.
 *
 * @param <T> Type of the bean being populated
 */
public interface BeanField<T> {

    /**
     * Sets the field to be processed.
     *
     * @param field Which field is being populated
     */
    void setField(Field field);

    /**
     * Gets the field to be processed.
     *
     * @return A field object
     * @see java.lang.reflect.Field
     */
    Field getField();

    /**
     * Populates the selected field of the bean.
     * This method performs conversion on the input string and assigns the
     * result to the proper field in the provided bean.
     *
     * @param bean  Object containing the field to be set.
     * @param value String containing the value to set the field to.
     * @param <T>   Type of the bean.
     * @throws CsvDataTypeMismatchException    When the result of data conversion returns
     *                                         an object that cannot be assigned to the selected field
     * @throws CsvRequiredFieldEmptyException  When a field is mandatory, but there is no
     *                                         input datum in the CSV file
     * @throws CsvConstraintViolationException When the internal structure of
     *                                         data would be violated by the data in the CSV file
     */
    <T> void setFieldValue(T bean, String value)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException,
            CsvConstraintViolationException;
    
    /**
     * This method takes the current value of the field in question in the bean
     * passed in and converts it to a string.
     * This method is used to write beans back out to a CSV file, and should
     * ideally provide an accurate representation of the field such that it is
     * roundtrip equivalent. That is to say, this method should write data out
     * just as it would expect to read the data in.
     * 
     * @param bean The bean holding the field to be written
     * @return A string representation of this field out of the bean passed in.
     *   If either the bean or the field are null, this method returns null to
     *   allow the writer to treat null specially. The writer may wish to write
     *   "(null)" or "\0" or "NULL" or some other key instead of a blank string.
     * 
     * @throws CsvDataTypeMismatchException If expected to convert an
     *   unsupported data type
     * @throws CsvRequiredFieldEmptyException If the field is marked as required,
     *   but currently contains null
     * @since 3.9
     */
    String write(T bean) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException;
}

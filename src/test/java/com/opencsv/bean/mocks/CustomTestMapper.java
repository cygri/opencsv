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
package com.opencsv.bean.mocks;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A basic custom mapper that maps all primitives and wrapped primitives.
 * Always returns the same values, regardless of the input, unless the
 * destination type is a string.
 *
 * @param <T> Type of the bean
 * @author Andrew Rucker Jones
 */
public class CustomTestMapper<T> extends AbstractBeanField<T> {
    @Override
    protected Object convert(String value)
            throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException, CsvConstraintViolationException {
        Class fieldType = field.getType();
        if (fieldType.equals(Boolean.TYPE) || fieldType.equals(Boolean.class))
            return Boolean.TRUE;
        if (fieldType.equals(Byte.TYPE) || fieldType.equals(Byte.class))
            return Byte.MAX_VALUE;
        if (fieldType.equals(Double.TYPE) || fieldType.equals(Double.class))
            return Double.MAX_VALUE;
        if (fieldType.equals(Float.TYPE) || fieldType.equals(Float.class))
            return Float.MAX_VALUE;
        if (fieldType.equals(Integer.TYPE) || fieldType.equals(Integer.class))
            return Integer.MAX_VALUE;
        if (fieldType.equals(Long.TYPE) || fieldType.equals(Long.class))
            return Long.MAX_VALUE;
        if (fieldType.equals(Short.TYPE) || fieldType.equals(Short.class))
            return Short.MAX_VALUE;
        if (fieldType.equals(Character.TYPE) || fieldType.equals(Character.class))
            return Character.MAX_VALUE;
        if (fieldType.equals(BigDecimal.class))
            return BigDecimal.TEN;
        if (fieldType.equals(BigInteger.class))
            return BigInteger.TEN;
        if (fieldType.isAssignableFrom(String.class))
            return "inside custom converter";
        throw new CsvDataTypeMismatchException(value, fieldType, String.format(
                "Unable to set field value for field '%s' with value '%s' "
                        + "- type is unsupported. Use primitive, boxed "
                        + "primitive, BigDecimal, BigInteger and String types only.",
                fieldType, value));
    }
}

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

/**
 * This class converts to {@link com.opencsv.bean.mocks.ComplexClassForCustomAnnotation}.
 *
 * @author Andrew Rucker Jones
 */
public class ConverterComplexClassForCustomAnnotation<T> extends AbstractBeanField<T> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException, CsvConstraintViolationException {
        ComplexClassForCustomAnnotation o;
        String[] sa = value.split("\\.", 3);
        if (sa[2].contains("derived")) {
            o = new ComplexDerivedClassForCustomAnnotation();
            ((ComplexDerivedClassForCustomAnnotation) o).f = (float) 1.0;
        } else if (sa[2].contains("badtype")) {
            return "Mismatched data type";
        } else {
            o = new ComplexClassForCustomAnnotation();
        }
        o.i = Integer.valueOf(sa[0]);
        o.c = sa[1].charAt(0);
        o.s = sa[2];
        return o;
    }
}

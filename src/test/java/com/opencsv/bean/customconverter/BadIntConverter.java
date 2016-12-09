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
package com.opencsv.bean.customconverter;

import com.opencsv.bean.AbstractBeanField;

/**
 * The sole purpose of this converter is to throw an exception on instantiation.
 * @author Andrew Rucker Jones
 */
public class BadIntConverter extends AbstractBeanField {

    private BadIntConverter() {
        // Throws an exception because access is private
    }

    @Override
    public Object convert(String value) {
        return null;
    }
}

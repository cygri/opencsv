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

import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.customconverter.BadIntConverter;

/**
 * Tests what happens when a bad converter is passed in.
 * @author Andrew Rucker Jones
 */
public class TestCase80 {
    @CsvCustomBindByName(converter = BadIntConverter.class, column = "test")
    @CsvCustomBindByPosition(converter = BadIntConverter.class, position = 1)
    public int test;
}

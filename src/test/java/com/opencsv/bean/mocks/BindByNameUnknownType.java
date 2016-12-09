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

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

/**
 * For test cases 21 und 22 (writing).
 * @author Andrew Rucker Jones
 */
public class BindByNameUnknownType {
    public static final String TOSTRING = "This is just a test of unknown datatypes.";
    
    @CsvBindByName
    @CsvBindByPosition(position = 0)
    private final BindByNameUnknownTypeInnerClass test =
            new BindByNameUnknownTypeInnerClass(TOSTRING);
    public BindByNameUnknownTypeInnerClass getTest() {return test;}
    
    private class BindByNameUnknownTypeInnerClass {
        private final String s;
        public BindByNameUnknownTypeInnerClass(String s) {
            this.s = s;
        }
        
        @Override
        public String toString() {
            return s;
        }
    }
}

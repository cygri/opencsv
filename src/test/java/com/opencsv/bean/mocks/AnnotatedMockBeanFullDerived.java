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

/**
 * A derived class with one additional field to test mapping with inheritance.
 * @author Andrew Rucker Jones
 */
public class AnnotatedMockBeanFullDerived extends AnnotatedMockBeanFull {
    
    /**
     * Field for annotation tests.
     * <p>Used for the following test cases, reading:<ul>
     * <li>81</li>
     * </ul></p>
     * <p>Used for the following test cases, writing:<ul>
     * <li>33</li>
     * <li>34</li>
     * </ul></p>
     */
    @CsvBindByName(required = true, column = "int in subclass")
    private int intInSubclass;

    public int getIntInSubclass() {
        return intInSubclass;
    }

    public void setIntInSubclass(int intInSubclass) {
        this.intInSubclass = intInSubclass;
    }
}

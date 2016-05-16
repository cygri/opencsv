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

import java.util.List;

/**
 * For annotation test cases 21 and 63.
 * Would love to do this an an inner class, but that won't work without
 * further refactoring. An inner class does not have a nullary constructor,
 * and can't be made to have one. A good, brief explanation can be found at
 * <a href="http://thecodersbreakfast.net/index.php?post/2011/09/26/Inner-classes-and-the-myth-of-the-default-constructor">
 * The Coder's Breakfast</a>.
 *
 * @author Andrew Rucker Jones
 */
public class TestCases21And63 {
    @CsvBindByName
    @CsvBindByPosition(position = 0)
    public List<Boolean> list;
}

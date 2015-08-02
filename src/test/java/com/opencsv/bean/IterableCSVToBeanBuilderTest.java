package com.opencsv.bean;
/**
 * Copyright 2005 Bytecode Pty Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.opencsv.CSVReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by scott on 7/28/15.
 */
public class IterableCSVToBeanBuilderTest {
    private IterableCSVToBeanBuilder builder;
    private MappingStrategy mockMappingStrategy;
    private CsvToBeanFilter mockFilter;
    private CSVReader mockReader;

    @Before
    public void createBuilder() {
        builder = new IterableCSVToBeanBuilder();
        mockMappingStrategy = mock(MappingStrategy.class);
        mockReader = mock(CSVReader.class);
        mockFilter = mock(CsvToBeanFilter.class);
    }

    @Test
    public void defaultBuilderMissingInformation() {

        String message = null;
        try {
            builder.build();
        } catch (RuntimeException rte) {
            message = rte.getMessage();
        }
        assertEquals(IterableCSVToBeanBuilder.NO_MAPPING_STRATEGY_DEFINED, message);
    }

    @Test
    public void willThrowExceptionIfMissingReader() {
        String message = null;
        try {
            builder.withMapper(mockMappingStrategy).build();
        } catch (RuntimeException rte) {
            message = rte.getMessage();
        }
        assertEquals(IterableCSVToBeanBuilder.NO_READER_DEFINED, message);
    }

    @Test
    public void builderWithoutFilter() {
        IterableCSVToBean bean = builder.withMapper(mockMappingStrategy)
                .withReader(mockReader)
                .build();
        assertEquals(mockMappingStrategy, builder.getStrategy());
        assertEquals(mockReader, builder.getCsvReader());
        assertNull(builder.getFilter());

        assertNotNull(bean);
        assertEquals(mockMappingStrategy, bean.getStrategy());
        assertEquals(mockReader, bean.getCSVReader());
        assertNull(bean.getFilter());
    }

    @Test
    public void builderWithFilter() {
        IterableCSVToBean bean = builder.withMapper(mockMappingStrategy)
                .withReader(mockReader)
                .withFilter(mockFilter)
                .build();
        assertEquals(mockMappingStrategy, builder.getStrategy());
        assertEquals(mockReader, builder.getCsvReader());
        assertEquals(mockFilter, builder.getFilter());

        assertNotNull(bean);
        assertEquals(mockMappingStrategy, bean.getStrategy());
        assertEquals(mockReader, bean.getCSVReader());
        assertEquals(mockFilter, bean.getFilter());
    }
}

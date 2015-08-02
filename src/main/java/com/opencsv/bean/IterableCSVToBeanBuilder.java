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

/**
 * Created by scott on 7/28/15.
 */
public class IterableCSVToBeanBuilder<T> {

    public static final String NO_MAPPING_STRATEGY_DEFINED = "Unable to instantiate IterableCSVToBeanBuilder because there is no MappingStrategy defined.";
    public static final String NO_READER_DEFINED = "Unable to instantiate IterableCSVToBeanBuilder because there is no CSVReader defined.";

    private MappingStrategy<T> mapper;
    private CSVReader csvReader;
    private CsvToBeanFilter filter;

    /**
     * Creates the IterableCSVToBean.
     *
     * @return an instance of IterableCSVToBean with
     */
    public IterableCSVToBean<T> build() {
        if (mapper == null) {
            throw new RuntimeException(NO_MAPPING_STRATEGY_DEFINED);
        }
        if (csvReader == null) {
            throw new RuntimeException(NO_READER_DEFINED);
        }
        return new IterableCSVToBean<T>(csvReader, mapper, filter);
    }

    public IterableCSVToBeanBuilder<T> withMapper(final MappingStrategy<T> mappingStrategy) {
        this.mapper = mappingStrategy;
        return this;
    }

    public IterableCSVToBeanBuilder<T> withReader(final CSVReader reader) {
        this.csvReader = reader;
        return this;
    }

    protected MappingStrategy getStrategy() {
        return mapper;
    }

    protected CSVReader getCsvReader() {
        return csvReader;
    }

    public Object getFilter() {
        return filter;
    }

    public IterableCSVToBeanBuilder<T> withFilter(final CsvToBeanFilter filter) {
        this.filter = filter;
        return this;
    }
}

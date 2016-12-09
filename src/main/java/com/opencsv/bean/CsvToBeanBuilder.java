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
package com.opencsv.bean;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import java.io.Reader;

/**
 * This class makes it possible to bypass all the intermediate steps and classes
 * in setting up to read from a CSV source to a list of beans.
 * <p>This is the place to start if you're reading a CSV source into beans,
 * especially if you're binding the input's columns to the bean's variables
 * using the annotations {@link CsvBindByName}, {@link CsvCustomBindByName},
 * {@link CsvBindByPosition}, {@link CsvCustomBindByPosition}, or {@link CsvBind}.</p>
 * <p>If you want nothing but defaults for the entire import, your code can look
 * as simple as this, where {@code myreader} is any valid {@link java.io.Reader Reader}:<br>
 * {@code List<MyBean> result = new CsvToBeanBuilder(myreader).withType(MyBean.class).build().parse();}</p>
 * <p>This builder is intelligent enough to guess the mapping strategy according to the
 * following strategy:</p><ol>
 * <li>If a mapping strategy is explicitly set, it is always used.</li>
 * <li>If {@link CsvBindByPosition} or {@link CsvCustomBindByPosition} is present,
 * {@link ColumnPositionMappingStrategy} is used.</li>
 * <li>Otherwise, {@link HeaderColumnNameMappingStrategy} is used. This includes
 * the case when {@link CsvBindByName}, {@link CsvCustomBindByName}, or
 * {@link CsvBind} are being used. The annotations will automatically be
 * recognized.</li></ol>
 * 
 * @param <T> Type of the bean to be populated
 * @author Andrew Rucker Jones
 * @since 3.9
 */
public class CsvToBeanBuilder<T> {
    
   /** @see CsvToBean#mappingStrategy */
   private MappingStrategy<T> mappingStrategy = null;
   
   /**
    * A CSVReader will be built out of this {@link java.io.Reader}.
    * @see CsvToBean#csvReader
    */
   private final Reader reader;
   
   /** @see CsvToBean#filter */
   private CsvToBeanFilter filter = null;
   
   /** @see CsvToBean#throwExceptions */
   private boolean throwExceptions = true;
   
   /** @see com.opencsv.CSVParser#nullFieldIndicator */
   private CSVReaderNullFieldIndicator nullFieldIndicator = null;
   
   /** @see com.opencsv.CSVReader#keepCR */
   private boolean keepCR;
   
   /** @see com.opencsv.CSVReader#skipLines */
   private Integer skipLines = null;
   
   /** @see com.opencsv.CSVReader#verifyReader */
   private Boolean verifyReader = null;
   
   /** @see com.opencsv.CSVParser#separator */
   private Character separator = null;
   
   /** @see com.opencsv.CSVParser#quotechar */
   private Character quoteChar = null;
   
   /** @see com.opencsv.CSVParser#escape */
   private Character escapeChar = null;
   
   /** @see com.opencsv.CSVParser#strictQuotes */
   private Boolean strictQuotes = null;
   
   /** @see com.opencsv.CSVParser#ignoreLeadingWhiteSpace */
   private Boolean ignoreLeadingWhiteSpace = null;
   
   /** @see com.opencsv.CSVParser#ignoreQuotations */
   private Boolean ignoreQuotations = null;

   /** @see HeaderColumnNameMappingStrategy#type */
   private Class<? extends T> type = null;
   
   /** This constructor must never be called, because Reader must be set. */
   private CsvToBeanBuilder() {
       reader = null; // Otherwise the compiler complains that reader can't be final.
       throw new IllegalStateException("The nullary constructor may never be used in CsvToBeanBuilder.");
   }
   
   /**
    * Constructor with the one parameter that is most definitely mandatory, and
    * always will be.
    * @param reader The reader that is the source of data for the CSV import
    */
   public CsvToBeanBuilder(Reader reader) {
       if(reader == null) {
           throw new IllegalArgumentException("The Reader must always be non-null.");
       }
       this.reader = reader;
   }
    
    /**
     * Builds the {@link CsvToBean} out of the provided information.
     * @return A valid {@link CsvToBean}
     * @throws IllegalStateException If a necesary parameter was not specified.
     *   Currently this means that both the mapping strategy and the bean type
     *   are not set, so it is impossible to determine a mapping strategy.
     */
    public CsvToBean build() throws IllegalStateException {
        // Check for errors in the configuration first
        if(mappingStrategy == null && type == null) {
            throw new IllegalStateException("Either a mapping strategy or the type of the bean to be populated must be specified.");
        }
        
        // Build Parser and Reader
        CsvToBean bean = new CsvToBean();
        CSVParser parser = buildParser();
        bean.setCsvReader(buildReader(parser));
        
        // Set variables in CsvToBean itself
        bean.setThrowExceptions(throwExceptions);
        if(filter != null) { bean.setFilter(filter); }
        
        // Now find the mapping strategy.
        if(mappingStrategy == null) {
            mappingStrategy = MappingUtils.<T>determineMappingStrategy(type);
        }
        bean.setMappingStrategy(mappingStrategy);
        
        return bean;
    }
    
    /**
     * Builds a {@link CSVParser} from the information provided to this builder.
     * This is an intermediate step in building the {@link CsvToBean}.
     * @return An appropriate {@link CSVParser}
     */
    private CSVParser buildParser() {
        CSVParserBuilder csvpb = new CSVParserBuilder();
        if(nullFieldIndicator != null) {
            csvpb.withFieldAsNull(nullFieldIndicator);
        }
        if(separator != null) {
            csvpb.withSeparator(separator);
        }
        if(quoteChar != null) {
            csvpb.withQuoteChar(quoteChar);
        }
        if(escapeChar != null) {
            csvpb.withEscapeChar(escapeChar);
        }
        if(strictQuotes != null) {
            csvpb.withStrictQuotes(strictQuotes);
        }
        if(ignoreLeadingWhiteSpace != null) {
            csvpb.withIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace);
        }
        if(ignoreQuotations != null) {
            csvpb.withIgnoreQuotations(ignoreQuotations);
        }
        
        return csvpb.build();
    }
    
    /**
     * Builds a {@link CSVReader} from the information provided to this builder.
     * This is an intermediate step in building the {@link CsvToBean}.
     * @param parser The {@link CSVParser} necessary for this reader
     * @return An appropriate {@link CSVReader}
     */
    private CSVReader buildReader(CSVParser parser) {
        CSVReaderBuilder csvrb = new CSVReaderBuilder(reader);
        csvrb.withCSVParser(parser);
        csvrb.withKeepCarriageReturn(keepCR);
        if(verifyReader != null) {
            csvrb.withVerifyReader(verifyReader);
        }
        if(skipLines != null) {
            csvrb.withSkipLines(skipLines);
        }
        return csvrb.build();
    }
    
    /**
     * @see CsvToBean#setMappingStrategy(com.opencsv.bean.MappingStrategy)
     * @param mappingStrategy Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withMappingStrategy(MappingStrategy<T> mappingStrategy) {
        this.mappingStrategy = mappingStrategy;
        return this;
    }

    /**
     * @see CsvToBean#setFilter(com.opencsv.bean.CsvToBeanFilter)
     * @param filter Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withFilter(CsvToBeanFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * @see CsvToBean#setThrowExceptions(boolean)
     * @param throwExceptions Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char, char, boolean, boolean, boolean, CSVReaderNullFieldIndicator)
     * @param indicator Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withFieldAsNull(CSVReaderNullFieldIndicator indicator) {
        this.nullFieldIndicator = indicator;
        return this;
    }
    
    /**
     * @see CSVReader#CSVReader(java.io.Reader, char, char, char, int, boolean, boolean, boolean)
     * @param keepCR Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withKeepCarriageReturn(boolean keepCR) {
        this.keepCR = keepCR;
        return this;
    }
    
    /**
     * @see CSVReader#CSVReader(Reader, int, ICSVParser, boolean, boolean)
     * @param verifyReader Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withVerifyReader(boolean verifyReader) {
        this.verifyReader = verifyReader;
        return this;
    }
    
    /**
     * @see CSVReader#CSVReader(Reader, int, ICSVParser, boolean, boolean)
     * @param skipLines Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withSkipLines(
         final int skipLines) {
      this.skipLines = skipLines;
      return this;
   }
    
    /**
     * @see CSVParser#CSVParser(char)
     * @param separator Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withSeparator(char separator) {
        this.separator = separator;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char)
     * @param quoteChar Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char, char)
     * @param escapeChar Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withEscapeChar(char escapeChar) {
        this.escapeChar = escapeChar;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char, char, boolean)
     * @param strictQuotes Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withStrictQuotes(boolean strictQuotes) {
        this.strictQuotes = strictQuotes;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char, char, boolean, boolean)
     * @param ignoreLeadingWhiteSpace Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        return this;
    }
    
    /**
     * @see CSVParser#CSVParser(char, char, char, boolean, boolean, boolean)
     * @param ignoreQuotations Silence JavaDoc warnings
     * @return Silence JavaDoc warnings
     */
    public CsvToBeanBuilder withIgnoreQuotations(boolean ignoreQuotations) {
        this.ignoreQuotations = ignoreQuotations;
        return this;
    }
    
    /**
     * Sets the type of the bean to be populated.
     * Ignored if {@link #withMappingStrategy(com.opencsv.bean.MappingStrategy)}
     * is called.
     * @param type Class of the destination bean
     * @return this
     * @see HeaderColumnNameMappingStrategy#setType(java.lang.Class)
     * @see ColumnPositionMappingStrategy#setType(java.lang.Class)
     */
    public CsvToBeanBuilder withType(Class<? extends T> type) {
        this.type = type;
        return this;
    }
}

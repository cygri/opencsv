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

import com.opencsv.bean.CsvBind;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A test class that should provide full coverage for tests of the opencsv
 * annotations and their functions.
 *
 * @author Andrew Rucker Jones
 */
public class AnnotatedMockBeanFull {

    /**
     * <p>Used for the following test cases:<ul>
     * <li>2</li>
     * <li>5</li>
     * <li>8</li>
     * <li>10</li>
     * <li>49</li>
     * <li>52</li>
     * <li>54</li>
     * </ul></p>
     */
    @CsvBindByName(column = "bool1")
    @CsvBindByPosition(position = 1)
    private Boolean boolWrapped;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>1</li>
     * <li>4</li>
     * <li>9</li>
     * <li>48</li>
     * <li>53</li>
     * </ul></p>
     */
    @CsvBindByName()
    @CsvBindByPosition(position = 2)
    private boolean boolPrimitive;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>11</li>
     * <li>49</li>
     * <li>55</li>
     * </ul></p>
     */
    @CsvBindByName(column = "byte1")
    @CsvBindByPosition(position = 3)
    private Byte byteWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>6</li>
     * <li>28</li>
     * <li>7</li>
     * <li>50</li>
     * <li>51</li>
     * </ul></p>
     */
    @CsvBindByName(required = true, column = "byte2", locale = "de")
    @CsvBindByPosition(required = true, position = 4, locale = "de")
    private Byte byteWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>48</li>
     * </ul></p>
     */
    @CsvBindByName(required = true, column = "byte3")
    @CsvBindByPosition(required = true, position = 5)
    private byte bytePrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "byte4", locale = "de")
    @CsvBindByPosition(position = 6, locale = "de")
    private byte bytePrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "double1", locale = "en_US")
    @CsvBindByPosition(position = 7, locale = "en_US")
    private Double doubleWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "double2", locale = "de")
    @CsvBindByPosition(position = 8, locale = "de")
    private Double doubleWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>48</li>
     * </ul></p>
     */
    @CsvBindByName(column = "double3")
    @CsvBindByPosition(position = 9)
    private double doublePrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "double4", locale = "de")
    @CsvBindByPosition(position = 10, locale = "de")
    private double doublePrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "float1")
    @CsvBindByPosition(position = 11)
    private Float floatWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "float2", locale = "de")
    @CsvBindByPosition(position = 12, locale = "de")
    private Float floatWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>22</li>
     * <li>48</li>
     * <li>64</li>
     * </ul></p>
     */
    @CsvBindByName(column = "float3")
    @CsvBindByPosition(position = 13)
    private float floatPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "float4", locale = "de")
    @CsvBindByPosition(position = 14, locale = "de")
    private float floatPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "integer1")
    @CsvBindByPosition(position = 15)
    private Integer integerWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "integer2", locale = "de")
    @CsvBindByPosition(position = 16, locale = "de")
    private Integer integerWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>24</li>
     * <li>48</li>
     * <li>66</li>
     * </ul></p>
     */
    @CsvBindByName(column = "integer3")
    @CsvBindByPosition(position = 17)
    @CsvBind
    private int integerPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "integer4", locale = "de")
    @CsvBindByPosition(position = 18, locale = "de")
    private int integerPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "long1")
    @CsvBindByPosition(position = 19)
    private Long longWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "long2", locale = "de")
    @CsvBindByPosition(position = 20, locale = "de")
    private Long longWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>26</li>
     * <li>48</li>
     * <li>68</li>
     * </ul></p>
     */
    @CsvBindByName(column = "long3")
    @CsvBindByPosition(position = 21)
    private long longPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "long4", locale = "de")
    @CsvBindByPosition(position = 22, locale = "de")
    private long longPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "short1")
    @CsvBindByPosition(position = 23)
    private Short shortWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "short2", locale = "de")
    @CsvBindByPosition(position = 24, locale = "de")
    private Short shortWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>48</li>
     * </ul></p>
     */
    @CsvBindByName(column = "short3")
    @CsvBindByPosition(position = 25)
    private short shortPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "short4", locale = "de")
    @CsvBindByPosition(position = 26, locale = "de")
    private short shortPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>5</li>
     * <li>49</li>
     * </ul></p>
     */
    @CsvBindByName(column = "char1")
    @CsvBindByPosition(position = 27)
    private Character characterWrapped;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>48</li>
     * </ul></p>
     */
    @CsvBindByName(column = "char2")
    @CsvBindByPosition(position = 28)
    private char characterPrimitive;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>29</li>
     * </ul></p>
     */
    @CsvBindByName(column = "bigdecimal1")
    @CsvBindByPosition(position = 29)
    private BigDecimal bigdecimalDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "bigdecimal2", locale = "de")
    @CsvBindByPosition(position = 30, locale = "de")
    private BigDecimal bigdecimalSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>29</li>
     * </ul></p>
     */
    @CsvBindByName(column = "biginteger1")
    @CsvBindByPosition(position = 31)
    private BigInteger bigintegerDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * </ul></p>
     */
    @CsvBindByName(column = "biginteger2", locale = "de")
    @CsvBindByPosition(position = 32, locale = "de")
    private BigInteger bigintegerSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * <li>78</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date1", required = true)
    @CsvBindByPosition(position = 33, required = true)
    @CsvDate()
    private Date dateDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * <li>70</li>
     * <li>71</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date2")
    @CsvBindByPosition(position = 34)
    @CsvDate()
    private GregorianCalendar gcalDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date3")
    @CsvBindByPosition(position = 35)
    @CsvDate()
    private Calendar calDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date4")
    @CsvBindByPosition(position = 36)
    @CsvDate()
    private XMLGregorianCalendar xmlcalDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date5")
    @CsvBindByPosition(position = 37)
    @CsvDate()
    private Time sqltimeDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date6")
    @CsvBindByPosition(position = 38)
    @CsvDate()
    private Timestamp sqltimestampDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date7", locale = "de")
    @CsvBindByPosition(position = 39, locale = "de")
    @CsvDate()
    private Date dateSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date8", locale = "de")
    @CsvBindByPosition(position = 40, locale = "de")
    @CsvDate()
    private GregorianCalendar gcalSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date9", locale = "de")
    @CsvBindByPosition(position = 41, locale = "de")
    @CsvDate()
    private Calendar calSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date10", locale = "de")
    @CsvBindByPosition(position = 42, locale = "de")
    @CsvDate()
    private XMLGregorianCalendar xmlcalSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date11", locale = "de")
    @CsvBindByPosition(position = 43, locale = "de")
    @CsvDate()
    private Time sqltimeSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>28</li>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date12", locale = "de")
    @CsvBindByPosition(position = 44, locale = "de")
    @CsvDate()
    private Timestamp sqltimestampSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>4</li>
     * <li>48</li>
     * </ul></p>
     */
    @CsvBindByName(column = "string1")
    @CsvBindByPosition(position = 0)
    private String stringClass;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>32</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date13")
    @CsvBindByPosition(position = 45)
    @CsvDate("MM/dd/yyyy")
    private GregorianCalendar gcalFormatDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>33</li>
     * </ul></p>
     */
    // https://issues.apache.org/jira/browse/BEANUTILS-486
//    @CsvBindByName(column = "date14", locale = "de_DE")
//    @CsvBindByPosition(position = 46, locale = "de_DE")
//    @CsvDate("dd. MMM yyyy")
    private GregorianCalendar gcalFormatSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>30</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date15")
    @CsvBindByPosition(position = 47)
    @CsvDate()
    private java.sql.Date sqldateDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>31</li>
     * </ul></p>
     */
    @CsvBindByName(column = "date16", locale = "de_DE")
    @CsvBindByPosition(position = 48, locale = "de_DE")
    @CsvDate()
    private java.sql.Date sqldateSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>35</li>
     * </ul></p>
     */
    @CsvBindByName(column = "float5", locale = "doesntexistquitecertain")
    @CsvBindByPosition(position = 49, locale = "doesntexistquitecertain")
    private float floatBadLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>76</li>
     * <li>77</li>
     * </ul></p>
     */
    @CsvBindByName(column = "itnogoodcolumnitverybad")
    @CsvBindByPosition(position = 100)
    private String columnDoesntExist;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>3</li>
     * <li>31</li>
     * <li>47</li>
     * </ul></p>
     */
    private String unmapped;

    public Boolean getBoolWrapped() {
        return boolWrapped;
    }

    public void setBoolWrapped(Boolean boolWrapped) {
        this.boolWrapped = boolWrapped;
    }

    /*
    For test case 79. I have read that introspection is used on beans, and
    that beans are only allowed a single setter method for any member variable
    that must have the same type as the member variable. Introspection fails to
    find the setter otherwise. See:
    http://commons.apache.org/proper/commons-beanutils/javadocs/v1.9.2/apidocs/org/apache/commons/beanutils/package-summary.html#FAQ.property
    */
    public void setBoolWrapped(String boolWrapped) {
        this.boolWrapped = Boolean.valueOf(boolWrapped);
    }

    public boolean isBoolPrimitive() {
        return boolPrimitive;
    }

    public void setBoolPrimitive(boolean boolPrimitive) {
        this.boolPrimitive = boolPrimitive;
    }

    public Byte getByteWrappedDefaultLocale() {
        return byteWrappedDefaultLocale;
    }

    public void setByteWrappedDefaultLocale(Byte byteWrappedDefaultLocale) {
        this.byteWrappedDefaultLocale = byteWrappedDefaultLocale;
    }

    public Byte getByteWrappedSetLocale() {
        return byteWrappedSetLocale;
    }

    public void setByteWrappedSetLocale(Byte byteWrappedSetLocale) {
        this.byteWrappedSetLocale = byteWrappedSetLocale;
    }

    public byte getBytePrimitiveDefaultLocale() {
        return bytePrimitiveDefaultLocale;
    }

    public void setBytePrimitiveDefaultLocale(byte bytePrimitiveDefaultLocale) {
        this.bytePrimitiveDefaultLocale = bytePrimitiveDefaultLocale;
    }

    public byte getBytePrimitiveSetLocale() {
        return bytePrimitiveSetLocale;
    }

    public void setBytePrimitiveSetLocale(byte bytePrimitiveSetLocale) {
        this.bytePrimitiveSetLocale = bytePrimitiveSetLocale;
    }

    public Double getDoubleWrappedDefaultLocale() {
        return doubleWrappedDefaultLocale;
    }

    public void setDoubleWrappedDefaultLocale(Double doubleWrappedDefaultLocale) {
        this.doubleWrappedDefaultLocale = doubleWrappedDefaultLocale;
    }

    public Double getDoubleWrappedSetLocale() {
        return doubleWrappedSetLocale;
    }

    public void setDoubleWrappedSetLocale(Double doubleWrappedSetLocale) {
        this.doubleWrappedSetLocale = doubleWrappedSetLocale;
    }

    public double getDoublePrimitiveDefaultLocale() {
        return doublePrimitiveDefaultLocale;
    }

    public void setDoublePrimitiveDefaultLocale(double doublePrimitiveDefaultLocale) {
        this.doublePrimitiveDefaultLocale = doublePrimitiveDefaultLocale;
    }

    public double getDoublePrimitiveSetLocale() {
        return doublePrimitiveSetLocale;
    }

    public void setDoublePrimitiveSetLocale(double doublePrimitiveSetLocale) {
        this.doublePrimitiveSetLocale = doublePrimitiveSetLocale;
    }

    public Float getFloatWrappedDefaultLocale() {
        return floatWrappedDefaultLocale;
    }

    public void setFloatWrappedDefaultLocale(Float floatWrappedDefaultLocale) {
        this.floatWrappedDefaultLocale = floatWrappedDefaultLocale;
    }

    public Float getFloatWrappedSetLocale() {
        return floatWrappedSetLocale;
    }

    public void setFloatWrappedSetLocale(Float floatWrappedSetLocale) {
        this.floatWrappedSetLocale = floatWrappedSetLocale;
    }

    public float getFloatPrimitiveDefaultLocale() {
        return floatPrimitiveDefaultLocale;
    }

    /* Commented out for test case 22.
    public void setFloatPrimitiveDefaultLocale(float floatPrimitiveDefaultLocale) {
        this.floatPrimitiveDefaultLocale = floatPrimitiveDefaultLocale;
    }
    */

    public float getFloatPrimitiveSetLocale() {
        return floatPrimitiveSetLocale;
    }

    public void setFloatPrimitiveSetLocale(float floatPrimitiveSetLocale) {
        this.floatPrimitiveSetLocale = floatPrimitiveSetLocale;
    }

    public Integer getIntegerWrappedDefaultLocale() {
        return integerWrappedDefaultLocale;
    }

    public void setIntegerWrappedDefaultLocale(Integer integerWrappedDefaultLocale) {
        this.integerWrappedDefaultLocale = integerWrappedDefaultLocale;
    }

    public Integer getIntegerWrappedSetLocale() {
        return integerWrappedSetLocale;
    }

    public void setIntegerWrappedSetLocale(Integer integerWrappedSetLocale) {
        this.integerWrappedSetLocale = integerWrappedSetLocale;
    }

    public int getIntegerPrimitiveDefaultLocale() {
        return integerPrimitiveDefaultLocale;
    }

    public void setIntegerPrimitiveDefaultLocale(int integerPrimitiveDefaultLocale) {
        this.integerPrimitiveDefaultLocale = Integer.MAX_VALUE - integerPrimitiveDefaultLocale;
    }

    public int getIntegerPrimitiveSetLocale() {
        return integerPrimitiveSetLocale;
    }

    public void setIntegerPrimitiveSetLocale(int integerPrimitiveSetLocale) {
        this.integerPrimitiveSetLocale = integerPrimitiveSetLocale;
    }

    public Long getLongWrappedDefaultLocale() {
        return longWrappedDefaultLocale;
    }

    public void setLongWrappedDefaultLocale(Long longWrappedDefaultLocale) {
        this.longWrappedDefaultLocale = longWrappedDefaultLocale;
    }

    public Long getLongWrappedSetLocale() {
        return longWrappedSetLocale;
    }

    public void setLongWrappedSetLocale(Long longWrappedSetLocale) {
        this.longWrappedSetLocale = longWrappedSetLocale;
    }

    public long getLongPrimitiveDefaultLocale() {
        return longPrimitiveDefaultLocale;
    }

    /* Private for test case 26. */
    private void setLongPrimitiveDefaultLocale(long longPrimitiveDefaultLocale) {
        this.longPrimitiveDefaultLocale = Long.MAX_VALUE - longPrimitiveDefaultLocale;
    }

    public long getLongPrimitiveSetLocale() {
        return longPrimitiveSetLocale;
    }

    public void setLongPrimitiveSetLocale(long longPrimitiveSetLocale) {
        this.longPrimitiveSetLocale = longPrimitiveSetLocale;
    }

    public Short getShortWrappedDefaultLocale() {
        return shortWrappedDefaultLocale;
    }

    public void setShortWrappedDefaultLocale(Short shortWrappedDefaultLocale) {
        this.shortWrappedDefaultLocale = shortWrappedDefaultLocale;
    }

    public Short getShortWrappedSetLocale() {
        return shortWrappedSetLocale;
    }

    public void setShortWrappedSetLocale(Short shortWrappedSetLocale) {
        this.shortWrappedSetLocale = shortWrappedSetLocale;
    }

    public short getShortPrimitiveDefaultLocale() {
        return shortPrimitiveDefaultLocale;
    }

    public void setShortPrimitiveDefaultLocale(short shortPrimitiveDefaultLocale) {
        this.shortPrimitiveDefaultLocale = shortPrimitiveDefaultLocale;
    }

    public short getShortPrimitiveSetLocale() {
        return shortPrimitiveSetLocale;
    }

    public void setShortPrimitiveSetLocale(short shortPrimitiveSetLocale) {
        this.shortPrimitiveSetLocale = shortPrimitiveSetLocale;
    }

    public Character getCharacterWrapped() {
        return characterWrapped;
    }

    public void setCharacterWrapped(Character characterWrapped) {
        this.characterWrapped = characterWrapped;
    }

    public char getCharacterPrimitive() {
        return characterPrimitive;
    }

    public void setCharacterPrimitive(char characterPrimitive) {
        this.characterPrimitive = characterPrimitive;
    }

    public BigDecimal getBigdecimalDefaultLocale() {
        return bigdecimalDefaultLocale;
    }

    public void setBigdecimalDefaultLocale(BigDecimal bigdecimalDefaultLocale) {
        this.bigdecimalDefaultLocale = bigdecimalDefaultLocale;
    }

    public BigDecimal getBigdecimalSetLocale() {
        return bigdecimalSetLocale;
    }

    public void setBigdecimalSetLocale(BigDecimal bigdecimalSetLocale) {
        this.bigdecimalSetLocale = bigdecimalSetLocale;
    }

    public BigInteger getBigintegerDefaultLocale() {
        return bigintegerDefaultLocale;
    }

    public void setBigintegerDefaultLocale(BigInteger bigintegerDefaultLocale) {
        this.bigintegerDefaultLocale = bigintegerDefaultLocale;
    }

    public BigInteger getBigintegerSetLocale() {
        return bigintegerSetLocale;
    }

    public void setBigintegerSetLocale(BigInteger bigintegerSetLocale) {
        this.bigintegerSetLocale = bigintegerSetLocale;
    }

    public Date getDateDefaultLocale() {
        return dateDefaultLocale;
    }

    public void setDateDefaultLocale(Date dateDefaultLocale) {
        this.dateDefaultLocale = dateDefaultLocale;
    }

    public GregorianCalendar getGcalDefaultLocale() {
        return gcalDefaultLocale;
    }

    public void setGcalDefaultLocale(GregorianCalendar gcalDefaultLocale) {
        this.gcalDefaultLocale = gcalDefaultLocale;
    }

    public Calendar getCalDefaultLocale() {
        return calDefaultLocale;
    }

    public void setCalDefaultLocale(Calendar calDefaultLocale) {
        this.calDefaultLocale = calDefaultLocale;
    }

    public XMLGregorianCalendar getXmlcalDefaultLocale() {
        return xmlcalDefaultLocale;
    }

    public void setXmlcalDefaultLocale(XMLGregorianCalendar xmlcalDefaultLocale) {
        this.xmlcalDefaultLocale = xmlcalDefaultLocale;
    }

    public Time getSqltimeDefaultLocale() {
        return sqltimeDefaultLocale;
    }

    public void setSqltimeDefaultLocale(Time sqltimeDefaultLocale) {
        this.sqltimeDefaultLocale = sqltimeDefaultLocale;
    }

    public Timestamp getSqltimestampDefaultLocale() {
        return sqltimestampDefaultLocale;
    }

    public void setSqltimestampDefaultLocale(Timestamp sqltimestampDefaultLocale) {
        this.sqltimestampDefaultLocale = sqltimestampDefaultLocale;
    }

    public Date getDateSetLocale() {
        return dateSetLocale;
    }

    public void setDateSetLocale(Date dateSetLocale) {
        this.dateSetLocale = dateSetLocale;
    }

    public GregorianCalendar getGcalSetLocale() {
        return gcalSetLocale;
    }

    public void setGcalSetLocale(GregorianCalendar gcalSetLocale) {
        this.gcalSetLocale = gcalSetLocale;
    }

    public Calendar getCalSetLocale() {
        return calSetLocale;
    }

    public void setCalSetLocale(Calendar calSetLocale) {
        this.calSetLocale = calSetLocale;
    }

    public XMLGregorianCalendar getXmlcalSetLocale() {
        return xmlcalSetLocale;
    }

    public void setXmlcalSetLocale(XMLGregorianCalendar xmlcalSetLocale) {
        this.xmlcalSetLocale = xmlcalSetLocale;
    }

    public Time getSqltimeSetLocale() {
        return sqltimeSetLocale;
    }

    public void setSqltimeSetLocale(Time sqltimeSetLocale) {
        this.sqltimeSetLocale = sqltimeSetLocale;
    }

    public Timestamp getSqltimestampSetLocale() {
        return sqltimestampSetLocale;
    }

    public void setSqltimestampSetLocale(Timestamp sqltimestampSetLocale) {
        this.sqltimestampSetLocale = sqltimestampSetLocale;
    }

    public String getStringClass() {
        return stringClass;
    }

    public void setStringClass(String stringClass) {
        this.stringClass = stringClass;
    }

    public String getColumnDoesntExist() {
        return columnDoesntExist;
    }

    public void setColumnDoesntExist(String columnDoesntExist) {
        this.columnDoesntExist = columnDoesntExist;
    }

    public String getUnmapped() {
        return unmapped;
    }

    public GregorianCalendar getGcalFormatDefaultLocale() {
        return gcalFormatDefaultLocale;
    }

    public void setGcalFormatDefaultLocale(GregorianCalendar gcalFormatDefaultLocale) {
        this.gcalFormatDefaultLocale = gcalFormatDefaultLocale;
    }

    public GregorianCalendar getGcalFormatSetLocale() {
        return gcalFormatSetLocale;
    }

    public void setGcalFormatSetLocale(GregorianCalendar gcalFormatSetLocale) {
        this.gcalFormatSetLocale = gcalFormatSetLocale;
    }

    public java.sql.Date getSqldateDefaultLocale() {
        return sqldateDefaultLocale;
    }

    public void setSqldateDefaultLocale(java.sql.Date sqldateDefaultLocale) {
        this.sqldateDefaultLocale = sqldateDefaultLocale;
    }

    public java.sql.Date getSqldateSetLocale() {
        return sqldateSetLocale;
    }

    public void setSqldateSetLocale(java.sql.Date sqldateSetLocale) {
        this.sqldateSetLocale = sqldateSetLocale;
    }

    public float getFloatBadLocale() {
        return floatBadLocale;
    }

    public void setFloatBadLocale(float floatBadLocale) {
        this.floatBadLocale = floatBadLocale;
    }

}

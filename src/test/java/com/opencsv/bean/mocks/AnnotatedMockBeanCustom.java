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

import com.opencsv.bean.*;
import com.opencsv.bean.customconverter.ConvertGermanToBoolean;
import com.opencsv.bean.customconverter.ConvertGermanToBooleanRequired;
import com.opencsv.bean.customconverter.ConvertSplitOnWhitespace;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * A test class that should provide full coverage for tests of the opencsv
 * annotations and their custom functions.
 *
 * @author Andrew Rucker Jones
 */
public class AnnotatedMockBeanCustom {

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>39</li>
     * <li>40</li>
     * <li>41</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "bool1", converter = ConvertGermanToBooleanRequired.class)
    @CsvCustomBindByPosition(position = 1, converter = ConvertGermanToBooleanRequired.class)
    private Boolean boolWrapped;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>39</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(converter = ConvertGermanToBooleanRequired.class)
    @CsvCustomBindByPosition(position = 2, converter = ConvertGermanToBooleanRequired.class)
    private boolean boolPrimitive;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>36</li>
     * <li>37</li>
     * <li>38</li>
     * <li>15</li>
     * <li>59</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "bool2", converter = ConvertGermanToBoolean.class)
    @CsvCustomBindByPosition(position = 45, converter = ConvertGermanToBoolean.class)
    private Boolean boolWrappedOptional;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>36</li>
     * <li>37</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "bool3", converter = ConvertGermanToBoolean.class)
    @CsvCustomBindByPosition(position = 46, converter = ConvertGermanToBoolean.class)
    private boolean boolPrimitiveOptional;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "byte1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 3, converter = CustomTestMapper.class)
    private Byte byteWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "byte2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 4, converter = CustomTestMapper.class)
    private Byte byteWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>18</li>
     * <li>57</li>
     * <li>62</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "byte3", converter = CustomTestMapper.class)
    @CsvBindByName(column = "byte1")
    @CsvCustomBindByPosition(position = 5, converter = CustomTestMapper.class)
    @CsvBindByPosition(position = 3)
    private byte bytePrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>19</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "byte4", converter = CustomTestMapper.class)
    @CsvBindByName(column = "byte2")
    @CsvCustomBindByPosition(position = 6, converter = CustomTestMapper.class)
    @CsvBindByPosition(position = 4)
    @CsvBind
    private byte bytePrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "double1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 7, converter = CustomTestMapper.class)
    private Double doubleWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "double2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 8, converter = CustomTestMapper.class)
    private Double doubleWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "double3", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 9, converter = CustomTestMapper.class)
    private double doublePrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "double4", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 10, converter = CustomTestMapper.class)
    private double doublePrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "float1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 11, converter = CustomTestMapper.class)
    private Float floatWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "float2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 12, converter = CustomTestMapper.class)
    private Float floatWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "float3", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 13, converter = CustomTestMapper.class)
    private float floatPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "float4", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 14, converter = CustomTestMapper.class)
    private float floatPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "integer1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 15, converter = CustomTestMapper.class)
    private Integer integerWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "integer2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 16, converter = CustomTestMapper.class)
    private Integer integerWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "integer3", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 17, converter = CustomTestMapper.class)
    private int integerPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "integer4", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 18, converter = CustomTestMapper.class)
    private int integerPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "long1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 19, converter = CustomTestMapper.class)
    private Long longWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "long2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 20, converter = CustomTestMapper.class)
    private Long longWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "long3", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 21, converter = CustomTestMapper.class)
    private long longPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "long4", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 22, converter = CustomTestMapper.class)
    private long longPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "short1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 23, converter = CustomTestMapper.class)
    private Short shortWrappedDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "short2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 24, converter = CustomTestMapper.class)
    private Short shortWrappedSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "short3", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 25, converter = CustomTestMapper.class)
    private short shortPrimitiveDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "short4", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 26, converter = CustomTestMapper.class)
    private short shortPrimitiveSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "char1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 27, converter = CustomTestMapper.class)
    private Character characterWrapped;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>13</li>
     * <li>57</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "char2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 28, converter = CustomTestMapper.class)
    private char characterPrimitive;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "bigdecimal1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 29, converter = CustomTestMapper.class)
    private BigDecimal bigdecimalDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "bigdecimal2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 30, converter = CustomTestMapper.class)
    private BigDecimal bigdecimalSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "biginteger1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 31, converter = CustomTestMapper.class)
    private BigInteger bigintegerDefaultLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li></li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "biginteger2", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 32, converter = CustomTestMapper.class)
    private BigInteger bigintegerSetLocale;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>12</li>
     * <li>56</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "string1", converter = CustomTestMapper.class)
    @CsvCustomBindByPosition(position = 0, converter = CustomTestMapper.class)
    private String stringClass;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>14</li>
     * <li>42</li>
     * <li>43</li>
     * <li>44</li>
     * <li>45</li>
     * <li>58</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "string2", converter = ConvertSplitOnWhitespace.class)
    @CsvCustomBindByPosition(position = 47, converter = ConvertSplitOnWhitespace.class)
    private List<String> complexString;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>23</li>
     * <li>17</li>
     * <li>61</li>
     * <li>65</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "complex1", converter = ConverterComplexClassForCustomAnnotation.class)
    @CsvCustomBindByPosition(position = 48, converter = ConverterComplexClassForCustomAnnotation.class)
    private ComplexClassForCustomAnnotation complexClass1;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>25</li>
     * <li>17</li>
     * <li>61</li>
     * <li>67</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "complex2", converter = ConverterComplexClassForCustomAnnotation.class)
    @CsvCustomBindByPosition(position = 49, converter = ConverterComplexClassForCustomAnnotation.class)
    private ComplexClassForCustomAnnotation complexClass2;

    /**
     * <p>Used for the following test cases:<ul>
     * <li>27</li>
     * <li>16</li>
     * <li>17</li>
     * <li>60</li>
     * <li>61</li>
     * <li>69</li>
     * </ul></p>
     */
    @CsvCustomBindByName(column = "complex3", converter = ConverterComplexClassForCustomAnnotation.class)
    @CsvCustomBindByPosition(position = 50, converter = ConverterComplexClassForCustomAnnotation.class)
    private ComplexClassForCustomAnnotation complexClass3;

    public Boolean getBoolWrapped() {
        return boolWrapped;
    }

    public void setBoolWrapped(Boolean boolWrapped) {
        this.boolWrapped = boolWrapped;
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

    public void setFloatPrimitiveDefaultLocale(float floatPrimitiveDefaultLocale) {
        this.floatPrimitiveDefaultLocale = floatPrimitiveDefaultLocale;
    }

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
        this.integerPrimitiveDefaultLocale = integerPrimitiveDefaultLocale;
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

    public void setLongPrimitiveDefaultLocale(long longPrimitiveDefaultLocale) {
        this.longPrimitiveDefaultLocale = longPrimitiveDefaultLocale;
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

    public String getStringClass() {
        return stringClass;
    }

    public void setStringClass(String stringClass) {
        this.stringClass = stringClass;
    }

    public Boolean getBoolWrappedOptional() {
        return boolWrappedOptional;
    }

    public void setBoolWrappedOptional(Boolean boolWrappedOptional) {
        this.boolWrappedOptional = boolWrappedOptional;
    }

    public boolean isBoolPrimitiveOptional() {
        return boolPrimitiveOptional;
    }

    public void setBoolPrimitiveOptional(boolean boolPrimitiveOptional) {
        this.boolPrimitiveOptional = boolPrimitiveOptional;
    }

    public List<String> getComplexString() {
        return complexString;
    }

    public void setComplexString(List<String> complexString) {
        this.complexString = complexString;
    }

    public ComplexClassForCustomAnnotation getComplexClass1() {
        return complexClass1;
    }
    
    /* Commented out for test case 23.
    public void setComplexClass1(ComplexClassForCustomAnnotation complexClass1) {
        this.complexClass1 = complexClass1;
    }
    */

    public ComplexClassForCustomAnnotation getComplexClass2() {
        return complexClass2;
    }

    public void setComplexClass2(ComplexClassForCustomAnnotation complexClass2) {
        this.complexClass2 = complexClass2;
        this.complexClass2.i = Integer.MAX_VALUE - this.complexClass2.i;
        this.complexClass2.c = 'z';
        this.complexClass2.s = "Inserted in setter method" + this.complexClass2.s;
    }

    public ComplexClassForCustomAnnotation getComplexClass3() {
        return complexClass3;
    }

    /* Private for test case 27. */
    private void setComplexClass3(ComplexClassForCustomAnnotation complexClass3) {
        this.complexClass3 = new ComplexClassForCustomAnnotation();
        this.complexClass3.i = -1;
        this.complexClass3.c = 'z';
        this.complexClass3.s = "Inserted in setter method";
    }

}

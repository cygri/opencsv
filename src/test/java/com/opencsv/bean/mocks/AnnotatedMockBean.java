package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

public class AnnotatedMockBean {
    @CsvBind
    private long familyId;
    @CsvBind
    private String familyName;
    @CsvBind
    private byte familySize;
    @CsvBind
    private double averageAge;
    @CsvBind
    private float averageIncome;
    @CsvBind
    private int numberOfPets;
    @CsvBind
    private short numberOfBedrooms;
    @CsvBind
    private char zipcodePrefix;
    @CsvBind
    private boolean hasBeenContacted;

    public long getFamilyId() {
        return familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public byte getFamilySize() {
        return familySize;
    }

    public double getAverageAge() {
        return averageAge;
    }

    public float getAverageIncome() {
        return averageIncome;
    }

    public int getNumberOfPets() {
        return numberOfPets;
    }

    public short getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public char getZipcodePrefix() {
        return zipcodePrefix;
    }

    public boolean isHasBeenContacted() {
        return hasBeenContacted;
    }
}

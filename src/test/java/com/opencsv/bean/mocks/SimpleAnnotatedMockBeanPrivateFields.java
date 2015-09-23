package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

public class SimpleAnnotatedMockBeanPrivateFields {
    @CsvBind
    private String privateField1;
    @CsvBind
    private String privateField2;

    public String getPrivateField1() {
        return privateField1;
    }

    public String getPrivateField2() {
        return privateField2;
    }
}

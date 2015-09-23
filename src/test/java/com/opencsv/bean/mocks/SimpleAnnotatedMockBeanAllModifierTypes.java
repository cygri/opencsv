package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

public class SimpleAnnotatedMockBeanAllModifierTypes {
    @CsvBind
    public String publicField;
    @CsvBind
    private String privateField;
    @CsvBind
    protected String protectedField;
    @CsvBind
    String packagePrivateField;

    public String getPublicField() {
        return publicField;
    }

    public String getPrivateField() {
        return privateField;
    }

    public String getProtectedField() {
        return protectedField;
    }

    public String getPackagePrivateField() {
        return packagePrivateField;
    }
}

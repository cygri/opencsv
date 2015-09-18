package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

public class SimpleAnnotatedMockBean {
    @CsvBind
    private String name;
    @CsvBind
    private String orderNumber;
    @CsvBind(required = true)
    private int num;

    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getNum() {
        return num;
    }
}

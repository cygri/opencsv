package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

public class SimplePartiallyAnnotatedMockBean {
    @CsvBind
    private  String name;
    private  String orderNumber;
    @CsvBind
    private  int num;

    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

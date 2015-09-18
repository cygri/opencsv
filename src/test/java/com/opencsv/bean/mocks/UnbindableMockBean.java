package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

import java.util.Date;

public class UnbindableMockBean {
    @CsvBind
    private Date date;
}

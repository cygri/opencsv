package com.opencsv.bean.mocks;

import com.opencsv.bean.CsvBind;

import java.util.Date;
import java.util.List;

public class UnbindableMockBean {
    @CsvBind
    private List<Date> date;
}

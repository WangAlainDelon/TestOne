package com.test.domain;

public class ConditionalResults {
    //股票代码
    private String stock_code;
    //股票名称
    private String stock_name;
    //多少天之内
    private Integer day;
    //涨跌幅
    private Float chg;
    //次数
    private Integer times;

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Float getChg() {
        return chg;
    }

    public void setChg(Float chg) {
        this.chg = chg;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}

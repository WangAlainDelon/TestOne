package com.test.domain;

import java.util.Date;

public class StockHistory {
    /**
     * 股票的历史信息
     */
    //id
    private Long id;
    //股票代码
    private String stock_code;
    //股票名称
    private String stock_name;
    //日期
    private Date date;
    //开盘价
    private String opening_price;
    //最高价
    private String maximum_price;
    // 收盘价
    private String closing_rice;
    // 最低价
    private String minimum_price;
    // 交易量(股)
    private String volume;
    // 交易金额(元)
    private String amount;
    //stock_id
    private String stock_id;
    //升跌
    private Float rise_and_fall;

    public Float getRise_and_fall() {
        return rise_and_fall;
    }

    public void setRise_and_fall(Float rise_and_fall) {
        this.rise_and_fall = rise_and_fall;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOpening_price() {
        return opening_price;
    }

    public void setOpening_price(String opening_price) {
        this.opening_price = opening_price;
    }

    public String getMaximum_price() {
        return maximum_price;
    }

    public void setMaximum_price(String maximum_price) {
        this.maximum_price = maximum_price;
    }

    public String getClosing_rice() {
        return closing_rice;
    }

    public void setClosing_rice(String closing_rice) {
        this.closing_rice = closing_rice;
    }

    public String getMinimum_price() {
        return minimum_price;
    }

    public void setMinimum_price(String minimum_price) {
        this.minimum_price = minimum_price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

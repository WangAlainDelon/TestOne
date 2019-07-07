package com.test.domain;

public class Stock {
    /**
     * 股票的详细信息
     */
    //id
    private Long id;
    //股票代码
    private String stock_code;
    //股票名称
    private String stock_name;
    //最新评级
    private String latest_ratings;
    // 目标价
    private String target_price;

    // 平均涨幅
    private String average_increase;
    // 行业
    private String industry;
    // 最新价
    private String latest_rice;
    // 涨跌幅
    private String chg;

    public String getLatest_rice() {
        return latest_rice;
    }

    public void setLatest_rice(String latest_rice) {
        this.latest_rice = latest_rice;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
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

    public String getLatest_ratings() {
        return latest_ratings;
    }

    public void setLatest_ratings(String latest_ratings) {
        this.latest_ratings = latest_ratings;
    }

    public String getTarget_price() {
        return target_price;
    }

    public void setTarget_price(String target_price) {
        this.target_price = target_price;
    }

    public String getAverage_increase() {
        return average_increase;
    }

    public void setAverage_increase(String average_increase) {
        this.average_increase = average_increase;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}

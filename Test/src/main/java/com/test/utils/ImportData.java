package com.test.utils;

import com.test.domain.Stock;
import com.test.domain.StockHistory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 爬虫抓取的数据采用JDBC的方式将数据导入数据库
 */
public class ImportData {
    public void saveStock(Stock stock) {
        DBHelper db = new DBHelper();
        String sql = "insert into stock(stock_code,stock_name,latest_ratings,target_price,average_increase,industry,latest_rice,chg)  values(?,?,?,?,?,?,?,?)";
        List<String> valuelist = new ArrayList<String>();
        valuelist.add(stock.getStock_code());
        valuelist.add(stock.getStock_name());
        valuelist.add(stock.getLatest_ratings());
        valuelist.add(stock.getTarget_price());
        valuelist.add(stock.getAverage_increase());
        valuelist.add(stock.getIndustry());
        valuelist.add(stock.getLatest_rice());
        valuelist.add(stock.getChg());
        int executeUpdate = db.executeUpdate(sql, valuelist);
        if (stock == null) {
            db.close();
        }
    }

    public void saveStockHistory(StockHistory stock) {
        DBHelper db = new DBHelper();
        String sql = " insert into stockhistory(stock_code,stock_name,date,opening_price,maximum_price,closing_rice,minimum_price,volume,amount,stock_id,rise_and_fall) values(?,?,?,?,?,?,?,?,?,?,?)";
        List valuelist = new ArrayList();
        valuelist.add(stock.getStock_code());
        valuelist.add(stock.getStock_name());
        valuelist.add(stock.getDate());
        valuelist.add(stock.getOpening_price());
        valuelist.add(stock.getMaximum_price());
        valuelist.add(stock.getClosing_rice());
        valuelist.add(stock.getMinimum_price());
        valuelist.add(stock.getVolume());
        valuelist.add(stock.getAmount());
        valuelist.add(stock.getStock_id());
        valuelist.add(stock.getRise_and_fall());

        int executeUpdate = db.executeUpdate(sql, valuelist);
        if (stock == null) {
            db.close();
        }
    }

    public Stock selectByCode(String code) {
        DBHelper db = new DBHelper();
        String sql = " SELECT * FROM stock WHERE stock_code=?";
        List valuelist = new ArrayList();
        valuelist.add(code);
        ResultSet resultSet = db.query(sql, valuelist);
        Stock re = new Stock();
        try {
            while (resultSet.next()) {
                re.setStock_code(resultSet.getString("stock_code"));
                re.setStock_name(resultSet.getString("stock_name"));
                re.setLatest_rice(resultSet.getString("latest_rice"));
                re.setIndustry(resultSet.getString("industry"));
                re.setChg(resultSet.getString("chg"));
                re.setAverage_increase(resultSet.getString("average_increase"));
                re.setId(resultSet.getLong("id"));
                re.setTarget_price(resultSet.getString("target_price"));
                re.setLatest_ratings(resultSet.getString("latest_ratings"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public int updateStock(Stock stock) {
        DBHelper db = new DBHelper();
        String sql = " UPDATE stock SET stock_name=?, latest_rice=?, industry=?, chg=?, average_increase=?,target_price=?,latest_ratings=? WHERE id=?";
        List valuelist = new ArrayList();
        valuelist.add(stock.getStock_name());
        valuelist.add(stock.getLatest_rice());
        valuelist.add(stock.getIndustry());
        valuelist.add(stock.getChg());
        valuelist.add(stock.getAverage_increase());
        valuelist.add(stock.getTarget_price());
        valuelist.add(stock.getLatest_ratings());
        valuelist.add(stock.getId());
        int i = db.executeUpdate(sql, valuelist);
        return i;
    }

    public StockHistory selectByCodeAndDate(String code,Date date) {
        DBHelper db = new DBHelper();
        //String sql = "SELECT * FROM  stockhistory  a WHERE   stock_code = ? ORDER BY a.date DESC LIMIT 1 ";
        String sql = "SELECT * FROM  stockhistory  a WHERE   stock_code = ? and date = ? ";
        List valuelist = new ArrayList();
        valuelist.add(code);
        valuelist.add(date);
        ResultSet resultSet = db.query(sql, valuelist);
        StockHistory stockHistory = new StockHistory();
        try {
            while (resultSet.next()) {
                stockHistory.setStock_code(resultSet.getString("stock_code"));
                stockHistory.setDate(resultSet.getDate("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockHistory;
    }

    public StockHistory selectHistoryByCode(String code) {
        DBHelper db = new DBHelper();
        String sql = "SELECT * FROM  stockhistory  a WHERE   stock_code = ? ORDER BY a.date DESC LIMIT 1 ";
        List valuelist = new ArrayList();
        valuelist.add(code);
        ResultSet resultSet = db.query(sql, valuelist);
        StockHistory stockHistory = new StockHistory();
        try {
            while (resultSet.next()) {
                stockHistory.setStock_code(resultSet.getString("stock_code"));
                stockHistory.setDate(resultSet.getDate("date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockHistory;
    }

}

package com.test.utils;

import java.sql.*;
import java.util.List;

/**
 * JDBC连接数据库的工具类
 */
public class DBHelper {

    private static final String Driver_Class = "com.mysql.jdbc.Driver";
    private static final String Driver_Url = "jdbc:mysql://localhost:3306/shares_reptile?characterEncoding=UTF-8";
    private static final String UserNmme = "root";
    private static final String Password = "133309";

    private static Connection conn = null;
    private PreparedStatement predtatement = null;
    private ResultSet rst = null;

    public DBHelper() {
        conn = getConnection();
    }

    public Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName(Driver_Class);
                conn = DriverManager.getConnection(Driver_Url, UserNmme, Password);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return conn;
    }

    public void close() {
        try {
            if (predtatement != null) {
                this.predtatement.close();
            }
            if (rst != null) {
                this.rst.close();
            }
            if (conn != null) {
                conn.close();
            }
            System.out.println("Close connection success.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String sql, List<String> sqlValues) {
        int result = -1;
        try {
            predtatement = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(predtatement, sqlValues);
            }
            result = predtatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int executeUpdateHistory(String sql, List<String> sqlValues) {
        int result = -1;
        try {
            predtatement = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                for (int i = 0; i < sqlValues.size(); i++) {

                    if (i == sqlValues.size() - 1) {
                        try {
                            System.out.println(sqlValues.get(i));
                            predtatement.setLong(i + 1, Long.parseLong(sqlValues.get(i)));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            System.out.println(sqlValues.get(i));
                            predtatement.setObject(i + 1, sqlValues.get(i));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            result = predtatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet query(String sql, List<String> sqlValues) {
        ResultSet resultSet = null;
        try {
            predtatement = conn.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(predtatement, sqlValues);
            }
            resultSet = predtatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private void setSqlValues(PreparedStatement pst, List<String> sqlValues) {
        for (int i = 0; i < sqlValues.size(); i++) {
            try {
                pst.setObject(i + 1, sqlValues.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

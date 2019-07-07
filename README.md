# Spring+SpringMVC+Mybatis+Maven+MySql+WebMagic框架搭建爬虫和数据分页展示项目   #
**1. 工程运行环境**  
JDK:1.8  
TOMCAT9
Windows10/IDEA 
**1主要功能
  1.使用WebMagic多线程爬虫框架从指定的网站抽取网站上所有的股票数据，和所有的历史数据。
  2.不需要进行代理IP配置，在程序内部自动爬取了免费的代理ip并且随机更换自己的IP地址
  3.启动工程后，内部的爬虫程序在每天网上十一点的时候定时更新数据，不会保存重复的数据
  4.启动工程后，会自动加载显示所有的股票数据，在股票代码输入框内，输入股票的代码点击“历史”按钮会分页显示该支股票的所有历史信息。
  5.输入股票的代码，天数，涨幅点击查询会看到改只股票在条件天数内涨跌幅大于条件涨跌幅的次数。
  6。初次部署可修改爬虫启动时间，在com.test.component.CrawlerTask中修改
  
 
**2打开方式:打开IDEA，CVS->Checkout from Version Control->Git 输入Git仓库地址 从git上拉取项目，导入IDEA即可。

**3. 后台使用到的框架**  
Spring+SpringMVC+Mybatis+Maven+WebMagic 
数据库使用:Mysql  
后台分页使用：PageHelp(与Mybatis一起使用)  
**4. 前台使用**  
框架：Bootstarp+Html  
**5. 数据源配置**  
数据库配置修改  
请修改Test\src\main\resources\jdbc.properties和com.test.utils.DBHelper中的内容，将jdbc_url、jdbc_username、jdbc_password修改成自己的，示例如下：     
`<jdbc_driverClassName>com.mysql.jdbc.Driver</jdbc_driverClassName>`      
`<jdbc_url>jdbc:mysql://localhost:3306/learning</jdbc_url>`  
`<jdbc_username>root</jdbc_username>`  
`<jdbc_password>christmas</jdbc_password>`   
      
**6. 建表语句**   
    SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `stock_code` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stock_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `latest_ratings` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `target_price` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `average_increase` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `industry` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `latest_rice` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `chg` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2135 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for stockhistory
-- ----------------------------
DROP TABLE IF EXISTS `stockhistory`;
CREATE TABLE `stockhistory`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stock_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `date` date DEFAULT NULL,
  `opening_price` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `maximum_price` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `closing_rice` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `minimum_price` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `volume` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `amount` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `stock_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rise_and_fall` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38607 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

   

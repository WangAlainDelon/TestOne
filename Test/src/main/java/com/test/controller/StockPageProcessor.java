package com.test.controller;

import com.test.domain.Stock;
import com.test.domain.StockHistory;
import com.test.utils.HttpRequest;
import com.test.utils.ImportData;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class StockPageProcessor implements PageProcessor {

    ImportData importData = new ImportData();

    //睡眠一秒,重试三次
    private final static Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .addHeader("Connection", "keep-alive")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");

    public void process(Page page) {
        System.out.println("进入process");
        //判断链接是股票的详情还是股票的历史数据
        Selectable url = page.getUrl();
        if (url.toString().startsWith("http://vip.stock.finance")) {
            //爬取股票的详细信息，并封装下一个爬取url
            Html html = page.getHtml();
            Selectable selectable = html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody//td[1]/a/text()");
            List<String> list = selectable.all();
            Stock stock = new Stock();
            /****************************/
            for (int i = 1; i < list.size(); i++) {
                //股票代码
                stock.setStock_code(list.get(i));
                int temp = i + 1;
                //股票名字
                stock.setStock_name(html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[2]/a/span/text()").get());
                //最新评级
                stock.setLatest_ratings(html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[3]/text()").get());
                // 目标价
                stock.setTarget_price(html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[4]/text()").get());
                // 平均涨幅
                stock.setAverage_increase(html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[7]/text()").get());
                // 行业
                stock.setIndustry(html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[9]/text()").get());
                /**最后这两个数据为ajax加载,再采用xpath规则已经获取不到数据了，所以需要重新发送请求*/
                //  http://hq.sinajs.cn/rn=时间戳&list=s_sz000039
                //获取元素的属性
                String id = html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr[" + temp + "]/td[2]/a/span/@id").get();
                String[] split = id.split("_");

                if (split != null) {
                    long time = System.currentTimeMillis();
                    String url3 = "http://hq.sinajs.cn/rn=" + time + "&list=s_" + split[1];
                    String result = HttpRequest.sendGet("http://hq.sinajs.cn/rn=" + time + "&list=s_" + split[1]);
                    String[] data = result.split(",");
                    if (!result.contains(",")) {
                        stock.setLatest_rice(null);
                        stock.setChg(null);
                    } else {
                        String[] date = result.split(",");
                        if (date != null) {
                            // 最新价
                            stock.setLatest_rice(date[1]);
                            // 涨跌幅
                            stock.setChg(date[2]);
                        }
                    }
                }
                //保存到数据库
                System.out.println(stock.getStock_code() + ":" + stock.getAverage_increase()
                        + ":" + stock.getChg() + ":" + stock.getIndustry() + ":" + stock.getLatest_ratings() + ":"
                        + stock.getLatest_rice() + ":" + stock.getTarget_price() + ":" + stock.getStock_name());
                //看看数据库中是否存在这条数据如果存在就更新，而不是插入
                Stock re = importData.selectByCode(stock.getStock_code());
                if (re.getId() != null) {
                    importData.updateStock(re);
                } else {
                    importData.saveStock(stock);
                }

                //添加新连接到爬取列表 http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/600660.phtml?year=2009&jidu=3
                /*for (int year = 2009; year < 2019; year++) {
                    for (int quarter = 1; quarter < 4; quarter++) {
                        String netxurl = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + stock.getStock_code() + ".phtml?year=" + year + "&jidu=" + quarter + "";
                        page.addTargetRequest(netxurl);
                    }
                }*/
                //换一个网站拉数据  http://www.aigaogao.com/tools/history.html?s=000717
                String netxurl = "http://www.aigaogao.com/tools/history.html?s=" + stock.getStock_code();
                page.addTargetRequest(netxurl);
            }
        }
        //爬取历史数据
        else if (url.toString().startsWith("http://www.aigaogao.com")) {
            System.out.println("爬取历史数据2");
            StockHistory stockHistory = new StockHistory();
            Html html = page.getHtml();
            Selectable selectable = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody//td[1]/a/text()");
            List<String> list = selectable.all();
            String wangzhanmaxtimestr = list.get(0);
            int temp = list.size();
            //首先拿到这个股票的代码
            String s = html.xpath("//*[@id=\"divContent\"]/center/table/tbody/tr[2]/td/text()").get();
            String stock_code = Pattern.compile("[^0-9]").matcher(s).replaceAll("");
            stockHistory.setStock_code(stock_code);
            String[] split = s.split(" ");
            stockHistory.setStock_name(split[0]);
            Stock stock = importData.selectByCode(stock_code);
            stockHistory.setStock_id(stock.getId().toString());
            //其次查询数据库如果数据库中的最大日期小于网站最大日期则抽取数据
            StockHistory stockHistoryreMax = importData.selectHistoryByCode(stockHistory.getStock_code());
            Date maxdate = new Date();
            Date wangzhanmax = new Date();
            //首次爬取
            if (stockHistoryreMax.getStock_code() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String max = simpleDateFormat.format(stockHistoryreMax.getDate());
                try {
                    maxdate = simpleDateFormat.parse(max);
                    wangzhanmax = simpleDateFormat.parse(wangzhanmaxtimestr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (maxdate.compareTo(wangzhanmax) <= 0) {
                    //从数据库最大日期往上开始抽数据
                    int index = 0;
                    if (list.contains(max)) {
                        index = list.indexOf(max);
                    }
                    for (int i = list.size() - 1; i > index; i--) {
                        temp--;
                        String datestr = list.get(i);
                        if (datestr != null) {
                            Date date = new Date(datestr);
                            stockHistory.setDate(date);
                            String open = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[2]/text()").get();
                            stockHistory.setOpening_price(open);
                            String high = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[3]/text()").get();
                            stockHistory.setMaximum_price(high);
                            String low = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[4]/text()").get();
                            stockHistory.setMinimum_price(low);
                            String close = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[5]/text()").get();
                            stockHistory.setClosing_rice(close);
                            String num = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[6]/text()").get();
                            stockHistory.setVolume(num);
                            String totalprice = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[7]/text()").get();
                            stockHistory.setAmount(totalprice);
                            String rise_and_fall = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[9]/span/text()").get();
                            if (rise_and_fall.contains("%")) {
                                String[] split1 = rise_and_fall.split("%");
                                stockHistory.setRise_and_fall(Float.parseFloat(split1[0].trim()));
                            } else {
                                stockHistory.setRise_and_fall(0f);
                            }
                            importData.saveStockHistory(stockHistory);
                        }
                    }
                }
            } else {
                for (int i = list.size() - 1; i > 0; i--) {
                    temp--;
                    String datestr = list.get(i);
                    if (datestr != null) {
                        Date date = new Date(datestr);
                        stockHistory.setDate(date);
                        String open = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[2]/text()").get();
                        stockHistory.setOpening_price(open);
                        String high = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[3]/text()").get();
                        stockHistory.setMaximum_price(high);
                        String low = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[4]/text()").get();
                        stockHistory.setMinimum_price(low);
                        String close = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[5]/text()").get();
                        stockHistory.setClosing_rice(close);
                        String num = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[6]/text()").get();
                        stockHistory.setVolume(num);
                        String totalprice = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[7]/text()").get();
                        stockHistory.setAmount(totalprice);
                        String rise_and_fall = html.xpath("//*[@id=\"ctl16_contentdiv\"]/table/tbody/tr[" + temp + "]/td[9]/span/text()").get();
                        if (rise_and_fall.contains("%")) {
                            String[] split1 = rise_and_fall.split("%");
                            stockHistory.setRise_and_fall(Float.parseFloat(split1[0].trim()));
                        } else {
                            stockHistory.setRise_and_fall(0f);
                        }
                        importData.saveStockHistory(stockHistory);
                    }
                }
            }
        }


        //爬取历史数据
        else if (url.toString().startsWith("http://money.finance.sina")) {
            StockHistory stockHistory = new StockHistory();
            Html html = page.getHtml();
            Selectable selectable = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody//td[1]/div/a/text()");
            List<String> list = selectable.all();
            int temp = 1;
            for (int i = 0; i < list.size(); i++) {
                temp++;
                String datestr = list.get(i);
                if (datestr != null) {
                    Date date = new Date(datestr);
                   /* SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = sdf.parse(datestr);*/

                    stockHistory.setDate(date);
                }
                String open = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[2]/div/text()").get();
                stockHistory.setOpening_price(open);
                String high = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[3]/div/text()").get();
                stockHistory.setMaximum_price(high);
                String close = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[4]/div/text()").get();
                stockHistory.setClosing_rice(close);
                String low = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[5]/div/text()").get();
                stockHistory.setMinimum_price(low);
                String num = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[6]/div/text()").get();
                stockHistory.setVolume(num);
                String totalprice = html.xpath("//*[@id=\"FundHoldSharesTable\"]/tbody/tr[" + temp + "]/td[7]/div/text()").get();
                stockHistory.setAmount(totalprice);
                //保存到数据库
                importData.saveStockHistory(stockHistory);
            }
        }

    }

    public Site getSite() {
        return site;
    }
}

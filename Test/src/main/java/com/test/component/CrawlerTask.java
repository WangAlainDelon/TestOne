package com.test.component;

import com.test.controller.StockPageProcessor;
import com.test.utils.HttpConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ListIterator;

@Component
public class CrawlerTask {

    /**
     * 每天晚上十一点，启动爬虫更新数据
     */
    @Scheduled(cron = "0 0 23 * * ? ") //30秒执行一次
    @Async
    public void grabbingData() {
        System.out.println("执行爬虫任务");
        try {
            //爬取股票的信息，
            //链接：http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=95&sr_p=-1
            for (long i = 1; i <= 1; i++) {
                Spider.create(new StockPageProcessor())
                        .addUrl("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=" + i + "&sr_p=-1")
                        .thread(5)
                        .setDownloader(Downloader.newIpDownloader())
                        .run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 定义一个自动任务爬取免费的IP
     */
    @Scheduled(cron = "*/50 * * * * ?")
    @Async
    void update() {
        System.out.println("更新ip");
        //剔除无效IP,使用ListIterator遍历修改，防止报并发异常错误,多线程环境下加锁
        synchronized (HttpConstants.ipList) {
            if (HttpConstants.ipList != null) {
                for (ListIterator<String> iter = HttpConstants.ipList.listIterator(); iter.hasNext(); ) {
                    String ip = iter.next();
                    if (!ifUseless(ip)) {
                        iter.remove();
                    }
                }
            }
        }
    }

    @Scheduled(cron = "*/50 * * * * ?")
    @Async
    void ips() {
        System.out.println("爬取ip");
        String string = null;
        try {
            Connection conn = Jsoup.connect("https://www.xicidaili.com/nn").timeout(3000);
            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.header("Accept-Encoding", "gzip, deflate, sdch");
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            Document document = conn.get();
            Elements tags = document.select("#ip_list > tbody > tr");
            for (Element element : tags) {
                //取得ip地址节点
                Elements tdChilds = element.select("tr > td:nth-child(2)");
                //取得端口号节点
                Elements tcpd = element.select("tr > td:nth-child(3)");
                if (StringUtils.isNotBlank(tdChilds.text()) && StringUtils.isNotBlank(tcpd.text())) {
                    string = tdChilds.text() + ":" + tcpd.text();
                    if (!ifUseless(string)) {
                        System.out.println(string);
                        HttpConstants.ipList.add(string);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //无效的ip 返回true 有效的ip返回false
    boolean ifUseless(String ip) {
        String[] split = ip.split(":");
        URL url = null;
        try {
            url = new URL("http://www.baidu.com");
            InetSocketAddress addr = new InetSocketAddress(split[0], Integer.parseInt(split[1]));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection(proxy);
                conn.setConnectTimeout(2000);
                in = conn.getInputStream();
            } catch (Exception e) {
                return true;
            }
            String s = IOUtils.toString(in);
            if (s.indexOf("baidu") > 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}


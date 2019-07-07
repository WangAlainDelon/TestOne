package com.test.component;

import com.test.controller.StockPageProcessor;
import us.codecraft.webmagic.Spider;

public class CrawlerThread extends Thread {
    @Override
    public void run() {
        try {
            //爬取股票的信息，
            //链接：http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=95&sr_p=-1
            for (int i = 1; i <= 95; i++) {
                Spider.create(new StockPageProcessor())
                        .addUrl("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=" + i + "&sr_p=-1")
                        .thread(3)
                        .setDownloader(Downloader.newIpDownloader())
                        .run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

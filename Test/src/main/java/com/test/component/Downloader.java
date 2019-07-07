package com.test.component;

import com.test.utils.HttpConstants;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.Random;

@Component
public class Downloader {
    public static HttpClientDownloader newIpDownloader() {
        HttpClientDownloader downloader = new HttpClientDownloader() {
            @Override
            protected void onError(Request request) {
                if (HttpConstants.ipList.size() >= 0) {
                    System.out.println("----------------设置代理ip-----------------");
                    Random random = new Random();
                    String s = HttpConstants.ipList.get(new Random().nextInt(HttpConstants.ipList.size() - 1));
                    String[] split = s.split(":");
                    setProxyProvider(SimpleProxyProvider.from(new Proxy(split[0], Integer.parseInt(split[1]))));
                }
            }
        };
        return downloader;
    }
}

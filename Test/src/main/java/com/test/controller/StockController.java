package com.test.controller;

import com.test.component.CrawlerThread;
import com.test.domain.ConditionalResults;
import com.test.domain.Stock;
import com.test.domain.StockHistory;
import com.test.domain.User;
import com.test.mapper.HttpStockHistoryMapper;
import com.test.service.HttpStockHistoryService;
import com.test.service.HttpStockService;
import com.test.utils.HttpConstants;
import com.test.utils.JsonDateValueProcessor;
import com.test.utils.PagedResult;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StockController {
    @Autowired
    private HttpStockService httpStockService;
    @Autowired
    private HttpStockHistoryService httpStockHistoryService;

    /***
     * 启动项目
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
       /* CrawlerThread crawlerThread = new CrawlerThread();
        crawlerThread.start();*/
        return "spider";
    }

    /***
     * 加载股票的最新信息
     * @param pageNumber
     * @param pageSize
     * @param code
     * @return
     */
    @RequestMapping(value = "/stock", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String stock(Integer pageNumber, Integer pageSize, String code) {
        try {
            PagedResult<Stock> pageResult = httpStockService.queryByPage(code, pageNumber, pageSize);
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }

    /***
     * 查询股票的历史信息
     * @param pageNumber
     * @param pageSize
     * @param code
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String history(Integer pageNumber, Integer pageSize, String code) {
        try {
            PagedResult<StockHistory> pageResult = httpStockHistoryService.queryByPage(code, pageNumber, pageSize);
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }

    /**
     * 条件查询
     *
     * @param code
     * @param day
     * @param chg
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String search(String code,Integer day,Float chg) {
        try {
            PagedResult<ConditionalResults> pageResult = new PagedResult<>();
            ConditionalResults conditionalResults = new ConditionalResults();
            List<Stock> stocks = httpStockService.queryStockByCode(code);
            if (stocks.size() > 0) {
                conditionalResults.setStock_code(stocks.get(0).getStock_code());
                conditionalResults.setStock_name(stocks.get(0).getStock_name());
                conditionalResults.setDay(day);
                conditionalResults.setChg(chg);
                int times = httpStockHistoryService.queryByCondition(code, day, chg);
                conditionalResults.setTimes(times);
                List<ConditionalResults> conditionalResultsList = new ArrayList<>();
                conditionalResultsList.add(conditionalResults);
                pageResult.setDataList(conditionalResultsList);
            }
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }

    /**
     * 返回成功
     *
     * @param obj 输出对象
     * @return 输出成功的JSON格式数据
     */
    public String responseSuccess(Object obj) {
        JSONObject jsonObj = null;
        if (obj != null) {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
            jsonObj = JSONObject.fromObject(obj, jsonConfig);
            jsonObj.element(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, false);
            jsonObj.element(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, "");
        }
        return jsonObj.toString();
    }

    /**
     * 返回失败
     *
     * @param errorMsg 错误信息
     * @return 输出失败的JSON格式数据
     */
    public String responseFail(String errorMsg) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(HttpConstants.RESPONSE_RESULT_FLAG_ISERROR, true);
        jsonObj.put(HttpConstants.SERVICE_RESPONSE_RESULT_MSG, errorMsg);
        return jsonObj.toString();
    }

}

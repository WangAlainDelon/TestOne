package com.test.service;

import com.test.domain.StockHistory;
import com.test.utils.PagedResult;
import org.springframework.stereotype.Component;

@Component
public interface HttpStockHistoryService {
    /**
     * 分页查询历史数据
     * @param code
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PagedResult<StockHistory> queryByPage(String code, Integer pageNumber, Integer pageSize);

    /**
     * 次数
     * @param day
     * @param code
     * @param chg
     * @return
     */
    int queryByCondition(String code,Integer day,Float chg);
}

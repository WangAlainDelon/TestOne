package com.test.service;

import com.test.domain.Stock;
import com.test.domain.StockHistory;
import com.test.utils.PagedResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HttpStockService {
    /**
     * 分页查询股票信息
     *
     * @param code
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PagedResult<Stock> queryByPage(String code, Integer pageNumber, Integer pageSize);

    /**
     * 根据股票代码查询股票
     *
     * @param code
     * @return
     */
    List<Stock> queryStockByCode(String code);
}

package com.test.mapper;

import com.test.domain.Stock;
import com.test.domain.StockHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HttpStockHistoryMapper {
    /**
     * 根据股票的代码查询股票的历史数据
     *
     * @param code
     * @return
     */
    List<StockHistory> selectByCode(@Param("code") String code);

    /**
     * 次数
     * @param code
     * @param da
     * @param chg
     * @return
     */
    int queryByCondition(String code,Integer day,Float chg);
}

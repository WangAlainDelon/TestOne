package com.test.mapper;

import com.test.domain.Stock;
import com.test.domain.StockHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HttpStockMapper {
    /**
     * code参数为空时，分页查询股票的最新信息
     *
     * @param code
     * @return
     */
    List<Stock> selectByCode(@Param("code") String code);
}

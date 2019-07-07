package com.test.service.imp;

import com.github.pagehelper.PageHelper;
import com.test.domain.Stock;
import com.test.domain.StockHistory;
import com.test.mapper.HttpStockHistoryMapper;
import com.test.mapper.HttpStockMapper;
import com.test.service.HttpStockService;
import com.test.utils.BeanUtil;
import com.test.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpStockServiceImp implements HttpStockService {

    @Autowired
    private HttpStockMapper httpStockMapper;
    @Override
    public PagedResult<Stock> queryByPage(String code, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(pageNo, pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
        return BeanUtil.toPagedResult(httpStockMapper.selectByCode(code));
    }

    @Override
    public List<Stock> queryStockByCode(String code) {
        return httpStockMapper.selectByCode(code);
    }
}

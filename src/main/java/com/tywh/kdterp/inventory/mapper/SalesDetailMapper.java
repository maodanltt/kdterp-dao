package com.tywh.kdterp.inventory.mapper;

import com.tywh.kdterp.pojo.QueryCondition;
import com.tywh.kdterp.pojo.SalesDetail;

import java.util.List;

public interface SalesDetailMapper {

    List<SalesDetail> querySalesDetailList(QueryCondition queryCondition) throws Exception;
}

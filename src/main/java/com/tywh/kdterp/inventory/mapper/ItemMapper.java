package com.tywh.kdterp.inventory.mapper;

import com.tywh.kdterp.pojo.Condition;
import com.tywh.kdterp.pojo.Item;

import java.util.List;
import java.util.Map;

public interface ItemMapper {

    List<Item> queryItemList(Condition condition) throws Exception;

    Integer queryZxscs(Condition condition) throws Exception;

//  Map<String, Integer> queryKucun(Condition conditon) throws Exception;
    List<Item> queryKucun(Condition condition) throws Exception;

    Item queryItemByName(String name) throws Exception;

}

package com.tywh.kdterp.inventory.dao;

import com.tywh.kdterp.pojo.Condition;
import com.tywh.kdterp.pojo.Item;

import java.util.List;
import java.util.Map;

public interface ItemDao {


    List<Item> queryItemList(Condition condition) throws Exception;

//    Map<String, Integer> queryXscs(Condition condition);

    Integer queryZxscs(Condition condition) throws Exception;

    Map<String, Integer> queryKucun() throws Exception;


}

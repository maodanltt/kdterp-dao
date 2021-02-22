package com.tywh.kdterp.inventory.mapper;

import com.tywh.kdterp.pojo.Condition;
import com.tywh.kdterp.pojo.Item;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext.xml");
        SqlSessionFactory sqlSessionFactory  = (SqlSessionFactory)ac.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Condition condition = new Condition();
        condition.setEnddate("2021-02");
        condition.setStartdate("2021-02");
        try {
            List<Item> list = sqlSession.selectList("queryItemList", Condition.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("ac");
    }
}

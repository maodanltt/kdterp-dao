package com.tywh.kdterp.inventory.dao.impl;

import com.tywh.kdterp.inventory.dao.ItemDao;
import com.tywh.kdterp.pojo.Condition;
import com.tywh.kdterp.pojo.Item;
import com.tywh.kdterp.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDaoImpl implements ItemDao {
    @Override
    public List<Item> queryItemList(Condition condition) throws Exception{

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Item> itemList = new ArrayList<>();
        List<String> paramList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("select a.shum,a.gjdj,a.tsfljc,b.xscs from FxsgkView a left join ");
            sql.append("(select shum,dj,tsfljc, sum(cs) as xscs from (select fhdbh,sxh,shum,tsfljc, dj,kwbh,cs from xsmxview ");
            sql.append("union select fhdbh,sxh,shum,tsfljc,dj,kwbh,cs from XsmxnView) xsmx where fhdbh in (select fhdbh ");
            sql.append("from (select fhdbh,convert(varchar(7),dateadd(month,0,shrq),120) shrq,khbh,fhzt,xsbmmc from xsdview ");
            sql.append("union select fhdbh,convert(varchar(7),dateadd(month,0,shrq),120) shrq,khbh,fhzt,xsbmmc from ");
            sql.append("xsdnview) xsd where fhzt in(?,?) and  shrq >= ? and shrq <= ? ");
            paramList.add("待发");
            paramList.add("已发");
            paramList.add(condition.getStartdate());
            paramList.add(condition.getEnddate());
            if (condition.getXsbmmc() != "") {
                sql.append("and xsbmmc like ? ");
                paramList.add("%" + condition.getXsbmmc() + "%");
            }
            sql.append("and khbh <> ?) group by shum,dj,tsfljc) b on a.shum = b.shum and a.tsfljc = b.tsfljc ");
            sql.append("and a.gjdj = b.dj where sxh not like ? and a.tsfljc not like ? and a.tsfljc not like ? and a.tsfljc not like ? and a.tsfljc not like ? ");
            paramList.add("2000000747");
            paramList.add("W%");
            paramList.add("%联考%");
            paramList.add("日历%");
            paramList.add("赠品%");
            paramList.add("宣传品%");
            if (condition.getBjbmmc() != "") {
                sql.append("and bmmc like ? ");
                paramList.add("%" + condition.getBjbmmc() + "%");
            }

            if (condition.getTsfljc() != "") {
                sql.append("and a.tsfljc like ? ");
                paramList.add("%" + condition.getTsfljc() + "%");
            }
            sql.append("group by a.shum,a.gjdj, a.tsfljc,b.xscs order by b.xscs desc");
            String sqlString = sql.toString();
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sqlString);
            for (int i=0; i<paramList.size(); i++) {
                ps.setString(i+1,paramList.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                String shum = rs.getString("shum");
                double gjdj = rs.getDouble("gjdj");
                String tsfljc = rs.getString("tsfljc");
                Integer xscs = rs.getInt("xscs");
                item.setShum(shum);
                item.setGjdj(gjdj);
                item.setTsfljc(tsfljc);
                item.setXscs(xscs);
                itemList.add(item);
            }
        } finally {
            DbUtil.close(null, ps, rs);
        }

        return itemList;
    }

    @Override
    public Integer queryZxscs(Condition condition) throws Exception{

        List<String> paramList = new ArrayList<>();

        StringBuilder sql = new StringBuilder("select sum(cs) as zxscs from (select fhdbh,sxh,shum,tsfljc, dj,kwbh,cs from xsmxview ");
        sql.append("union select fhdbh,sxh,shum,tsfljc,dj,kwbh,cs from XsmxnView) t2 where fhdbh in (select fhdbh from ");
        sql.append("(select fhdbh,convert(varchar(7),dateadd(month,0,shrq),120) shrq,khbh,fhzt,xsbmmc from xsdview union ");
        sql.append("select fhdbh,convert(varchar(7),dateadd(month,0,shrq),120) shrq,khbh,fhzt,xsbmmc from xsdnview) t1 where ");
        sql.append("fhzt in(?,?) and  shrq >= ? and shrq <= ? ");
        paramList.add("待发");
        paramList.add("已发");
        paramList.add(condition.getStartdate());
        paramList.add(condition.getEnddate());
        if (condition.getXsbmmc() != "") {
            sql.append("and xsbmmc like ? ");
            paramList.add("%" + condition.getXsbmmc() + "%");
        }
        sql.append("and khbh <> ?) and tsfljc not like ? and tsfljc not like ? and tsfljc not like ? and tsfljc not like ? and sxh not like ? ");
        paramList.add("2000000747");
        paramList.add("W%");
        paramList.add("%联考%");
        paramList.add("日历%");
        paramList.add("赠品%");
        paramList.add("宣传品%");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, Integer> xscsMap = new HashMap<>();
        Integer zxscs = null;
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i=0; i<paramList.size(); i++) {
                ps.setString(i+1,paramList.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                zxscs = rs.getInt("zxscs");
            }
        } finally {
            DbUtil.close(null, ps, rs);
        }
        return zxscs;
    }

    @Override
    public Map<String, Integer> queryKucun() throws Exception{

        StringBuilder sql = new StringBuilder("select shum,tsfljc,dj,kjqj,SUM(qckc) qckc,SUM(qmkc) qmkc from ( ");
        sql.append("SELECT shum,tsfljc,dj,qckc,qmkc,kjqj,sxh,kwbh FROM kcknView union ");
        sql.append("select shum,tsfljc,dj,qckc,qmkc,convert(varchar(7),dateadd(month,0,getdate()),120) ");
        sql.append("kjqj,sxh,kwbh from KucunView) t1 ");
        sql.append("where KWBH = ? AND kjqj >= ? and tsfljc not like ? and tsfljc not like ? and tsfljc not like ? and tsfljc not like ? ");
        sql.append("and sxh not like ? group by shum,tsfljc,dj,kjqj");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, Integer> kucunMap = new HashMap<>();
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1,"00HG");
            ps.setString(2,"2020-01");
            ps.setString(3,"%联考%");
            ps.setString(4,"日历%");
            ps.setString(5,"赠品%");
            ps.setString(6,"宣传品%");
            ps.setString(7,"W%");
            rs = ps.executeQuery();
            while (rs.next()) {
                String shum = rs.getString("shum");
                String tsfljc = rs.getString("tsfljc");
                double dj = rs.getDouble("dj");
                String kjqj = rs.getString("kjqj");
                Integer qckc = rs.getInt("qckc");
                Integer qmkc = rs.getInt("qmkc");
                String qckcKey = shum + "-" + dj + "-" + tsfljc + "-" + kjqj + "-qckc";
                String qmkcKey = shum + "-" + dj + "-" + tsfljc + "-" + kjqj + "-qmkc";
                kucunMap.put(qckcKey,qckc);
                kucunMap.put(qmkcKey,qmkc);
            }
        } finally {
            DbUtil.close(null, ps, rs);
        }
        return kucunMap;
    }

}

package com.factory.service;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.avaje.ebean.EbeanServer;
import com.factory.entity.IpInfo;
import com.factory.util.EbeanFactoryUtil;

/**
 * @author mike_yi
 * @description ip池更新处理
 * @since 
 */
public class IpInforService {
  public static final String ipUrl ="http://dps.kuaidaili.com/api/getdps/?orderid=931029953411924&num=50&ut=1&format=json&sep=1";
  public void excuteUpdate() {
	  EbeanServer ebean = EbeanFactoryUtil.getEbean();
	  //先清除上个批次信息
	  try {
		String ipInfo=Jsoup.connect(ipUrl).ignoreContentType(true).get().body().text();
		JSONArray ipJsonArr=JSON.parseObject(ipInfo).getJSONObject("data").getJSONArray("proxy_list");
		for(Object ip:ipJsonArr) {
			IpInfo ipx= new IpInfo();
			String ipfo=ip.toString();
			ipx.setAddress(ipfo.split(":")[0]);
			ipx.setPort(Integer.parseInt(ipfo.split(":")[1]));
			ipx.setUsername("kim");
			ipx.setPassword("avfnxus8");
			ipx.setStatu(0);
			ebean.save(ipx);
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
  public static void main(String[] args){
  }
}

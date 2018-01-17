package com.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.avaje.ebean.EbeanServer;
import com.factory.entity.IpInfo;
import com.factory.util.EbeanFactoryUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class KuaidaiProcess implements PageProcessor{
    Site site = Site.me().setSleepTime(1000).setTimeOut(5000);
	@Override
	public Site getSite() {
		return this.site;
	}
	@Override
	public void process(Page page) {
		EbeanServer ebean =  EbeanFactoryUtil.getEbean();
		ebean.createSqlUpdate("delete from ip_info").execute();
		JSONArray ipArr = JSON.parseObject(page.getJson().get()).getJSONObject("data").getJSONArray("proxy_list");
		ipArr.forEach(ipInfo ->{
		 System.out.println(ipInfo.toString());	
		 IpInfo mIp= new IpInfo();
		 mIp.setAddress(ipInfo.toString().split(":")[0]);
		 mIp.setPort(Integer.parseInt(ipInfo.toString().split(":")[1]));
		 mIp.setUsername("kim");
		 mIp.setPassword("avfnxus8");
		 mIp.setStatu(0);
		 ebean.save(mIp);
		});
	}
	public void execute() {
		System.out.println("+++start+++");
		Spider.create(this).addUrl("http://dps.kuaidaili.com/api/getdps/?orderid=931029953411924&num=50&ut=1&format=json&sep=1").thread(1).run();
	}
}

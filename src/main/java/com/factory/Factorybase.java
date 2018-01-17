package com.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.avaje.ebean.EbeanServer;
import com.factory.entity.Factory;
import com.factory.entity.IpInfo;
import com.factory.entity.QueueFactory;
import com.factory.entity.Url;
import com.tvc.be.db.PersistenceFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

public class Factorybase implements PageProcessor {
	private final static Executor executor = Executors.newFixedThreadPool(20);//启用多线程
	public static Site site = Site.me().setSleepTime(1000).setTimeOut(5000);
	public static EbeanServer evb = PersistenceFactory.load("classpath:dbconfig.properties").getEbeanServer();
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Factory fac = new Factory();
		// 名字
		String name = page.getHtml().xpath("//h1[@id='poiName']/text()").get();
		fac.setName(name);
		Selectable sele = page.getHtml().xpath("//ul[@class='POI_ulA']");
		// 地址
		String location = sele.xpath("//li").nodes().get(1).get();
		String mlocation = Jsoup.parse(location).text();
		fac.setLocation(mlocation);
		// 电话
		String telphone = page.getHtml().xpath("//li[@class='telCls']/text()").get().trim();	
		fac.setTelphone(telphone);
		evb.save(fac);
		Url url = (Url)page.getRequest().getExtra("url");
		url.setStatu(1);
		evb.update(url);
		IpInfo ipx =(IpInfo)page.getRequest().getExtra("ipInfo");
		ipx.setStatu(1);
		evb.update(ipx);
	}
	public static void main(String[] args) {
		QueueFactory<IpInfo> queIp = new QueueFactory<IpInfo>();
		List<Url> listu=evb.find(Url.class).where("statu=0").findList();
		System.out.println(listu.size());
		for(int i=0;i<listu.size();i++) {
			final int j= i;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					if(queIp.getSize()<1) {
						List<IpInfo> tList=evb.find(IpInfo.class).where("statu=0").setMaxRows(20).findList();
						System.out.println(tList.size());
						queIp.pushs(tList);
					}
					Url url = listu.get(j);
					Map<String, Object> urlMap = new HashMap<String,Object>(); 
					Request req = new Request();
                    IpInfo ipInfo = queIp.pop();
					HttpHost host= new HttpHost(ipInfo.getAddress(),ipInfo.getPort());
					site.setHttpProxy(host);
					site.setUsernamePasswordCredentials(new UsernamePasswordCredentials(ipInfo.getUsername(),ipInfo.getPassword()));
					req.setMethod(HttpConstant.Method.GET);
					req.setUrl(url.getLink());
                    urlMap.put("ipInfo", ipInfo);
					urlMap.put("url", url);
					req.setExtras(urlMap);
					Spider.create(new Factorybase()).addRequest(req).thread(1).run();
				}});
		}
		
	}
	public static String getProxy() {
		String proxyst="";
		String url ="http://dps.kuaidaili.com/api/getdps/?orderid=931029953411924&num=15&ut=1&format=json&sep=1";
		try {
			String ele=Jsoup.connect(url).ignoreContentType(true).get().body().text();
			proxyst= JSON.parseObject(ele).getJSONObject("data").getJSONArray("proxy_list").get(1).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return proxyst;
	}
}

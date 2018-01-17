package com.factory;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class Test implements PageProcessor{
	public Site site = Site.me().setSleepTime(1000).setTimeOut(5000);
	@Override
	public Site getSite() {
		return this.site;
	}

	@Override
	public void process(Page page) {
	  Selectable sele=page.getHtml().xpath("//li[@class='telCls']/text()");
	  System.out.println(sele.get().trim());
	}
    public static void main(String[] args) {
    	String url="http://poi.mapbar.com/guangzhou/MAPIJBXEQEFICOSWSBXRC";
		Spider.create(new Test()).addUrl(url).thread(1).run();
	}
}

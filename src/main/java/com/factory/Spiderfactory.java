package com.factory;

import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.factory.entity.Url;
import com.tvc.be.db.PersistenceFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class Spiderfactory implements PageProcessor {
	public Site site = Site.me().setSleepTime(1000).setTimeOut(5000);
	public static EbeanServer evb = PersistenceFactory.load("classpath:dbconfig.properties").getEbeanServer();
	public static int number =1 ;
	public static int count = 0;
	
	public Site getSite() {
		return this.site;
	}
	public void process(Page page) {
		if (page.getUrl().toString().startsWith("http://poi.mapbar.com/dongguan/B60/")) {
			List<Selectable> seleList = page.getHtml().xpath("//div[@class='sortC']/dl").nodes();
			for(int x=0;x<seleList.size();x++) {
				Selectable sel = seleList.get(x);
				List<Selectable> aSeles = sel.xpath("//dd/a").nodes();
				for (Selectable sel2 : aSeles) {
					String urlFa = sel2.xpath("//a/@href").toString();
					Url url =new Url();
					url.setLink(urlFa);
					evb.save(url);
				}
			}
		}
	}

	public static void main(String[] args) {
		String url = "http://poi.mapbar.com/dongguan/B60/";
		Spider.create(new Spiderfactory()).addUrl(url).thread(3).run();
	}
}

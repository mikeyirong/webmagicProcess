package com.factory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

public class Spiderfactory implements PageProcessor {
	public Site site = Site.me().setSleepTime(1000).setTimeOut(5000);
	public static HSSFWorkbook wb = new HSSFWorkbook();
	public static HSSFSheet sheet = wb.createSheet("factory");
	public static int number =1 ;
	public Site getSite() {
		return this.site;
	}

	public void process(Page page) {
		int i = 0;
		if (page.getUrl().toString().startsWith("http://poi.mapbar.com/guangzhou/B60/")) {
			List<Selectable> seleList = page.getHtml().xpath("//div[@class='sortC']/dl").nodes();
			for (Selectable sel : seleList) {
				List<Selectable> aSeles = sel.xpath("//dd/a").nodes();
				for (Selectable sel2 : aSeles) {
					String urlFa = sel2.xpath("//a/@href").toString();
					Request req = new Request();
					req.setMethod(HttpConstant.Method.GET);
					req.setUrl(urlFa);
					page.addTargetRequest(urlFa);
				}
			}
		} else {
			if(number<6293){
				HSSFRow row = sheet.createRow(number);
				// 名字
				String name = page.getHtml().xpath("//h1[@id='poiName']/text()").get();
				Selectable sele = page.getHtml().xpath("//ul[@class='POI_ulA']");
				HSSFCell cell = row.createCell(0);
				cell.setCellValue(name);
				// 地址
				String location = sele.xpath("//li").nodes().get(1).get();
				String mlocation = Jsoup.parse(location).text();
				HSSFCell cell1 = row.createCell(1);
				cell1.setCellValue(mlocation);
				// 电话
				String telphone = page.getHtml().xpath("//li[@class='telCls']/text()").get().trim();
				HSSFCell cell2 = row.createCell(2);
				cell2.setCellValue(telphone);
				number+=1;
				System.out.println(number+"=="+name+"=="+mlocation+"=="+telphone);
			}else{
				HSSFRow row = sheet.createRow(number);
				// 名字
				String name = page.getHtml().xpath("//h1[@id='poiName']/text()").get();
				Selectable sele = page.getHtml().xpath("//ul[@class='POI_ulA']");
				HSSFCell cell = row.createCell(0);
				cell.setCellValue(name);
				// 地址
				String location = sele.xpath("//li").nodes().get(1).get();
				String mlocation = Jsoup.parse(location).text();
				HSSFCell cell1 = row.createCell(1);
				cell1.setCellValue(mlocation);
				// 电话
				String telphone = page.getHtml().xpath("//li[@class='telCls']/text()").get().trim();
				HSSFCell cell2 = row.createCell(2);
				cell2.setCellValue(telphone);
				File file = new File("C:\\Users\\Yanli Xu\\Desktop\\robot.xlsx");// Excel文件生成后存储的位置。
				OutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					wb.write(fos);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		}
		System.out.println("i======" + i);
	}

	public static void main(String[] args) {
		String url = "http://poi.mapbar.com/guangzhou/B60/";
		Spider.create(new Spiderfactory()).addUrl(url).thread(2).run();
	}
}

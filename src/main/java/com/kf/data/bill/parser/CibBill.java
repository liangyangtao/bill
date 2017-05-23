package com.kf.data.bill.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.bill.BaseBillParser;
import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.mybatis.factory.BaseDao;

public class CibBill extends BaseBillParser {

	private String bankName = "兴业银行";
	private String tableName = "cibBill";

	public void spider() {
		String url = "http://wealth.cib.com.cn/retail/onsale/index.html";
		Fetcher fetcher = Fetcher.getInstance();
		String html = fetcher.get(url);
		Document document = Jsoup.parse(html);
		Element bodyElement = document.select(".middle").first();
		Elements tables = bodyElement.select("table");
		for (Element element : tables) {
			try {
				System.out.println(element);
				Elements trElements = element.select("tr");
				
				
				
				for (int i= 1; i<trElements.size()-1 ;i++) {
					 Element trElement  = trElements.get(i);
					Elements tdElement = trElement.select("td");

					String starttimeText = tdElement.get(1).text();
					String endtimeText = tdElement.get(2).text();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					Date start = sdf.parse(starttimeText);
					Date end = sdf.parse(endtimeText);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					String startStr = sdf2.format(start);
					String endStr = sdf2.format(end);
					Date now = new Date();
					String product_stats = "";
					if (now.after(start) && now.before(end)) {
						product_stats = "发售中";
					} else {
						product_stats = "结束认购";
					}
					
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("product_bank", bankName);
					result.put("product_stats", product_stats);
					result.put("product_limit", tdElement.get(4).text().trim() +"天");
					result.put("product_income_type", "");
					result.put("product_currency", tdElement.get(3).text());
					result.put("product_purchasse_amount", tdElement.get(5).text()+"元");
					result.put("product_type", "");
					result.put("product_name", tdElement.get(0).text());
					result.put("product_code", "");
					result.put("product_start", startStr);
					result.put("product_end", endStr);
					result.put("product_expire", "");
					result.put("product_anticipated",tdElement.get(6).text() );
					result.put("product_invest_type", "");
					result.put("product_risk_type", "");
					result.put("product_channel", "");
					result.put("product_area", "");
					result.put("unmd", Md5Tools.GetMD5Code(tdElement.get(1).text()));
					result.put("uptime", new Date());
					String sql = "insert into " + tableName + " ";
					new BaseDao().executeMapSql(sql, result);
				}
				 Element trElement  = trElements.get(trElements.size() -1);
				 Elements tdElement = trElement.select("td");
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("product_bank", bankName);
					result.put("product_stats", "发售中");
					result.put("product_limit", tdElement.get(3).text().trim() );
					result.put("product_income_type", "");
					result.put("product_currency", tdElement.get(2).text());
					result.put("product_purchasse_amount", tdElement.get(4).text()+"元");
					result.put("product_type", "");
					result.put("product_name", tdElement.get(0).text());
					result.put("product_code", "");
					result.put("product_start", "7:00");
					result.put("product_end", "15:15");
					result.put("product_expire", "");
					result.put("product_anticipated",tdElement.get(5).text() );
					result.put("product_invest_type", "");
					result.put("product_risk_type", "");
					result.put("product_channel", "");
					result.put("product_area", "");
					result.put("unmd", Md5Tools.GetMD5Code(tdElement.get(1).text()));
					result.put("uptime", new Date());
					String sql = "insert into " + tableName + " ";
					new BaseDao().executeMapSql(sql, result);
				 
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}

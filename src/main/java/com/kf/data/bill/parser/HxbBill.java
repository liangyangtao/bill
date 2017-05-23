package com.kf.data.bill.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.crawler.tools.MergeTable;
import com.kf.data.mybatis.factory.BaseDao;

public class HxbBill {

	private String bankName = "华夏银行";
	private String tableName = "hxbBill";

	public void spider() {
		String url = "http://www.hxb.com.cn/home/cn/personal/longying/licai/list.shtml";
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
		Element tableElement = document.select(".pro_contp > div >  table").first();
		Document document2 = new MergeTable().megerTable(tableElement);
		Elements trElements = document2.select("tr");
		for (int i = 1; i < trElements.size(); i++) {
			try {
				Element trElement = trElements.get(i);
				Elements tdElement = trElement.select("td");
				String timeText = tdElement.get(1).text();
				String product_stats = "";
				String startStr = "";
				String endStr = "";
				if (timeText.trim().equals("开放")) {
					product_stats = "发售中";
				} else {
					String starttimeText = timeText.split("至")[0];
					String endtimeText = timeText.split("至")[0];
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
					Date start = sdf.parse(starttimeText);
					Date end = sdf.parse(endtimeText);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					startStr = sdf2.format(start);
					endStr = sdf2.format(end);
					Date now = new Date();

					if (now.after(start) && now.before(end)) {
						product_stats = "发售中";
					} else {
						product_stats = "结束认购";
					}
				}
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("product_bank", bankName);
				result.put("product_stats", product_stats);
				result.put("product_limit", "");
				result.put("product_income_type", "");
				result.put("product_currency", "");
				result.put("product_purchasse_amount", tdElement.get(3).text() + "万元");
				result.put("product_type", tdElement.get(5).text());
				result.put("product_name", tdElement.get(0).text());
				result.put("product_code", "");
				result.put("product_start", startStr);
				result.put("product_end", endStr);
				result.put("product_expire", "");
				result.put("product_anticipated", tdElement.get(2).text());
				result.put("product_invest_type", "");
				result.put("product_risk_type", "");
				result.put("product_channel", tdElement.get(4).text());
				result.put("product_area", "");
				result.put("unmd", Md5Tools.GetMD5Code(tdElement.get(0).text()));
				result.put("uptime", new Date());
				String sql = "insert into " + tableName + " ";
				new BaseDao().executeMapSql(sql, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

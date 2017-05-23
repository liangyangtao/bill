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

public class HsbcBill extends BaseBillParser {

	private String bankName = "汇丰银行";

	private String tableName = "hsbcbill";

	public void spider() {
		String url = "http://www.hsbc.com.cn/1/2/personal-banking-cn/investment/structuredinvestment";
		Fetcher fetcher = Fetcher.getInstance();
		String html = fetcher.get(url);
		Document document = Jsoup.parse(html);
		Element bodyElement = document.select(".contentStyle05e").first();
		Elements tables = bodyElement.select(".product_table1");
		for (Element element : tables) {
			try {
				System.out.println(element);
				Element trElement = element.select("tr").get(1);
				Elements tdElement = trElement.select("td");
				String timeText = tdElement.get(4).text();
				String time[] = timeText.split("至");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				Date start = sdf.parse(time[0]);
				Date end = sdf.parse(time[1]);
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
				result.put("product_limit", tdElement.get(3).text());
				result.put("product_income_type", "");
				result.put("product_currency", tdElement.get(2).text());
				result.put("product_purchasse_amount", "");
				result.put("product_type", "");
				result.put("product_name", tdElement.get(0).text());
				result.put("product_code", tdElement.get(1).text());
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				String startStr = sdf2.format(start);
				String endStr = sdf2.format(end);
				result.put("product_start", startStr);
				result.put("product_end", endStr);
				result.put("product_expire", "");
				result.put("product_anticipated", "");
				result.put("product_invest_type", "");
				result.put("product_risk_type", tdElement.get(5).text());
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

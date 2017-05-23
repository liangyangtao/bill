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

public class HkbeaBill extends BaseBillParser {
	// http://www.hkbea.com.cn/cfgl/lccp/index.shtml
	private String bankName = "东亚银行";

	private String tableName = "hkbeabill";

	public void spider() {
		downpageBB();
		downpageFBB();
	}

	private void downpageFBB() {
		try {
			String url = "http://www.hkbea.com.cn/cfgl/lccp/gnjjdxyw/fbbtzcp/index.shtml";
			System.out.println(url);
			Fetcher fetcher = Fetcher.getInstance();
			String html = fetcher.get(url);
			parser(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void downpageBB() {
		int pageNum = 1;
		int pageSite = 100;
		int i = 1;
		for (; i <= pageNum; i++) {
			try {
				String url = "";
				if (i == 1) {
					url = "http://www.hkbea.com.cn/cfgl/lccp/gnjjdxyw/bbtzcp/index.shtml";
				} else {
					url = "http://www.hkbea.com.cn/cfgl/lccp/gnjjdxyw/bbtzcp/index_" + i + ".shtml";
				}
				System.out.println(url);
				Fetcher fetcher = Fetcher.getInstance();
				String html = fetcher.get(url);
				pageNum = parser(html);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public int parser(String html) {
		Document document = Jsoup.parse(html);
		Element bodyElement = document.select(".list_td_big").first();
		System.out.println(bodyElement);
		Element tableElement = bodyElement.select("table").first();
		Elements trElements = tableElement.select("tr");
		for (Element trElement : trElements) {
			try {
				Elements tdElement = trElement.select("td");
				String timeText = tdElement.get(3).text();
				String time[] = timeText.split("-");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
				result.put("product_limit", tdElement.get(4).text());
				result.put("product_income_type", "");
				result.put("product_currency", tdElement.get(5).text());
				result.put("product_purchasse_amount", "");
				result.put("product_type", "");
				result.put("product_name", tdElement.get(2).text());
				result.put("product_code", "");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				String startStr = sdf2.format(start);
				String endStr = sdf2.format(end);
				result.put("product_start", startStr);
				result.put("product_end", endStr);
				result.put("product_expire", "");
				result.put("product_anticipated", "");
				result.put("product_invest_type", "");
				result.put("product_risk_type", "");
				result.put("product_channel", "");
				result.put("product_area", "");
				result.put("unmd", Md5Tools.GetMD5Code(tdElement.get(2).text()));
				result.put("uptime", new Date());
				String sql = "insert into " + tableName + " ";
				new BaseDao().executeMapSql(sql, result);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 5;
	}
}

package com.kf.data.bill.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.mybatis.factory.BaseDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CmbcBill {
	private String bankName = "中国民生银行";
	private String tableName = "cmbcBill";

	public void spider() {
		int pageNum = 1;
		int pageSite = 100;
		int i = 1;
		for (; i <= pageNum; i++) {
			String url = "https://service.cmbc.com.cn/pai_ms/cft/queryTssPrdInScreenfoForJson.gsp?rd=0.5654770532387512&page="
					+ i + "&rows=10&jsonpcallback=jQuery171003445446316812906_1495514585838&_=1495514663196";
			Fetcher fetcher = Fetcher.getInstance();
			String html = fetcher.get(url);
			html = StringUtils.substringBetween(html, "({", "})");
			html = "{" + html + "}";
			pageNum = parser(html);
		}

	}

	private int parser(String html) {
		JSONObject resultObject = JSONObject.fromObject(html);
		int pageCount = resultObject.getInt("pageCount");
		JSONArray jsonArray = resultObject.getJSONArray("list");
		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("product_bank", bankName);
			result.put("product_stats", "");
			result.put("product_limit", jsonObject.getString("live_time"));
			result.put("product_income_type", "");
			result.put("product_currency", "");
			result.put("product_purchasse_amount", jsonObject.getString("pfirst_amt"));
			result.put("product_type", jsonObject.getString("prd_type"));
			result.put("product_name", jsonObject.getString("prd_name"));
			result.put("product_code", jsonObject.getString("prd_code"));
			result.put("product_start", jsonObject.getString("start_date"));
			result.put("product_end", jsonObject.getString("end_date"));
			result.put("product_expire", jsonObject.getString("prd_next_date"));
			result.put("product_anticipated", jsonObject.getString("next_income_rate"));
			result.put("product_invest_type", "");
			result.put("product_risk_type", jsonObject.getString("risk_level"));
			result.put("product_channel", "");
			result.put("product_area", "");
			result.put("unmd", Md5Tools.GetMD5Code(jsonObject.getString("prd_name")));
			result.put("uptime", new Date());
			String sql = "insert into " + tableName + " ";
			new BaseDao().executeMapSql(sql, result);

		}
		return pageCount;
	}
}

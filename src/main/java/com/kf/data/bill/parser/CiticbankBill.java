package com.kf.data.bill.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.mybatis.factory.BaseDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CiticbankBill {

	private String bankName = "中信银行";
	private String tableName = "citicbankBill";

	public void spider() {
		int pageNum = 1;
		int pageSite = 100;
		int i = 1;
		for (; i <= pageNum; i++) {
			String url = "https://etrade.citicbank.com/portalweb/fd/getFinaList.htm";
			Map<String, String> params = new HashMap<String, String>();
			params.put("branchId", "701100");
			params.put("totuseAmt", "02");
			params.put("orderField", "ppo_incomerate");
			params.put("orderType", "desc");
			params.put("currentPage", ""+i);
			params.put("pageSize", "10");
			params.put("pwdControlFlag", "0");
			params.put("responseFormat", "JSON");
			params.put("random", "3740");
			String html = Fetcher.getInstance().post(url, params, null, "utf-8");

			pageNum = parser(html);
		}

	}

	private int parser(String html) {
		JSONObject resultObject = JSONObject.fromObject(html);
		int pageCount = resultObject.getInt("pageCount");
		JSONObject content = resultObject.getJSONObject("content");
		JSONArray jsonArray = content.getJSONArray("resultList");
		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("product_bank", bankName);
			result.put("product_stats", "");
			result.put("product_limit", "");
			result.put("product_income_type", "");
			result.put("product_currency", "");
			result.put("product_purchasse_amount", jsonObject.getString("firstAmt"));
			result.put("product_type", jsonObject.getString("prdType"));
			result.put("product_name", jsonObject.getString("prdName"));
			result.put("product_code", jsonObject.getString("prdNo"));
			result.put("product_start", jsonObject.getString("ipoBeginDate"));
			result.put("product_end", jsonObject.getString("ipoEndDate"));
//			result.put("product_expire", jsonObject.getString("prd_next_date"));
			result.put("product_anticipated", jsonObject.getString("incomerate"));
			result.put("product_invest_type", "");
			result.put("product_risk_type", jsonObject.getString("riskLevel"));
			result.put("product_channel", "");
			result.put("product_area", "");
			result.put("unmd", Md5Tools.GetMD5Code(jsonObject.getString("prdName")));
			result.put("uptime", new Date());
			String sql = "insert into " + tableName + " ";
			new BaseDao().executeMapSql(sql, result);

		}
		return pageCount;
	}

}

package com.kf.data.bill.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kf.data.bill.BaseBillParser;
import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.mybatis.factory.BaseDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CmbChinaBill extends BaseBillParser {

	private String bankName = "招商银行";

	private String tableName = "cmbchinabill";

	public CmbChinaBill() {

	}

	public void spider() {
		downpage();
	}

	public void downpage() {
		int pageNum = 1;
		int pageSite = 100;
		int i = 1;
		for (; i <= pageNum; i++) {
			try {
				String url = "http://www.cmbchina.com/cfweb/svrajax/product.ashx?op=search&type=m&pageindex=" + i
						+ "&salestatus=&baoben=&currency=&term=&keyword=&series=02&risk=&city=&date=&pagesize="
						+ pageSite + "&orderby=ord1&t=0.19476589183758886";
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
		html = html.substring(1, html.length() - 1);
		JSONObject resultObject = JSONObject.fromObject(html);
		int totalPage = resultObject.getInt("totalPage");
		JSONArray jsonArray = resultObject.getJSONArray("list");
		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("product_bank", bankName);
			result.put("product_stats", jsonObject.getString("IsCanBuy").equals("true") ? "可购买" : "即将发售");
			result.put("product_limit", jsonObject.getString("FinDate"));
			result.put("product_income_type", jsonObject.getString("Style"));
			result.put("product_currency", jsonObject.getString("Currency"));
			result.put("product_purchasse_amount", jsonObject.getString("InitMoney"));
			result.put("product_type", "");// jsonObject.getString("TypeCode")
			result.put("product_name", jsonObject.getString("PrdName"));
			result.put("product_code", jsonObject.getString("PrdCode"));
			result.put("product_start", jsonObject.getString("BeginDate"));
			result.put("product_end", jsonObject.getString("EndDate"));
			result.put("product_expire", jsonObject.getString("ExpireDate"));
			result.put("product_anticipated", jsonObject.getString("NetValue"));
			result.put("product_invest_type", jsonObject.getString("Style"));
			result.put("product_risk_type", jsonObject.getString("Risk"));
			result.put("product_channel", jsonObject.getString("SaleChannelName"));
			result.put("product_area", jsonObject.getString("AreaCode"));
			result.put("unmd", Md5Tools.GetMD5Code(jsonObject.getString("PrdName")));
			result.put("uptime", new Date());
			String sql = "insert into " + tableName + " ";
			new BaseDao().executeMapSql(sql, result);
		}
		return totalPage;
	}

}

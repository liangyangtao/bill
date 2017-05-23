package com.kf.data.bill.parser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.crawler.fetcher.Fetcher;
import com.kf.data.crawler.tools.Md5Tools;
import com.kf.data.mybatis.factory.BaseDao;

public class IcbcBill {
	private String bankName = "中国工商银行";
	private String tableName = "icbcBill";

	public void spider() {

		int pagesite = 5;
		for (int i = 1; i <= pagesite; i++) {
			String url = "https://mybank.icbc.com.cn/servlet/ICBCBaseReqServletNoSession?dse_operationName=per_FinanceCurProListP3NSOp&pageFlag_turn=2&nowPageNum_turn="
					+ i + "&Area_code=0200&requestChannel=302";
			// String url =
			// "https://mybank.icbc.com.cn/servlet/ICBCBaseReqServletNoSession?dse_operationName=per_FinanceCurProListP3NSOp&p3bank_error_backid=120103&pageFlag=0&Area_code=0200&requestChannel=302";
			String html = Fetcher.getInstance().post(url, null, null, "utf-8");
			System.out.println(html);
			parser(html);
		}

	}

	public int parser(String html) {

		Document document = Jsoup.parse(html);
		Element dataElement = document.select("#datatableModel").first();
		Elements trElements = dataElement.select(".ebdp-pc4promote-circularcontainer");
		for (Element element : trElements) {
			String name = element
					.select(".ebdp-pc4promote-circularcontainer-title-ellipsis,.ebdp-pc4promote-circularcontainer-title")
					.first().text();
			String time = element.select(".ebdp-pc4promote-circularcontainer-text1").first().text();
			Elements tdElement = element.select(".ebdp-pc4promote-circularcontainer-table > tbody > tr > td");

			String rate = tdElement.get(0).text();
			String initmoney = tdElement.get(1).text();
			String limit = tdElement.get(2).text();
			String rank = tdElement.get(3).text();

			// 步步为赢收益递增型灵活期限人民币理财产品WY1001WY1001
			String startStr = "";
			String endStr = "";
			if (time.contains("购买开放日")) {
				startStr = time.split("开放日：")[1];
			}
			{
				if (time.contains("募集期")) {
					startStr = time.split("募集期：")[1];
					startStr = startStr.split("-")[0];
					endStr = time.split("募集期：")[1].split("-")[1];
				}
				// 【现金管理】最近购买开放日：2017-05-23

				// 预期年化收益率/业绩基准 2.30%-3.80%
				// 起购金额 10万
				initmoney = initmoney.split("起购金额 ")[1];
				// 期限 无固定期限
				limit = limit.split("期限")[1];
				// 产品风险等级 2
				rank = rank.split("产品风险等级")[1];
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("product_bank", bankName);
				result.put("product_stats", "");
				result.put("product_limit", limit);
				result.put("product_income_type", "");
				result.put("product_currency", "");
				result.put("product_purchasse_amount", initmoney);
				result.put("product_type", "");
				result.put("product_name", name);
				result.put("product_code", "");
				result.put("product_start", startStr);
				result.put("product_end", endStr);
				result.put("product_expire", "");
				result.put("product_anticipated", rate);
				result.put("product_invest_type", "");
				result.put("product_risk_type", rank);
				result.put("product_channel", "");
				result.put("product_area", "");
				result.put("unmd", Md5Tools.GetMD5Code(tdElement.get(0).text()));
				result.put("uptime", new Date());
				String sql = "insert into " + tableName + " ";
				new BaseDao().executeMapSql(sql, result);

			}
		}
		return 5;
	}
}

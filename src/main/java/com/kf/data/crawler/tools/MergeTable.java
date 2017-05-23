package com.kf.data.crawler.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Title: MergeTable.java
 * @Package com.kf.data.crawler.tools
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liangyt
 * @date 2017年5月23日 下午7:29:44
 * @version V1.0
 */
public class MergeTable {
	public static Document megerTable(Element tableElement) {
		Elements trElements = tableElement.select("tr");
		int row = trElements.size();
		int col = 0;
		for (int i = 0; i < trElements.size(); i++) {
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td,th");
			if (col < tdElements.size()) {
				col = tdElements.size();
			}
		}
		String[][] aa = new String[row][col];
		for (int i = 0; i < trElements.size(); i++) {
			int n = 0;
			Element trElement = trElements.get(i);
			Elements tdElements = trElement.select("td,th");
			for (int j = 0; j < tdElements.size(); j++) {
				Element tdElement = tdElements.get(j);
				if (aa[i][n] == null) {

					aa[i][n] = tdElement.text();
					String rowsStr = tdElement.attr("rowspan");
					String colStr = tdElement.attr("colspan");
					if (!rowsStr.isEmpty()) {
						int rowInt = Integer.parseInt(rowsStr);
						for (int k = 1; k < rowInt; k++) {
							aa[i + k][n] = tdElement.text();
						}
					}
					if (!colStr.isEmpty()) {
						int colInt = Integer.parseInt(colStr);
						for (int k = 1; k < colInt; k++) {
							n++;
							aa[i][n] = tdElement.text();
						}

					}
				} else {
					j--;
				}
				n++;
			}
		}
		Document document2 = Jsoup.parse("<table></table>");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < aa.length; i++) {
			sb.append("<tr>");
			for (int j = 0; j < aa[i].length; j++) {
				sb.append("<td>");
				sb.append(aa[i][j] == null ? "" : aa[i][j]);
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		document2.select("table").first().append(sb.toString());
		return document2;
	}
}

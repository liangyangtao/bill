package com.kf.data.bill;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kf.data.crawler.fetcher.Fetcher;

/**
 * Unit test for simple App.
 */
public class AppTest {

	public static void main(String[] args) {
		String url = "http://obdna1jlc.bkt.clouddn.com/47177971da704a87ef9a9131f2cc411b.pdf.html";
		String html = Fetcher.getInstance().get(url);
		Document document = Jsoup.parse(html);
//		formatElements(document.body());
		System.out.println(document.body().select("tables"));

	}

	public static void formatElements(Element contentElement) {
		// 去重属性
		removeElementAttr(contentElement);
		Elements allElements = contentElement.children();
		for (Element element : allElements) {
			try {
				removeElementAttr(element);
				if (element != null) {
					formatElements(element);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;

			}

		}

	}

	// 移除所有的属性

	public static void removeElementAttr(Element element) {
		if (element == null) {
			return;
		}
		Attributes attributes = element.attributes();
		Iterator<Attribute> iterator = attributes.iterator();
		while (iterator.hasNext()) {
			Attribute attribute = iterator.next();
			element.removeAttr(attribute.getKey());
			iterator = attributes.iterator();
		}
	}

}

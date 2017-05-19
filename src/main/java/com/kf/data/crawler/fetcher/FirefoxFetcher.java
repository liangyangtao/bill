package com.kf.data.crawler.fetcher;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kf.data.crawler.tools.ProxyIpTools;


/****
 * 
 * @author lyt
 * 
 *         fireFox
 *
 */
public class FirefoxFetcher extends WebDriverFetcher {

	public static boolean isProxy = true;
	public static ProxyIpTools proxyIpTools = ProxyIpTools.getInstence();

	private static Map<Integer, WebDriver> webdirvers = new HashMap<Integer, WebDriver>();

	public static Integer proxyNum = 1;

	public FirefoxFetcher() {
	
	}

	/***
	 * 如果代理失效关闭浏览器，在选择一个代理服务器，启动一个浏览器
	 * 
	 * @param index
	 */

	public synchronized static void closeWebDrive(int index) {
		WebDriver driver = webdirvers.get(index);
		System.out.println("正在关闭第" + index + "个webdriver");
		if (driver != null) {
//			driver.close();
			driver.quit();
		} else {
			System.out.println("关闭的Driver 是空的");
		}
		WebDriver newDriver = createWebDrive();
		webdirvers.put(index, newDriver);

	}

	public static synchronized WebDriver createWebDrive() {
		WebDriver driver = null;
		if (isProxy) {
			String proxyIp = proxyIpTools.getNextIPByApi();
			if (proxyIp == null) {
				return null;
			}
			System.out.println("使用的IP是" + proxyIp);
			String temp[] = proxyIp.split(":");
			FirefoxProfile profile = new FirefoxProfile();
			// 使用代理
			profile.setPreference("network.proxy.type", 1);
			// http协议代理配置
			profile.setPreference("network.proxy.http", temp[0]);
			profile.setPreference("network.proxy.http_port", temp[1]);

			// 所有协议公用一种代理配置，如果单独配置，这项设置为false，再类似于http的配置
			profile.setPreference("network.proxy.share_proxy_settings", false);

			// 对于localhost的不用代理，这里必须要配置，否则无法和webdriver通讯
			profile.setPreference("network.proxy.no_proxies_on", "localhost");
			driver = new FirefoxDriver(profile);
		} else {
			DesiredCapabilities cap = new DesiredCapabilities();
			driver = new FirefoxDriver(cap);
		}
		driver.manage().window().maximize();
		return driver;
	}

	public static void init() {
		for (int i = 0; i < proxyNum; i++) {
			try {
				WebDriver newDriver = createWebDrive();
				webdirvers.put(i, newDriver);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

	}

	public static synchronized WebDriver getNextWebDriver(int index) {
		System.out.println("正在启用第" + index + "个WebDriver");
		WebDriver webDriver = webdirvers.get(index);
		if (webDriver == null) {
			webDriver = createWebDrive();
			webdirvers.put(index, webDriver);
		}
		return webDriver;
	}

}

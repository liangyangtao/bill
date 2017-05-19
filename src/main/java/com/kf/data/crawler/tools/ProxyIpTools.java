package com.kf.data.crawler.tools;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Title: ProxyIpTools.java
 * @Package com.kf.data.crawler.tianyancha.tools
 * @Description: 调取代理IP的工具类
 * @author liangyt
 * @date 2017年5月2日 下午6:05:12
 * @version V1.0
 */
public class ProxyIpTools {

	public static final String checkIpUrl = "http://httpbin.org/ip";
	public static final String ipApi = "http://api.zdaye.com/?api=201607021010446529&pw=kaidmc&nport=80%2C8080%2C8090%2C3128%2C8088&gb=2";
	public static final String ipApi2 = "http://s.zdaye.com/?api=201704191104061999&count=200&fitter=2&px=1";
	public static final String ipApi3 = "http://s.zdaye.com/?api=201704191104061999&count=100&fitter=2&px=1";
	public static final String ipApi4 = "http://api.ip.data5u.com/dynamic/get.html?order=95700c7666bcf4058a83c5012fea71c4";
	public static String LOCALIP = "1.202.219.242";
	private static ProxyIpTools proxyIpTools;
	public static Integer CHECKNUM = 0;
	public static LinkedBlockingQueue<String> ips = new LinkedBlockingQueue<String>();

	public ProxyIpTools() {
	}

	public void getUsedIp() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				getApiIPs();
			}
		}, 1000);

	}

	public void writeProxy(String proxy) {
		try {
			String path = FileReaderTools.class.getClassLoader().getResource("").toURI().getPath();
			FileOutputStream fos = new FileOutputStream(path + File.separator + "usedProxy.txt", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(proxy + "\n");
			bw.flush();
			fos.close();
			osw.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized String getNextIPByApi() {
		String ip = null;

		while (true) {
			if (ips.size() == 0) {
				getApiIPs();
			}
			try {
				String str = ips.take();
				System.out.println("正在检测的ip" + str);
				String temp[] = str.split(":");
				if (temp.length == 2) {
					boolean isUsed = checkProxyIp(temp[0], Integer.parseInt(temp[1]));
					if (isUsed) {
						ip = str;
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ip;

	}
	// public static void main(String[] args) {
	// ProxyIpTools proxyIpTools =new ProxyIpTools();
	// String html = proxyIpTools.getHtml(ipApi3, "utf-8");
	// List<String> aa = Arrays.asList(html.split("\\r\\n"));
	// for (String object : aa) {
	// System.out.println(object);
	// if(object.trim().isEmpty()){
	// continue;
	// }
	// String temp[] = object.split(":");
	// if (temp.length == 2) {
	// boolean isUsed = proxyIpTools.checkProxyIp(temp[0],
	// Integer.parseInt(temp[1]));
	// if (isUsed) {
	// System.out.println("代理可用");
	// }
	// }
	//
	// }
	// }

	private void getApiIPs() {

		String html = getHtml(KfConstant.proxyIp, "utf-8");
		List<String> aa = null;
		if (KfConstant.proxyIp.contains("data5u")) {
			aa = Arrays.asList(html.split("\\n"));
		} else {
			aa = Arrays.asList(html.split("\\r\\n"));
		}
		for (String object : aa) {
			if (object.trim().isEmpty()) {
				continue;
			}
			System.out.println("正在检测的ip" + object);
			String temp[] = object.split(":");
			if (temp.length == 2) {
				boolean isUsed = checkProxyIp(temp[0], Integer.parseInt(temp[1]));
				if (isUsed) {
					for (int i = 0; i < 28; i++) {
						try {
							ips.put(object);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
	}

	public boolean checkProxyIp(String ip, int port) {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL url = new URL(checkIpUrl);
			// 打开和URL之间的连接
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			// conn.setDoOutput(true);
			// conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			if (200 == conn.getResponseCode()) {
				// 得到输入流
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				String html = baos.toString("gbk");
				if (!html.contains(LOCALIP)) {
					return true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("检测代理IP不可用" + ip);
			// e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return false;

	}

	public String getHtml(String regUrl, String charset) {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL url = new URL(regUrl);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			// conn.setDoOutput(true);
			// conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			if (200 == conn.getResponseCode()) {
				// 得到输入流
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				String html = baos.toString(charset);
				return html;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return "";

	}

	public static synchronized ProxyIpTools getInstence() {
		if (proxyIpTools == null) {
			proxyIpTools = new ProxyIpTools();
		}
		return proxyIpTools;
	}

}

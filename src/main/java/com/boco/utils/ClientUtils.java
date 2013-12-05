package com.boco.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.thrift.transport.TTransportException;

import com.boco.lq.hbase.client.BaseClient;
import com.boco.lq.hbase.query.thrift.Header;

public class ClientUtils {

	private static final String CONFIG_PATH = "/config/thrift.properties";
	private static Properties prop = ConfigUtils.getConfig(CONFIG_PATH);

	private static Header header = null;

	/**
	 * 返回baseclient
	 * 
	 * @return
	 */
	public static BaseClient getClientInstance() {
		BaseClient conn = null;
		if (true) {
			int port = Integer.parseInt(prop.getProperty("thrift.port", "8920"));
			try {
				conn = BaseClient.getQueryClient(prop.getProperty("thrift.ip"),
						port);
			} catch (TTransportException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	/**
	 * 返回默认header
	 * 
	 * @param timeout
	 * @return
	 */
	public static Header getHeader() {
		if (null == header) {
			header = new Header(prop.getProperty("thrift.user", "test"),
					prop.getProperty("thrift.password", "test"), 60000);
		}
		return header;
	}

	/**
	 * 返回自定义header
	 * 
	 * @param timeout
	 * @return
	 */
	public static Header getCustomerHeader(int timeout) {
		return new Header(prop.getProperty("thrift.user", "test"),
				prop.getProperty("thrift.password", "test"), timeout);
	}
}

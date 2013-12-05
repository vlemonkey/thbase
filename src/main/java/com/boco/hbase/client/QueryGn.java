package com.boco.hbase.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.lq.hbase.client.BaseClient;
import com.boco.lq.hbase.query.thrift.Condition;
import com.boco.lq.hbase.query.thrift.Filter;
import com.boco.lq.hbase.query.thrift.Header;
import com.boco.lq.hbase.query.thrift.MatchType;
import com.boco.lq.hbase.query.thrift.Order;
import com.boco.lq.hbase.query.thrift.OrderType;
import com.boco.lq.hbase.query.thrift.Output;
import com.boco.lq.hbase.query.thrift.Page;
import com.boco.lq.hbase.query.thrift.Result;
import com.boco.utils.ClientUtils;
import com.boco.utils.JsonUtils;

public class QueryGn {

	private static final String[] OUTPUTS = {"DELAY", "LAC", "CI", "IMEI",
			"SERVICE_TYPE", "START_TIME", "END_TIME", "TOTAL_DELAY",
			"UP_BYTES", "DOWN_BYTES", "TOTAL_BYTES", "MOBILE_CLASS",
			"CLIENT_IP", "SERVER_IP", "SERVICE_SUCCESS", "APN", "IMSI", 
			"CONTENT_TYPE", "HOST",	"SERVICE_INFO", "MSISDN"};
	
	private static Logger LOG = LoggerFactory.getLogger(QueryGn.class);

	public static void main(String[] args) throws Exception{
		System.out.println(getJsonData("51D8731EF1BF475F479BEE61CB299B42", "2013-07-31 16:00:00", "2013-08-02 17:30:30"));
	}

	private static List<Output> buildOut() {
		List<Output> outputs = new ArrayList<Output>();
		for (String output : OUTPUTS) {
			outputs.add(new Output(output));
		}
		return outputs;
	}

	public static String getJsonData(String mobile, String startTime,
			String endTime) throws TException, IOException {
		// 连接thrift server端接口
		BaseClient conn = ClientUtils.getClientInstance();

		// 设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = ClientUtils.getHeader(); //60000
		// 设置分页参数
		Page page = new Page(1, 1000);
		// 添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOut();
		// 过滤条件
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("MSISDN", MatchType.EQUAL, mobile));
		// 排序条件
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("END_TIME", OrderType.ASC));

		Map<String, String> attributes = new HashMap<String, String>();
		Condition condition = new Condition(startTime, endTime, outputs,
				filters, orders, attributes);

		// 调用thrift接口
		Result result = conn.queryGn(header, page, condition);
		
		if (result.errorcode != 0) {
			LOG.error(result.errormsg);
			return "";
		}else {
			if(null == result.getDatas()) return "";
			List<Map<String, String>> list = JsonUtils.strlist2MapList(OUTPUTS, result.getDatas().datas);
			String strJson = JsonUtils.listmap2Json(list);
			return strJson;
		}
	}
}
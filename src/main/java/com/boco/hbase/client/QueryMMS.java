package com.boco.hbase.client;

import static org.apache.commons.lang.StringUtils.isNotBlank;

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

public class QueryMMS {

	private static final String[] OUTPUTS = {"MESSAGE_ID","ORG_ADDRESS","REC_ADDRESS","RECV_TIME","SEND_TIME","MSG_LEN","FAIL_CAUSE"};
	
	private static Logger LOG = LoggerFactory.getLogger(QueryMMS.class);
	
	public static void main(String[] args) throws Exception{
		String ret = getJsonData("51D8731EF1BF475F479BEE61CB299B42", null, "2013-08-01 00:00:00", "2013-08-01 23:30:30", "RECV_TIME", 1);
		System.out.println(ret);
	}

	private static List<Output> buildOut() {
		List<Output> outputs = new ArrayList<Output>();
		for (String output : OUTPUTS) {
			outputs.add(new Output(output));
		}
		return outputs;
	}
	
	public static String getJsonData(String call, String called, 
			String startTime, String endTime, 
			String sortName, int sort) throws TException, IOException {

		// 连接thrift server端接口
		BaseClient conn = ClientUtils.getClientInstance();

		// 设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = ClientUtils.getHeader();
		// 设置分页参数
		Page page = new Page(1, 1000);
		// 添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOut();
		// 过滤条件
		List<Filter> filters = new ArrayList<Filter>();
		
		// ORG_ADDRESS
		if (isNotBlank(call)) {
			filters.add(new Filter("ORG_ADDRESS", MatchType.EQUAL,
					call));
		}
		// REC_ADDRESS
		if (isNotBlank(called)) {
			filters.add(new Filter("REC_ADDRESS", MatchType.EQUAL,
					called));
		}
		
		// 排序条件
		List<Order> orders = new ArrayList<Order>();
		switch (sort) {
		case 1:
			orders.add(new Order(sortName, OrderType.ASC));
			break;
		default:
			orders.add(new Order(sortName, OrderType.DESC));
			break;
		}

		Map<String, String> attributes = new HashMap<String, String>();
		Condition condition = new Condition(startTime, endTime, outputs,
				filters, orders, attributes);

		// 调用thrift接口
		Result result = conn.queryMms(header, page, condition);
		
		if (result.errorcode != 0) {
			LOG.error(result.errormsg);
			// 关闭连接
			conn.close();
			return "";
		}else {
			if(null == result.getDatas()) return "";
			List<Map<String, String>> list = JsonUtils.strlist2MapList(OUTPUTS, result.getDatas().datas);
			String strJson = JsonUtils.listmap2Json(list);
			// 关闭连接
			conn.close();
			return strJson;
		}
	}
}

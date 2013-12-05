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

public class QuerySMS {

	private static final String[] LIST_OUTPUTS = { "SM_ID", "EMA_CLASS",
			"ORG_ADDR", "DEST_ADDR", "SUBMIT_TIME", "TIME_STAMP", "SMS_STATUS",
			"DELIVER_COUNTS","UDHI","UDL","ISHWZX" };

	private static final String[] DETAIL_OUTPUTS = { "SM_ID", "EMA_CLASS",
			"SCHEDULE", "PRI", "MMS", "ORG_ADDR", "SUBMIT_TIME", "MT_MSC_ADDR",
			"RESULT", "ORG_FEE", "SMS_STATUS", "MESSAGE_TYPE", "UD", "DCS",
			"UDHI", "EXPIRE", "PID", "SRR", "DEST_ADDR", "TIME_STAMP",
			"MO_MSC_ADDR", "DELIVER_COUNTS", "DEST_FEE", "RN", "UDL" };

	private static final String[] LOCUS_OUTPUTS = {"SM_ID","ISMOMT","TRACE_DATE","SMS_STATUS",
		"RESULT","EMA_CLASS","UDHI","MT_MSC_ADDR","M_MSC_ADDR"};

	private static Logger LOG = LoggerFactory.getLogger(QuerySMS.class);

	public static void main(String[] args) throws Exception {
		 String ret = getListJsonData("8613986037370", "",
		 "2013-07-28 16:00:00", "2013-08-02 17:30:30", "SUBMIT_TIME", 1);
		 System.out.println("list:" + ret);
		
//		 String ret2 = getLocusJsonData("2233639335", "2013-08-01 16:00:00",
//				"2013-08-12 17:30:30", "1");
//		 System.out.println("locus:" + ret2);
//	
//		String ret3 = getDetailJsonData("2233639335", "2013-08-01 16:00:00",
//				"2013-08-12 17:30:30", "1");
//		 System.out.println("detail:" + ret3);
		 
	}

	private static List<Output> buildOutDetail() {
		List<Output> outputs = new ArrayList<Output>();
		for (String output : DETAIL_OUTPUTS) {
			outputs.add(new Output(output));
		}
		return outputs;
	}

	private static List<Output> buildOutList() {
		List<Output> outputs = new ArrayList<Output>();
		for (String output : LIST_OUTPUTS) {
			outputs.add(new Output(output));
		}
		return outputs;
	}

	private static List<Output> buildLocusList() {
		List<Output> outputs = new ArrayList<Output>();
		for (String output : LOCUS_OUTPUTS) {
			outputs.add(new Output(output));
		}
		return outputs;
	}

	public static String getLocusJsonData(String smsId, String startTime,
			String endTime, String isHWZX) throws TException, IOException {
		// 连接thrift server端接口
		BaseClient conn = ClientUtils.getClientInstance();

		// 设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = ClientUtils.getHeader();
		// 设置分页参数
		Page page = new Page(1, 1000);
		// 添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildLocusList();
		// 过滤条件
		List<Filter> filters = new ArrayList<Filter>();
		// SM_ID
		filters.add(new Filter("SM_ID", MatchType.EQUAL, smsId));
		filters.add(new Filter("ISHWZX", MatchType.EQUAL, isHWZX));

		// 排序条件
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("TIME_STAMP", OrderType.ASC));

		Map<String, String> attributes = new HashMap<String, String>();
		Condition condition = new Condition(startTime, endTime, outputs,
				filters, orders, attributes);

		// 调用thrift接口
		Result result = conn.querySmsTrace(header, page, condition);
		if (result.errorcode != 0) {
			LOG.error(result.errormsg);
			// 关闭连接
			conn.close();
			return "";
		} else {
			if (null == result.getDatas())
				return "";
			List<Map<String, String>> list = JsonUtils.strlist2MapList(
					LOCUS_OUTPUTS, result.getDatas().datas);
			String strJson = JsonUtils.listmap2Json(list);
			// 关闭连接
			conn.close();
			return strJson;
		}
	}

	public static String getDetailJsonData(String smsId, String startTime,
			String endTime, String isHWZX) throws TException, IOException {
		// 连接thrift server端接口
		BaseClient conn = ClientUtils.getClientInstance();

		// 设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = ClientUtils.getHeader();
		// 设置分页参数
		Page page = new Page(1, 1000);
		// 添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOutDetail();
		// 过滤条件
		List<Filter> filters = new ArrayList<Filter>();
		// SM_ID
		filters.add(new Filter("SM_ID", MatchType.EQUAL, smsId));
		filters.add(new Filter("ISHWZX", MatchType.EQUAL, isHWZX));

		// 排序条件
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("SUBMIT_TIME", OrderType.ASC));

		Map<String, String> attributes = new HashMap<String, String>();
		Condition condition = new Condition(startTime, endTime, outputs,
				filters, orders, attributes);

		// 调用thrift接口
		Result result = conn.querySms(header, page, condition);
		if (result.errorcode != 0) {
			LOG.error(result.errormsg);
			// 关闭连接
			conn.close();
			return "";
		} else {
			if (null == result.getDatas())
				return "";
			List<Map<String, String>> list = JsonUtils.strlist2MapList(
					DETAIL_OUTPUTS, result.getDatas().datas);
			String strJson = JsonUtils.listmap2Json(list);
			// 关闭连接
			conn.close();
			return strJson;
		}
	}

	public static String getListJsonData(String call, String called,
			String startTime, String endTime, String sortName, int sort)
			throws TException, IOException {
		// 连接thrift server端接口
		BaseClient conn = ClientUtils.getClientInstance();

		// 设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = ClientUtils.getHeader();
		// 设置分页参数
		Page page = new Page(1, 1000);
		// 添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOutList();
		// 过滤条件
		List<Filter> filters = new ArrayList<Filter>();
		// ORG_ADDRESS
		if (isNotBlank(call)) {
			filters.add(new Filter("ORG_ADDR", MatchType.EQUAL, call));
		}
		// REC_ADDRESS
		if (isNotBlank(called)) {
			filters.add(new Filter("DEST_ADDR", MatchType.EQUAL, called));
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
		Result result = conn.querySms(header, page, condition);
		if (result.errorcode != 0) {
			LOG.error(result.errormsg);
			// 关闭连接
			conn.close();
			return "";
		} else {
			if (null == result.getDatas())
				return "";
			List<Map<String, String>> list = JsonUtils.strlist2MapList(
					LIST_OUTPUTS, result.getDatas().datas);
			String strJson = JsonUtils.listmap2Json(list);
			// 关闭连接
			conn.close();
			return strJson;
		}
	}
}

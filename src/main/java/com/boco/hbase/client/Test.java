package com.boco.hbase.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

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

public class Test {
      
    public static void main(String[] args) {
    	Test qc = new Test();
    	try {
//    			qc.testQuery();
			qc.testQueryByKey();
//			 qc.testQueryTrace(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
   private List<Output> buildOut(){
	   List<Output> outputs = new ArrayList<Output>();	
	   outputs.add(new Output("SM_ID"));			   
	   outputs.add(new Output("EMA_CLASS"));
	   outputs.add(new Output("SCHEDULE"));
	   outputs.add(new Output("PRI"));
	   outputs.add(new Output("MMS"));
	   outputs.add(new Output("ORG_ADDR"));
	   outputs.add(new Output("SUBMIT_TIME"));		  
	   outputs.add(new Output("MT_MSC_ADDR"));
	   outputs.add(new Output("RESULT"));
	   outputs.add(new Output("ORG_FEE"));
	   outputs.add(new Output("SMS_STATUS"));
	   outputs.add(new Output("MESSAGE_TYPE"));
	   outputs.add(new Output("UD"));
	   outputs.add(new Output("DCS"));
	   outputs.add(new Output("UDHI"));
	   outputs.add(new Output("EXPIRE"));
	   outputs.add(new Output("PID"));
	   outputs.add(new Output("SRR"));
	   outputs.add(new Output("DEST_ADDR"));
	   outputs.add(new Output("TIME_STAMP"));	   
	   outputs.add(new Output("MO_MSC_ADDR"));
	   outputs.add(new Output("DELIVER_COUNTS"));
	   outputs.add(new Output("DEST_FEE"));
	   outputs.add(new Output("RN"));
	   outputs.add(new Output("UDL"));
	   return outputs;
   }
   public void testQueryByKey() throws TException, IOException{
   	
   	//连接thrift server端接口
   	BaseClient conn = BaseClient.getQueryClient("10.0.7.216", 8920);
   	
		long time = System.currentTimeMillis();
		//设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = new Header("test","test",60000);
		//设置分页参数
		Page page = new Page(1,1000);
		//添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOut();
		//过滤条件
		List<Filter> filters = new ArrayList<Filter>();		
		filters.add(new Filter("SM_ID",MatchType.EQUAL,"2233639335"));
		
		//排序条件
		List<Order> orders =new ArrayList<Order>();	
//		orders.add(new Order("SUBMIT_TIME",OrderType.ASC));
		
		String starttime = "2013-07-19 16:00:00";
		String endtime = "2013-08-20 17:30:30";
		
		Map<String,String> attributes = new HashMap<String,String>();
		Condition condition = new Condition(starttime,endtime,outputs,filters,orders,attributes);		
		
		//调用thrift接口
		Result result = conn.querySms(header, page,condition);
		if (result.errorcode !=0) {
			System.out.print(result.errormsg);
		} else {
			System.out.println("total:" + result.getDatas().count);
			System.out.println(result.errormsg);
			for (String s : result.getDatas().getMetadata()) {
				System.out.print(s + "|");
			}
			System.out.println();
			for (List<String> s : result.getDatas().datas) {
				for (String ps : s) {
					System.out.print(ps + "|");
				}
				System.out.println();
			}
		}
		System.out.println("cost time:" + (System.currentTimeMillis() - time));
		
		//关闭连接
		conn.close();
   }
   public void testQueryTrace() throws TException, IOException{	   	
	   	//连接thrift server端接口
	   	BaseClient conn = BaseClient.getQueryClient("10.0.7.216", 8920);  	
			long time = System.currentTimeMillis();
			//设置HBase用户名，密码，以及查询超时时间（单位毫秒）
			Header header = new Header("test","test",60000);
			//设置分页参数
			Page page = new Page(1,1000);
			//添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
			List<Output> outputs = new ArrayList<Output>();	
			
			outputs.add(new Output("SM_ID"));
			outputs.add(new Output("ISMOMT"));
			outputs.add(new Output("TIME_STAMP"));
			outputs.add(new Output("DEST_ADDR"));
			outputs.add(new Output("RESULT"));
			
			
			//过滤条件
			List<Filter> filters = new ArrayList<Filter>();		
			filters.add(new Filter("SM_ID",MatchType.EQUAL,"2233639335"));
			
			//排序条件
			List<Order> orders =new ArrayList<Order>();	
			
			String starttime = "2013-07-20 16:00:00";
			String endtime = "2013-08-20 17:30:30";
			
			Map<String,String> attributes = new HashMap<String,String>();
			Condition condition = new Condition(starttime,endtime,outputs,filters,orders,attributes);				
			//调用thrift接口
			Result result = conn.querySmsTrace(header, page,condition);
			if (result.errorcode !=0) {
				System.out.print(result.errormsg);
			} else {
				System.out.println("total:" + result.getDatas().count);
				System.out.println(result.errormsg);
				for (String s : result.getDatas().getMetadata()) {
					System.out.print(s + "|");
				}
				System.out.println();
				for (List<String> s : result.getDatas().datas) {
					for (String ps : s) {
						System.out.print(ps + "|");
					}
					System.out.println();
				}
			}
			System.out.println("cost time:" + (System.currentTimeMillis() - time));		
			//关闭连接
			conn.close();
	   }
    public void testQuery() throws TException, IOException{
    	
    	//连接thrift server端接口
    	BaseClient conn = BaseClient.getQueryClient("10.0.7.216", 8920);
    	
		long time = System.currentTimeMillis();
		//设置HBase用户名，密码，以及查询超时时间（单位毫秒）
		Header header = new Header("test","test",60000);
		//设置分页参数
		Page page = new Page(1,1000);
		//添加需要查询的字段，此处需要参照表设计文档（暂时还未定）
		List<Output> outputs = buildOut();
		//过滤条件
		List<Filter> filters = new ArrayList<Filter>();		
//		filters.add(new Filter("ORG_ADDR",MatchType.EQUAL,"8615172138675"));
		filters.add(new Filter("ORG_ADDR",MatchType.EQUAL,"10086"));
		
		//排序条件
		List<Order> orders =new ArrayList<Order>();	
		orders.add(new Order("SUBMIT_TIME",OrderType.ASC));
		
		String starttime = "2013-07-19 16:00:00";
		String endtime = "2013-08-21 17:30:30";
		
		Map<String,String> attributes = new HashMap<String,String>();
		Condition condition = new Condition(starttime,endtime,outputs,filters,orders,attributes);		
		
		//调用thrift接口
		Result result = conn.querySms(header, page,condition);
		if (result.errorcode !=0) {
			System.out.print(result.errormsg);
		} else {
			System.out.println("total:" + result.getDatas().count);
			System.out.println(result.errormsg);
			for (String s : result.getDatas().getMetadata()) {
				System.out.print(s + "|");
			}
			System.out.println();
			for (List<String> s : result.getDatas().datas) {
				for (String ps : s) {
					System.out.print(ps + "|");
				}
				System.out.println();
			}
		}
		System.out.println("cost time:" + (System.currentTimeMillis() - time));
		
		//关闭连接
		conn.close();
    }
}

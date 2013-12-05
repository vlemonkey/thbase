<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>GN 查询</title>
<%@ include file="script.jsp" %>
<script> 
var lazy;
var count;
function FindData(){ 
	// 清空
	cost.innerHTML=0;
	$('#mytable').datagrid('loadData', { total: 0, rows: [] });
	$('#RANDOM').val(new Date().getTime());
	$('#mytable').datagrid('load',{ 
	MSISDN:$('#MSISDN').val(), 
	START_TIME:$('#START_TIME').datebox("getValue"),
	END_TIME:$('#END_TIME').datebox("getValue"),
	RANDOM:$('#RANDOM').val()} 
	); 
	
	count = 0;
	lazy = setInterval("getCostSMS('cost')", 500);
}

function getCostSMS(name) {
	if (count > 200) {
		clearInterval(lazy);
	}
	count++;
	var queryCost = 0;
	var r = $('#RANDOM').val();
	$.post("GetCost", {
		RANDOM : r
	}, function(data) {
		queryCost = data;
		if (queryCost != "null") {
			$('#' + name).text(queryCost);
			clearInterval(lazy);
		}
	});
}

</script> 
</head>
<body>
	<div>GN查询耗时: <span id="cost">0</span> 毫秒</div>
	<table id='mytable' class="easyui-datagrid" style="width: auto;height:500px"
		url="GnQuery" title="请输入 GN 查询条件" rownumbers="true"
		toolbar="#searchtool" loadMsg="正在查询...">
		<thead>
			<tr>
				<th field="MSISDN" Width="120">手机号码</th>
				<th field="DELAY" width="120">连接延时</th>
				<th field="LAC" width="120">LAC信息</th>
				<th field="CI" width="150">CI信息</th>
				<th field="IMEI" width="150">IMEI</th>
				<th field="SERVICE_TYPE" width="80">业务类型</th>
				<th field="START_TIME" width="100">开始时间</th>
				<th field="END_TIME" width="100">结束时间</th>
				<th field="TOTAL_DELAY" width="100">时长(秒)</th>
				<th field="UP_BYTES" width="100">上行流量</th>
				<th field="DOWN_BYTES" width="100">下行流量</th>
				<th field="TOTAL_BYTES" width="100">总流量</th>
				<th field="MOBILE_CLASS" width="100">2G/3G激活</th>
				<th field="CLIENT_IP" width="100">用户IP</th>
				<th field="SERVER_IP" width="100">目的SP-IP</th>
				<th field="APN" width="100">APN</th>
				<th field="IMSI" width="100">IMSI</th>
				<th field="CONTENT_TYPE" width="100">Content-Type</th>
				<th field="HOST" width="100">HOST</th>
				<th field="SERVICE_INFO" width="100">URL</th>
			</tr>
		</thead>
	</table>

	<div id="searchtool" style="padding: 5px">
		<span>手机号:</span><input type="text" id="MSISDN" value="15071049482" size=10 />
		<span>开始时间:</span>
		<input class="easyui-datetimebox" id="START_TIME"  name="START_TIME" data-options="required:true" 
			value="2013-08-01 16:00:00" style="width:150px">
		<span>结束时间:</span>
		<input class="easyui-datetimebox" id="END_TIME"  name="END_TIME" data-options="required:true" 
			value="2013-08-01 17:30:30" style="width:150px">
		 <a href="javascript:FindData()" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'">查询</a>
	</div>
	
	<input type="hidden" id="RANDOM" />
</body>
</html>
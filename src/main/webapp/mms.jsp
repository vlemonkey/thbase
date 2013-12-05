<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>MMS 查询</title>
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
	ORG_ADDRESS:$('#ORG_ADDRESS').val(),
	REC_ADDRESS:$('#REC_ADDRESS').val(),
	START_TIME:$('#START_TIME').datebox("getValue"),
	END_TIME:$('#END_TIME').datebox("getValue"),
	SORT_NAME:$('#SORT_NAME').val(), 
	SORT:$('#SORT').val(), 
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
	<div>MMS查询耗时: <span id="cost">0</span> 毫秒</div>
	<table id='mytable' class="easyui-datagrid" style="width: auto;height:500px"
		url="MMSQuery" title="请输入 MMS 查询条件" rownumbers="true"
		toolbar="#searchtool" loadMsg="正在查询...">
		<thead>
			<tr>
				<th field="MESSAGE_ID" Width="120">消息ID</th>
				<th field="ORG_ADDRESS" width="120">发送方号码</th>
				<th field="REC_ADDRESS" width="120">接收方号码</th>
				<th field="RECV_TIME" width="150">提交时间</th>
				<th field="SEND_TIME" width="150">结束时间</th>
				<th field="MSG_LEN" width="80">大小</th>
				<th field="FAIL_CAUSE" width="200">状态</th>
			</tr>
		</thead>
	</table>

	<div id="searchtool" style="padding: 5px">
		<span>主叫手机号:</span><input type="text" id="ORG_ADDRESS" value="" size=10 />
		<span>被叫手机号:</span><input type="text" id="REC_ADDRESS" value="008618727552057" size=10 />
		<span>开始时间:</span>
		<input class="easyui-datetimebox" id="START_TIME" data-options="required:true" 
			value="2013-08-01 16:00:00" style="width:150px">
		<span>结束时间:</span>
		<input class="easyui-datetimebox" id="END_TIME" data-options="required:true" 
			value="2013-08-12 17:30:30" style="width:150px">
		<span>排序字段:</span>
		<select id="SORT_NAME">
			<option value="RECV_TIME">发送时间</option>
			<option value="SEND_TIME">接收时间</option>
		</select>
		<span>升降序:</span>
		<select id="SORT">
			<option value="1">升序</option>
			<option value="2">降序</option>
		</select>
		 <a href="javascript:FindData()" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'">查询</a>
	<div>
	
	<input type="hidden" id="RANDOM" />
</body>
</html>
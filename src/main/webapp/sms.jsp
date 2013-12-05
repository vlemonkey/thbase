<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>Insert title here</title>
<%@ include file="script.jsp"%>
<script>
	$(document)
			.ready(
					function() {
						$("#mytable")
								.datagrid(
										{
											title : 'SMS日志列表',
											height : 500,
											//是否折叠  
											collapsible : false,
											fit : false,//自动大小  
											fitColumns : true,
											//数据在一行显示  
											nowrap : false,
											//行条纹化  
											striped : true,
											//固定序号  
											rownumbers : true,
											//是否可以多选  
											singleSelect : true,
											remoteSort : false,
											// idField: 'ID',  
											url : 'SMSQuery',
											loadMsg : '正在加载，请稍后。。。',
											//是否显示分页  
											pagination : false,
											//固定列  
											columns : [ [
													{
														title : "消息ID",
														field : 'SM_ID',
														width : 150
													},
													{
														title : "是否长短信",
														field : 'UDHI',
														width : 150
													},
													{
														title : "长短信多包信息",
														field : 'EMA_CLASS',
														width : 150
													},
													{
														title : "短信长度",
														field : 'UDL',
														width : 150
													},
													{
														title : "发送方号码",
														field : 'ORG_ADDR',
														width : 150
													},
													{
														title : "接收方号码",
														field : 'DEST_ADDR',
														width : 150
													},
													{
														title : "提交时间",
														field : 'SUBMIT_TIME',
														width : 150
													},
													{
														title : "结束时间",
														field : 'TIME_STAMP',
														width : 150
													},
													{
														title : "短消息状态",
														field : 'SMS_STATUS',
														width : 150
													},
													{
														title : "投递次数",
														field : 'DELIVER_COUNTS',
														width : 150
													},
													{
														title : "厂家",
														field : 'ISHWZX',
														width : 150
													},
													{
														field : 'opt',
														title : '操作',
														width : 150,
														align : 'center',
														rowspan : 2,
														formatter : function(
																value, rec) {
															return "<span><a href=\"javascript:view('"
																	+ rec.SM_ID
																	+ "','" + rec.ISHWZX + "')\">查看</a>&nbsp;</span>"
																	+ "<span><a href=\"javascript:locus('"
																	+ rec.SM_ID
																	+ "','" + rec.ISHWZX + "')\">轨迹</a>&nbsp;</span>";
														}
													} ] ],
											toolbar : "#searchtool"
										});
					});

	var lazy;
	var count;
	function FindData() {
		// 清空
		cost.innerHTML = 0;
		$('#mytable').datagrid('loadData', {
			total : 0,
			rows : []
		});
		$('#RANDOM').val(new Date().getTime());
		$('#mytable').datagrid('load', {
			ORG_ADDRESS : $('#ORG_ADDRESS').val(),
			REC_ADDRESS : $('#REC_ADDRESS').val(),
			START_TIME : $('#START_TIME').datebox("getValue"),
			END_TIME : $('#END_TIME').datebox("getValue"),
			SORT_NAME : $('#SORT_NAME').val(),
			SORT : $('#SORT').val(),
			RANDOM : $('#RANDOM').val()
		});
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

	function view(id, factory) {
		$('#RANDOM').val(new Date().getTime());
		var r = $('#RANDOM').val();
		$.post("SMSDetail", {
			SM_ID : id,
			START_TIME : $('#START_TIME').datebox("getValue"),
			END_TIME : $('#END_TIME').datebox("getValue"),
			ISHWZX: factory,
			RANDOM : r
		}, function(data) {
			if (data.length > 2) {
				data = eval(data);
				var arr = [ "1SM_ID", "1EMA_CLASS", "1SCHEDULE", "1PRI",
						"1MMS", "1ORG_ADDR", "1SUBMIT_TIME", "1MT_MSC_ADDR",
						"1RESULT", "1ORG_FEE", "1SMS_STATUS", "1MESSAGE_TYPE",
						"1UD", "1DCS", "1UDHI", "1EXPIRE", "1PID", "1SRR",
						"1DEST_ADDR", "1TIME_STAMP", "1MO_MSC_ADDR",
						"1DELIVER_COUNTS", "1DEST_FEE", "1RN", "1UDL" ];
				for ( var i = 0; i < arr.length; i++) {
					var v = eval("data[0]." + arr[i].substring(1));
					$('#' + arr[i]).text(v);
				}
			}
			getCost("cost1");
			$('#win').window('open');
		});
	}
	//关闭登录窗口
	function closeWin(name) {
		$('#' + name).window('close');
	}

	function locus(id, factory) {
		deleteLocusRow();
		$('#RANDOM').val(new Date().getTime());
		var r = $('#RANDOM').val();
		$.post("SMSLocus", {
			SM_ID : id,
			START_TIME : $('#START_TIME').datebox("getValue"),
			END_TIME : $('#END_TIME').datebox("getValue"),
			ISHWZX: factory,
			RANDOM : r
		}, function(data) {
			if (data.length > 2) {
				data = eval(data);
				for ( var i = 0; i < data.length; i++) {
					var newRow = document.all("locustable").insertRow();
					newRow.insertCell().innerHTML = data[i].SM_ID;
					newRow.insertCell().innerHTML = data[i].ISMOMT;
					newRow.insertCell().innerHTML = data[i].TRACE_DATE;
					newRow.insertCell().innerHTML = data[i].M_MSC_ADDR;
					newRow.insertCell().innerHTML = data[i].SMS_STATUS;
					newRow.insertCell().innerHTML = data[i].RESULT;
				}
			}
			getCost("cost2");
			$("#locuswin").window("open");
		});
	}

	function deleteLocusRow() {
		var table = document.all("locustable");
		while (table.rows.length > 1) {
			table.deleteRow(1);
		}
	}
</script>
</head>
<body>
	<div>
		SMS查询耗时: <span id="cost">0</span> 毫秒
	</div>
	<table id='mytable'>
	</table>

	<div id="searchtool" style="padding: 5px">
		<span>主叫手机号:</span><input type="text" id="ORG_ADDRESS" value=""
			size=10 /> <span>被叫手机号:</span><input type="text" id="REC_ADDRESS"
			value="8613972792221" size=10 /> <span>开始时间:</span> <input
			class="easyui-datetimebox" id="START_TIME"
			data-options="required:true" value="2013-08-28 00:00:00"
			style="width: 150px"> <span>结束时间:</span> <input
			class="easyui-datetimebox" id="END_TIME" data-options="required:true"
			value="2013-08-28 12:00:00" style="width: 150px"> <span>排序字段:</span>
		<select id="SORT_NAME">
			<option value="SUBMIT_TIME">发送时间</option>
			<option value="TIME_STAMP">接收时间</option>
		</select> <span>升降序:</span> <select id="SORT">
			<option value="1">升序</option>
			<option value="2">降序</option>
		</select> <a href="javascript:FindData()" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'">查询</a>
	</div>

	<input type="hidden" id="RANDOM" />

	<!-- 短信轨迹弹出页面 -->
	<div id="locuswin" class="easyui-window" title="短信轨迹" closed="true"
		style="width: 790px; height: 450px; padding: 5px; align: center">
		<br>
		<table id="locustable" cellpadding="5" cellspacing="0"
			style="border-collapse: collapse; font-size: 12px; width: 98%; bordercolor: #FFF"
			border="1">
			<tr>
				<th width="100px">消息ID</th>
				<th width="100px">轨迹类型</th>
				<th width="100px">轨迹时间</th> 
				<th width="100px">相邻网元</th>
				<th width="100px">处理状态</th>
				<th width="100px">错误描述</th>
			</tr>
		</table>
		<div>
			SMS轨迹查询耗时: <span id="cost2">0</span> 毫秒
		</div>
		<p></p>
		<div region="south" border="false" style="text-align: center">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
				href="javascript:closeWin('locuswin')">关 闭</a>
		</div>
	</div>



	<!-- 详细信息弹出页面 -->
	<div id="win" class="easyui-window" title="详细信息" closed="true"
		style="width: 790px; height: 500px; padding: 5px; align: center">
		<br>
		<table cellpadding="5" cellspacing="0"
			style="border-collapse: collapse; font-size: 12px; width: 98%; bordercolor: #FFF"
			border="1">
			<tr>
				<td width="100px">消息ID</td>
				<td width="300px"><span id="1SM_ID">&nbsp;</span></td>
				<td width="100px">编码方式</td>
				<td width="300px"><span id="1DCS">&nbsp;</span></td>
			</tr>
			<tr>
				<td>长短信标识</td>
				<td><span id="1EMA_CLASS">&nbsp;</span></td>
				<td>UDHI</td>
				<td><span id="1UDHI">&nbsp;</span></td>
			</tr>
			<tr>
				<td>定时时间</td>
				<td><span id="1SCHEDULE">&nbsp;</span></td>
				<td>有效期</td>
				<td><span id="1EXPIRE">&nbsp;</span></td>
			</tr>
			<tr>
				<td>短信优先级</td>
				<td><span id="1PRI">&nbsp;</span></td>
				<td>协议标识ID</td>
				<td><span id="1PID">&nbsp;</span></td>
			</tr>
			<tr>
				<td>MMS标志</td>
				<td><span id="1MMS">&nbsp;</span></td>
				<td>状态报告</td>
				<td><span id="1SRR">&nbsp;</span></td>
			</tr>
			<tr>
				<td>主叫号码</td>
				<td><span id="1ORG_ADDR">&nbsp;</span></td>
				<td>被叫号码</td>
				<td><span id="1DEST_ADDR">&nbsp;</span></td>
			</tr>
			<tr>
				<td>提交时间</td>
				<td><span id="1SUBMIT_TIME">&nbsp;</span></td>
				<td>完成时间</td>
				<td><span id="1TIME_STAMP">&nbsp;</span></td>
			</tr>
			<tr>
				<td>相邻提交网元</td>
				<td><span id="1MT_MSC_ADDR">&nbsp;</span></td>
				<td>相邻投递网元</td>
				<td><span id="1MO_MSC_ADDR">&nbsp;</span></td>
			</tr>
			<tr>
				<td>错误码</td>
				<td><span id="1RESULT">&nbsp;</span></td>
				<td>发送次数</td>
				<td><span id="1DELIVER_COUNTS">&nbsp;</span></td>
			</tr>
			<tr>
				<td>主叫预付费</td>
				<td><span id="1ORG_FEE">&nbsp;</span></td>
				<td>被叫预付费</td>
				<td><span id="1DEST_FEE">&nbsp;</span></td>
			</tr>
			<tr>
				<td>处理状态</td>
				<td><span id="1SMS_STATUS">&nbsp;</span></td>
				<td>消息参考</td>
				<td><span id="1RN">&nbsp;</span></td>
			</tr>
			<tr>
				<td>消息类型</td>
				<td><span id="1MESSAGE_TYPE">&nbsp;</span></td>
				<td>消息内容长度</td>
				<td><span id="1UDL">&nbsp;</span></td>
			</tr>
			<tr>
				<td>消息内容</td>
				<td colspan="3"><textarea id="1UD" cols=75></textarea></td>
			</tr>
		</table>
		<div>
			SMS详单查询耗时: <span id="cost1">0</span> 毫秒
		</div>
		<p></p>
		<div region="south" border="false" style="text-align: center">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
				href="javascript:closeWin('win')">关 闭</a>
		</div>
	</div>
	<br>
	<!-- -->
	<input type="button" onclick="locus('2233639335','1')" value="轨迹" />
	<input type="button" onclick="view('2233639335', '2')" value="详单" />
</body>
</html>
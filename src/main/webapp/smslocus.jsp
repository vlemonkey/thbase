<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="script.jsp"%>
</head>
<body>
	<input type="hidden" id="RANDOM" />
	<!-- 短信轨迹弹出页面 -->
	<table id='locustable' class="easyui-datagrid"
		style="width: auto; height: 500px" url="SMSLocus" title="短信轨迹"
		rownumbers="true">
		<thead>
			<tr>
				<th field="SM_ID" Width="120">消息ID</th>
				<th field="ISMOMT" width="120">轨迹类型</th>
				<th field="TRACE_DATE" width="120">轨迹时间</th>
				<th field="SMS_STATUS" width="120">处理状态</th>
				<th field="EMA_CLASS" width="120">长短信标识</th>
				<th field="RESULT" width="150">错误描述</th>
			</tr>
		</thead>
	</table>
	<div>
		SMS详单查询耗时: <span id="cost2">0</span> 毫秒
	</div>
	<p></p>

	<script defer>
		var lazy;
		var count;

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

		function getUrlParam(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
			var r = window.location.search.substr(1).match(reg); //匹配目标参数
			if (r != null)
				return unescape(r[2]);
			return null; //返回参数值
		}

		function locus() {
			alert(1);
			cost2.innerHTML = 0;
			$('#RANDOM').val(new Date().getTime());
			var r = $('#RANDOM').val();
			alert(2);
			$('#locustable').datagrid('loadData', {
				total : 0,
				rows : []
			});
			alert(3);
			var id = getUrlParam("id");
			var startTime = getUrlParam("START_TIME");
			var endTime = getUrlParam("END_TIME");
			alert(id + startTime + endTime);
			$('#locustable').datagrid('load', {
				SM_ID : id,
				START_TIME : startTime,
				END_TIME : endTime,
				RANDOM : r
			});
			alert(4);
			
			count = 0;
			lazy = setInterval("getCostSMS('cost2')", 500);
		}

		setTimeout(locus(), 5000);
	</script>
</body>
</html>
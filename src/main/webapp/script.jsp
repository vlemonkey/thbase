<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/jquery/css/easyui.css">
<script type="text/javascript" src="<%=basePath %>/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/jquery/easyui-lang-zh_CN.js"></script>
<script>
function getCost(name) {
	var queryCost = 0;
	var r = $('#RANDOM').val();
	$.post("GetCost", {
		RANDOM : r
	}, function(data) {
		queryCost = data;
		if (queryCost != "null") {
			$('#' + name).text(queryCost);
		}
	});
}
</script>
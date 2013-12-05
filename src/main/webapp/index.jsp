<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>湖北日志查询系统</title>
<%@ include file="script.jsp" %>
</head>
<body class="easyui-layout">
	<div region="north" border="false"
		style="height: 60px; background: #B3DFDA;">
		<center>
			<h1>湖北日志查询系统</h1>
		</center>
	</div>
	<div region="west" split="true" title="目录"
		style="width: 200px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false">
			<div title="日志查询" style="overflow: auto;">
			<center>
				<p> </p>
				<p><a target="mainFrame" href="gn.jsp">GN查询</a></p>
				<p><a target="mainFrame" href="sms.jsp">SMS查询</a></p>
				<p><a target="mainFrame" href="mms.jsp">MMS查询</a></p>
			</center>
			</div>
		</div>
	</div>
	<div region="south" border="false"
		style="height: 90px; background: #A9FACD; padding: 10px;">
		<%@ include file="footer.jsp"%>
	</div>
	<div region="center" title="查询">
		<iframe name="mainFrame" scrolling="no" frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
	</div>
</body>
</html>
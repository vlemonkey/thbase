$(function() {
	$('#mmsTable').datagrid({
		title : 'My Title',
		iconCls : 'icon-save',
		width : 600,
		height : 350,
		nowrap : false,
		striped : true,
		collapsible : true,
		url : 'datagrid_data.json',
		sortName : 'code',
		sortOrder : 'desc',
		remoteSort : false,
		idField : 'code',
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			title : 'code',
			field : 'code',
			width : 80,
			sortable : true
		} ] ],
		columns : [ [ {
			title : 'Base Information',
			colspan : 3
		}, {
			field : 'opt',
			title : 'Operation',
			width : 100,
			align : 'center',
			rowspan : 2,
			formatter : function(value, rec) {
				return '<span style="color:red">Edit Delete</span>';
			}
		} ], [ {
			field : 'name',
			title : 'Name',
			width : 120
		}, {
			field : 'addr',
			title : 'Address',
			width : 120,
			rowspan : 2,
			sortable : true,
			sorter : function(a, b) {
				return (a > b ? 1 : -1);
			}
		}, {
			field : 'col4',
			title : 'Col41',
			width : 150,
			rowspan : 2
		} ] ],
		pagination : true,
		rownumbers : true
	});
	var p = $('#mmsTable').datagrid('getPager');
	if (p) {
		$(p).pagination({
			onBeforeRefresh : function() {
				alert('before refresh');
			}
		});
	}
});
function resize() {
	$('#mmsTable').datagrid('resize', {
		width : 700,
		height : 400
	});
}
function getSelected() {
	var selected = $('#mmsTable').datagrid('getSelected');
	if (selected) {
		alert(selected.code + ":" + selected.name + ":" + selected.addr + ":"
				+ selected.col4);
	}
}
function getSelections() {
	var ids = [];
	var rows = $('#mmsTable').datagrid('getSelections');
	for ( var i = 0; i < rows.length; i++) {
		ids.push(rows[i].code);
	}
	alert(ids.join(':'));
}
function clearSelections() {
	$('#mmsTable').datagrid('clearSelections');
}
function selectRow() {
	$('#mmsTable').datagrid('selectRow', 2);
}
function selectRecord() {
	$('#mmsTable').datagrid('selectRecord', '002');
}
function unselectRow() {
	$('#mmsTable').datagrid('unselectRow', 2);
}
function mergeCells() {
	$('#mmsTable').datagrid('mergeCells', {
		index : 2,
		field : 'addr',
		rowspan : 2,
		colspan : 2
	});
}
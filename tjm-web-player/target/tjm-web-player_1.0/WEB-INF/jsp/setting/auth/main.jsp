<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Auth Setting</title>
<script src="http://code.jquery.com/jquery-1.10.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<%-- easyui --%>
<script src="${pageContext.request.contextPath}/js/easyui/easyloader.js"></script>
<script type="text/javascript">
$(function() {
	
	$('.funcBlock').bind({
		mouseover : function() {
			$(this).css('text-decoration','underline');
		},mouseout:function(){
			$(this).css('text-decoration','none');
		}
	});
	$('.home').bind('click', function(){
		location.href = '<%=request.getContextPath()%>/';
	});
	$('.auth').bind('click', function(){
		location.href = '<%=request.getContextPath()%>/setting/auth/';
	});
	$('.visibility').bind('click', function(){
		location.href = '<%=request.getContextPath()%>
	/setting/visibility/';
						});

	});
</script>
<style type="text/css">
.funcBlock {
	float: left;
	width: 80px;
	height: 20px;
	text-align: center;
	color: #B3B3B3;
	font-weight: bold;
	cursor: pointer;
	padding-left: 5px;
	border-left: solid thin #B3B3B3;
}
</style>
</head>
<body style="margin: 0px; padding: 0px;">
	<%-- function Row --%>
	<div
		style="height: 25px; background-color: #422121; padding: 15px 30px 0px 30px">
		<div class="funcBlock home">Home</div>
		<div class="easyui-menubutton funcBlock setting"
			data-options="menu:'#mm'">Setting</div>
	</div>
	<div id="mm" style="width: 150px;">
		<div class="auth">Auth</div>
		<div class="visibility">Visibility</div>
	</div>


	<div id="menu" style="width: 250px; height: 450px;">
		<ul id="tree" class="easyui-tree"
			data-options=" 
					checkbox:true,
                    method:'post',
                    url:'<%=request.getContextPath()%>/setting/visibility/folder',
                    onClick:function(node){
                    	onclickCallBack(node);
                    }
                    ">
		</ul>
	</div>


</body>
</html>
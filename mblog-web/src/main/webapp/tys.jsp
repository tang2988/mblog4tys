<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="./assets/vendors/laydate/laydate.js"></script>
</head>
<body>

<div>
	<ul>
		<li>
		<select name="sysSrc">
			<option value="All">ALL</option>
			<option value="RYX">RYX瑞银信</option>
			<option value="YB">YB易宝</option>
		</select>
		</li>
		<li>
			<input name="yearmonthdatestart" onclick="laydate({ format: 'YYYYMMDD'})">
			<input name="yearmonthdateend" onclick="laydate({ format: 'YYYYMMDD'})"> 
		</li>
		<li>
			<input name="mobile"  >
		</li>
		<li>
			<input name="button" value="同步数据" onclick="sync()"  >
			<input name="button" value="查询" onclick="list()"  >
		</li>
	</ul>
</div>

</body>
</html>
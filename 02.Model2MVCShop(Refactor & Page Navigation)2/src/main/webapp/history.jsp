<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

<title>��� ��ǰ ����</title>

</head>
<body>
	����� ��� ��ǰ�� �˰� �ִ�
<br>
<br>
<%
	request.setCharacterEncoding("euc-kr");
	response.setCharacterEncoding("euc-kr");
%>
<c:forEach var="i" items="${URLDecoder.decode(cookie.history.value,'euc_kr').split(',')}" begin="0" step="1">
	<a href="/getProduct.do?prodNo=${i}&menu=search"	target="rightFrame">${i}</a><br/>
</c:forEach>
<button onclick="window.close();">�ݱ�</button>
</body>
</html>
<%@page import="com.model2.mvc.common.util.CommonUtil"%>
<%@page import="com.model2.mvc.common.Page"%>
<%@page import="com.model2.mvc.service.domain.Purchase"%>
<%@page import="com.model2.mvc.common.Search"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%
	List<Purchase> list= (List<Purchase>)request.getAttribute("list");
	Page resultPage=(Page)request.getAttribute("resultPage");
	
	Search search = (Search)request.getAttribute("search");
	//==> null �� ""(nullString)���� ����
	String searchCondition = CommonUtil.null2str(search.getSearchCondition());
	String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
	
	String menu = request.getParameter("menu");
%>
<!DOCTYPE html>
<html>
<head>
<title>���� ��� ��ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	const num = /^[0-9]+/g;
	// �˻� / page �ΰ��� ��� ��� Form ������ ���� JavaScrpt �̿�  
	function fncGetProductList(currentPage) {
		const keyword = document.getElementById("searchKeyword").value;
		//if(keyword != ""){
		//	if(!num.test(keyword)){
		//		alert("���ڸ�");
		//		return;
		//	}
		//}
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();
	}
	
	function fncPageProductList(currentPage){
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();
	}
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
						���� �����ȸ
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37">
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">			
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option id="searchCondition" value="0" <%= (searchCondition.equals("0") ? "selected" : "")%>>��ǰ��ȣ</option>
				<option id="searchCondition" value="1" <%= (searchCondition.equals("1") ? "selected" : "")%>>��ǰ��</option>
			</select>
			<input 	type="text" name="searchKeyword" value="<%= searchKeyword %>"  class="ct_input_g" id="searchKeyword"
							style="width:200px; height:20px" >
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23"/>
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList('1');">�˻�</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >
			��ü  <%= resultPage.getTotalCount() %> �Ǽ�,	���� <%= resultPage.getCurrentPage() %> ������
		</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ��ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">ȸ����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ȭ��ȣ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">�����Ȳ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��������</td>		
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<%
		for(int i=0; i<list.size(); i++) {
			Purchase vo = list.get(i);
	%>
	<tr class="ct_list_pop">
		<td align="center"><%= i + 1 %></td>
		<td><input type="hidden" value="<%= vo.getBuyer().getUserId()%>" /></td>
		<td align="left">
			<%if(vo.getTranCode().equals("1  ")||vo.getTranCode().equals("2  ")) {%>
				<a href="/getPurchase.do?tranNo=<%=vo.getTranNo() %>"><%= vo.getBuyer().getUserId()%></a>
			<%}else{ %>
				<%= vo.getBuyer().getUserId()%>
			<%} %>
		</td>
		<td></td>
		<td align="left"><%= vo.getReceiverName() %></td>
		<td></td>
		<td align="left"><%= vo.getReceiverPhone() %>	</td>
		<td></td>
		<td align="left">
			<%if(vo.getTranCode().equals("1  ")) {%>
				���� ���ſϷ� ���� �Դϴ�.
			<%} else if(vo.getTranCode().equals("2  ")) {%>
				���� ����� ���� �Դϴ�.
			<%} else {%>
				���� ��ۿϷ� ���� �Դϴ�.
			<%} %>
		</td>
		<td></td>
		<td align="left">
			<% if(vo.getTranCode().equals("2  ")) {%>
				<a href="/updateTranCode.do?prodNo=<%=vo.getPurchaseProd().getProdNo()%>&tranCode=3&con=user">���ǵ���</a>
			<%} else {%>
			<%} %>
		</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<% } %>
</table>

<!-- PageNavigation Start... -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top:10px;">
	<tr>
		<td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
			<% if( resultPage.getCurrentPage() <= resultPage.getPageUnit() ){ %>
					�� ����
			<% }else{ %>
					<a href="javascript:fncPageProductList('<%=resultPage.getCurrentPage()-1%>')">�� ����</a>
			<% } %>

			<%	for(int i=resultPage.getBeginUnitPage();i<= resultPage.getEndUnitPage() ;i++){	%>
					<a href="javascript:fncPageProductList('<%=i %>');"><%=i %></a>
			<% 	}  %>
	
			<% if( resultPage.getEndUnitPage() >= resultPage.getMaxPage() ){ %>
					���� ��
			<% }else{ %>
					<a href="javascript:fncPageProductList('<%=resultPage.getEndUnitPage()+1%>')">���� ��</a>
			<% } %>
		
    	</td>
	</tr>
</table>
<!-- PageNavigation End... -->

</form>
</div>

</body>
</html>
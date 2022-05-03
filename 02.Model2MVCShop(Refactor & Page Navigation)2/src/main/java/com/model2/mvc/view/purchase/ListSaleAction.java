package com.model2.mvc.view.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListSaleAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("saleList시작");
		Search search = new Search();
		String menu = request.getParameter("menu");
		
		int currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		search.setCurrentPage(currentPage);
		//System.out.println(searchVO.getCurrentPage()+"PAGE며ㅛㅊ페이지");
		
		
		// web.xml  meta-data 로 부터 상수 추출 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);		
				
		List<Product> nList = new ArrayList<Product>();					//product
		List<Product> pList= (List<Product>)request.getAttribute("list");
		
		//System.out.println("111번"+pList.get(1).getProdNo());
		String num = "";
		if(pList !=null) {
			for(int i=0; i<pList.size(); i++) {				
				num += ","+pList.get(i).getProdNo();	
			}			
		}
		System.out.println(num);
		String sSearch = request.getParameter("searchKeyword")+num;
		System.out.println("search목록"+sSearch);
		
		String condition =request.getParameter("serachCondition");
		
		if(condition !=null) {
			if(condition.equals("1")||condition.equals("2")) {
				if(request.getParameter("serachKeyword").equals("")) {
					condition ="0";
				}
			}
		}
		
		search.setSearchCondition(condition);
		search.setSearchKeyword(sSearch);		

		PurchaseService service = new PurchaseServiceImpl();
		//Map<String,Object> pmap=service.getPurchaseList(search);
		Map<String,Object> pmap=service.getSaleList(search);//purchase
		
		// 여기까지 완료
		List<Purchase> PurchaseList = (List<Purchase>)pmap.get("list");
		
		if(pmap !=null) {
			Product po = null;
			for(int i=0; i<pList.size(); i++) {
				po = pList.get(i);				
				for(int j=0; j<PurchaseList.size(); j++) {
					if(po.getProdNo()==PurchaseList.get(j).getPurchaseProd().getProdNo()) {
						po.setProTranCode(PurchaseList.get(j).getTranCode());
					}
				}
								
				nList.add(po);
			}			
		}
		for (Product p12 : nList) {
			System.out.println(p12);
		}
		
		// Model View 연결
		request.setAttribute("listp", nList);
//		request.setAttribute("search", search);
//		request.setAttribute("resultPage", resultPage);				
		
		return "forward:/product/listProduct.jsp";
	}

}

package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("upcode����");
		Purchase vo = new Purchase();
		Product pvo = new Product();
		pvo.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		vo.setPurchaseProd(pvo);
		vo.setTranCode(request.getParameter("tranCode"));
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(vo);
		String menu =request.getParameter("menu");
		System.out.println(menu);
		/*
		 	1 => ������
		 	2 => �����
		 	3 => ���ſϷ�
		*/
		
		if(request.getParameter("con").equals("user")) {
			return "/listPurchase.do";
		} 
			System.out.println(menu);
			return "redirect:/listProduct.do?menu="+menu;
		
	}

}

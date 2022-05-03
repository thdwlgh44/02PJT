package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Purchase pVo = new Purchase();
		
		// 유저정보 찾기
		HttpSession session = request.getSession(true);
		User uVo = (User)session.getAttribute("user");
		
		// 상품정보 찾기
		ProductService prodService = new ProductServiceImpl();
		Product prodVo=prodService.getProduct(Integer.parseInt(request.getParameter("prodNo")));
		
		// 구매 데이터 넣기
		pVo.setBuyer(uVo);
		pVo.setPurchaseProd(prodVo);
		System.out.println(request.getParameter("divyAddr"));		
		System.out.println(request.getParameter("divyDate"));
		pVo.setDivyAddr(request.getParameter("divyAddr"));
		pVo.setDivyDate(request.getParameter("divyDate"));
		pVo.setDivyRequest(request.getParameter("divyRequest"));
		pVo.setPaymentOption(request.getParameter("paymentOption"));
		pVo.setReceiverName(request.getParameter("receiverName"));
		pVo.setReceiverPhone(request.getParameter("receiverPhone"));
		pVo.setTranCode("1");
		
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(pVo);
		
		return "redirect:/listPurchase.do";
	}

}

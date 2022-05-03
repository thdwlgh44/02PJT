package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Purchase vo = new Purchase();
		vo.setPaymentOption(request.getParameter("paymentOption"));
		vo.setReceiverName(request.getParameter("receiverName"));
		vo.setReceiverPhone(request.getParameter("receiverPhone"));
		vo.setDivyAddr(request.getParameter("divyAddr"));
		vo.setDivyRequest(request.getParameter("divyRequest"));
		vo.setDivyDate(request.getParameter("divyDate"));
		vo.setTranNo(Integer.parseInt(request.getParameter("tranNo")));
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updatePurchase(vo);
		
		return "redirect:/listPurchase.do";
	}

}

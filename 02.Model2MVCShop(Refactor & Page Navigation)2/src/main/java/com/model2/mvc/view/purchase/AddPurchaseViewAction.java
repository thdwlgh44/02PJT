package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.dao.ProductDAO;

public class AddPurchaseViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User uVo=(User)session.getAttribute("user");
		System.out.println(uVo);
		// 상품 번호 찾기
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		// 상품찾기
		ProductDAO prodDAO = new ProductDAO();
		Product prodVO = null;
		prodVO = prodDAO.findProduct(prodNo);
		
		request.setAttribute("prodVO", prodVO);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

}

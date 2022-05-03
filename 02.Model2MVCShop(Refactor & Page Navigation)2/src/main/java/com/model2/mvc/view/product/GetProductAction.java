package com.model2.mvc.view.product;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		int prodNO=Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService service = new ProductServiceImpl();
		Product vo=service.getProduct(prodNO);		
		request.setAttribute("vo", vo);
		
		Cookie[] cookies = request.getCookies();
		Cookie cookie =null;
		Cookie oCookie = null;
		String history = null;		
		
		if(cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				
				oCookie = cookies[i];
				if (oCookie.getName().equals("history")) {
					history =(URLDecoder.decode(oCookie.getValue(), "euc_kr")); Integer.toString(vo.getProdNo());
					System.out.println(history);
					String[]h=history.split(",");
					List<String> list = new ArrayList<String>();
					for(int j=0; j<h.length; j++) {
						System.out.println(h[j]);
						if(!list.contains(h[j])) {
							list.add(h[j]);
						}
					}
					
					for(int j=0; j<list.size()-1; j++) {
						history += list.get(j)+",";
					}
					System.out.println(history);
					history += list.get(list.size()-1);
					System.out.println("getCookie history"+history);
					cookie = new Cookie("history",URLEncoder.encode(history,"EUC_KR"));
				} else {
					cookie = new Cookie("history", URLEncoder.encode(Integer.toString(vo.getProdNo()),"EUC_KR"));
				}
			}
		}					
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		
		return "forward:/product/getProduct.jsp";
	}

}

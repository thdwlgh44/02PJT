package com.model2.mvc.service.purchase.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;

public class PurchaseServiceImpl implements PurchaseService {
	private PurchaseDAO dao;
	private ProductDAO prodDAO;
	
	public PurchaseServiceImpl() {
		// TODO Auto-generated constructor stub
		dao = new PurchaseDAO();
		prodDAO = new ProductDAO();
	}
	
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		dao.insertPurchase(purchase);
	}

	@Override
	public Purchase getPurcahse(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return dao.findPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search,String userId) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPurchaseList(search,userId);
	}
	
	@Override
	public Map<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return dao.getSaleList(search);
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		dao.updatePurchase(purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		dao.updateTranCode(purchase);
	}

	

}

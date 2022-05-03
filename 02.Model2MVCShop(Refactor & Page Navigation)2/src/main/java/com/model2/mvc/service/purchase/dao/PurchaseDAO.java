package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class PurchaseDAO {
	public PurchaseDAO() {		
	}
	
	public Purchase findPurchase(int tranNo) {
		System.out.println("PDAO FindPurchase start");
		Connection con = null;
		User user = null;
		Purchase p = null;
		Product product = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
	
			con = DBUtil.getConnection();
			
			String sql = "select *\r\n"
					+ "from users u, product p, transaction t\r\n"
					+ "where u.user_id=t.buyer_id AND p.prod_no=t.prod_no AND t.tran_no =?";			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, tranNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// userVo 넣기
				user = new User();
				user.setUserId(rs.getString("USER_ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setRole(rs.getString("ROLE"));
				user.setSsn(rs.getString("SSN"));
				user.setPhone(rs.getString("CELL_PHONE"));
				user.setAddr(rs.getString("ADDR"));
				user.setEmail(rs.getString("EMAIL"));
				user.setRegDate(rs.getDate("REG_DATE"));
				
				// productVo 넣기
				product = new Product();
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				product.setProdDetail(rs.getString("prod_detail"));
				product.setManuDate(rs.getString("manufacture_day"));
				product.setPrice(rs.getInt("price"));
				product.setFileName(rs.getString("image_file"));
				product.setRegDate(rs.getDate("reg_date"));
				
				p = new Purchase();
				p.setBuyer(user);
				p.setPurchaseProd(product);
				p.setDivyAddr(rs.getString("demailaddr"));
				p.setDivyDate(rs.getString("dlvy_date"));
				p.setDivyRequest(rs.getString("dlvy_request"));
				p.setPaymentOption(rs.getString("payment_option"));
				p.setReceiverName(rs.getString("receiver_name"));
				p.setReceiverPhone(rs.getString("receiver_phone"));
				p.setTranCode(rs.getString("tran_status_code"));
				p.setOrderDate(rs.getDate("order_data"));
				p.setTranNo(rs.getInt("tran_no"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PDAO FindPurchase end");
		}		
		return p;
	}
	
	public Map<String, Object> getPurchaseList(Search search,String userId){
		System.out.println("PDAO getPurchaseList start");
		Connection con = null;
		Purchase p = null;
		Product prod = null;
		User u = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			con = DBUtil.getConnection();
			
			String sql = "SELECT t.*, p.prod_name\r\n"
					+ "FROM transaction t, product p \r\n"
					+ "WHERE t.prod_no=p.prod_no AND buyer_id = '"+userId+"'";
			if (search.getSearchCondition() != null) {
				if (search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("")) {
					sql += " AND prod_no='" + search.getSearchKeyword()
							+ "'";
				} else if (search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("")) {
					sql ="SELECT "
							+ "t.tran_no,t.prod_no,t.buyer_id,t.payment_option,t.receiver_name,t.receiver_phone,t.demailaddr,t.dlvy_request,t.tran_status_code,t.order_data,t.dlvy_date"
							+ " FROM transaction t, product p WHERE t.prod_no=p.prod_no AND prod_name LIKE'%"
							+search.getSearchKeyword()+"%'";
				}
			}
			sql += " ORDER BY t.prod_no";

			System.out.println("ProductDAO 원본SQL:>>>>>>> : "+sql);
			
			int totalCount = this.getTotalCount(sql);
			System.out.println("ProductDAO :: totalCOunt >>> "+totalCount);
			
			// 바뀐 sql
			sql = this.makeCurrentPageSql(sql, search);
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			System.out.println(search);
			
			List<Purchase> list = new ArrayList<Purchase>();
			while(rs.next()) {
				p = new Purchase();
				prod = new Product();
				u = new User();					
								
				
				prod.setProdNo(rs.getInt("prod_no"));
				prod.setProdName(rs.getString("prod_name"));
				p.setPurchaseProd(prod);
				
				u.setUserId(rs.getString("buyer_id"));					
				p.setBuyer(u);
				
				p.setTranNo(rs.getInt("tran_no"));
				p.setReceiverName(rs.getString("receiver_name"));
				p.setReceiverPhone(rs.getString("receiver_phone"));
				p.setDivyAddr(rs.getString("demailaddr"));
				p.setDivyRequest(rs.getString("dlvy_request"));
				p.setDivyAddr(rs.getString("dlvy_date"));
				p.setOrderDate(rs.getDate("order_data"));
				p.setTranCode(rs.getString("tran_status_code"));
				
				System.out.println("DB Purchase List Test"+p);
				list.add(p);
			}
			
			map.put("totalCount", new Integer(totalCount));
			System.out.println("list.size() : "+ list.size());
			map.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			System.out.println("PDAO GetPurchaseList end");
		}
		
		return map;
	}
	
	// 판매페이지
	public Map<String, Object> getSaleList(Search search){
		System.out.println("PDAO GetSaleList start");
		Connection con = null;
		Purchase p = null;
		Product prod = null;
		User u = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			con = DBUtil.getConnection();
			
			String sql = "SELECT * FROM transaction";
			String keyword =search.getSearchKeyword();
			String[] keywordA =keyword.split(",");
			//System.out.println(keywordA[0]);
			System.out.println(search.getSearchCondition()+"<<<<<<<<< 컨디션 번호");
			if(keywordA[0] =="" || keywordA[0] ==null) {
				sql += " WHERE prod_no='" + keywordA[0]
						+ "'";
			} else {
				sql += " WHERE";
				for(int i=1; i<keywordA.length-1; i++) {
					sql += " prod_no ='"+keywordA[i]+"' OR";
				}
				//System.out.println(keywordA[keywordA.length-1]);
				System.out.println(keywordA.length+"길이길이");
				sql += " prod_no ='"+keywordA[keywordA.length-1]+"'";
			}
			
			sql += " ORDER BY prod_no";

			System.out.println("ProductDAO SQL:>>>>>>> : "+sql);
			
			int totalCount = this.getTotalCount(sql);
			System.out.println("ProductDAO :: totalCOunt >>> "+totalCount);
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.println(search);
			
			List<Purchase> list = new ArrayList<Purchase>();
			while(rs.next()) {
				p = new Purchase();
				prod = new Product();
				u = new User();
				
				prod.setProdNo(rs.getInt("prod_no"));
				p.setPurchaseProd(prod);
				
				u.setUserId(rs.getString("buyer_id"));
				p.setBuyer(u);
				
				p.setTranNo(rs.getInt("tran_no"));
				p.setTranCode(rs.getString("tran_status_code"));
				
				System.out.println("DB Purchase List Test"+p);
				list.add(p);
			}
			map.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			System.out.println("PDAO GetSaleList end");
		}
		
		return map;
	}
	
	// 구매하기
	public void insertPurchase(Purchase p) {
		System.out.println("PDAO InsertPurchase start");
		Connection con =null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtil.getConnection();
			
			String sql ="insert into TRANSACTION values ((seq_transaction_tran_no.NEXTVAL),?,?,?,?,?,?,?,?,SYSDATE,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, p.getPurchaseProd().getProdNo());
			pstmt.setString(2,p.getBuyer().getUserId());
			pstmt.setString(3,p.getPaymentOption());
			pstmt.setString(4,p.getReceiverName());
			pstmt.setString(5,p.getReceiverPhone());
			System.out.println(p.getDivyDate());
			pstmt.setString(6,p.getDivyAddr());
			pstmt.setString(7,p.getDivyRequest());
			pstmt.setString(8,p.getTranCode());
			pstmt.setString(9,p.getDivyDate().replace("-", ""));
			
			int r = pstmt.executeUpdate();
			
			if(r == 1) {
				System.out.println("purchase Insert Sucess");
			} else {
				System.out.println("purchase Insert Fail");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			System.out.println("PDAO InsertPurchase end");
		}
	}
	
	// update
	public void updatePurchase(Purchase p) {
		System.out.println("PDAO UpdatePurchase start");
		Connection con =null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtil.getConnection();
			
			String sql ="update TRANSACTION\r\n"
					+ "SET\r\n"
					+ "payment_option =?,\r\n"
					+ "receiver_name=?,\r\n"
					+ "receiver_phone=?,\r\n"
					+ "demailaddr=?,\r\n"
					+ "dlvy_request=?,\r\n"
					+ "dlvy_date=?"
					+ "WHERE tran_no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,p.getPaymentOption());
			pstmt.setString(2,p.getReceiverName());
			pstmt.setString(3,p.getReceiverPhone());
			pstmt.setString(4,p.getDivyAddr());
			pstmt.setString(5,p.getDivyRequest());
			pstmt.setString(6,p.getDivyDate());
			pstmt.setInt(7,p.getTranNo());
			
			int r = pstmt.executeUpdate();
			if(r == 1) {
				System.out.println("Insert 성공");
				System.out.println("PDAO UpdatePurchase end");
			} else {
				System.out.println("Insert 실패");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {			
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateTranCode(Purchase p) {
		System.out.println("PDAO updateTranCode start");
		Connection con =null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtil.getConnection();
			
			String sql ="UPDATE TRANSACTION\r\n"
					+ "SET\r\n"
					+ "tran_status_code=?\r\n"
					+ "WHERE prod_no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, p.getTranCode());
			pstmt.setInt(2, p.getPurchaseProd().getProdNo());
			
			int r = pstmt.executeUpdate();
			if(r == 1) {
				System.out.println("Update 성공");
				System.out.println("PDAO updateTranCode end");
			} else {
				System.out.println("Update 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	// Page 전체 Row
	private int getTotalCount(String sql) throws Exception {
		sql = "SELECT COUNT(*) "
				+ "FROM ( " + sql +" ) countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt(1);
		}
		
		pstmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
		
	private String makeCurrentPageSql(String sql, Search search) {
		sql = "SELECT * "
				+ "FROM (SELECT inner_table.*, ROWNUM as idx "
						+ "FROM ("+sql+") inner_table "
						+ "WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" )"+
				"WHERE idx BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		System.out.println("PurchaseDAO SQL :: >>>>>  "+ sql);
		
		return sql;
	}
}

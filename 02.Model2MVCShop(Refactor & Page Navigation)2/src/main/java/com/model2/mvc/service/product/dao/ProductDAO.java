package com.model2.mvc.service.product.dao;

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

public class ProductDAO {

	public ProductDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public Product findProduct(int prodNo) {
		System.out.println("PDAO FindProduct start");
		Connection con = DBUtil.getConnection();
		Product product = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
	
			con = DBUtil.getConnection();
			
			String sql = "SELECT * FROM product WHERE prod_no=?";			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, prodNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				product = new Product();
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				product.setProdDetail(rs.getString("prod_detail"));
				product.setManuDate(rs.getString("manufacture_day"));
				product.setPrice(rs.getInt("price"));
				product.setFileName(rs.getString("image_file"));
				product.setRegDate(rs.getDate("reg_date"));
				System.out.println(product);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("sql Exception");
		} catch (Exception e) {
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
			System.out.println("PDAO FindProduct end");
		}		
		
		return product;
	}
	
	public Map<String, Object> getProductList(Search search){
		
		System.out.println("PDAO GetProductList start");
		Connection con = null;
		Product product = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		try {
	
			con = DBUtil.getConnection();
			
			String sql = "SELECT p.prod_no,prod_name,price,reg_date,NVL(tran_status_code,0) tranCode\r\n"
					+ "FROM product p, transaction t\r\n"
					+ "WHERE ";
			if (search.getSearchCondition() != null) {
				if (search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("")) {
					sql += "p.prod_no='" + search.getSearchKeyword()
							+ "' AND";
				} else if (search.getSearchCondition().equals("2") &&  !search.getSearchKeyword().equals("")) {
					sql += "p.prod_name LIKE'%" + search.getSearchKeyword()
							+ "%' AND";
				}
			}
			sql += " p.prod_no=t.prod_no(+)";
			sql += " ORDER BY prod_no";

			System.out.println("ProductDAO 원본SQL:>>>>>>> : "+sql);
			
			int totalCount = this.getTotalCount(sql);
			System.out.println("ProductDAO :: totalCOunt >>> "+totalCount);
			
			// 바뀐 sql
			sql = this.makeCurrentPageSql(sql, search);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.println(search);
			
			List<Product> list = new ArrayList<Product>();
			while(rs.next()) {
				product = new Product();
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				//product.setProdDetail(rs.getString("prod_detail"));
				//product.setManuDate(rs.getString("manufacture_day"));
				product.setPrice(rs.getInt("price"));
				//product.setFileName(rs.getString("image_file"));
				product.setRegDate(rs.getDate("reg_date"));
				product.setProTranCode(rs.getString("tranCode"));
				//System.out.println("trancode : >>>>>>>>>"+product.getProTranCode());
				System.out.println(product);

				list.add(product);
			}
			map.put("totalCount", new Integer(totalCount));
			System.out.println("list.size() : "+ list.size());
			map.put("list", list);			
			
		} catch (Exception e) {
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
			System.out.println("PDAO GetProductList end");
		}
		
		return map;
	}
	
	public void insertProduct(Product product) {
		System.out.println("PDAO InsertProduct start");
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {	
			con = DBUtil.getConnection();
			
			String sql = "insert into PRODUCT values ((seq_product_prod_no.NEXTVAL),?,?,?,?,?,sysdate)";			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, product.getProdName());
			pstmt.setString(2, product.getProdDetail());
			pstmt.setString(3, product.getManuDate());
			pstmt.setInt(4, product.getPrice());
			pstmt.setString(5, product.getFileName());
			int s = pstmt.executeUpdate();
			
			if(s==1) {
				System.out.println("Insert 성공");
			} else {
				System.out.println("SQL Insert 실패");
			}
		} catch (Exception e) {
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
			
			System.out.println("PDAO InsertProduct end");
		}
	}
	
	public void updateProduct(Product product) {
		System.out.println("PDAO UpdateProduct start");
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
	
			con = DBUtil.getConnection();
			
			String sql = "UPDATE PRODUCT\r\n"
					+ "SET\r\n"
					+ "prod_name =?,\r\n"
					+ "prod_detail =?,\r\n"
					+ "manufacture_day=?,\r\n"
					+ "price =?,\r\n"
					+ "image_file=?\r\n"
					+ "where prod_no =?";			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, product.getProdName());
			pstmt.setString(2, product.getProdDetail());
			pstmt.setString(3, product.getManuDate());
			pstmt.setInt(4, product.getPrice());
			pstmt.setString(5, product.getFileName());
			pstmt.setInt(6, product.getProdNo());
						
			int s = pstmt.executeUpdate();
			if(s==1) {
				System.out.println("Update 성공");
			} else {
				System.out.println("SQL Update 실패");
			}
			
		} catch (Exception e) {
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
			System.out.println("PDAO UpdateProduct end");
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
		System.out.println("ProductDAO SQL :: >>>>>  "+ sql);
		
		return sql;
	}
}

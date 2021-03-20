package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.*;
import ncu.im3069.demo.app.ApplicationHelper;
import ncu.im3069.demo.app.Member;
import ncu.im3069.tools.JsonReader;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.Application;
import ncu.im3069.demo.app.ProductHelper;

/**
 * Servlet implementation class ApplicationController
 */
@WebServlet("/api/application.do")
public class ApplicationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 /** ah，ApplicationHelper之物件與Application相關之資料庫方法（Sigleton） */
    private ApplicationHelper ah =  ApplicationHelper.getHelper();
      
    private ProductHelper ph = ProductHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
	        JsonReader jsr = new JsonReader(request);
	        String member_id = jsr.getParameter("id");
	        String type = jsr.getParameter("type");
	        String from = jsr.getParameter("from");
	        int id = Integer.parseInt(member_id);
	        //System.out.print(id);
	        //System.out.print(type);
	        //int member_id = 3;
	        //String type = "買家";
	        
	        /** 判斷該字串是否的種類，若為物主代表要取回物主編號的他人申請之資料，否則代表要取回買家自己申請之資料 */
	        if(type.equals("物主")) {
	        	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有物主的他人申請之資料，回傳之資料為JSONObject物件 */
	            //JSONObject query = ah.getByOwnerId(Integer.parseInt(member_id));
	            JSONObject query = ah.getByOwnerId(id);
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有申請資料取得成功");
	            resp.put("response", query);
	    
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        	
	        	
	        }
	        else if(type.equals("買家")) {
	        	
	        	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有該買家的自己申請之資料，回傳之資料為JSONObject物件 */
	            JSONObject query = ah.getByBuyerId(id);
	        	//JSONObject query = ah.getByBuyerId();
	        	
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有申請資料取得成功");
	            resp.put("response", query);
	    
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        /**訂單詳情*/
	        else {
	        	
	        	
	        	
	        	 /** 透過ApplicationHelper物件之getByApplicationId()方法取回這筆申請之資料，回傳之資料為JSONObject物件 */
	            JSONObject query = ah.getByApplicationId(id);
	            
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有申請資料取得成功");
	            resp.put("response", query);
	    
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        	
	        }
	        
	        
	       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        JSONArray jsa = new JSONArray();
        /** 取出經解析到JSONObject之Request參數 */
        /*String product_id = jsr.getParameter("product_id");
        System.out.print(product_id);
        String oid = jsr.getParameter("owner_id");
        System.out.print(oid);
        int owner_id = Integer.parseInt(oid);
        String bid = jsr.getParameter("buyer_id");
        int buyer_id = Integer.parseInt(bid);
        String mean_of_transaction = jsr.getParameter("mean_of_transaction");
        String t = jsr.getParameter("token");
        Double token = Double.parseDouble(t);*/
        /*int product_id = 2;
        int owner_id = 2;
        int buyer_id = 7;
        String mean_of_transaction ="以物易物";
        Double token = 50.0;*/
        int product_id = jso.getInt("product_id");
        //int owner_id = jso.getInt("owner_id");
        //System.out.print(oid);
        //int owner_id = Integer.parseInt(oid);
        int buyer_id = jso.getInt("buyer_id");
        //int buyer_id = Integer.parseInt(bid);
        String mean_of_transaction = jso.getString("mean_of_transaction");
        //Double token = jso.getDouble("token");
        //Double token = Double.parseDouble(t);
       
        
        JSONObject jproduct = ph.getByID(Integer.toString(product_id));
        jsa = jproduct.getJSONArray("data");
        JSONObject j = jsa.getJSONObject(0);
        String product_name = j.getString("name");
        Double token = j.getDouble("token");
        String description = j.getString("describtion");
        String category = j.getString("category");
        Boolean product_status = j.getBoolean("status");
        String area = j.getString("area");
        int owner_id = j.getInt("member_id");
        Product p = new Product(product_id,product_name, category, description, token, area, product_status);
        
       // Product product = ph.getById_me(Integer.parseInt(product_id));
        //product.setId(Integer.parseInt(product_id));
       //product.setId(product_id);
        
        /** 建立一個新的會員物件 */
        Application a = new Application(mean_of_transaction, owner_id, buyer_id, p, token);
        JSONObject data = ah.create(a);
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 註冊會員資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        
        int application_id = jso.getInt("application_id");
        int exchangedProduct_id = jso.getInt("exchangedProduct_id");
        String exchangedProduct_name = jso.getString("exchangedProduct_name");
        
        
        JSONObject query = ah.updateStatus(jso);
        if(!exchangedProduct_name.isEmpty()) {
        	
        	query.put("updateExchangedProduct",ah.updateExchangedProductId(exchangedProduct_id,application_id));
        	
        }
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "申請移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
        
        
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = ah.deleteById(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "申請移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}

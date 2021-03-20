package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.ApplicationHelper;
import ncu.im3069.demo.app.Help;
import ncu.im3069.demo.app.HelpHelper;
import ncu.im3069.demo.app.Member;
import ncu.im3069.tools.JsonReader;
import ncu.im3069.demo.app.ExpectedProduct;
import ncu.im3069.demo.app.Application;
import ncu.im3069.demo.app.ExpectedProductHelper;

/**
 * Servlet implementation class ApplicationController
 */
@WebServlet("/api/help.do")
public class HelpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private HelpHelper hh =  HelpHelper.getHelper();
      
    private ExpectedProductHelper eph = ExpectedProductHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelpController() {
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
	        String help_id = jsr.getParameter("id");
	        String type = jsr.getParameter("type");
	        String from = jsr.getParameter("from");
	        //String fail = jsr.getParameter("fail");
	        int id = Integer.parseInt(help_id);
	        //System.out.print(type);
	        //int member_id = 3;
	        //String type = "買家";
	        System.out.print(from);
	        /** 判斷該字串是否的種類，若為物主代表要取回物主編號的他人申請之資料，否則代表要取回買家自己申請之資料 */
	        if(type.equals("許願者")) {
	        	 /** 透過HelpHelper物件之getByWishId()方法取回所有物主的他人申請之資料，回傳之資料為JSONObject物件 */
	            //JSONObject query = ah.getByOwnerId(Integer.parseInt(member_id));
	            JSONObject query = hh.getByWisherId(id);
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有申請資料取得成功");
	            resp.put("response", query);
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        	
	        	
	        }
	        else if(type.equals("賣家")) {
	        	
	        	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有該買家的自己申請之資料，回傳之資料為JSONObject物件 */
	            JSONObject query = hh.getBysellerId(id);
	            //JSONObject query1 = hh.getBysellerId_wish(id);
	        	//JSONObject query = ah.getBysellerId();
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有申請資料取得成功");
	            resp.put("response", query);
	            //resp.put("response", query1);
	            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	            jsr.response(resp, response);
	        }
	        else if(type.equals("查看許願訂單")) {
	        	 JSONObject query = hh.getByWishId_look(id);
		            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		            JSONObject resp = new JSONObject();
		            resp.put("status", "200");
		            resp.put("message", "許願訂單");
		            resp.put("response", query);
		            resp.put("helpID", id);
		            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		            jsr.response(resp, response);
	        }
	        else if(from.equals("許願訂單")||from.equals("賣家許願訂單")) {
	        	 JSONObject query = hh.getByWishId_order(id);
		            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		            JSONObject resp = new JSONObject();
		            resp.put("status", "200");
		            resp.put("message", "取物成功/失敗");
		            resp.put("response", query);
		            resp.put("helpID", id);
		            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		            jsr.response(resp, response);
	        }
	        /**訂單詳情*/
	        else {
	        	 /** 透過HelpHelper物件之getByHelpId()方法取回這筆申請之資料，回傳之資料為JSONObject物件 */
	            JSONObject query = hh.getByhelpId(id);
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "幫助訂單");
	            resp.put("response", query);
	            resp.put("helpID", id);
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
        
        /** 取出經解析到JSONObject之Request參數 */
       /* String product_id = jsr.getParameter("product_id");
        String oid = jsr.getParameter("owner_id");
        System.out.print(oid);
        int owner_id = Integer.parseInt(oid);
        String bid = jsr.getParameter("buyer_id");
        int buyer_id = Integer.parseInt(bid);
        String mean_of_transaction = jsr.getParameter("mean_of_transaction");
        String t = jsr.getParameter("token");
        Double token = Double.parseDouble(t);*/
        //int product_id = 2;
        //int owner_id = 2;
        //int buyer_id = 7;
        //String mean_of_transaction ="以物易物";
        //Double token = 50.0;
        int product_id = jso.getInt("product_id");
        int wid = jso.getInt("wisher_id");
        System.out.print(wid);
        //int wisher_id = Integer.parseInt(wid);
        int sid = jso.getInt("seller_id");
        //int seller_id = Integer.parseInt(sid);
        Double token = jso.getDouble("token");
        //Double token = Double.parseDouble(t);
        System.out.print(token);
        
        ExpectedProduct eproduct = eph.getById_me(product_id);
        //product.setId(Integer.parseInt(product_id));
        //product.setId(product_id);
        
        /** 建立一個新的會員物件 */
        Help h = new Help( wid, sid, eproduct,token);
        JSONObject data = hh.create(h);
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
		JsonReader jsr = new JsonReader(request);
        //String help_id = jsr.getParameter("id");
        JSONObject jso = jsr.getObject();
       // String from = jsr.getParameter("type");
        int h_id = jso.getInt("id");
        //int s_id = jso.getInt("s_id");
        
        String from = jso.getString("type");
        System.out.print(from);
       // String status = jsr.getParameter("status");
        //String member_id = jsr.getParameter("member_id");
        //int id = Integer.parseInt(member_id);
        //int h_id = Integer.parseInt(help_id);
        //int h_id = 5;
        if(from.equals("許願者")) {
       	 /** 透過HelpHelper物件之getByWishId()方法取回所有物主的他人申請之資料，回傳之資料為JSONObject物件 */
           //JSONObject query = ah.getByOwnerId(Integer.parseInt(member_id));
           JSONObject query = hh.updateWisherStatus(h_id);
           JSONObject query1 = hh.updateEProductStatus(h_id);
           /** 新建一個JSONObject用於將回傳之資料進行封裝 */
           JSONObject resp = new JSONObject();
           resp.put("status", "200");
           resp.put("message", "許願成功");
           resp.put("response", query);
           resp.put("response", query1);
           /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
           jsr.response(resp, response);
       }
       else if(from.equals("許願訂單")) {
       	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有該買家的自己申請之資料，回傳之資料為JSONObject物件 */
           JSONObject query = hh.updateSuccess(h_id);
           JSONObject query1 = hh.updateToken(h_id);
           JSONObject query2 = hh.updateFinishTime(h_id);
       	//JSONObject query = ah.getBysellerId();
           /** 新建一個JSONObject用於將回傳之資料進行封裝 */
           JSONObject resp = new JSONObject();
           resp.put("status", "200");
           resp.put("message", "取物成功/失敗敗");
           resp.put("response", query);
           resp.put("response", query1);
           resp.put("response", query2);
           /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
           jsr.response(resp, response);
       }
       else if(from.equals("取物失敗")) {
         	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有該買家的自己申請之資料，回傳之資料為JSONObject物件 */
             JSONObject query = hh.updateFailure(h_id);
             JSONObject query1 = hh.updateFinishTime(h_id);
         	//JSONObject query = ah.getBysellerId();
             /** 新建一個JSONObject用於將回傳之資料進行封裝 */
             JSONObject resp = new JSONObject();
             resp.put("status", "200");
             resp.put("message", "取物成功/失敗敗");
             resp.put("response", query);
             resp.put("response", query1);
             /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
             jsr.response(resp, response);
         }
       
       /**訂單詳情*/
       //else {
       	 /** 透過HelpHelper物件之getByHelpId()方法取回這筆申請之資料，回傳之資料為JSONObject物件 */
           //JSONObject query = hh.getByhelpId(h_id);
           /** 新建一個JSONObject用於將回傳之資料進行封裝 */
           //JSONObject resp = new JSONObject();
           //resp.put("status", "200");
           //resp.put("message", "所有申請資料取得成功");
           //resp.put("response", query);
           //resp.put("helpID", h_id);
           /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
           //jsr.response(resp, response);
       	
       //}
        
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
        JSONObject query = hh.deleteById(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "申請移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}

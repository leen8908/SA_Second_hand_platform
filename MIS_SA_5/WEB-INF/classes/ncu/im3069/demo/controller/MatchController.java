package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import ncu.im3069.tools.JsonReader;



import ncu.im3069.demo.app.MatchHelper;
import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.MatchHelper;
import ncu.im3069.demo.app.Match;
import ncu.im3069.demo.app.ProductHelper;

/**
 * Servlet implementation class MatchController
 */
@WebServlet("/api/match.do")
public class MatchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 /** ah，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MatchHelper mah =  MatchHelper.getHelper();
      
    private ProductHelper ph = ProductHelper.getHelper();
    private MemberHelper mh = MemberHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatchController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        String get_id = jsr.getParameter("id");
        String type = jsr.getParameter("type");
        String from = jsr.getParameter("from");
        int id = Integer.parseInt(get_id);
        //System.out.print(id);
        //System.out.print(type);
        //int member_id = 3;
        //String type = "買家";
        
        /** 判斷該字串是否的種類，若為物主代表要取回物主編號的他人申請之資料，否則代表要取回買家自己申請之資料 */
        if(type.equals("物主")) {
        	 /** 透過ApplicationHelper物件之getByOwnerId()方法取回所有物主的他人申請之資料，回傳之資料為JSONObject物件 */
            //JSONObject query = ah.getByOwnerId(Integer.parseInt(member_id));
            JSONObject query = mah.getByOwnerId(id);
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
            JSONObject query = mah.getByBuyerId(id);
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
            JSONObject query = mah.getByMatchId(id);
            
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
	/*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}*/

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        JSONObject jowner = new JSONObject();
        JSONObject jbuyer = new JSONObject();
        JSONArray jsamember = null;
        JSONObject query = null;
        
        if(jso.getString("type").equals("token")) {
        	
        	
        	int buyer_id = jso.getInt("buyer_id");
        	int owner_id = jso.getInt("owner_id");
        	Double token = jso.getDouble("token");
        
        	/**取得owner和buyer的代幣總數量*/
        	jbuyer = mh.getByID(Integer.toString(buyer_id));
        	jsamember = jbuyer.getJSONArray("data");
        	jbuyer = jsamember.getJSONObject(0);
        	Double buyer_token = jbuyer.getDouble("token_amount");
        
        	jowner = mh.getByID(Integer.toString(owner_id));
        	jsamember = jowner.getJSONArray("data");
        	jowner = jsamember.getJSONObject(0);
        	Double owner_token = jowner.getDouble("token_amount");
        	query = mah.updateMemberToken(buyer_token, owner_token, token, buyer_id, owner_id);
        	
        }
        
        else {
        	
        	
        	 query = mah.updateStatus(jso);
        }
        
        
        
        
       
       
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "申請移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
        
	}
	
	

}

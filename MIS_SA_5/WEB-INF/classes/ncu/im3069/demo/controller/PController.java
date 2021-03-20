package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.ExpectedProduct;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class PController
 */
@WebServlet("/api/PController.do")
public class PController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ProductHelper ph =  ProductHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        //JSONObject jso = jsr.getObject();
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id_list");
        String member_id = jsr.getParameter("member_id_list");
        String managerOrNot = jsr.getParameter("managerOrNot_list");
        boolean manager_status = managerOrNot.contains("true");
        String buyer_id = jsr.getParameter("buyer_id");
        String product_name = jsr.getParameter("product_name_list");
       // String product_name = jso.getString("product_name_list");
        System.out.println(product_name);
        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回個別商品之資料，否則代表要取回全部資料庫內商品之資料 */
        
        if (!id.isEmpty() ) {
          JSONObject query2=ph.getByID(id);
          resp.put("status", "200");
          resp.put("message", "所有商品資料取得成功");
          resp.put("response", query2);
        }
        /**取個人商品*/
       else if(!member_id.isEmpty()) {
        	JSONObject query1=ph.getByMemberId(member_id);
            resp.put("status", "200");
            resp.put("message", "所有商品資料取得成功");
            resp.put("response", query1);
        }
        /**管理員取所有商品*/
       else if(manager_status) {
        	JSONObject query3=ph.getAllByManager();
            resp.put("status", "200");
            resp.put("message", "所有商品資料取得成功");
            resp.put("response", query3);
        }
        /**搜尋商品*/
       else if(!product_name.isEmpty()) {
    	   System.out.println(product_name);
        	JSONObject query4=ph.getByName(product_name, jsr.getParameter("member_id"));
            resp.put("status", "200");
            resp.put("message", "所有商品資料取得成功");
            resp.put("response", query4);
        }
       else if(!buyer_id.isEmpty()) {
    	   JSONObject query = ph.getByMemberId(buyer_id);

   		resp.put("status", "200");
   		resp.put("message", "所有商品資料取得成功");
   		resp.put("response", query);
    	   
       }
        
       else {
    	   
          JSONObject query = ph.getAllProduct(jsr.getParameter("member_id"));
          resp.put("status", "200");
          resp.put("message", "所有商品資料取得成功");
          resp.put("response", query);
        }
        
        jsr.response(resp, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
       
        /** 取出經解析到JSONObject之Request參數 */
        int member_id = jso.getInt("member_id");
        String name=jso.getString("name");
        String describtion = jso.getString("describtion");
        String category = jso.getString("category");
        String area = jso.getString("area");
       // boolean status = jso.getBoolean("status");
        double token=jso.getDouble("token");
        //String image = jso.getString("image");
        
        
        
        /** 建立一個新的商品物件 */
        Product p = new Product(name,category, token,describtion, area,member_id);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(name.isEmpty() || category.isEmpty()|| describtion.isEmpty()||area.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        else {
            /** 透過ProductHelper物件的create()方法新建一個商品至資料庫 */
            JSONObject data = ph.create(p);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 新增商品...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
	}
	/**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = ph.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "商品移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
	
	
	
	/**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        String name=jso.getString("name");
        //String category = jso.getString("category");
        String describtion = jso.getString("describtion");
        String area = jso.getString("area");
        //Double token=jso.getDouble("token");
        

        /** 透過傳入之參數，新建一個以這些參數之會員Product物件 */
        Product p = new Product(id,name, describtion, area);
        
        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        JSONObject data = p.update_data();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新商品資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
                }
            
          
}



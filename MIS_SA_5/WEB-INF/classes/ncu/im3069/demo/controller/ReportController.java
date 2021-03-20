package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.Report;
import ncu.im3069.demo.app.ReportHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ReportController
 */
@WebServlet("/api/report.do")
public class ReportController extends HttpServlet {
	private ReportHelper rh =  ReportHelper.getHelper();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        
        
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        
        /** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
        JSONObject query = rh.getAll();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "所有會員資料取得成功");
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
        boolean success = jso.getBoolean("success");  
        int id = jso.getInt("id");  
        int manager_id = jso.getInt("manager_id"); 

        
        if(success)
        {	

         int product_id = jso.getInt("product_id");    
         int reported_member_id = jso.getInt("reported_member_id"); 
        /** 透過傳入之參數，新建一個以這些參數之會員Report物件 */
        Report r = new Report(id,manager_id);
        
        /** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
        Member m = new Member(reported_member_id);
        
        /** 透過傳入之參數，新建一個以這些參數之會員Product物件 */
        Product p = new Product(product_id);
        
        /** 透過Member物件的update()方法至資料庫更新該檢舉資料，回傳之資料為JSONObject物件 */
        JSONObject data = r.update_status(success);
        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        JSONObject data2 = m.update_status();
        /** 透過Member物件的update()方法至資料庫更新該商品資料，回傳之資料為JSONObject物件 */
        JSONObject data3 = p.update_status();
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新資料...");
        resp.put("response", data);
        

        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
        }
        else
        {
        	/** 透過傳入之參數，新建一個以這些參數之會員Report物件 */
            Report r = new Report(id,manager_id);
            /** 透過Member物件的update()方法至資料庫更新該檢舉資料，回傳之資料為JSONObject物件 */
            JSONObject data = r.update_status(success);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 更新資料...");
            resp.put("response", data);
            

            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        
      }
}

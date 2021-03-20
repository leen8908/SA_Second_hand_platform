package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.Report;
import ncu.im3069.demo.app.ReportHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/reportProduct.do")
public class ReportProductController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** mh，MemberHelper 之物件與 Member 相關之資料庫方法（Sigleton） */
	private MemberHelper mh =  MemberHelper.getHelper();
	
	/** rh，ReportHelper 之物件與 Report 相關之資料庫方法（Sigleton） */
	private ReportHelper rh = ReportHelper.getHelper();

    public ReportProductController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
			JsonReader jsr = new JsonReader(request);
        	/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
			String member_id = jsr.getParameter("member_id");
			/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
			String product_id = jsr.getParameter("product_id");
			
			
	        mh = MemberHelper.getHelper();
	        ph = ProductHelper.getHelper();
	        
	        //** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            JSONObject data1 = mh.getByID(member_id);
            //** 透過ProductHelper物件的getByID()方法自資料庫取回該商品之資料，回傳之資料為JSONObject物件 */
            JSONObject data2 = ph.getByID(product_id);
            
            JSONObject[] data = {data1,data2};
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "資料取得成功");
            resp.put("response", data);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
	}
	
	/**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int report_id = jso.getInt("report_id");
        String report_name = jso.getString("report_name");
        int product_id = jso.getInt("product_id");
        String product_name= jso.getString("product_name");
        int reported_id= jso.getInt("reported_id");
        String content= jso.getString("content");
        
        /** 建立一個新的檢舉物件 */
        Report r = new Report(report_id,report_name,product_id,product_name,reported_id, content);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(content.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'檢舉理由不能空白\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        else {
        	/** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = rh.create(r);
                     	
        	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "您的檢舉~飛鴿傳書給管理員啦! ヾ(・Θ・)ノ〃");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

}

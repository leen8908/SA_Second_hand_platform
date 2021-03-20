package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.tools.JsonReader;

public class HelpHelper {

	private static HelpHelper hh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private ExpectedProductHelper eph = ExpectedProductHelper.getHelper();
    private MemberHelper mh = MemberHelper.getHelper();
    
    private HelpHelper() {
    }
    
    public static HelpHelper getHelper() {
        if(hh == null) hh = new HelpHelper();
        
        return hh;
    }
    
    /**
     * 透過物主編號（ID）取得幫助資料
     *
     * @param id 物主編號
     * @return the JSON object 回傳SQL執行結果與該物主編號之他人幫助資料(包含：訂單編號、買家名字、幫助日期、狀態、商品品名、類別、描述、交易方式、(物主選擇的物品名or代幣))
     */
    public JSONObject getByWisherId(int id) {
    	
    	 /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        Help h = null;
        /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        ExpectedProduct p = null;
        Member m = null;
        /** 用於儲存所有檢索回之幫助，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
       // JSONObject jso2 = new JSONObject();
        //JsonReader jsr ;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        //long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
       // ArrayList<help> help_list  = new ArrayList<help>();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`wishpool_order`"+
                    "INNER JOIN `missa`.`wishpool_status`"+
                    "ON `wishpool_order`.`help_id` = `wishpool_status`.`id`"+
                    "INNER JOIN `missa`.`product`"+
                    "ON `wishpool_order`.`expectedProduct_id` = `product`.`id`"+
                    "WHERE `missa`.`wishpool_order`.`wisher_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = ? AND `missa`.`product`.`status` = 0";
            
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            pres.setInt(2, 0);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
           
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("id");
                int seller_id = rs.getInt("seller_id");
                int expectedProduct_id = rs.getInt("expectedProduct_id");
                int help_id = rs.getInt("help_id");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                
                p = eph.getById_me(expectedProduct_id);
                
               jso = mh.getByID(Integer.toString(seller_id));
               jsa2 = jso.getJSONArray("data");
               JSONObject j = jsa2.getJSONObject(0);
               String seller_name = j.getString("name");
               jso = mh.getByID(Integer.toString(id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String wisher_name = j.getString("name");
                //a.setwisherName(getName(a.getwisherId()));
                /** 將每一筆幫助資料產生一個新help物件 */
               h = new Help(order_id,seller_id, p , status, start_time,help_id);
               
               /**呼叫setsellerName設a的seller_name*/
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
               //help_list.add(a);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(h.getData());
            }
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
         
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
       // long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("data", jsa);

        return response;
    	
    } //end getBywisherId
    
    /**
     * 透過買家編號（ID）取得幫助資料
     *
     * @param id 買家編號
     * @return the JSON object 回傳SQL執行結果與該買家編號之自己幫助資料(包含：訂單編號、物主名字、幫助日期、狀態、商品品名、類別、描述、交易方式、(物主選擇的物品名or代幣))
     */
    public JSONObject getBysellerId(int id) {
    	
    	 /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        Help h = null;
        /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        ExpectedProduct p = null;
        Member m = null;
        /** 用於儲存所有檢索回之幫助，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        //long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`wishpool_order`"+
            "INNER JOIN `missa`.`wishpool_status`"+
            "ON `wishpool_order`.`help_id` = `wishpool_status`.`id`"+
            "INNER JOIN `missa`.`product`"+
            "ON `wishpool_order`.`expectedProduct_id` = `product`.`id`"+
            "WHERE `missa`.`wishpool_order`.`seller_id` = ? ";

            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            //pres.setInt(2, 0);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
           
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("id");
                int wisher_id = rs.getInt("wisher_id");
                int expectedProduct_id = rs.getInt("expectedProduct_id");
                int help_id = rs.getInt("help_id");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                p = eph.getById_me(expectedProduct_id);
               jso = mh.getByID(Integer.toString(wisher_id));
               jsa2 = jso.getJSONArray("data");
               JSONObject j = jsa2.getJSONObject(0);
               String wisher_name = j.getString("name");
               jso = mh.getByID(Integer.toString(id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String seller_name = j.getString("name");
                /** 將每一筆幫助資料產生一個新help物件 */
               h = new Help(order_id, help_id, p,  wisher_id, status, start_time);
               
               /**呼叫setsellerName設a的seller_name*/
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(h.getData());
            }
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
         
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
       // long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        //response.put("time", duration);
        response.put("data", jsa);

        return response;
    	
    } //end getBywisherId
    
   
    
    /**透過幫助編號（ID）取得幫助資料
     * 
     * @param id 幫助編號
     * @return
     */
    
    public JSONObject getByhelpId(int id) {
    	
    	 /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        Help h = null;
        /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
        ExpectedProduct p = null;
        Member m = null;
        /** 用於儲存所有檢索回之幫助，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        //long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
       // ArrayList<help> help_list  = new ArrayList<help>();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`wishpool_order`"+
            "INNER JOIN `missa`.`wishpool_status`"+
            "ON `missa`.`wishpool_order`.`help_id` = `missa`.`wishpool_status`.`id`"+
            "WHERE `missa`.`wishpool_order`.`help_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            //pres.setInt(2, 0);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
           
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("id");
                int seller_id = rs.getInt("seller_id");
                int wisher_id = rs.getInt("wisher_id");
                int expectedProduct_id = rs.getInt("expectedProduct_id");
                int help_id = rs.getInt("help_id");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                Double token = rs.getDouble("token");
                p = eph.getById_me(expectedProduct_id);
               jso = mh.getByID(Integer.toString(seller_id));
               jsa2 = jso.getJSONArray("data");
               JSONObject j = jsa2.getJSONObject(0);
               String seller_name = j.getString("name");
               //String seller_phone = j.getString("phoneNumber");
               jso = mh.getByID(Integer.toString(wisher_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String wisher_name = j.getString("name");
               //String wisher_phone = j.getString("phoneNumber");
                /** 將每一筆幫助資料產生一個新help物件 */
               h = new Help(order_id, token, wisher_id, seller_id,  p, help_id, status, start_time,m);
               
              
               
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
               //h.setwisherPhone(wisher_phone);
               //h.setsellerPhone(seller_phone);
               //help_list.add(a);
                /** 取出該幫助之資料並封裝至 JSONsonArray 內 */
               jso = h.getData();
               jso.put("token", token);
               
               jsa.put(jso);
            }
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
         
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
       // long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        //response.put("time", duration);
        response.put("data", jsa);

        return response;
    	
    }	// end getByhelpId
    
    public JSONObject getByWishId_look(int id) {
    	
   	 /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
       Help h = null;
       /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
       ExpectedProduct p = null;
       Member m = null;
       /** 用於儲存所有檢索回之幫助，以JSONArray方式儲存 */
       JSONArray jsa = new JSONArray();
       JSONArray jsa2 = null;
       JSONObject jso = new JSONObject();
      // JSONObject jso2 = new JSONObject();
       //JsonReader jsr ;
       /** 記錄實際執行之SQL指令 */
       String exexcute_sql = "";
       /** 紀錄程式開始執行時間 */
       //long start_time = System.nanoTime();
       /** 紀錄SQL總行數 */
       int row = 0;
       /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
       ResultSet rs = null;
      // ArrayList<help> help_list  = new ArrayList<help>();
       
       try {
           /** 取得資料庫之連線 */
           conn = DBMgr.getConnection();
           /** SQL指令 */
           String sql = "SELECT * FROM `missa`.`wishpool_order`"+
           "INNER JOIN `missa`.`wishpool_status`"+
           	"ON `wishpool_order`.`help_id` = `wishpool_status`.`id`"+
           "WHERE `missa`.`wishpool_order`.`wisher_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = ?";
           
           
           /** 將參數回填至SQL指令當中 */
           pres = conn.prepareStatement(sql);
           pres.setInt(1, id);
           pres.setInt(2, 1);
           /** 執行查詢之SQL指令並記錄其回傳之資料 */
           rs = pres.executeQuery();

           /** 紀錄真實執行的SQL指令，並印出 **/
           exexcute_sql = pres.toString();
           System.out.println(exexcute_sql);
           
           /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
          
           while(rs.next()) {
               /** 每執行一次迴圈表示有一筆資料 */
               row += 1;
               
               /** 將 ResultSet 之資料取出 */
               int order_id = rs.getInt("id");
               int seller_id = rs.getInt("seller_id");
               int expectedProduct_id = rs.getInt("expectedProduct_id");
               int help_id = rs.getInt("help_id");
               String status  = rs.getString("status");
               Timestamp start_time = rs.getTimestamp("start_time");
               
               p = eph.getById_me(expectedProduct_id);
               
              jso = mh.getByID(Integer.toString(seller_id));
              jsa2 = jso.getJSONArray("data");
              JSONObject j = jsa2.getJSONObject(0);
              String seller_name = j.getString("name");
              jso = mh.getByID(Integer.toString(id));
              jsa2 = jso.getJSONArray("data");
              j = jsa2.getJSONObject(0);
              String wisher_name = j.getString("name");
               //a.setwisherName(getName(a.getwisherId()));
               /** 將每一筆幫助資料產生一個新help物件 */
              h = new Help(order_id,seller_id, p , status, start_time,help_id);
              
              /**呼叫setsellerName設a的seller_name*/
              h.setwisherName(wisher_name);
              h.setsellerName(seller_name);
              //help_list.add(a);
               /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
              jsa.put(h.getData());
           }
           
       } catch (SQLException e) {
           /** 印出JDBC SQL指令錯誤 **/
           System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
       } catch (Exception e) {
           /** 若錯誤則印出錯誤訊息 */
           e.printStackTrace();
       } finally {
           /** 關閉連線並釋放所有資料庫相關之資源 **/
           DBMgr.close(rs, pres, conn);
       }
       
        
       /** 紀錄程式結束執行時間 */
       long end_time = System.nanoTime();
       /** 紀錄程式執行時間 */
      // long duration = (end_time - start_time);
       
       /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
       JSONObject response = new JSONObject();
       response.put("sql", exexcute_sql);
       response.put("row", row);
       //response.put("time", duration);
       response.put("data", jsa);

       return response;
   	
   } //end getBywisherId
    
    public JSONObject getByWishId_order(int id) {
    	
   	 /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
       Help h = null;
       /** 新建一個 help 物件之 a 變數，用於紀錄每一筆查詢回之幫助資料 */
       ExpectedProduct p = null;
       Member m = null;
       /** 用於儲存所有檢索回之幫助，以JSONArray方式儲存 */
       JSONArray jsa = new JSONArray();
       JSONArray jsa2 = null;
       JSONObject jso = new JSONObject();
      // JSONObject jso2 = new JSONObject();
       //JsonReader jsr ;
       /** 記錄實際執行之SQL指令 */
       String exexcute_sql = "";
       /** 紀錄程式開始執行時間 */
       //long start_time = System.nanoTime();
       /** 紀錄SQL總行數 */
       int row = 0;
       /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
       ResultSet rs = null;
      // ArrayList<help> help_list  = new ArrayList<help>();
       
       try {
           /** 取得資料庫之連線 */
           conn = DBMgr.getConnection();
           /** SQL指令 */
           String sql = "SELECT * FROM `missa`.`wishpool_order`"+
                   "INNER JOIN `missa`.`wishpool_status`"+
                   	"ON `wishpool_order`.`help_id` = `wishpool_status`.`id`"+
                   "WHERE `missa`.`wishpool_order`.`help_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = 1";
           
           /** 將參數回填至SQL指令當中 */
           pres = conn.prepareStatement(sql);
           pres.setInt(1, id);
           /** 執行查詢之SQL指令並記錄其回傳之資料 */
           rs = pres.executeQuery();

           /** 紀錄真實執行的SQL指令，並印出 **/
           exexcute_sql = pres.toString();
           System.out.println(exexcute_sql);
           
           /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
          
           while(rs.next()) {
               /** 每執行一次迴圈表示有一筆資料 */
               row += 1;
               
               /** 將 ResultSet 之資料取出 */
               int order_id = rs.getInt("id");
               int seller_id = rs.getInt("seller_id");
               int wisher_id = rs.getInt("wisher_id");
               int expectedProduct_id = rs.getInt("expectedProduct_id");
               int help_id = rs.getInt("help_id");
               String status  = rs.getString("status");
               Timestamp start_time = rs.getTimestamp("start_time");
               Timestamp finish_time = rs.getTimestamp("finish_time");
               Double token = rs.getDouble("token");
               p = eph.getById_me(expectedProduct_id);
              jso = mh.getByID(Integer.toString(seller_id));
              jsa2 = jso.getJSONArray("data");
              JSONObject j = jsa2.getJSONObject(0);
              String seller_name = j.getString("name");
              String seller_phone = j.getString("phoneNumber");
              jso = mh.getByID(Integer.toString(wisher_id));
              jsa2 = jso.getJSONArray("data");
              j = jsa2.getJSONObject(0);
              String wisher_name = j.getString("name");
              String wisher_phone = j.getString("phoneNumber");
               /** 將每一筆幫助資料產生一個新help物件 */
              h = new Help(order_id, token, wisher_id, seller_id,  p, help_id, status, start_time, finish_time);
              
             
              
              h.setwisherName(wisher_name);
              h.setsellerName(seller_name);
              h.setwisherPhone(wisher_phone);
              h.setsellerPhone(seller_phone);
              //help_list.add(a);
               /** 取出該幫助之資料並封裝至 JSONsonArray 內 */
              jso = h.getData();
              jso.put("token", token);
              
              jsa.put(jso);
           }
           
       } catch (SQLException e) {
           /** 印出JDBC SQL指令錯誤 **/
           System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
       } catch (Exception e) {
           /** 若錯誤則印出錯誤訊息 */
           e.printStackTrace();
       } finally {
           /** 關閉連線並釋放所有資料庫相關之資源 **/
           DBMgr.close(rs, pres, conn);
       }
       
        
       /** 紀錄程式結束執行時間 */
       long end_time = System.nanoTime();
       /** 紀錄程式執行時間 */
      // long duration = (end_time - start_time);
       
       /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
       JSONObject response = new JSONObject();
       response.put("sql", exexcute_sql);
       response.put("row", row);
       //response.put("time", duration);
       response.put("data", jsa);

       return response;
   	
   }	// end getByhelpId
    
    public JSONObject create(Help h) {
    	
    	//new help( wisher_id, seller_id, product, token, help_id)
    	
    	/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
         
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`wishpool_status`(`start_time`, `status`, `wishOrNot`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            
            Timestamp create_time =Timestamp.valueOf(LocalDateTime.now());
            String status = "等待回覆";
            int wishOrNot = 0;
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setTimestamp(1, create_time);
            pres.setString(2, status);
            pres.setInt(3, wishOrNot);
            
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            ResultSet rs = pres.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                
            }
            sql= "INSERT INTO `missa`.`wishpool_order`( `wisher_id`, `seller_id`, `expectedProduct_id`, `token`, `help_id`)"
                    + " VALUES(?, ?, ?, ?, ?)";
            int wisher_id = h.getwisherId();
            int seller_id = h.getsellerId();
            int expectedProduct_id = h.getExpectedProduct().getId();
            double token = h.getExpectedProduct().getToken();
            
            
            pres = conn.prepareStatement(sql);
            pres.setInt(1, wisher_id);
            pres.setInt(2, seller_id);
            pres.setInt(3, expectedProduct_id);
            pres.setDouble(4, token);
            pres.setInt(5, id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            row += pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql += pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    	
    }	//end create
       
    public JSONObject deleteById(int help_id) {
    	
    	/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            
            /** SQL指令 */
            String sql = "DELETE FROM `missa`.`wishpool_order`, `missa`.`wishpool_status` USING `missa`.`wishpool_order` LEFT JOIN `missa`.`wishpool_status`"
            		+ "ON `missa`.`wishpool_order`.`help_id` = `missa`.`wishpool_status`.`id`"
            		+ "WHERE `missa`.`wishpool_order`.`help_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = ? ";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, help_id);
            pres.setInt(2, 0);
            /** 執行刪除之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    	
    } // end deleteById
   
    public JSONObject updateWisherStatus(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`wishpool_status` SET `status` = '許願成功',`wishOrNot` = 1  WHERE `id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(1, id);
            System.out.println(id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    public JSONObject updateEProductStatus(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`product` "+
            			"INNER JOIN `missa`.`wishpool_order`"+
            			"ON `product`.`id` = `wishpool_order`.`expectedProduct_id`"+
            			"SET `status` = 1 "
            			+ " WHERE `wishpool_order`.`help_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(1, id);
            System.out.println(id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject updateSuccess(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`wishpool_status` SET `status` = '取物成功，賣家新增代幣10枚' WHERE `id` = ?";
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(1, id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject updateToken(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
                        String sql = "Update `missa`.`members`"+
                        				"INNER JOIN `missa`.`wishpool_order`"+
                        				"ON `members`.`id` = `wishpool_order`.`seller_id`"+
                        				 "SET `members`.`token_amount` = `token_amount` + 10 "+
                        				" WHERE `wishpool_order`.`help_id` = ? ";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(1, id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
   
    public JSONObject updateFailure(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`wishpool_status` SET `status` = '取物失敗' WHERE `id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(1, id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject updateFinishTime(int id) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`wishpool_status` SET `finish_time` = ? WHERE `id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(2, id);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

}

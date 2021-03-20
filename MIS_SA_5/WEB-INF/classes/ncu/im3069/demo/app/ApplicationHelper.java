package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.tools.JsonReader;

public class ApplicationHelper {

	private static ApplicationHelper ah;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private ProductHelper ph = ProductHelper.getHelper();
    private MemberHelper mh = MemberHelper.getHelper();
    private MatchHelper mah = MatchHelper.getHelper();
    private ApplicationHelper() {
    }
    
    public static ApplicationHelper getHelper() {
        if(ah == null) ah = new ApplicationHelper();
        
        return ah;
    }
    
    /**
     * 透過物主編號（ID）取得申請資料
     *
     * @param id 物主編號
     * @return the JSON object 回傳SQL執行結果與該物主編號之他人申請資料(包含：訂單編號、買家名字、申請日期、狀態、商品品名、類別、描述、交易方式、(物主選擇的物品名or代幣))
     */
    public JSONObject getByOwnerId(int id) {
    	
    	 /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Application a = null;
        /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Product p = null;
        
        /** 用於儲存所有檢索回之申請，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`order_me`"+
            "INNER JOIN `missa`.`order_status`"+
            	"ON `order_me`.`application_id` = `order_status`.`id`"+
            "WHERE `missa`.`order_me`.`owner_id` = ? AND `missa`.`order_status`.`matchOrNot` = ?";
            
            
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
                int buyer_id = rs.getInt("buyer_id");
                int product_id = rs.getInt("product_id");
                int application_id = rs.getInt("application_id");
                String mean_of_transaction = rs.getString("means_of_transaction");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                int exchangedProduct_id = rs.getInt("exchangedProduct_id");
                
                jso = ph.getByID(Integer.toString(product_id));
                jsa2 = jso.getJSONArray("data");
                JSONObject j = jsa2.getJSONObject(0);
                String product_name = j.getString("name");
                Double token = j.getDouble("token");
                String description = j.getString("describtion");
                String category = j.getString("category");
                Boolean product_status = j.getBoolean("status");
                String area = j.getString("area");
                p = new Product(product_id,product_name, category, description, token, area, product_status);
                
                
               jso = mh.getByID(Integer.toString(buyer_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String buyer_name = j.getString("name");
               jso = mh.getByID(Integer.toString(id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String owner_name = j.getString("name");
               
               /** 將每一筆申請資料產生一個新Application物件 */
               a = new Application(order_id, application_id, buyer_id, id, status, start_time, p, mean_of_transaction);
               
               
               /**呼叫setProductInfo設a的product_name、category、description*/
               //setProductInfo(a);
               /**呼叫setBuyerName設a的buyer_name*/
               //a.setBuyerName(getName(a.getBuyerId()));
               //a.setOwnerName(getName(a.getOwnerId()));
               a.setOwnerName(owner_name);
               a.setBuyerName(buyer_name);
               JSONObject application_object = a.getData();
               application_object.put("exchangedProduct_id", exchangedProduct_id);
               
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(application_object);
               
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
    	
    } //end getByOwnerId
    
    /**
     * 透過買家編號（ID）取得申請資料
     *
     * @param id 買家編號
     * @return the JSON object 回傳SQL執行結果與該買家編號之自己申請資料(包含：訂單編號、物主名字、申請日期、狀態、商品品名、類別、描述、交易方式、(物主選擇的物品名or代幣))
     */
    public JSONObject getByBuyerId(int id) {
    	
    	 /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Application a = null;
        /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Product p = null;
        
        /** 用於儲存所有檢索回之申請，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`order_me`"+
            "INNER JOIN `missa`.`order_status`"+
            	"ON `order_me`.`application_id` = `order_status`.`id`"+
            "WHERE `missa`.`order_me`.`buyer_id` = ? AND `missa`.`order_status`.`matchOrNot` = ?";
            
            
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
                int owner_id = rs.getInt("owner_id");
                int product_id = rs.getInt("product_id");
                int application_id = rs.getInt("application_id");
                String mean_of_transaction = rs.getString("means_of_transaction");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                int exchangedProduct_id = rs.getInt("exchangedProduct_id");
                
                jso = ph.getByID(Integer.toString(product_id));
                jsa2 = jso.getJSONArray("data");
                JSONObject j = jsa2.getJSONObject(0);
                String product_name = j.getString("name");
                Double token = j.getDouble("token");
                String description = j.getString("describtion");
                String category = j.getString("category");
                Boolean product_status = j.getBoolean("status");
                String area = j.getString("area");
                p = new Product(product_id,product_name, category, description, token, area, product_status);
                
               jso = mh.getByID(Integer.toString(owner_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String owner_name = j.getString("name");
               jso = mh.getByID(Integer.toString(id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String buyer_name = j.getString("name");
                
                /** 將每一筆申請資料產生一個新Application物件 */
               a = new Application(order_id, application_id, id, owner_id, status, start_time, p, mean_of_transaction);
               
               /**呼叫setOrderStatus設a的status、start_time*/
               //setOrderStatus(a);
               /**呼叫setProductInfo設a的product_name、category、description*/
               //setProductInfo(a);
               /**呼叫setBuyerName設a的buyer_name*/
               //a.setBuyerName(getName(a.getBuyerId()));
               //a.setOwnerName(getName(a.getOwnerId()));
               a.setOwnerName(owner_name);
               a.setBuyerName(buyer_name);
               JSONObject application_object = a.getData();
               application_object.put("exchangedProduct_id", exchangedProduct_id);
               
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(application_object);
               
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
    	
    } //end getByOwnerId
    
    /**透過申請編號（ID）取得申請資料
     * 
     * @param id 申請編號
     * @return
     */
    public JSONObject getByApplicationId(int id) {
    	
    	 /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Application a = null;
        /** 新建一個 Application 物件之 a 變數，用於紀錄每一筆查詢回之申請資料 */
        Product p = null;
        
        /** 用於儲存所有檢索回之申請，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
        JSONObject jso2 = new JSONObject();
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
       // ArrayList<Application> application_list  = new ArrayList<Application>();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`order_me`"+
            "INNER JOIN `missa`.`order_status`"+
            	"ON `missa`.`order_me`.`application_id` = `missa`.`order_status`.`id`"+
            "WHERE `missa`.`order_me`.`application_id` = ? AND `missa`.`order_status`.`matchOrNot` = ?";
            
            
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
                int buyer_id = rs.getInt("buyer_id");
                int owner_id = rs.getInt("owner_id");
                int product_id = rs.getInt("product_id");
                int application_id = rs.getInt("application_id");
                String mean_of_transaction = rs.getString("means_of_transaction");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                //Double token = rs.getDouble("token");
                int exchangedProduct_id = 0;
                if(rs.getInt("exchangedProduct_id")!=0) {
                	
                	exchangedProduct_id = rs.getInt("exchangedProduct_id");
                	
                }
                System.out.print(exchangedProduct_id);
                
                jso = ph.getByID(Integer.toString(product_id));
                jsa2 = jso.getJSONArray("data");
                JSONObject j = jsa2.getJSONObject(0);
                String product_name = j.getString("name");
                Double token = j.getDouble("token");
                String description = j.getString("describtion");
                String category = j.getString("category");
                Boolean product_status = j.getBoolean("status");
                String area = j.getString("area");
                p = new Product(product_id,product_name, category, description, token, area, product_status);
                
               jso = mh.getByID(Integer.toString(buyer_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String buyer_name = j.getString("name");
               jso = mh.getByID(Integer.toString(owner_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String owner_name = j.getString("name");
               
                /** 將每一筆申請資料產生一個新Application物件 */
               a = new Application(order_id, application_id, buyer_id, owner_id, status, start_time, p, mean_of_transaction);
               
              
               
               a.setOwnerName(owner_name);
               a.setBuyerName(buyer_name);
               a.setBuyerId(buyer_id);
               a.setOwnerId(owner_id);
               //application_list.add(a);
                /** 取出該申請之資料並封裝至 JSONsonArray 內 */
               jso = a.getData();
               jso.put("token", token);
               if(exchangedProduct_id!=0) {
            	   
           	    jso2 = ph.getByID(Integer.toString(exchangedProduct_id));
           	    jsa2 = jso2.getJSONArray("data");
           	    j = jsa2.getJSONObject(0);
           	    jso.put("exchangedProduct_name", j.getString("name"));
           	    jso.put("exchangedProduct_id", exchangedProduct_id);
           	    
              }
               else {
            	   
            	   jso.put("exchangedProduct_name", "");
            	   jso.put("exchangedProduct_id", 0);
            	   
               }
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
    	
    }	// end getByApplicationId
    
    public JSONObject create(Application a) {
    	
    	//new Application(means_of_transaction, owner_id, buyer_id, product, token, application_id)
    	
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
            String sql = "INSERT INTO `missa`.`order_status`(`start_time`, `status`, `matchOrNot`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            
            Timestamp create_time =Timestamp.valueOf(LocalDateTime.now());
            String status = "等待物主回覆";
            int matchOrNot = 0;
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setTimestamp(1, create_time);
            pres.setString(2, status);
            pres.setInt(3, matchOrNot);
            
            
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
            sql= "INSERT INTO `missa`.`order_me`(`means_of_transaction`, `owner_id`, `buyer_id`, `product_id`, `token`, `application_id`)"
                    + " VALUES(?, ?, ?, ?, ?, ?)";
            String mean_of_transaction = a.getTransactionMean();
            int owner_id = a.getOwnerId();
            int buyer_id = a.getBuyerId();
            int product_id = a.getProduct().getId();
            double token = a.getToken();
            
            
            pres = conn.prepareStatement(sql);
            pres.setString(1, mean_of_transaction);
            pres.setInt(2, owner_id);
            pres.setInt(3, buyer_id);
            pres.setInt(4, product_id);
            pres.setDouble(5, token);
            pres.setInt(6, id);
            
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
    
    public JSONObject updateStatus(JSONObject jso) {
    	
    	/** 取得所需之參數 */
    	String mean_of_transaction = jso.getString("mean_of_transaction");
        //int exchangedProduct_id = jso.getInt("exchangedProduct_id");
        String from = jso.getString("from");
        int application_id = jso.getInt("application_id");
        //String exchangedProduct_name = jso.getString("exchangedProduct_name");
    	/** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /**紀錄修改時間*/
        Timestamp update_time =Timestamp.valueOf(LocalDateTime.now());
        String sql = "";
        String status = "";
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            if(mean_of_transaction.equals("以物易物")) {
            	if(from.equals("物主")) {
            	sql = "Update `missa`.`order_status` SET `feedback_time` = ? , `status` = ? WHERE `id` = ?";
            	status = "等待買家回覆";
            	}
            	else {
            		sql = "Update `missa`.`order_status` SET `finish_time` = ? , `status` = ? WHERE `id` = ?";
            		status = "完成申請";
            		mah.create(application_id, update_time);
            	}
            }
            else {	//代幣
            	
            	sql = "Update `missa`.`order_status` SET `finish_time` = ? , `status` = ? WHERE `id` = ?";
            	status = "完成申請";
            	mah.create(application_id, update_time);
            	
            }
            
            
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setTimestamp(1, update_time);
            pres.setString(2, status);
            pres.setInt(3, application_id);
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
    
    public JSONObject updateExchangedProductId(int exchangedProduct_id, int application_id) {
    	
    	
    	
    	/** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /**紀錄修改時間*/
        Timestamp update_time =Timestamp.valueOf(LocalDateTime.now());
        String sql = "";
        
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            
            	
           sql = "Update `missa`.`order_me` SET `exchangedProduct_id` = ? WHERE `application_id` = ?";
           
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, exchangedProduct_id);
            pres.setInt(2, application_id);
           
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
    
    
    
   
    
    public JSONObject deleteById(int application_id) {
    	
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
            String sql = "DELETE FROM `missa`.`order_me`, `missa`.`order_status` USING `missa`.`order_me` LEFT JOIN `missa`.`order_status`"
            		+ "ON `missa`.`order_me`.`application_id` = `missa`.`order_status`.`id`"
            		+ "WHERE `missa`.`order_me`.`application_id` = ? AND `missa`.`order_status`.`matchOrNot` = ? ";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, application_id);
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
   
   	
   

}

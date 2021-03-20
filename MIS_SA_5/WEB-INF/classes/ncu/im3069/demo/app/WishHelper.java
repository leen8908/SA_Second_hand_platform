package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.tools.JsonReader;

public class WishHelper {

	private static WishHelper hh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private ProductHelper ph = ProductHelper.getHelper();
    private MemberHelper mh = MemberHelper.getHelper();
    
    private WishHelper() {
    }
    
    public static WishHelper getHelper() {
        if(hh == null) hh = new WishHelper();
        
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
        Product p = null;
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
                
                p = ph.getByID(expectedProduct_id);
                
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
               
               /**呼叫setOrderStatus設a的status、start_time*/
               //setOrderStatus(a);
               /**呼叫setProductInfo設a的product_name、category、description*/
               //setProductInfo(a);
               /**呼叫setsellerName設a的seller_name*/
               //a.setsellerName(getName(a.getsellerId()));
               //a.setwisherName(getName(a.getwisherId()));
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
               //help_list.add(a);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(h.getData());
               /*jso.put("order_id", order_id);
               jso.put("wisher_name", p.getName());
               jso.put("product_id", product_id);
               jso.put("help_id", help_id);
               jso.put("mean_of_transaction", mean_of_transaction);
               jso.put("status", status);
               jso.put("start_time", start_time);
               jso.put("seller_name", getName(id));
               //jso.put("wisher_name", getName(wisher_id));
               jsa.put(jso);
               jso = new JSONObject();*/
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
        Product p = null;
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
            "WHERE `missa`.`wishpool_order`.`seller_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = ?";
            
            
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
                int wisher_id = rs.getInt("wisher_id");
                int expectedProduct_id = rs.getInt("expectedProduct_id");
                int help_id = rs.getInt("help_id");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                
                p = ph.getByID(expectedProduct_id);
                
               jso = mh.getByID(Integer.toString(wisher_id));
               jsa2 = jso.getJSONArray("data");
               JSONObject j = jsa2.getJSONObject(0);
               String wisher_name = j.getString("name");
               jso = mh.getByID(Integer.toString(id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String seller_name = j.getString("name");
                //a.setwisherName(getName(a.getwisherId()));
                /** 將每一筆幫助資料產生一個新help物件 */
               h = new Help(order_id, help_id, p,  wisher_id, status, start_time);
               
               /**呼叫setOrderStatus設a的status、start_time*/
               //setOrderStatus(a);
               /**呼叫setProductInfo設a的product_name、category、description*/
               //setProductInfo(a);
               /**呼叫setsellerName設a的seller_name*/
               //a.setsellerName(getName(a.getsellerId()));
               //a.setwisherName(getName(a.getwisherId()));
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
               //help_list.add(a);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
               jsa.put(h.getData());
               /*jso.put("order_id", order_id);
               jso.put("wisher_name", p.getName());
               jso.put("product_id", product_id);
               jso.put("help_id", help_id);
               jso.put("mean_of_transaction", mean_of_transaction);
               jso.put("status", status);
               jso.put("start_time", start_time);
               jso.put("seller_name", getName(id));
               //jso.put("wisher_name", getName(wisher_id));
               jsa.put(jso);
               jso = new JSONObject();*/
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
        Product p = null;
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
            	"ON `missa`.`wishpool_order`.`help_id` = `missa`.`wishpool_status`.`id`"+
            "WHERE `missa`.`wishpool_order`.`help_id` = ? AND `missa`.`wishpool_status`.`wishOrNot` = ?";
            
            
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
                int wisher_id = rs.getInt("wisher_id");
                int expectedProduct_id = rs.getInt("expectedProduct_id");
                int help_id = rs.getInt("help_id");
                String status  = rs.getString("status");
                Timestamp start_time = rs.getTimestamp("start_time");
                Double token = rs.getDouble("token");
                p = ph.getByID(expectedProduct_id);
               jso = mh.getByID(Integer.toString(seller_id));
               jsa2 = jso.getJSONArray("data");
               JSONObject j = jsa2.getJSONObject(0);
               String seller_name = j.getString("name");
               jso = mh.getByID(Integer.toString(wisher_id));
               jsa2 = jso.getJSONArray("data");
               j = jsa2.getJSONObject(0);
               String wisher_name = j.getString("name");
               
                /** 將每一筆幫助資料產生一個新help物件 */
               h = new Help(order_id, token, wisher_id, seller_id,  p, help_id, status, start_time);
               
              
               
               h.setwisherName(wisher_name);
               h.setsellerName(seller_name);
               
               //help_list.add(a);
                /** 取出該幫助之資料並封裝至 JSONsonArray 內 */
               jso = h.getData();
               jso.put("token", token);
               
               jsa.put(jso);
               /*jso.put("order_id", order_id);
               jso.put("wisher_name", p.getName());
               jso.put("product_id", product_id);
               jso.put("help_id", help_id);
               jso.put("mean_of_transaction", mean_of_transaction);
               jso.put("status", status);
               jso.put("start_time", start_time);
               jso.put("seller_name", getName(id));
               //jso.put("wisher_name", getName(wisher_id));
               jsa.put(jso);
               jso = new JSONObject();*/
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
            String sql = "INSERT INTO `missa`.`wishpool_order`(`start_time`, `status`, `wishOrNot`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            
            Timestamp create_time =Timestamp.valueOf(LocalDateTime.now());
            String status = "等待許願者回覆";
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
            double token = h.getToken();
            
            
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
    
    /**getBywisherId呼叫此方法來設help物件的start_time, status
     * 
     * @param help help物件
     */
    /*public void setOrderStatus(help help) {

    	//new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)
    	 String exexcute_sql = "";
       
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
     /*   ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
           /* conn = DBMgr.getConnection();
            /** SQL指令 */
           /* String sql = "SELECT * FROM `missa`.`order_status` WHERE `id` = ?";
            
            
            /** 將參數回填至SQL指令當中 */
          /*  pres = conn.prepareStatement(sql);
            pres.setInt(1, help.gethelpId());
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
         /*   rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
        /*    exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 將 ResultSet 之資料取出 */
        /*    String status  = rs.getString("status");
            Timestamp start_time = rs.getTimestamp("start_time");
            /**將help的start_time, status設值*/
       /*     help.setStartTime(start_time);
            help.setStatus(status);
            
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
        /*    System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
      /*      e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
     /*       DBMgr.close(rs, pres, conn);
        }
    	
        
    }	//end setOrderStatus
    
    /**getBywisherId呼叫此方法來設help物件的product_name, description, category
     * 
     * @param help
     */
  /*  public void setProductInfo(help help) {
    	
    	 String exexcute_sql = "";
         
         
         ResultSet rs = null;
         
         try {
             /** 取得資料庫之連線 */
  //           conn = DBMgr.getConnection();
             /** SQL指令 */
 //            String sql = "SELECT * FROM `missa`.`product` WHERE `id` = ?";
             
             
             /** 將參數回填至SQL指令當中 */
//             pres = conn.prepareStatement(sql);
  //           pres.setInt(1, help.getProductId());
             /** 執行查詢之SQL指令並記錄其回傳之資料 */
   //          rs = pres.executeQuery();

             /** 紀錄真實執行的SQL指令，並印出 **/
     //        exexcute_sql = pres.toString();
       //      System.out.println(exexcute_sql);
             
             /** 將 ResultSet 之資料取出 */
         //    String product_name = rs.getString("name");
    //         String description = rs.getString("description");
    //         String category = rs.getString("category");
             
     //        sql = "SELECT * FROM `missa`.`product` WHERE `id` = ?";

             /** 將參數回填至SQL指令當中 */
   //          pres = conn.prepareStatement(sql);
     //        pres.setInt(1, help.getExchangedProductId());
             /** 執行查詢之SQL指令並記錄其回傳之資料 */
    //         rs = pres.executeQuery();
             
             /** 紀錄真實執行的SQL指令，並印出 **/
    //         exexcute_sql = pres.toString();
    //         System.out.println(exexcute_sql);
             
    //         String exchangedProduct_name = rs.getString("name");
             
             /**將help的product_name, description, category, exchangedProductName設值*/
    //         help.setProductName(product_name);
      //       help.setDescription(description);
    //         help.setCategory(category);
    //         help.setExchangedProductName(exchangedProduct_name);
             
   //      } catch (SQLException e) {
             /** 印出JDBC SQL指令錯誤 **/
   //          System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
  //       } catch (Exception e) {
             /** 若錯誤則印出錯誤訊息 */
   //          e.printStackTrace();
  //       } finally {
             /** 關閉連線並釋放所有資料庫相關之資源 **/
  //           DBMgr.close(rs, pres, conn);
    //     }
    	
 //   }	//end setProductInfo
    
    /**getBywisherId或getBysellerId呼叫此方法來取得help物件的seller_name/wisher_name
     * 
     * @param member_id
     */
   /* public String getName(int member_id) {
    	
    	String exexcute_sql = "";
    	String member_name = "";
        
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
   //     ResultSet rs = null;
        
   //     try {
    //        /** 取得資料庫之連線 */
   //         conn = DBMgr.getConnection();
   //         /** SQL指令 */
   //         String sql = "SELECT * FROM `missa`.`members` WHERE `missa`.`members`.`id` = ?";
            
            
            /** 將參數回填至SQL指令當中 */
  //          pres = conn.prepareStatement(sql);
  //          pres.setInt(1, member_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
  //          rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
  //          exexcute_sql = pres.toString();
  //          System.out.println(exexcute_sql);
            
            /** 將 ResultSet 之資料取出 */
  //          member_name = rs.getString("name");
            
            
           
            
  //      } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
  //          System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
   //     } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
  //          e.printStackTrace();
    //    } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
   //         DBMgr.close(rs, pres, conn);
     //   }
        
   //     return member_name;
    	
    //}	//end setsellerName
    
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
   
   	
   

}

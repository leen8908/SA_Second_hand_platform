package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.demo.app.ExpectedProduct;

public class ExpectedProductHelper {
    private ExpectedProductHelper() {
        
    }
    
    private static ExpectedProductHelper eph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private MemberHelper mh = MemberHelper.getHelper();
    
    public static ExpectedProductHelper getHelper() {
        /** Singleton檢查是否已經有ProductHelper物件，若無則new一個，若有則直接回傳 */
        if(eph == null) eph = new ExpectedProductHelper();
        
        return eph;
    }
    /**
     * 取得全部資料
     */
    public JSONObject getAllEProduct(String m_id) {
        /** 新建一個 ExpectedProduct 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	ExpectedProduct ep = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
        JSONObject j = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`product`  where `product`.`expectedProductOrNot`=1 and `status`=0"
            		+" and `product`.`member_id` != ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            pres.setString(1, m_id);
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int product_id = rs.getInt("id");
                String name = rs.getString("name");
                String category=rs.getString("category");
                String describtion = rs.getString("describtion");
                //image
                //String image = rs.getString("image");
                double token = rs.getDouble("token");
                String area =rs.getString("area");
                boolean status=rs.getBoolean("status");
                int member_id = rs.getInt("member_id");
                jso = mh.getByID(Integer.toString(member_id));
                jsa2 = jso.getJSONArray("data");
                j = jsa2.getJSONObject(0);
                String owner_name = j.getString("name");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                ep = new ExpectedProduct(product_id, name,category,describtion, token,area,status,member_id );
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                ep.setMemberId(member_id);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                JSONObject jproduct = ep.getData();
                jproduct.put("owner_name", owner_name);
                jsa.put(jproduct);
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
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    /**
     * 用ID取得所有資料
     * 不知道要不要做
     * */
    public JSONObject getByIdList(String data) {
      /** 新建一個 ExpectedProduct 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
      ExpectedProduct p = null;
      /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
      JSONArray jsa = new JSONArray();
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
          String[] in_para = DBMgr.stringToArray(data, ",");
          /** SQL指令 */
          String sql = "SELECT * FROM `missa`.`product` WHERE `product`.`id`";
          for (int i=0 ; i < in_para.length ; i++) {
              sql += (i == 0) ? "in (?" : ", ?";
              sql += (i == in_para.length-1) ? ")" : "";
          }
          
          /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
          pres = conn.prepareStatement(sql);
          for (int i=0 ; i < in_para.length ; i++) {
            pres.setString(i+1, in_para[i]);
          }
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
              int product_id = rs.getInt("id");
              String name = rs.getString("name");
              String category=rs.getString("category");
              String describtion = rs.getString("describtion");
              double token = rs.getDouble("token");
              String area=rs.getString("area");
              boolean status=rs.getBoolean("status");
              String image = rs.getString("image");
              
              
              /** 將每一筆商品資料產生一名新Product物件 */
              p = new ExpectedProduct(product_id, name, category, describtion,token, area,status);
              /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
              jsa.put(p.getData());
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
      long duration = (end_time - start_time);
      
      /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
      JSONObject response = new JSONObject();
      response.put("sql", exexcute_sql);
      response.put("row", row);
      response.put("time", duration);
      response.put("data", jsa);

      return response;
  }
    /**
     * 查看資料
     * 利用MEMBER_ID取得資料
     * 要存入JSONObjectject
     */
    public JSONObject getByMemberId(String id) {
        /** 新建一個 Product 物件之 p 變數，用於紀錄每一位查詢回之商品資料 */
        ExpectedProduct ep = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
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
            String sql = "SELECT * FROM `missa`.`product` WHERE `member_id` = ? and expectedProductOrNot=1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該商品編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	int product_id = rs.getInt("id");
                String name = rs.getString("name");
                String category=rs.getString("category");
                String describtion = rs.getString("describtion");
                //String image = rs.getString("image");
                double token = rs.getDouble("token");
                String area =rs.getString("area");
                //int member_id=rs.getInt("member_id"); 
                boolean status=rs.getBoolean("status");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                ep = new ExpectedProduct( product_id,name,category,describtion, token,area,status );
                /** 取出該商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(ep.getData());
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
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 查看單筆資料
     * 利用名字取得資料
     * 用於搜尋商品
     */
    public JSONObject getByName(String product_name, String m_id) {
    	 /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	ExpectedProduct ep = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        JSONArray jsa2 = null;
        JSONObject jso = new JSONObject();
        JSONObject j = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`product` WHERE  `name` LIKE ? AND `product`.`status`= 0 AND `product`.`expectedProductOrNot`= 1 "+
            "and `product`.`member_id` != ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, "%"+product_name+"%");
            pres.setString(2, m_id);
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
                int product_id = rs.getInt("id");
                String name = rs.getString("name");
                String category=rs.getString("category");
                String describtion = rs.getString("describtion");
                double token = rs.getDouble("token");
                String area =rs.getString("area");
                boolean status=rs.getBoolean("status");
                boolean expectedProductOrNot=rs.getBoolean("expectedProductOrNot");
                int member_id = rs.getInt("member_id");
                jso = mh.getByID(Integer.toString(member_id));
                jsa2 = jso.getJSONArray("data");
                j = jsa2.getJSONObject(0);
                String owner_name = j.getString("name");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                ep = new ExpectedProduct(product_id, name,category,describtion, token,area,status );
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                ep.setMemberId(member_id);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                JSONObject jproduct = ep.getData();
                jproduct.put("owner_name", owner_name);
                jsa.put(jproduct);
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
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    /**
     * 取得單筆資料
     * 查看單筆資料
     * 利用ID取得資料
     * 要存入JSONObjectject
     */
    public JSONObject getByID(String id) {
        /** 新建一個 ExpectedProduct 物件之 p 變數，用於紀錄每一位查詢回之商品資料 */
        ExpectedProduct ep = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
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
            String sql = "SELECT * FROM `missa`.`product` WHERE `id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該商品編號之資料，因此其實可以不用使用 while 迴圈 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	int product_id = rs.getInt("id");
                String name = rs.getString("name");
                String category=rs.getString("category");
                String describtion = rs.getString("describtion");
                double token = rs.getDouble("token");
                String area =rs.getString("area");
                boolean status=rs.getBoolean("status");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                ep = new ExpectedProduct(product_id, name,category,describtion, token,area,status );
                /** 取出該商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(ep.getData());
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
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    /**
     * 新增一個商品
     * 
     * @param p Product物件
     * 
     */
    
    public JSONObject create(ExpectedProduct ep) {
    	/** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        //12/27 here
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            /** member_id從cookie取*/
            String sql = 
            		"INSERT INTO `missa`.`product`(`name`, `category`,`describtion`, `token`,`area`,`status`,`add_time`,`corr_time`,`del_time`,`expectedProductOrNot`,`member_id`)"
                    + "VALUES( ?,?, ?, ?, ?, ?,?,?,?,?,?)";
            
            /** 取得所需之參數 */
            //int member_id = ep.getMemberId();
            String name = ep.getName();
            String category=ep.getCategory();
            String describtion = ep.getDescribtion();
            double token = ep.getToken();
            String area=ep.getArea();
            int member_id=ep.getMemberId();            

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);

            pres.setString(1, name);
            pres.setString(2, category);
            pres.setString(3, describtion);
            pres.setDouble(4, token);
            pres.setString(5, area);
            pres.setBoolean(6, false);
            pres.setTimestamp(7,Timestamp.valueOf(LocalDateTime.now()));
            pres.setTimestamp(8,null);
            pres.setTimestamp(9,null);
            pres.setBoolean(10,true);
            pres.setInt(11,member_id);
           
                        
            /** 執行新增之SQL指令並記錄影響之行數 */
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
        response.put("time", duration);
        response.put("row", row);

        return response; 
    }
    /**
     * 修改商品之資料
     *
     * @param p Product物件
     * 
     */
    public JSONObject update(ExpectedProduct ep) {
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
            String sql = "Update `missa`.`product` SET  `describtion`= ? , `area` = ?,`corr_time`=? WHERE `id` = ?";
            
            /** 取得所需之參數 */
            
            int id = ep.getId();
            //String name = ep.getName();
            String describtion = ep.getDescribtion();
            String area=ep.getArea();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            //pres.setString(1, name);
            pres.setString(1, describtion);
            pres.setString(2, area);
            pres.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            pres.setInt(4, id);
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

    /**
     * 透過商品編號（ID）刪除商品
     *
     * @param id 商品編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByID(int id) {
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
            String sql = "DELETE FROM `missa`.`product` WHERE `id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
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
    }
    public ExpectedProduct getById_me(int id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
        ExpectedProduct p = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`product` WHERE `product`.`id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	int product_id = rs.getInt("id");
                String name = rs.getString("name");
                String category=rs.getString("category");
                String describtion = rs.getString("describtion");
                //String image = rs.getString("image");
                double token = rs.getDouble("token");
                String area =rs.getString("area");
                boolean status=rs.getBoolean("status");
                /** 將每一筆商品資料產生一名新Product物件 */
                p = new ExpectedProduct( product_id,name,category,describtion, token,area,status );
                /** 取出該商品之資料並封裝至 JSONsonArray 內 */
                //jsa.put(p.getData());
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

        return p;
    }

}
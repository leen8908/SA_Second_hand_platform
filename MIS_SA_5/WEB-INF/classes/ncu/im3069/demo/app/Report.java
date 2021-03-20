package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;


public class Report {

    /** id，訂單編號 */
    private int id;


    /** report_member_name，檢舉人的帳號名稱  */
    private String report_member_name;
    
    /** report_member_id，檢舉人的id */
    private int report_member_id;

    /** reported_member_id，被檢舉人的id */
    private int reported_member_id;

    /** product，被檢舉的商品*/
    private int product_id;
    
    /** product，被檢舉的商品名*/
    private String product_name;

    /** Match，被檢舉的媒合*/
    /**private Match = match;**/
    
    /** list，被檢舉的許願*/
    /**private Wish = wish;*/
    
    /** content，檢舉理由*/
    private String content;
    
    /** product，審核的管理員*/
    private int manager_id;
    
    /** status，審核狀態  */
    private String status;

    /** date，審核創建時間 */
    private Timestamp date;

    /** finish_date，審核完成時間 */
    private Timestamp finish_date;

    /** rh，ReportHelper 之物件與 Report 相關之資料庫方法（Sigleton） */
    private ReportHelper rh = ReportHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立新的檢舉
     *
     * @param report_member_id 檢舉會員編號
     * @param report_member_name 檢舉會員帳號名
     * @param product_id 被檢舉商品編號
     * @param product_name 被檢舉商品名
     * @param reported_member_id 被檢舉會員編號
     * @param content 檢舉理由
     */
    public Report(int report_member_id, String report_member_name, int product_id, String product_name,int reported_member_id, String content) {
        this.report_member_id = report_member_id;
        this.report_member_name = report_member_name;
        this.product_id = product_id;
        this.product_name = product_name;
        this.reported_member_id = reported_member_id;
        this.content = content;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Report物件<br>
     * 採用多載（overload）方法進行，此建構子用於登入會員資料時，產生一名新的會員
     *
     * @param id
     */
    public Report(int id,int manager_id) {
    	this.id = id;
    	this.manager_id=manager_id;
    }
    
    /**
     * 查看!!
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立新的檢舉
     *
     * @param report_member_id 檢舉會員編號
     * @param report_member_name 檢舉會員帳號名
     * @param product_id 被檢舉商品編號
     * @param product_name 被檢舉商品名
     * @param reported_member_id 被檢舉會員編號
     * @param content 檢舉理由
     */
    public Report(int id,int report_member_id, String report_member_name, int product_id, String product_name,int reported_member_id,String status ,String content,int manager_id,Timestamp date,Timestamp finish_date) {
        this.id=id;
    	this.report_member_id = report_member_id;
        this.report_member_name = report_member_name;
        this.product_id = product_id;
        this.product_name = product_name;
        this.reported_member_id = reported_member_id;
        this.status=status;
        this.content = content;
        this.manager_id=manager_id;
        this.date=date;
        this.finish_date=finish_date;
    }


    /**
     * 取得檢舉編號
     *
     * @return int 回傳檢舉編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * 取得檢舉人的帳號名稱
     *
     * @return String 回傳檢舉人的帳號名稱
     */
    public String getReportMemberName() {
        return this.report_member_name;
    }

    /**
     * 取得檢舉人的會員編號
     *
     * @return int 回傳檢舉人的會員編號
     */
    public int getReportMemberID() {
        return this.report_member_id;
    }

    /**
     * 取得檢舉人的會員編號
     *
     * @return int 回傳被檢舉人的會員編號
     */
    public int getReportedMemberID() {
        return this.reported_member_id;
    }

    /**
     * 取得被檢舉商品的編號
     *
     * @return int 回傳被檢舉商品的編號
     */
    public int getProductID() {
        return this.product_id;
    }
    
    /**
     * 取得被檢舉商品的名稱
     *
     * @return String 回傳被檢舉商品的名稱
     */
    public String getProductName() {
        return this.product_name;
    }

    /**
     * 取得檢舉理由
     *
     * @return String
     */
    public String getCotent() {
        return this.content;
    }

    /**
     * 取得管理員的會員編號
     *
     * @return int 回傳管理員的會員編號
     */
    public int getManagerID() {
        return this.manager_id;
    }

    /**
     * 取得檢舉狀態
     *
     * @return String 回傳檢舉狀態
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * 取得檢舉建立的時間
     *
     * @return Timestamp 取得該檢舉建立的時間
     */
    public Timestamp getDate() {
        return this.date;
    }
    
    /**
     * 取得檢舉審核完的時間
     *
     * @return Timestamp 取得該檢舉建立的時間
     */
    public Timestamp getFinishDate() {
        return this.finish_date;
    }
    /**
     * 取得該檢舉所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("report_member_name", getReportMemberName());
        jso.put("report_member_id", getReportMemberID());
        jso.put("reported_member_id", getReportedMemberID());
        jso.put("product_id", getProductID());
        jso.put("product_name", getProductName());
        jso.put("content", getCotent());
        jso.put("manager_id", getManagerID());
        jso.put("status", getStatus());
        jso.put("date", getDate());
        jso.put("finish_date", getFinishDate());
        
        return jso;
    }
    
    /**
	 * 
	 * 用來存更新狀態的資料*/
    public JSONObject update_status(boolean success) {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名商品是否已經在資料庫 */
        if(this.id != 0 ) {
            /** 透過ProductHelper物件，更新目前之商品資料置資料庫中 */
            data = rh.updateStatus(this.id,this.manager_id,success);
        }        
        return data;
    }

}

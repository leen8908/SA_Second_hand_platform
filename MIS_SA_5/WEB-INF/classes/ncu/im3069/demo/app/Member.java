package ncu.im3069.demo.app;

import org.json.*;
import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Member
 * Member類別（class）具有會員所需要之屬性與方法，並且儲存與會員相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Member {
    
    /** id，會員編號 */
    private int id;
       
    /** name，會員帳號 */
    private String name;
    
    /** password，會員密碼 */
    private String password;
    
    /** email，會員電子郵件信箱 */
    private String email;
    
    /** phoneNumber，會員手機號碼 */
    private String phoneNumber;
    
    /** token_amount，會員之代幣總額 */
    private double token_amount;
    
    /** status，會員之狀態(是否被停權) */
    private boolean status;
    
    /** managerOrNot，是否為管理員 */
    private boolean managerOrNot;
        
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立會員資料時，產生一名新的會員
     *
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     * @param phoneNumber 會員手機號碼
     */
    public Member(String name, String password, String email,String  phoneNumber ) {
    	this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新會員資料時，將每一筆資料新增為一個會員物件
     * @param id 會員編號
     * @param password 會員密碼
     * @param phoneNumber 會員手機號碼
     */
    public Member(int id, String password ,String phoneNumber) {
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢會員資料時，將每一筆資料新增為一個會員物件
     *
     * @param id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員帳號
     * @param phoneNumber 會員手機號碼
     * @param token_amount 會員代幣總額
     * @param status 會員是否被停權
     * @param managerOrNot 會員是否為管理員
     */
    public Member(int id, String email, String password, String name,String phoneNumber,double token_amount, boolean status,boolean managerOrNot) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.token_amount=token_amount;
        this.managerOrNot=managerOrNot;

    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於登入會員資料時，產生一名新的會員
     *
     * @param password 會員密碼
     * @param name 會員姓名
     */
    public Member(int id,boolean status,boolean managerOrNot) {
    	this.id = id;
        this.status = status;
        this.managerOrNot = managerOrNot;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於登入會員資料時，產生一名新的會員
     *
     * @param password 會員密碼
     * @param name 會員姓名
     */
    public Member(String name, String password) {
    	this.name = name;
        this.password = password;
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於登入會員資料時，產生一名新的會員
     *
     * @param id
     */
    public Member(int id) {
    	this.id = id;
    }
    
    /**
     * 取得會員之編號
     *
     * @return the id 回傳會員編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得會員之電子郵件信箱
     *
     * @return the email 回傳會員電子郵件信箱
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 取得會員帳號
     *
     * @return the name 回傳會員帳號
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 取得會員之密碼
     *
     * @return the password 回傳會員密碼
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 取得會員之手機號碼
     *
     * @return the password 回傳手機號碼
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    /**
     * 取得會員之代幣總額
     *
     * @return the password 回傳會員代幣總額
     */
    public double getToken() {
        return this.token_amount;
    }
    
    /**
     * 取得會員是否被停權
     *
     * @return the status 回傳會員狀態
     */
    public boolean getStatus() {
        return this.status;
    }
    
    /**
     * 取得會員是否為管理員
     *
     * @return the status 回傳會員狀態
     */
    public boolean getManagerOrNot() {
        return this.managerOrNot;
    }
        
    
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = mh.update(this);
        }
        
        return data;
    }
    
    /**
     * 管理員更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject updateByManager() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = mh.updateByManager(this);
        }
        
        return data;
    }
    
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        jso.put("phoneNumber", getPhoneNumber());
        jso.put("token_amount", getToken());
        jso.put("status", getStatus());
        jso.put("managerOrNot", getManagerOrNot());
        
        return jso;
    }
    
    
    /**
	 * 
	 * 用來存更新狀態的資料*/
    public JSONObject update_status() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名商品是否已經在資料庫 */
        if(this.id != 0 ) {
            /** 透過ProductHelper物件，更新目前之商品資料置資料庫中 */
            data = mh.changeStatus(this.id);
        }        
        return data;
    }

}
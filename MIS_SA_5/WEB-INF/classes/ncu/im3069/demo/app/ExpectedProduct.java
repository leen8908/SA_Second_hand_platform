package ncu.im3069.demo.app;

import org.json.*;

import java.util.*;
import java.sql.*;
public class ExpectedProduct {
	
    private int id;
    private String name;
    private double token;
    private String category;
	private String describtion;
	private Timestamp add_time;
	private Timestamp del_time;
	private Timestamp corr_time;
	private String area;
	private boolean status;
 
	private int member_id;
	private boolean expectedProductOrNot;
	private int report_id;
	
	private ExpectedProductHelper eph =  ExpectedProductHelper.getHelper();
	/**
     * 實例化（Instantiates）一個新的（new）ExpectedProduct 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public ExpectedProduct(int id) {
		this.id = id;
	}
	/*新增*/
	public ExpectedProduct(String name,String category, double token,String describtion, String area,int member_id) {
		this.name = name;
		this.category=category;
		this.token = token;
		this.describtion=describtion;
		this.area = area;
		this.member_id=member_id;
	}
	/*修改*/
	public ExpectedProduct(int id, String name,String describtion, String area) {
		this.id = id;
		this.name = name;
		this.describtion=describtion;
		this.area = area;
	}
	/*查看*/
	public ExpectedProduct (int id,String name, String category, String describtion, double token, String area, boolean status) {
		this.id = id;
		this.name = name;
		this.category=category;
		this.describtion=describtion;
		this.token=token;
		this.area = area;
		this.status=status;
	}
	/*新增help*/
	public ExpectedProduct (int id,String name, String category, String describtion, double token, String area, boolean status,int member_id) {
		this.id = id;
		this.name = name;
		this.category=category;
		this.describtion=describtion;
		this.token=token;
		this.area = area;
		this.status=status;
		this.member_id=member_id;
	}
	
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	public String getCategory() {
		return this.category;
	}
	public String getDescribtion() {
		return this.describtion;
	}
	public double getToken() {
		return this.token;
	}
	public Timestamp getAddtime() {
		return this.add_time;
	}
	public Timestamp getCorrtime() {
		return this.corr_time;
	}
	public Timestamp getDeltime() {
		return this.del_time;
	}
	public Boolean getStatus() {
		return this.status;
	}
	public String getArea() {
		return this.area;
	}
	public int getMemberId() {
		return this.member_id;
	}
	public void setMemberId(int m_id) {
		this.member_id = m_id;
	}
	public boolean getExpectedProductOrNot() {
		return this.expectedProductOrNot;
	}
	public void setReportedId() {
		this.report_id=report_id;
	}
	public int getReportedId() {
		return this.report_id;
	}
	/**
	 * 
	 * 用來存更新後的資料*/
    public JSONObject update_data() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名商品是否已經在資料庫 */
        if(this.id != 0) {
            /** 透過ProductHelper物件，更新目前之商品資料置資料庫中 */
            data = eph.update(this);
        }
        
        return data;
    }

	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("name", getName());
        jso.put("category", getCategory());
        jso.put("describtion", getDescribtion());
        jso.put("area", getArea());
        jso.put("token", getToken());
        jso.put("status", getStatus());
        jso.put("add_time", getAddtime());
        jso.put("corr_time", getCorrtime());
        jso.put("del_time", getDeltime());
        jso.put("member_id",getMemberId());
        jso.put("expectedProductorNot",getExpectedProductOrNot());
        jso.put("report_id",getReportedId());
        
       
        return jso;
    }
}

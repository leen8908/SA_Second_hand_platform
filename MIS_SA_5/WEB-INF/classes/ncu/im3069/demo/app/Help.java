package ncu.im3069.demo.app;

import java.sql.*;


import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.app.Product;


public class Help {

	private int order_id;
	private int help_id;
	private ExpectedProduct product;
	private Timestamp start_time;
	private Timestamp finish_time;
	private int wisher_id;
	private int seller_id;
	private String wisher_name;
	private String seller_name;
	private String status;
	private double token;
	private Member member;
	private String wisher_phone;
	private String seller_phone;
	
	/**實例化（Instantiates）一個新的（new）Help 物件
	 * 採用多載（overload）方法進行，此建構子用於取得訂單的申請資料時，產生申請詳情
	 * @param oid
	 * @param sid
	 * @param p
	 * @param hid
	 * @param edp
	 * @param t
	 * @param status
	 * @param start_time
	 * @param wisher_id
	 */
	public Help(int oid, int sid, int hid, ExpectedProduct p, double t, String status, Timestamp start_time, int wisher_id) {
		
		this.order_id = oid;
		this.seller_id = sid;
		this.help_id = hid;
		this.product = p;
		this.token = t;
		this.status = status;
		this.start_time = start_time;
		this.wisher_id = wisher_id;
	}
	
	/**實例化（Instantiates）一個新的（new）help 物件
	 * 採用多載（overload）方法進行，此建構子用於新增申請資料時，提出的申請
	 * @param oid 訂單編號
	 * @param pid 商品編號
	 * @param hid 申請編號
	 * @param edpid 許願者的期望物品
	 * @param t 代幣
	 * @param status 狀態
	 * @param start_time 申請時間
	 * @param wisher_id 許願者編號
	 */
	public Help(int oid, int hid, ExpectedProduct p,  int wisher_id, String status,Timestamp start_time) {
		//getgetBysellerId的建構子
		this.order_id = oid;
		this.help_id = hid;
		this.product = p;
		this.wisher_id = wisher_id;
		this.status = status;
		this.start_time = start_time;
	}
	
	public Help(int oid,int sid, ExpectedProduct p , String status, Timestamp start_time, int hid) {
		//getByWisherId的建構子
		this.order_id = oid;
		this.seller_id = sid;
		this.product = p;
		this.status = status;
		this.start_time = start_time;
		this.help_id = hid;
	}
	public Help(int oid, int hid, int sid, int wisher_id, String status, Timestamp start_time, ExpectedProduct p) {
		
		this.order_id = oid;
		this.help_id = hid;
		this.seller_id = sid;
		this.wisher_id = wisher_id;
		this.status = status;
		this.start_time = start_time;
		this.product = p;
	}
	
	/**實例化（Instantiates）一個新的（new）help 物件
	 * 採用多載（overload）方法進行，此建構子用於取得訂單的申請資料時，產生全部的申請清單
	 * @param oid
	 * @param sid
	 * @param p
	 * @param hid
	 * @param status
	 * @param start_time
	 * @param wisher_id
	 */
	public Help(int oid, double t, int wisher_id, int sid, ExpectedProduct p, int hid, String status, Timestamp start_time) {
		//getByhelpId的建構子
		this.order_id = oid;
		this.seller_id = sid;
		this.product = p;
		this.help_id = hid;
		this.status = status;
		this.start_time = start_time;
		this.wisher_id = wisher_id;
		this.token = t;
	}
	
	public Help(int oid, double t, int wisher_id, int sid, ExpectedProduct p, int hid, String status, Timestamp start_time, Timestamp finish_time) {
		//getByhelpId的建構子
		this.order_id = oid;
		this.seller_id = sid;
		this.product = p;
		this.help_id = hid;
		this.status = status;
		this.start_time = start_time;
		this.wisher_id = wisher_id;
		this.token = t;
		this.finish_time = finish_time;
	}
	
	public Help(int oid, double t, int wisher_id, int sid, ExpectedProduct p, int hid, String status, Timestamp start_time, Member m) {
		//getByhelpId的建構子
		this.order_id = oid;
		this.seller_id = sid;
		this.product = p;
		this.help_id = hid;
		this.status = status;
		this.start_time = start_time;
		this.wisher_id = wisher_id;
		this.token = t;
		this.member = m;
	}
	
	public Help( int wid, int sid, ExpectedProduct p, double t) {
		
		this.wisher_id = wid;
		this.seller_id = sid;
		this.product = p;
		this.token = t;
		
		
	}
	
	public Help(int hid, String status) {
		this.help_id = hid;
		this.status = status;
	}

	public int getOrderId() {
		
		return this.order_id;
		
	}
	
	public void setOrderId(int oi) {
		
		this.order_id = oi;
		
	}
	
	public int getwisherId() {
		
		return this.wisher_id;
		
	}
	
	public void setwisherId(int oid) {
		
		this.order_id = oid;
		
	}
	
	public int getsellerId() {
		
		return this.seller_id;
		
	}
	
	public void setsellerId(int sid) {
		
		this.seller_id = sid;
		
	}
	
	public String getsellerName() {
		
		return this.seller_name;
		
	}
	
	public void setsellerName(String sn) {
		
		this.seller_name = sn;
		
	}

	
	
	public String getwisherName() {
		
		return this.wisher_name;
		
	}
	
	public void setwisherName(String on) {
		
		this.wisher_name = on;
		
	}
	
	public String getsellerPhone() {
		
		return this.seller_phone;
		
	}
	
	public void setsellerPhone(String sp) {
		
		this.seller_phone = sp;
		
	}

	
	
	public String getwisherPhone() {
		
		return this.wisher_phone;
		
	}
	
	public void setwisherPhone(String wp) {
		
		this.wisher_phone = wp;
		
	}
	
	
	
	public int gethelpId() {
		
		return this.help_id;
		
	}
	
	
	public Timestamp getStartTime() {
		
		return this.start_time;
		
	}
	
	public void setStartTime(Timestamp t) {
		
		this.start_time = t;
		
	}
	
	public Timestamp getFinishTime() {
		
		return this.finish_time;
		
	}
	
	public void setFinishTime(Timestamp ft) {
		
		this.finish_time = ft;
		
	}
	
	public String getStatus() {
		
		return this.status;
		
	}
	
	public void setStatus(String s) {
		
		this.status = s;
		
	}
	
	public ExpectedProduct getExpectedProduct() {
		
		return this.product;
		
	}
	
	
	/*public void setExpectedProductId(Product p) {
		
		this.product = p;
		
	}*/
	
	
	
	
	public double getToken() {
		
		return this.token;
		
	}
	
	public void setToken(double t) {
		
		this.token = t;
		
	}
	
	public JSONObject getData() {
		System.out.print(product.getName());
        /** 透過JSONObject將該申請所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("order_id", getOrderId());
        jso.put("wisher_name", getwisherName());
        jso.put("seller_name", getsellerName());
        jso.put("product_name", product.getName());
        jso.put("start_time", getStartTime());
        jso.put("finish_time", getFinishTime());
        jso.put("status", getStatus());
        jso.put("category", product.getCategory());
        jso.put("description", product.getDescribtion());
        //jso.put("image", product.getImage());
        jso.put("help_id", gethelpId());
        //jso.put("member_email", getwisherName());
        jso.put("seller_phone", getsellerPhone());
        jso.put("wisher_phone", getwisherPhone());
        //jso.put("exchangedProductName", getExchangedProductName());
        //jso.put("token", getToken());
        System.out.print(getwisherName());
        return jso;
    }
	

}

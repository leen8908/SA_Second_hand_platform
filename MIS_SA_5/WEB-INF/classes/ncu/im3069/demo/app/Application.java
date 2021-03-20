package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.app.Product;


public class Application {

	private int order_id;
	private int application_id;
	private Product product;
	private Timestamp start_time;
	private Timestamp feedback_time;
	private Timestamp finish_time;
	private int owner_id;
	private int buyer_id;
	private String owner_name;
	private String buyer_name;
	private String status;
	private String mean_of_transaction;
	private double token;
	private Product exchangedProduct;
	
	
	/**實例化（Instantiates）一個新的（new）Application 物件
	 * 採用多載（overload）方法進行，此建構子用於
	 * @param oid
	 * @param bid
	 * @param p
	 * @param aid
	 * @param mot
	 * @param edp
	 * @param t
	 * @param status
	 * @param start_time
	 * @param owner_id
	 */
	/*public Application(int oid, int bid, Product p, int aid, String mot, Product edp, double t, String status, Timestamp start_time, int owner_id) {
		
		this.order_id = oid;
		this.buyer_id = bid;
		this.product = p;
		this.application_id = aid;
		this.mean_of_transaction = mot;
		this.exchangedProduct = edp;
		this.token = t;
		this.status = status;
		this.start_time = start_time;
		this.owner_id = owner_id;
	}*/
	
	/**實例化（Instantiates）一個新的（new）Application 物件
	 * 採用多載（overload）方法進行，此建構子用於查看申請資料
	 * @param oid 訂單編號
	 * @param pid 商品編號
	 * @param aid 申請編號
	 * @param mot 交易方式
	 * @param edpid 物主選擇作為交換的商品
	 * @param t 代幣
	 * @param status 狀態
	 * @param start_time 申請時間
	 * @param owner_id 物主編號
	 */
	public Application(int oid, int aid, int bid, int owner_id, String status, Timestamp start_time, Product p, String mot) {
		
		this.order_id = oid;
		this.application_id = aid;
		this.buyer_id = bid;
		this.owner_id = owner_id;
		this.status = status;
		this.start_time = start_time;
		this.product = p;
		this.mean_of_transaction = mot;
		
	}
	
	/**實例化（Instantiates）一個新的（new）Application 物件
	 * 採用多載（overload）方法進行，此建構子用於
	 * @param oid
	 * @param bid
	 * @param p
	 * @param aid
	 * @param status
	 * @param start_time
	 * @param owner_id
	 */
	/*public Application(int oid, int bid, Product p, int aid, String status, Timestamp start_time, int owner_id) {
		
		this.order_id = oid;
		this.buyer_id = bid;
		this.product = p;
		this.application_id = aid;
		this.status = status;
		this.start_time = start_time;
		this.owner_id = owner_id;
		
	}*/
	
	/**實例化（Instantiates）一個新的（new）Application 物件
	 * 採用多載（overload）方法進行，此建構子用於applicationController新增新的申請
	 * @param mot
	 * @param oid
	 * @param bid
	 * @param p
	 * @param t
	 */
	public Application(String mot, int oid, int bid, Product p, double t) {
		
		this.mean_of_transaction = mot;
		this.owner_id = oid;
		this.buyer_id = bid;
		this.product = p;
		this.token = t;
		
		
	}
	public int getOrderId() {
		
		return this.order_id;
		
	}
	
	public void setOrderId(int oi) {
		
		this.order_id = oi;
		
	}
	
	public int getOwnerId() {
		
		return this.owner_id;
		
	}
	
	public void setOwnerId(int oid) {
		
		this.order_id = oid;
		
	}
	
	public int getBuyerId() {
		
		return this.buyer_id;
		
	}
	
	public void setBuyerId(int bid) {
		
		this.buyer_id = bid;
		
	}
	
	public String getBuyerName() {
		
		return this.buyer_name;
		
	}
	
	public void setBuyerName(String bn) {
		
		this.buyer_name = bn;
		
	}

	public String getOwnerName() {
		
		return this.owner_name;
		
	}
	
	public void setOwnerName(String on) {
		
		this.owner_name = on;
		
	}
	
	public int getApplicationId() {
		
		return this.application_id;
		
	}
	
	public Product getProduct() {
		
		return this.product;
		
	}
	
	public Timestamp getStartTime() {
		
		return this.start_time;
		
	}
	
	public void setStartTime(Timestamp t) {
		
		this.start_time = t;
		
	}
	
	public String getStatus() {
		
		return this.status;
		
	}
	
	public void setStatus(String s) {
		
		this.status = s;
		
	}
	
	

	
	
	public String getTransactionMean() {
		
		return this.mean_of_transaction;
		
	}
	
	public void setTransactionMean(String s) {
		
		this.mean_of_transaction = s;
		
	}
	
	public Product getExchangedProduct() {
		
		return this.exchangedProduct;
		
	}
	
	public void setExchangedProductId(Product p) {
		
		this.exchangedProduct = p;
		
	}
	
	
	
	public double getToken() {
		
		return this.token;
		
	}
	
	public void setToken(double t) {
		
		this.token = t;
		
	}
	
	public JSONObject getData() {
		
        /** 透過JSONObject將該申請所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("order_id", getOrderId());
        jso.put("owner_name", getOwnerName());
        jso.put("buyer_name", getBuyerName());
        jso.put("product_name", product.getName());
        jso.put("start_time", getStartTime());
        jso.put("status", getStatus());
        jso.put("category", product.getCategory());
        jso.put("description", product.getDescribtion());
        jso.put("mean_of_transaction", getTransactionMean());
        jso.put("application_id", getApplicationId());
        jso.put("product_id", product.getId());
        jso.put("buyer_id", getBuyerId());
        jso.put("owner_id", getOwnerId());
        //jso.put("exchangedProductName", getExchangedProductName());
        //jso.put("token", getToken());
        
        return jso;
    }
	

}

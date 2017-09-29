package Pojo;

public class ProductItemsPojo {
	
	
	private String itemid;
	private String item_name;
	private String mainImage_url;
	private String price;
	private String price_type;
	private String pro_owner_Id;
	private boolean seller_online_status; 
	
	
	
	public ProductItemsPojo(String itemid, String item_name,
			String mainImage_url, String price,String price_type,String pro_owner_Id,boolean  seller_online_status) {
		super();
		this.itemid = itemid;
		this.item_name = item_name;
		this.mainImage_url = mainImage_url;
		this.price = price;
		this.price_type=price_type;
		this.pro_owner_Id=pro_owner_Id;
		this.seller_online_status=seller_online_status;
	}
	public String getPrice_type() {
		return price_type;
	}
	public void setPrice_type(String price_type) {
		this.price_type = price_type;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getMainImage_url() {
		return mainImage_url;
	}
	public void setMainImage_url(String mainImage_url) {
		this.mainImage_url = mainImage_url;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPro_owner_Id() {
		return pro_owner_Id;
	}
	public void setPro_owner_Id(String pro_owner_Id) {
		this.pro_owner_Id = pro_owner_Id;
	}
	public boolean isSeller_online_status() {
		return seller_online_status;
	}
	public void setSeller_online_status(boolean seller_online_status) {
		this.seller_online_status = seller_online_status;
	}

}

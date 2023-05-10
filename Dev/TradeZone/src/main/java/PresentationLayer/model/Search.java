package PresentationLayer.model;

public class Search {
	
	private String search_product;
	private String search_by;
	private String max_price;
	private String product_rate;	//default - 0
	private String store_rate;	   //default - 0

	public String getSearchProducts() {
		return search_product;
	}

	public void setSearch_product(String search_product){
		this.search_product = search_product;
	}

	public void setSearch_by(String searchBy){
		this.search_by = searchBy;
	}

	public void setMax_price(String max_price){
		this.max_price = max_price;
	}

	public void setProduct_rate(String product_rate){
		this.product_rate = product_rate;
	}

	public void setStore_rate(String store_rate){
		this.store_rate = store_rate;
	}

	public String getSearchBy() {
		return search_by;
	}

	public String getMaxPrice() {
		return max_price;
	}

	public String getProductRate() {
		return product_rate;
	}

	public String getStoreRate() {
		return store_rate;
	}

	public String printAll(){
		return "search_product = " + this.search_product + " search by = " + this.search_by +
				" max price = " + this.max_price + " product rate = " + this.product_rate +
				" store rate = " + this.store_rate;
	}
}

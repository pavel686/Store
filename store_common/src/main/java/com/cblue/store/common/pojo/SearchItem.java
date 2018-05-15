package com.cblue.store.common.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable {

	private String id;
	private String title;
	private String sellponit;
	private long  price;
	private String image;
	private String categoryname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellponit() {
		return sellponit;
	}
	public void setSellponit(String sellponit) {
		this.sellponit = sellponit;
	}
	
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	
	
	
}

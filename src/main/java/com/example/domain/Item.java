package com.example.domain;

import java.util.List;

/**
 * itemsテーブルの情報を格納するエンティティ.
 * 
 * @author shumpei
 *
 */
public class Item {

	/** ID */
	private Integer id;
	/** 名前 */
	private String name;
	/** 状況 */
	private Integer condition;
	/** カテゴリー */
	private Integer category;
	/** ブランド */
	private String brand;
	/** 価格 */
	private double price;
	/** 配送状況 */
	private Integer shipping;
	/** 説明 */
	private String description;
	/** カテゴリー名のリスト */
	private List<String> categoryNameList;

	public Item() {
	}

	public Item(Integer id, String name, Integer condition, Integer category, String brand, double price,
			Integer shipping, String description, List<String> categoryNameList) {
		super();
		this.id = id;
		this.name = name;
		this.condition = condition;
		this.category = category;
		this.brand = brand;
		this.price = price;
		this.shipping = shipping;
		this.description = description;
		this.categoryNameList = categoryNameList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCategoryNameList() {
		return categoryNameList;
	}

	public void setCategoryNameList(List<String> categoryNameList) {
		this.categoryNameList = categoryNameList;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", condition=" + condition + ", category=" + category + ", brand="
				+ brand + ", price=" + price + ", shipping=" + shipping + ", description=" + description
				+ ", categoryNameList=" + categoryNameList + "]";
	}

}

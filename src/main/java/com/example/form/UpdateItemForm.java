package com.example.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品の更新情報を受け取るフォームクラス.
 * 
 * @author shumpei
 *
 */
public class UpdateItemForm {

	/** ID */
	private Integer id;

	/** 名前 */
	@NotBlank(message = "error:may not be empty")
	private String name;

	/** 価格 */
	@NotBlank(message = "error:may not be empty")
	@Pattern(regexp = "\\d+(\\.\\d+)?", message = "error:may not be other than numbers")
	private String price;

	/** ブランド */
	private String brand;

	/** カテゴリー */
	private Integer category;

	/** 状況 */
	private Integer condition;

	/** 説明 */
	@NotBlank(message = "error:may not be empty")
	private String description;

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UpdateItemForm [id=" + id + ", name=" + name + ", price=" + price + ", brand=" + brand + ", category="
				+ category + ", condition=" + condition + ", description=" + description + "]";
	}

}

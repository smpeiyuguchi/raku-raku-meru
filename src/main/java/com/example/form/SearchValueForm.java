package com.example.form;

/**
 * 検索時のパラメータを受け取るためのフォームクラス.
 * 
 * @author shumpei
 *
 */
public class SearchValueForm {

	/** 親カテゴリID */
	private Integer parentId;
	/** 中カテゴリID */
	private Integer childId;
	/** 子カテゴリID */
	private Integer grandChildId;
	/** 名前 */
	private String name;
	/** ブランド */
	private String brand;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public Integer getGrandChildId() {
		return grandChildId;
	}

	public void setGrandChildId(Integer grandChildId) {
		this.grandChildId = grandChildId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "SearchValueForm [parentId=" + parentId + ", childId=" + childId + ", grandChildId=" + grandChildId
				+ ", name=" + name + ", brand=" + brand + "]";
	}

}

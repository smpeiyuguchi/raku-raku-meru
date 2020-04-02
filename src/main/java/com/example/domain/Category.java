package com.example.domain;

import java.util.List;

/**
 * カテゴリーテーブルを操作するためのエンティティ.
 * 
 * @author shumpei
 *
 */
public class Category {

	/** ID */
	private Integer id;
	/** 親カテゴリID */
	private Integer parent;
	/** 名前 */
	private String name;
	/** 連動するカテゴリーリスト */
	private List<Category> categoryList;

	public Category() {
	}

	public Category(Integer id, Integer parent, String name, List<Category> categoryList) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
		this.categoryList = categoryList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parent=" + parent + ", name=" + name + ", categoryList=" + categoryList + "]";
	}

}

package com.example.domain;

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

	public Category() {
	}

	public Category(Integer id, Integer parent, String name) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
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

	@Override
	public String toString() {
		return "Category [id=" + id + ", parent=" + parent + ", name=" + name + "]";
	}

}

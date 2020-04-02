package com.example.domain;

/**
 * 全カテゴリ情報を格納するエンティティ.
 * 
 * @author shumpei
 *
 */
public class AllCategory {

	private Integer parentCategoryId;

	private String parentCategoryName;

	private Integer childCategoryID;

	private String childCategoryName;

	private Integer grandchildCategoryID;

	private String grandchildCategoryName;

	public AllCategory() {
	}

	public AllCategory(Integer parentCategoryId, String parentCategoryName, Integer childCategoryID,
			String childCategoryName, Integer grandchildCategoryID, String grandchildCategoryName) {
		super();
		this.parentCategoryId = parentCategoryId;
		this.parentCategoryName = parentCategoryName;
		this.childCategoryID = childCategoryID;
		this.childCategoryName = childCategoryName;
		this.grandchildCategoryID = grandchildCategoryID;
		this.grandchildCategoryName = grandchildCategoryName;
	}

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public Integer getChildCategoryID() {
		return childCategoryID;
	}

	public void setChildCategoryID(Integer childCategoryID) {
		this.childCategoryID = childCategoryID;
	}

	public String getChildCategoryName() {
		return childCategoryName;
	}

	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}

	public Integer getGrandchildCategoryID() {
		return grandchildCategoryID;
	}

	public void setGrandchildCategoryID(Integer grandchildCategoryID) {
		this.grandchildCategoryID = grandchildCategoryID;
	}

	public String getGrandchildCategoryName() {
		return grandchildCategoryName;
	}

	public void setGrandchildCategoryName(String grandchildCategoryName) {
		this.grandchildCategoryName = grandchildCategoryName;
	}

	@Override
	public String toString() {
		return "AllCategory [parentCategoryId=" + parentCategoryId + ", parentCategoryName=" + parentCategoryName
				+ ", childCategoryID=" + childCategoryID + ", childCategoryName=" + childCategoryName
				+ ", grandchildCategoryID=" + grandchildCategoryID + ", grandchildCategoryName="
				+ grandchildCategoryName + "]";
	}

}

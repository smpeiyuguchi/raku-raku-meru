package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Category;
import com.example.repository.CategoryRepository;

/**
 * カテゴリー情報の処理制御を行うサービスクラス.
 * 
 * @author shumpei
 *
 */
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * IDからカテゴリ情報を取得する.
	 * 
	 * @param id ID
	 * @return カテゴリ情報
	 */
	public Category SearchById(Integer id) {
		return categoryRepository.load(id);
	}

	/**
	 * 親カテゴリ情報を全件取得する.
	 * 
	 * @return 親カテゴリ情報
	 */
	public List<Category> searchParent() {
		return categoryRepository.findParent();
	}

	/**
	 * 子カテゴリ情報を全件取得する.
	 * 
	 * @return 子カテゴリ情報
	 */
	public List<Category> searchChild() {
		return categoryRepository.findChild();
	}

	/**
	 * 親カテゴリIDから子カテゴリ情報を取得する.
	 * 
	 * @param parenId 親カテゴリID
	 * @return 子カテゴリ情報
	 */
	public List<Category> searchChildByParentId(Integer parentId) {
		List<Category> categoryList = null;
		if (parentId == 0) {
			categoryList = categoryRepository.findChild();
		} else {
			categoryList = categoryRepository.findChildByParentId(parentId);
		}
		return categoryList;
	}

	/**
	 * 親カテゴリIDから孫カテゴリ情報を取得する.
	 * 
	 * @param parentId 親カテゴリID
	 * @return 孫カテゴリ情報
	 */
	public List<Category> searchGrandChildByParentId(Integer parentId) {
		List<Category> categoryList = null;
		if (parentId == 0) {
			categoryList = categoryRepository.findGrandChild();
		} else {
			categoryList = categoryRepository.findGrandChildByParentId(parentId);
		}
		return categoryList;
	}

	/**
	 * 孫カテゴリ情報を全件取得する.
	 * 
	 * @return 孫カテゴリ情報
	 */
	public List<Category> searchGrandChild() {
		return categoryRepository.findGrandChild();
	}

	/**
	 * 子カテゴリIDから孫カテゴリ情報を取得する.
	 * 
	 * @param childId 子カテゴリID
	 * @return 孫カテゴリ情報
	 */
	public List<Category> searchGrandChildByChildId(Integer childId) {
		List<Category> categoryList = null;
		if (childId == 0) {
			categoryList = categoryRepository.findGrandChild();
		} else {
			categoryList = categoryRepository.findGrandChildByChildId(childId);
		}
		return categoryList;
	}

}

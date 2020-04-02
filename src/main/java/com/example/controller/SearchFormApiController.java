package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Category;
import com.example.service.CategoryService;

/**
 * 検索フォームを動的に制御する.
 * 
 * @author shumpei
 *
 */
@RestController
@RequestMapping("/search_form_api")
public class SearchFormApiController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 親カテゴリに応じて子カテゴリ・孫カテゴリを絞り込む.
	 * 
	 * @param parentId 親カテゴリID
	 * @return 子カテゴリ一覧
	 */
	@RequestMapping(value = "/refine_category", method = RequestMethod.POST)
	public Map<String, List<Category>> refineChildAndGrandChildCategory(String parentId) {
		Map<String, List<Category>> categoryMap = new HashMap<>();
		List<Category> childCategoryList = categoryService.searchChildByParentId(Integer.parseInt(parentId));
		List<Category> grandChildCategoryList = categoryService.searchGrandChildByParentId(Integer.parseInt(parentId));
		categoryMap.put("childCategoryList", childCategoryList);
		categoryMap.put("grandChildCategoryList", grandChildCategoryList);
		return categoryMap;
	}

	/**
	 * 子カテゴリに応じて孫カテゴリを絞り込む.
	 * 
	 * @param childId 子カテゴリID
	 * @return 孫カテゴリ一覧
	 */
	@RequestMapping(value = "/refine_grand_child_category", method = RequestMethod.POST)
	public Map<String, List<Category>> refineGrandChildCategory(String childId) {
		Map<String, List<Category>> grandChildCategoryMap = new HashMap<>();
		List<Category> grandChildCategoryList = categoryService.searchGrandChildByChildId(Integer.parseInt(childId));
		grandChildCategoryMap.put("grandChildCategoryList", grandChildCategoryList);
		return grandChildCategoryMap;
	}

}

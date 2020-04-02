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
	private CategoryService CategoryService;

	/**
	 * 親カテゴリに応じて子カテゴリを絞り込む.
	 * 
	 * @param parentId 親カテゴリID
	 * @return 子カテゴリ一覧
	 */
	@RequestMapping(value = "/refine_child_category", method = RequestMethod.POST)
	public Map<String, List<Category>> refineChildCategory(String parentId) {
		Map<String, List<Category>> childCategoryMap = new HashMap<>();
		List<Category> childCategoryList = CategoryService.searchChildByParentId(Integer.parseInt(parentId));
		childCategoryMap.put("childCategoryList", childCategoryList);
		return childCategoryMap;
	}

}

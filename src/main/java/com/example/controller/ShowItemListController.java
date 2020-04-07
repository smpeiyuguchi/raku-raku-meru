package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.service.CategoryService;
import com.example.service.ItemService;

/**
 * 商品情報一覧を表示するためのコントローラ.
 * 
 * @author shumpei
 *
 */
@Controller
@RequestMapping("")
public class ShowItemListController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryService categoryService;

	/**
	 * 商品情報一覧を表示する.
	 * 
	 * @param model      リクエストスコープ
	 * @param pageNumber ページ番号
	 * @return 商品一覧画面
	 */
	@RequestMapping("/")
	public String showItemList(Model model, Integer parentId, Integer childId, Integer grandChildId, String name,
			String brand, Integer pageNumber) {
		setSearchForm(model, parentId, childId, grandChildId, name, brand);
		List<Item> itemList = null;

		if (name == null) {
			name = "";
		}
		if (brand == null) {
			brand = "";
		}

		if (pageNumber == null) {
			pageNumber = 1;
		}

		if (grandChildId != null && grandChildId != 0) {
			itemList = itemService.searchItemListByGrandChildIdAndSearchValue(grandChildId, name, brand, pageNumber);
		} else if (childId != null && childId != 0) {
			itemList = itemService.searchItemListByChildIdAndSearchValue(childId, name, brand, pageNumber);
		} else if (parentId != null && parentId != 0) {
			itemList = itemService.searchItemListByParentIdAndSearchValue(parentId, name, brand, pageNumber);
		} else {
			itemList = itemService.searchItemListBySearchValue(name, brand, pageNumber);
		}

		int totalPageNumber = itemService.countTotalPageNumber();

		model.addAttribute("itemList", itemList);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("pageNumber", pageNumber);
		return "list";
	}

	/**
	 * 検索用のフォームをセットする.
	 * 
	 * @param model リクエストスコープ
	 */
	public void setSearchForm(Model model, Integer parentId, Integer childId, Integer grandChildId, String name,
			String brand) {

		if (parentId != null && parentId != 0) {
			Category parentCategory = categoryService.SearchById(parentId);
			List<Category> childCategoryList = categoryService.searchChildByParentId(parentId);
			model.addAttribute("parentCategory", parentCategory);
			model.addAttribute("childCategoryList", childCategoryList);
		}

		if (childId != null && childId != 0) {
			Category childCategory = categoryService.SearchById(childId);
			List<Category> grandChildCategoryList = categoryService.searchGrandChildByChildId(childId);
			model.addAttribute("childCategory", childCategory);
			model.addAttribute("grandChildCategoryList", grandChildCategoryList);
		}

		if (grandChildId != null && grandChildId != 0) {
			Category grandChildCategory = categoryService.SearchById(grandChildId);
			model.addAttribute("grandChildCategory", grandChildCategory);
		}

		if (name != null) {
			model.addAttribute("searchName", name);
		}

		if (brand != null) {
			model.addAttribute("searchBrand", brand);
		}

		List<Category> parentCategoryList = categoryService.searchParent();
		model.addAttribute("parentCategoryList", parentCategoryList);
	};
}

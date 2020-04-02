package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.SearchValueForm;
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
	
	@ModelAttribute
	public SearchValueForm setUpSearchValueForm() {
		return new SearchValueForm();
	}

	/**
	 * 商品情報一覧を表示する.
	 * 
	 * @param model      リクエストスコープ
	 * @param pageNumber ページ番号
	 * @return 商品一覧画面
	 */
	@RequestMapping("/")
	public String showItemList(Model model, SearchValueForm form, Integer pageNumber) {
		setSearchForm(model);
		if (pageNumber == null) {
			pageNumber = 1;
		}

		List<Item> itemList = itemService.searchItemListByMultipleCondition(form.getParentId(), form.getChildId(),
				form.getGrandChildId(), form.getName(), form.getBrand(), pageNumber);
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
	public void setSearchForm(Model model) {
		List<Category> parentCategoryList = categoryService.searchParent();
		List<Category> childCategoryList = categoryService.searchChild();
		List<Category> grandChildCategoryList = categoryService.searchGrandChild();
		model.addAttribute("parentCategoryList", parentCategoryList);
		model.addAttribute("childCategoryList", childCategoryList);
		model.addAttribute("grandChildCategoryList", grandChildCategoryList);
	};
}

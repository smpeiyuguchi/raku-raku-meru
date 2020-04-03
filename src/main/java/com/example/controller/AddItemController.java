package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.AddItemForm;
import com.example.service.CategoryService;
import com.example.service.ItemService;

/**
 * 商品登録操作を行うコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("")
@Controller
public class AddItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public AddItemForm setUpAddItemForm() {
		return new AddItemForm();
	}

	/**
	 * 商品追加画面に遷移する.
	 * 
	 * @param model　リクエストスコープ
	 * @return 商品追加画面
	 */
	@RequestMapping("/to-add")
	public String toAdd(Model model) {
		List<Category> parentCategoryList = categoryService.searchParent();
		model.addAttribute("parentCategoryList", parentCategoryList);
		return "add";
	}

	/**
	 * 商品を追加する.
	 * 
	 * @param form フォーム
	 * @param result 商品一覧画面
	 * @return 商品一覧画面
	 */
	@RequestMapping("/add-item")
	public String addItem(Model model, @Validated AddItemForm form, BindingResult result) {
		if(form.getCategory() == 0) {
			result.rejectValue("category", "500", "error:may not be empty");
		}
		if (result.hasErrors()) {
			return toAdd(model);
		}
		Item item = new Item();
		item.setName(form.getName());
		item.setPrice((int)Double.parseDouble(form.getPrice()));
		item.setBrand(form.getBrand());
		item.setCategory(form.getCategory());
		item.setCondition(form.getCondition());
		item.setDescription(form.getDescription());
		item.setShipping(0);
		itemService.addItem(item);
		return "redirect:/";

	}

}

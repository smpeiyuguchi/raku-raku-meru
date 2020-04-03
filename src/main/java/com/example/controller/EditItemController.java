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
import com.example.form.UpdateItemForm;
import com.example.service.CategoryService;
import com.example.service.ItemService;

/**
 * 商品情報を編集するコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("")
@Controller
public class EditItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public UpdateItemForm setUpUpdateItemForm(Integer itemId) {
		UpdateItemForm updateItemForm = new UpdateItemForm();
		Item item = itemService.searchByItemId(itemId);
		updateItemForm.setName(item.getName());
	    updateItemForm.setPrice(String.valueOf(item.getPrice()));
	    updateItemForm.setCategory(item.getCategory());
	    updateItemForm.setBrand(item.getBrand());
	    updateItemForm.setCondition(item.getCondition());
	    updateItemForm.setDescription(item.getDescription());
		return updateItemForm;
	}

	/**
	 * 商品編集ページを表示する.
	 * 
	 * @param model リクエストスコープ
	 * @param itemId 商品ID
	 * @return 商品編集ページ
	 */
	@RequestMapping("/to-edit")
	public String toEdit(Model model, Integer itemId) {
		Item item = itemService.searchByItemId(itemId);
		List<Category> parentCategoryList = categoryService.searchParent();
		model.addAttribute("parentCategoryList", parentCategoryList);
		model.addAttribute("item", item);
		return "edit";
	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param model リクエストスコープ
	 * @param form フォーム
	 * @param result　エラー
	 * @return　商品詳細画面
	 */
	@RequestMapping("/update-item")
	public String updateItem(Model model, @Validated UpdateItemForm form, BindingResult result) {
		if (form.getCategory() == 0) {
			result.rejectValue("category", "500", "error:may not be empty");
		}
		if (result.hasErrors()) {
			return toEdit(model, form.getId());
		}
		Item item = new Item();
		item.setId(form.getId());
		item.setName(form.getName());
		item.setPrice((int)Double.parseDouble(form.getPrice()));
		item.setBrand(form.getBrand());
		item.setCategory(form.getCategory());
		item.setCondition(form.getCondition());
		item.setDescription(form.getDescription());
		itemService.updateItem(item);
		return "redirect:/show-item-detail?itemId=" + form.getId();
	}

}

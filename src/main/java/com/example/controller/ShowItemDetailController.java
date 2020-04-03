package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ItemService;

/**
 * 商品詳細画面を表示するコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("")
@Controller
public class ShowItemDetailController {

	@Autowired
	private ItemService itemService;

	/**
	 * 商品詳細ページを表示する.
	 * 
	 * @param model リクエストスコープ
	 * @param itemId 商品ID
	 * @return 商品詳細ページ
	 */
	@RequestMapping("/show-item-detail")
	public String showItemDetail(Model model, Integer itemId) {
		Item item = itemService.searchByItemId(itemId);
		model.addAttribute("item", item);
		return "detail";
	}

}

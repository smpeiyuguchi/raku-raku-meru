package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.ItemService;

/**
 * 商品の削除を操作するコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("/admin")
@Controller
public class DeleteItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * IDに紐づく商品を削除する.
	 * 
	 * @param id ID
	 * @return 商品一覧画面
	 */
	@RequestMapping("/delete")
	public String deleteItem(Integer id) {
		itemService.deleteItem(id);
		return "redirect:/";
	}

}

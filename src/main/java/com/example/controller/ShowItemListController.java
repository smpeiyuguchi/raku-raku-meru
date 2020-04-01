package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
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

	/**
	 * 商品情報一覧を表示する.
	 * 
	 * @param model      リクエストスコープ
	 * @param pageNumber ページ番号
	 * @return 商品一覧画面
	 */
	@RequestMapping("/")
	public String showItemList(Model model, Integer largeCategory, Integer middleCategory, Integer smallCategory,
			String name, String brand, Integer pageNumber) {
		if (pageNumber == null) {
			pageNumber = 1;
		}
		// 仮パラメータ
		largeCategory = 1;
		middleCategory = 11;
		smallCategory = 150;
		name = "PINK";
//		brand = "V";

		List<Item> itemList = itemService.searchItemListByMultipleCondition(largeCategory, middleCategory,
				smallCategory, name, brand, pageNumber);
		int totalPageNumber = itemService.countTotalPageNumber();

		model.addAttribute("itemList", itemList);
		model.addAttribute("totalPageNumber", totalPageNumber);
		model.addAttribute("pageNumber", pageNumber);
		return "list";
	}
}

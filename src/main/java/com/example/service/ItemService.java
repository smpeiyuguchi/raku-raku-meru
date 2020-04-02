package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

/**
 * 商品情報の処理制御を行うサービスクラス.
 * 
 * @author shumpei
 *
 */
@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * ページ番号に該当する商品情報一覧を取得する.
	 * 
	 * @param pageNum ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> searchItemList(int pageNumber) {
		List<Item> itemList = itemRepository.findByPageNumber(pageNumber);
		return itemList;
	}

	/**
	 * 複数の検索条件から商品情報一覧を取得する
	 * 
	 * @param parentId     親カテゴリーID
	 * @param childId      子カテゴリーID
	 * @param grandChildId 孫カテゴリーID
	 * @param name         名前
	 * @param brand        ブランド
	 * @param pageNumber   ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> searchItemListByMultipleCondition(Integer parentId, Integer childId, Integer grandChildId,
			String name, String brand, int pageNumber) {
		List<Item> itemList = null;
		if (name == null) {
			name = "";
		}
		if (brand == null) {
			brand = "";
		}
		// カテゴリーの検索値の有無によって条件分岐
		if (grandChildId != 0) {
			System.out.println("孫カテゴリで検索");
			itemList = itemRepository.findByGrandChildAndSearchValue(grandChildId, name, brand, pageNumber);
		} else if (childId != 0) {
			System.out.println("子カテゴリで検索");
			itemList = itemRepository.findByChildAndSearchValue(childId, name, brand, pageNumber);
		} else if (parentId != 0) {
			System.out.println("親カテゴリで検索");
			itemList = itemRepository.findByParentAndSearchValue(parentId, name, brand, pageNumber);
		} else {
			System.out.println("名前・ブランド・検索なしで検索");
			itemList = itemRepository.findByNameAndBrand(name, brand, pageNumber);
		}
		return itemList;
	}

	/**
	 * 総ページ数を計算する.
	 * 
	 * @return 総ページ数
	 */
	public Integer countTotalPageNumber() {
		int itemNumberPerPage = 30;
		int totalPageNumber = 0;
		int totalItemNumber = itemRepository.countTotalItemNumber();
		if (totalItemNumber % 30 == 0) {
			totalPageNumber = totalItemNumber / itemNumberPerPage;
		} else {
			totalPageNumber = totalItemNumber / itemNumberPerPage + 1;
		}
		return totalPageNumber;
	}

}

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
	 * 孫カテゴリIDから商品一覧を検索する.
	 * 
	 * @param grandChildId 孫カテゴリID
	 * @param name         名前
	 * @param brand        ブランド
	 * @param pageNumber   ページ番号
	 * @return 商品一覧情報
	 */
	public List<Item> searchItemListByGrandChildIdAndSearchValue(Integer grandChildId, String name, String brand,
			int pageNumber) {
		System.out.println("孫カテゴリで検索");
		return itemRepository.findByGrandChildAndSearchValue(grandChildId, name, brand, pageNumber);
	}

	/**
	 * 子カテゴリIDから商品一覧を検索する.
	 * 
	 * @param childId    孫カテゴリID
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品一覧情報
	 */
	public List<Item> searchItemListByChildIdAndSearchValue(Integer childId, String name, String brand,
			int pageNumber) {
		System.out.println("子カテゴリで検索");
		return itemRepository.findByChildAndSearchValue(childId, name, brand, pageNumber);
	}

	/**
	 * 親カテゴリIDから商品一覧を検索する.
	 * 
	 * @param parentId   孫カテゴリID
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品一覧情報
	 */
	public List<Item> searchItemListByParentIdAndSearchValue(Integer parentId, String name, String brand,
			int pageNumber) {
		System.out.println("親カテゴリで検索");
		return itemRepository.findByParentAndSearchValue(parentId, name, brand, pageNumber);
	}

	/**
	 * 検索値から商品一覧を検索する.
	 * 
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品一覧情報
	 */
	public List<Item> searchItemListBySearchValue(String name, String brand, int pageNumber) {
		System.out.println("名前・ブランド・検索なしで検索");
		return itemRepository.findByNameAndBrand(name, brand, pageNumber);
	}

	/**
	 * 商品IDから商品情報を検索する.
	 * 
	 * @param itemId 商品ID
	 * @return 商品情報
	 */
	public Item searchByItemId(Integer itemId) {
		return itemRepository.findByitemId(itemId);
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

	/**
	 * 商品情報を登録する.
	 * 
	 * @param item 商品情報
	 */
	public void addItem(Item item) {
		itemRepository.insert(item);
	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param item 商品情報
	 */
	public void updateItem(Item item) {
		itemRepository.update(item);
	}

	/**
	 * 商品を削除する.
	 * 
	 * @param id ID
	 */
	public void deleteItem(Integer id) {
		itemRepository.delete(id);
	}

}

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
	 * 複数の検索条件から商品情報一覧を取得する（ブランド指定なし）
	 * 
	 * @param largeCategory  大カテゴリーID
	 * @param middleCategory 中カテゴリーID
	 * @param smallCategory  小カテゴリーID
	 * @param name           名前
	 * @param pageNumber     ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> searchItemListByMultipleCondition(int largeCategory, int middleCategory, int smallCategory,
			String name, int pageNumber) {
		return itemRepository.findBySearchCondition(largeCategory, middleCategory, smallCategory, name, pageNumber);
	}

	/**
	 * 複数の検索条件から商品情報一覧を取得する（ブランド指定あり）
	 * 
	 * @param largeCategory  大カテゴリーID
	 * @param middleCategory 中カテゴリーID
	 * @param smallCategory  小カテゴリーID
	 * @param name           名前
	 * @param brand          ブランド
	 * @param pageNumber     ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> searchItemListByMultipleCondition(int largeCategory, int middleCategory, int smallCategory,
			String name, String brand, int pageNumber) {
		return itemRepository.findBySearchCondition(largeCategory, middleCategory, smallCategory, name, brand,
				pageNumber);
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

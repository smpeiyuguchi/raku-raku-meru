package com.example.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * itemsテーブルを操作するためのリポジトリクラス.
 * 
 * @author shumpei
 *
 */
@Repository
public class ItemRepository {

	private final static RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("i_id"));
		item.setName(rs.getString("i_name"));
		item.setCondition(rs.getInt("i_condition"));
		item.setCategory(rs.getInt("i_category"));
		item.setBrand(rs.getString("i_brand"));
		item.setPrice(rs.getDouble("i_price"));
		item.setShipping(rs.getInt("i_shipping"));
		item.setDescription(rs.getString("i_description"));
		List<String> categoryNameList = Arrays.asList(rs.getString("c_name_all").split("/"));
		item.setCategoryNameList(categoryNameList);
		return item;
	};

	private final static RowMapper<Item> ITEM_CATEGORY_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("商品ID"));
		item.setName(rs.getString("商品名"));
		item.setCondition(rs.getInt("状況"));
		item.setCategory(rs.getInt("商品カテゴリー"));
		item.setBrand(rs.getString("ブランド"));
		item.setPrice(rs.getDouble("価格"));
		item.setShipping(rs.getInt("配送状況"));
		item.setDescription(rs.getString("説明"));
		String largeCategory = rs.getString("大カテゴリ名");
		String middleCategory = rs.getString("中カテゴリ名");
		String smallCategory = rs.getString("小カテゴリ名");
		List<String> categoryNameList = Arrays.asList(largeCategory, middleCategory, smallCategory);
		item.setCategoryNameList(categoryNameList);
		return item;
	};

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * ページ番号から商品情報を20件取得する.
	 * 
	 * @param pageNumber ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findByPageNumber(int pageNumber) {
		// 1回あたりの取得行数を指定
		int limitNumber = 30;
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * limitNumber;
		String sql = "SELECT i.id i_id, i.name i_name, i.condition i_condition, i.category i_category, "
				+ "i.brand i_brand, i.price i_price, i.shipping i_shipping, i.description i_description, c.name_all c_name_all "
				+ "FROM items i LEFT OUTER JOIN category c ON i.category = c.id ORDER BY i.id "
				+ "LIMIT :limitNumber OFFSET :offsetNumber";
		SqlParameterSource param = new MapSqlParameterSource().addValue("limitNumber", limitNumber)
				.addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 商品の合計数をカウントする.
	 * 
	 * @return 商品の合計数
	 */
	public Integer countTotalItemNumber() {
		String sql = "SELECT COUNT(*) FROM items";
		int totalItemNumber = jdbc.queryForObject(sql, Integer.class);
		return totalItemNumber;
	}

	/**
	 * 複数の検索条件から商品情報一覧を取得する(ブランド名なし）
	 * 
	 * @param largeCategory  大カテゴリーID
	 * @param middleCategory 中カテゴリーID
	 * @param smallCategory  小カテゴリーID
	 * @param name           名前
	 * @param brand          ブランド
	 * @param pageNumber     ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findBySearchCondition(int largeCategory, int middleCategory, int smallCategory, String name,
			int pageNumber) {
		// 1回あたりの取得行数を指定
		int limitNumber = 30;
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * limitNumber;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category ");
		// 条件の有無によって検索を制御する
		sql.append("WHERE c1.id = CASE WHEN :largeCategory IS NOT NULL THEN :largeCategory ELSE c1.id END ");
		sql.append("AND c2.id = CASE WHEN :middleCategory IS NOT NULL THEN :middleCategory ELSE c2.id END ");
		sql.append("AND c3.id = CASE WHEN :smallCategory IS NOT NULL THEN :smallCategory ELSE c3.id END ");
		sql.append("AND i.name LIKE :name ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory)
				.addValue("middleCategory", middleCategory).addValue("smallCategory", smallCategory)
				.addValue("name", "%" + name + "%")
				.addValue("limitNumber", limitNumber).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 複数の検索条件から商品情報一覧を取得する(ブランド指定あり）
	 * 
	 * @param largeCategory  大カテゴリーID
	 * @param middleCategory 中カテゴリーID
	 * @param smallCategory  小カテゴリーID
	 * @param name           名前
	 * @param brand          ブランド
	 * @param pageNumber     ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findBySearchCondition(int largeCategory, int middleCategory, int smallCategory, String name,
			String brand, int pageNumber) {
		// 1回あたりの取得行数を指定
		int limitNumber = 30;
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * limitNumber;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category ");
		// 条件の有無によって検索を制御する
		sql.append("WHERE c1.id = CASE WHEN :largeCategory IS NOT NULL THEN :largeCategory ELSE c1.id END ");
		sql.append("AND c2.id = CASE WHEN :middleCategory IS NOT NULL THEN :middleCategory ELSE c2.id END ");
		sql.append("AND c3.id = CASE WHEN :smallCategory IS NOT NULL THEN :smallCategory ELSE c3.id END ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND i.brand LIKE :brand "); //ブランド指定
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory)
				.addValue("middleCategory", middleCategory).addValue("smallCategory", smallCategory)
				.addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
				.addValue("limitNumber", limitNumber).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

}

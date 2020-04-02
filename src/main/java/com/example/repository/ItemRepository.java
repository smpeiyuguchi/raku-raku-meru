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

	/** １ページあたりの商品件数 */
	private final static int ITEM_NUMBER_PER_PAGE = 30;

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
	 * IDから商品情報を検索する.
	 * 
	 * @param id ID
	 * @return 商品情報
	 */
	public Item load(Integer id) {
		String sql = "SELECT i.id i_id, i.name i_name, i.condition i_condition, i.category i_category, i.brand i_brand, "
				+ "i.price i_price, i.shipping i_shipping, c.name_all c_name_all FROM items i "
				+ "LEFT OUTER JOIN category c ON i.category = c.id WHERE i.id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		return template.queryForObject(sql, param, ITEM_CATEGORY_ROW_MAPPER);
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
	 * 名前とブランドから商品情報を取得する.
	 * 
	 * @param name 名前
	 * @param brand　ブランド
	 * @param pageNumber ページ番号
	 * @return　商品情報一覧
	 */
	public List<Item> findByNameAndBrand(String name, String brand, Integer pageNumber) {
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * ITEM_NUMBER_PER_PAGE;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '%%' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
				.addValue("limitNumber", ITEM_NUMBER_PER_PAGE).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 親カテゴリと検索値から商品情報を取得する.
	 * 
	 * @param parentId   親カテゴリID
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findByParentAndSearchValue(Integer parentId, String name, String brand, Integer pageNumber) {
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * ITEM_NUMBER_PER_PAGE;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c1.id = :parentId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '%%' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId)
				.addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
				.addValue("limitNumber", ITEM_NUMBER_PER_PAGE).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 子カテゴリと検索値から商品情報を取得する.
	 * 
	 * @param parentId   親カテゴリID
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findByChildAndSearchValue(Integer childId, String name, String brand, Integer pageNumber) {
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * ITEM_NUMBER_PER_PAGE;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c2.id = :childId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '% %' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("childId", childId)
				.addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
				.addValue("limitNumber", ITEM_NUMBER_PER_PAGE).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 孫カテゴリと検索値から商品情報を取得する.
	 * 
	 * @param grandChildId 親カテゴリID
	 * @param name         名前
	 * @param brand        ブランド
	 * @param pageNumber   ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findByGrandChildAndSearchValue(Integer grandChildId, String name, String brand,
			Integer pageNumber) {
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * ITEM_NUMBER_PER_PAGE;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 大カテゴリID, c1.name 大カテゴリ名, C2.id AS 中カテゴリID, c2.name AS 中カテゴリ名, "
				+ "c3.id 小カテゴリID, c3.name 小カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c3.id = :grandChildId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '% %' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("grandChildId", grandChildId)
				.addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
				.addValue("limitNumber", ITEM_NUMBER_PER_PAGE).addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

}

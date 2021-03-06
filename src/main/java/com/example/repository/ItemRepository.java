package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Category;
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

	// Itemに親カテゴリ・中カテゴリ・子カテゴリをセットした状態で取得するように修正
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
		Category parent = new Category();
		parent.setId(rs.getInt("親カテゴリID"));
		parent.setName(rs.getString("親カテゴリ名"));
		Category child = new Category();
		child.setId(rs.getInt("子カテゴリID"));
		child.setName(rs.getString("子カテゴリ名"));
		Category grandChild = new Category();
		grandChild.setId(rs.getInt("孫カテゴリID"));
		grandChild.setName(rs.getString("孫カテゴリ名"));
		item.setParent(parent);
		item.setChild(child);
		item.setGrandChild(grandChild);
		return item;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * IDから商品情報を検索する.
	 * 
	 * @param id ID
	 * @return 商品情報
	 */
	public Item findByitemId(Integer itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, C2.id AS 子カテゴリID, c2.name AS 子カテゴリ名, "
				+ "c3.id 孫カテゴリID, c3.name 孫カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("i.id = :itemId");
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		return template.queryForObject(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
	}

	/**
	 * 名前とブランドから商品情報を取得する.
	 * 
	 * @param name       名前
	 * @param brand      ブランド
	 * @param pageNumber ページ番号
	 * @return 商品情報一覧
	 */
	public List<Item> findBySearchValue(String name, String brand, Integer pageNumber) {
		// 取得する行までに除外する行数を指定
		int offsetNumber = (pageNumber - 1) * ITEM_NUMBER_PER_PAGE;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, C2.id AS 子カテゴリID, c2.name AS 子カテゴリ名, "
				+ "c3.id 孫カテゴリID, c3.name 孫カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
				+ "i.category AS 商品カテゴリー, i.brand AS ブランド, i.price AS 価格, i.shipping AS 配送状況, i.description AS 説明 ");
		sql.append("FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("i.name LIKE :name ");
		sql.append(
				"AND CASE WHEN :brand = '%%' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		sql.append("ORDER BY c1.id LIMIT :limitNumber OFFSET :offsetNumber");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%").addValue("limitNumber", ITEM_NUMBER_PER_PAGE)
				.addValue("offsetNumber", offsetNumber);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 名前とブランド名で検索した時の合計件数を取得する.
	 * 
	 * @param name 名前
	 * @param brand ブランド
	 * @return 合計件数
	 */
	public Integer countTotalNumberfindBySearchValue(String name, String brand) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(i.id) FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '%%' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%");
		return template.queryForObject(sql.toString(), param, Integer.class);
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
		sql.append("SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, C2.id AS 子カテゴリID, c2.name AS 子カテゴリ名, "
				+ "c3.id 孫カテゴリID, c3.name 孫カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
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
	 * 親カテゴリと検索値で検索した時の合計件数を取得する.
	 * 
	 * @param parentId 親カテゴリID
	 * @param name 名前
	 * @param brand ブランド
	 * @return 合計件数
	 */
	public Integer countTotalNumberFindByParentAndSearchValue(Integer parentId, String name, String brand) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(i.id) FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c1.id = :parentId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '%%' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%").addValue("parentId", parentId);
		return template.queryForObject(sql.toString(), param, Integer.class);
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
		sql.append("SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, C2.id AS 子カテゴリID, c2.name AS 子カテゴリ名, "
				+ "c3.id 孫カテゴリID, c3.name 孫カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
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
	 * 子カテゴリと検索値で検索した時の合計件数を取得する.
	 * @param childId 子カテゴリID
	 * @param name 名前
	 * @param brand ブランド
	 * @return 合計件数
	 */
	public Integer countTotalNumberFindByChildAndSearchValue(Integer childId, String name, String brand) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(i.id) FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c2.id = :childId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '% %' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%").addValue("childId", childId);
		return template.queryForObject(sql.toString(), param, Integer.class);
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
		sql.append("SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, C2.id AS 子カテゴリID, c2.name AS 子カテゴリ名, "
				+ "c3.id 孫カテゴリID, c3.name 孫カテゴリ名, i.id AS 商品ID, i.name AS 商品名, i.condition AS 状況, "
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
	
	/**
	 * 孫カテゴリと検索値で検索した時の合計件数を取得する.
	 * 
	 * @param grandChildId 孫カテゴリID
	 * @param name 名前
	 * @param brand ブランド
	 * @return 合計件数
	 */
	public Integer countTotalNumberFindByGrandChildAndSearchValue(Integer grandChildId, String name, String brand) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(i.id) FROM category c1 INNER JOIN category c2 ON c1.id = c2.parent INNER JOIN category "
				+ "c3 ON c2.id = c3.parent RIGHT OUTER JOIN items i ON c3.id = i.category WHERE ");
		sql.append("c3.id = :grandChildId ");
		sql.append("AND i.name LIKE :name ");
		sql.append("AND CASE WHEN :brand = '% %' THEN (i.brand LIKE :brand OR i.brand IS NULL) ELSE i.brand LIKE :brand END ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%").addValue("grandChildId", grandChildId);
		return template.queryForObject(sql.toString(), param, Integer.class);
	}

	/**
	 * 商品情報を追加する.
	 * 
	 * @param item 商品情報
	 */
	public void insert(Item item) {
		String sql = "INSERT INTO items (name, condition, category, brand, price, shipping, description) "
				+ "VALUES (:name, :condition, :category, :brand, :price, :shipping, :description) ";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		template.update(sql, param);
	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param item 商品情報
	 */
	public void update(Item item) {
		String sql = "UPDATE items SET id = :id, name = :name, condition = :condition, category = :category, "
				+ "brand = :brand, price = :price, shipping = :shipping, description = :description "
				+ "WHERE id = :id";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		System.out.println(param);
		template.update(sql, param);
	}
	
	/**
	 * 商品を削除する.
	 * 
	 * @param id ID
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM items WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}

}

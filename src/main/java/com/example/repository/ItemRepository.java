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

}

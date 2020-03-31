package com.example.repository;

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
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setCondition(rs.getInt("condition"));
		item.setCategory(rs.getInt("category"));
		item.setBrand(rs.getString("brand"));
		item.setPrice(rs.getDouble("price"));
		item.setShipping(rs.getInt("shipping"));
		item.setDescription(rs.getString("description"));
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
		String sql = "SELECT id, name, condition, category, brand, price, shipping, description "
				+ "FROM items ORDER BY id LIMIT :limitNumber OFFSET :offsetNumber ";
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

package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Category;

/**
 * カテゴリーテーブルを操作するためのリポジトリクラス.
 * 
 * @author shumpei
 *
 */
@Repository
public class CategoryRepository {

	private final static RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setParent(rs.getInt("parent"));
		category.setName(rs.getString("name"));
		return category;
	};

	private final static RowMapper<Category> GRAND_CHILD_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("孫カテゴリID"));
		category.setName(rs.getString("孫カテゴリ名"));
		return category;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * IDに紐づくカテゴリ情報を取得する.
	 * 
	 * @param id ID
	 * @return カテゴリ情報
	 */
	public Category load(Integer id) {
		String sql = "SELECT id, parent, name FROM category WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		return template.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
	}
	
	
	
	/**
	 * 親カテゴリ情報を全て取得する.
	 * 
	 * @return 親カテゴリ情報
	 */
	public List<Category> findParent() {
		String sql = "SELECT id, parent, name FROM category WHERE parent IS NULL AND name_all IS NULL ORDER BY name";
		return template.query(sql, CATEGORY_ROW_MAPPER);
	}

	/**
	 * 子カテゴリ情報を全て取得する.
	 * 
	 * @return 子カテゴリ情報
	 */
	public List<Category> findChild() {
		String sql = "SELECT id, parent, name FROM category WHERE parent IS NOT NULL AND name_all IS NULL ORDER BY name";
		return template.query(sql, CATEGORY_ROW_MAPPER);
	}

	/**
	 * 親カテゴリIDから子カテゴリ情報を全て取得する.
	 * 
	 * @param parentId 親カテゴリID
	 * @return 子カテゴリ情報
	 */
	public List<Category> findChildByParentId(Integer parentId) {
		String sql = "SELECT id, parent, name FROM category WHERE parent = :parentId AND name_all IS NULL ORDER BY name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId);
		return template.query(sql, param, CATEGORY_ROW_MAPPER);
	}

	/**
	 * 孫カテゴリ情報を全て取得する.
	 * 
	 * @return 孫カテゴリ情報
	 */
	public List<Category> findGrandChild() {
		String sql = "SELECT id, parent, name FROM category WHERE name_all IS NOT NULL ORDER BY name";
		return template.query(sql, CATEGORY_ROW_MAPPER);
	}

	/**
	 * 親カテゴリIDから孫カテゴリ情報を取得する.
	 * 
	 * @param parentId 親カテゴリID
	 * @return 孫カテゴリ情報
	 */
	public List<Category> findGrandChildByParentId(Integer parentId) {
		String sql = "SELECT c1.id 親カテゴリID, c1.name 親カテゴリ名, c2.id 孫カテゴリID, c2.name 孫カテゴリ名 "
				+ "FROM category c1 INNER JOIN category c2 ON c1.name = split_part(c2.name_all, '/', 1) "
				+ "WHERE c1.id = :parentId ORDER BY c2.name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId);
		return template.query(sql, param, GRAND_CHILD_ROW_MAPPER);
	}

	/**
	 * 子カテゴリIDから孫カテゴリ情報を全て取得する.
	 * 
	 * @param childId 子カテゴリID
	 * @return 孫カテゴリ情報
	 */
	public List<Category> findGrandChildByChildId(Integer childId) {
		String sql = "SELECT id, parent, name FROM category WHERE parent = :childId ORDER BY name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("childId", childId);
		System.out.println(template.query(sql, param, CATEGORY_ROW_MAPPER));
		return template.query(sql, param, CATEGORY_ROW_MAPPER);
	}

}

package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Category;
import com.example.repository.CategoryRepository;

/**
 * カテゴリー情報の処理制御を行うサービスクラス.
 * 
 * @author shumpei
 *
 */
@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * 親カテゴリ情報を全件取得する.
	 * 
	 * @return　親カテゴリ情報
	 */
	public List<Category> searchParent(){
		return categoryRepository.findParent();
	}
	
	/**
	 * 子カテゴリ情報を全件取得する.
	 * 
	 * @return　子カテゴリ情報
	 */
	public List<Category> searchChild(){
		return categoryRepository.findChild();
	}
	
	/**
	 * 孫カテゴリ情報を全件取得する.
	 * 
	 * @return　孫カテゴリ情報
	 */
	public List<Category> searchGrandChild(){
		return categoryRepository.findGrandChild();
	}
	
	

}

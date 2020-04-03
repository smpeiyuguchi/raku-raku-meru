package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * ログインユーザーの情報を格納するエンティティ.
 * 
 * @author shumpei
 *
 */
public class LoginUser extends org.springframework.security.core.userdetails.User{
	
	private static final long serialVersionUID = 1L;
	private final User user;

	/**
	 * ログイン時に利用者に権限を付与する.
	 * 
	 * @param user          ユーザー情報
	 * @param authorityList 権限リスト
	 */
	public LoginUser(User user, Collection<GrantedAuthority> authorityList) {
		// SpringFramworkのユーザークラスのコンストラクタ
		super(user.getEmail(), user.getPassword(), authorityList);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	

}

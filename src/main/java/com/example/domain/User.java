package com.example.domain;

/**
 * ユーザー情報を格納するドメインクラス.
 * 
 * @author shumpei
 *
 */
public class User {
	
	/** ID */
	private Integer id;
	/** 名前 */
	private String name;
	/** Eメール */
	private String email;
	/** パスワード */
	private String password;
	/** 権限 */
	private Integer authority;

	public User() {
	}

	public User(Integer id, String name, String email, String password, Integer authority) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.authority = authority;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAuthority() {
		return authority;
	}

	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", authority="
				+ authority + "]";
	}

}

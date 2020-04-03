package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登録ユーザー情報を受け取るフォームクラス.
 * 
 * @author shumpei
 *
 */
public class RegisterUserForm {

	/** 名前 */
	@NotBlank(message = "error:may not be empty")
	private String name;

	/** Eメール */
	@NotBlank(message = "error:may not be empty")
	@Email(message = "error:may not be incorrect format")
	private String email;

	/** パスワード */
	@NotBlank(message = "error:may not be empty")
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", 
		message = "error:include at least 8 characters, numbers, uppercase letters, lowercase letters, and symbols")
	private String password;

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

	@Override
	public String toString() {
		return "RegisterUserForm [name=" + name + ", email=" + email + ", password=" + password + "]";
	}

}

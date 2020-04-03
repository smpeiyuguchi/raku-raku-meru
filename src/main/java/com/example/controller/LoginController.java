package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログイン操作を行うコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("")
@Controller
public class LoginController {
	
	/**
	 * ログインページを表示する.
	 * 
	 * @param model リクエストスコープ
	 * @param error　エラー
	 * @return ログイン画面
	 */
	@RequestMapping("/tologin")
	public String toLogin(Model model, @RequestParam(required = false) String error) {
		if(error != null) {
			model.addAttribute("errorMessage", "error:failed to login");
		}
		return "login";
	}

}

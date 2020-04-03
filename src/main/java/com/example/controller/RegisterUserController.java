package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.RegisterUserForm;
import com.example.service.UserService;

/**
 * ユーザー情報を登録するコントローラ.
 * 
 * @author shumpei
 *
 */
@RequestMapping("")
@Controller
public class RegisterUserController {

	@Autowired
	private UserService userService;

	@ModelAttribute
	public RegisterUserForm setUpRegisterUserForm() {
		return new RegisterUserForm();
	}

	@RequestMapping("/to-register")
	public String toRegister() {
		return "register";
	}

	@RequestMapping("/register-user")
	public String registerUser(@Validated RegisterUserForm form, BindingResult result) {
		if (userService.findByEmail(form.getEmail()) != null) {
			result.rejectValue("email", "500", "error:mail address is already registered");
		}
		if (result.hasErrors()) {
			return "redirect:/to-register";
		}
		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		userService.registerUser(user);
		return "redirect:/";
		
	}

}

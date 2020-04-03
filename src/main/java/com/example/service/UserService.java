package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ユーザーの処理制御を行うサービスクラス.
 * 
 * @author shumpei
 *
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * メールアドレスからユーザー情報を検索する.
	 * 
	 * @param email メールアドレス
	 * @return ユーザー情報
	 */
	public User findByEmail(String email) {
		List<User> userList = userRepository.findAllByEmail(email);
		if (userList.size() == 0) {
			return null;
		} else {
			return userList.get(0);
		}
	}

	/**
	 * ユーザー情報を登録する.
	 * 
	 * @param user ユーザー
	 */
	public void registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.insert(user);
	}

}

package com.broadridge.stocks.service;

import com.broadridge.stocks.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}

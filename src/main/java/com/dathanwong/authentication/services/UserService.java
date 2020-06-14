package com.dathanwong.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.dathanwong.authentication.models.User;
import com.dathanwong.authentication.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository repo;
	
	public UserService(UserRepository repo) {
		this.repo = repo;
	}
	
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return repo.save(user);
	}
	
	public User findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = repo.findById(id);
		if(user.isPresent()) {
			return user.get();
		}else {
			return null;
		}
	}
	
	public boolean authenticateUser(String email, String password) {
		User user = repo.findByEmail(email);
		if(user == null) {
			return false;
		}else {
			if(BCrypt.checkpw(password, user.getPassword())) {
				return true;
			}else {
				return false;
			}
		}
	}
}

package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return (List<User>) userRepo.findAll();
	}
	
	public List<Role> listRoles(){
		return (List<Role>)roleRepo.findAll();
	}

	public void save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if( isUpdatingUser ) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			// 기존의 패스워드 유지
			if( user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);			
		}
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}
	
	// if find null ! new user
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		boolean isCreatingNew = (id == null);
		
		if(userByEmail == null) return true;
		
		if( isCreatingNew ) {
			if(userByEmail != null ) return false;
		} else {
			if( userByEmail.getId() != id){
				return false;
			}
		}
		 
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();			
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Cloud not find any user with Id" + id);
		}
	}
}

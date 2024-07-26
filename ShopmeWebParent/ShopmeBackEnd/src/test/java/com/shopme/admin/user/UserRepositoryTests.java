package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userHeo = new User("test@test.com", "pass", "HEO", "KM");
		userHeo.addRole(roleAdmin);
		
		User saveUser = repo.save(userHeo);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		User userHeo2 = new User("test2@test.com", "pass", "HEO2", "KM");
		userHeo2.addRole(roleEditor);
		userHeo2.addRole(roleAssistant);
		
		User saveUser = repo.save(userHeo2);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userHeo = repo.findById(1).get();
		System.out.println(userHeo);
		assertThat(userHeo).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userHeo = repo.findById(1).get();
		userHeo.setEnabled(true);
		userHeo.setEmail("test321@test321.com");
		repo.save(userHeo);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userHeo2 = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userHeo2.getRoles().remove(roleEditor);
		userHeo2.addRole(roleSalesperson);
		
		repo.save(userHeo2);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "test@test.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
}

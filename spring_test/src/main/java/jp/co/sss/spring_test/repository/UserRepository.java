package jp.co.sss.spring_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring_test.entity.User;

public interface UserRepository  extends JpaRepository<User, Integer>{

	User findByEmailAndPasswords(String email, String passwords);
}

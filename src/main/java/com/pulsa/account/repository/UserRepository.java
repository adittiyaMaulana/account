package com.pulsa.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pulsa.account.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query(
			value="SELECT * FROM user where user_name=:userName"
					+ " and password=:password",
			nativeQuery=true
	)
	Optional<User> findByUserNameAndPassword(
			@Param("userName")String userName, 
			@Param("password") String hashPassword);
}

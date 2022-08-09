package com.product.manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.product.manager.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	
//	Optional<User> findById(Long id);

	List<User> findByRoleName(String roleName);

	@Query("select u from User u join u.role " +
			"where lower(u.firstName) like lower(concat('%', :query, '%')) " +
			"or lower(u.lastName) like lower(concat('%', :query, '%')) " +
			"or lower(concat(u.firstName, ' ', u.lastName)) like concat('%', :query, '%')"
	)
	Page<User> searchUsers(@Param("query") String query, Pageable page);
}

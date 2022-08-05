package com.product.manager.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.manager.entity.Role;
import com.product.manager.entity.User;
import com.product.manager.repository.UserRepository;

@Service("ProductManagerUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOp = userRepository.findByEmail(email);
		if (userOp.isPresent()) {
			User user = userOp.get();
			return new org.springframework.security.core.userdetails.User(email, user.getPassword(), getUserAuthorities(user.getRole()));
		} else {
			throw new UsernameNotFoundException("Not found user has email: " + email);
		}
	}

	private List<GrantedAuthority> getUserAuthorities(Role role) {
		return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
	}
}

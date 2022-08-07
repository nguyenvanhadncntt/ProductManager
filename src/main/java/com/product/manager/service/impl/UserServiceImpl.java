package com.product.manager.service.impl;

import com.product.manager.convert.impl.UserConvert;
import com.product.manager.dto.UserDTO;
import com.product.manager.entity.User;
import com.product.manager.repository.UserRepository;
import com.product.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvert userConvert;

    @Override
    public Page<UserDTO> searchUser(String query, Pageable page) {
        Page<User> userPage = userRepository.searchUsers(query, page);
        return userPage.map(user -> userConvert.convertEntityToDTO(user));
    }
}

package com.product.manager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.product.manager.dto.UserDTO;

public interface UserService {
    Page<UserDTO> searchUser(String query, Pageable pageable);
}

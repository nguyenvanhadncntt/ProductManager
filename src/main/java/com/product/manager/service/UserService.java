package com.product.manager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.product.manager.dto.UserCreationDTO;
import com.product.manager.dto.UserDTO;

public interface UserService {
    Page<UserDTO> searchUser(String query, Pageable pageable);
    UserDTO getUserByEmail(String email);
    UserDTO getUserById(Long id);
    Boolean checkEmailAlreadyExist(String email);
    Boolean checkUserAlreadyExistById(Long id);
    UserCreationDTO createNewUser(UserCreationDTO user);
    UserCreationDTO updateUser(UserCreationDTO user);
}

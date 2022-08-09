package com.product.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.product.manager.convert.impl.UserConvert;
import com.product.manager.convert.impl.UserCreationConvert;
import com.product.manager.dto.UserCreationDTO;
import com.product.manager.dto.UserDTO;
import com.product.manager.entity.User;
import com.product.manager.repository.UserRepository;
import com.product.manager.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvert userConvert;
    
    @Autowired
    private UserCreationConvert userCreationConvert;
    
    @Override
    public Page<UserDTO> searchUser(String query, Pageable page) {
        Page<User> userPage = userRepository.searchUsers(query, page);
        return userPage.map(user -> userConvert.convertEntityToDTO(user));
    }

	@Override
	public UserDTO getUserByEmail(String email) {
		var userOptional = userRepository.findByEmail(email);
		return userOptional.isPresent() ? userConvert.convertEntityToDTO(userOptional.get()): null;
	}

	@Override
	public UserDTO getUserById(Long id) {
		var userOptional = userRepository.findById(id);
		return userOptional.isPresent() ? userConvert.convertEntityToDTO(userOptional.get()): null;
	}

	@Override
	public Boolean checkEmailAlreadyExist(String email) {
		var userOptional = userRepository.findByEmail(email);
		return userOptional.isPresent();
	}

	@Override
	public UserCreationDTO createNewUser(UserCreationDTO userCreationDto) {
		User user = userCreationConvert.convertDTOToEntity(userCreationDto);
		user = userRepository.save(user);
		return userCreationConvert.convertEntityToDTO(user);
	}

	@Override
	public UserCreationDTO updateUser(UserCreationDTO userdto) {
		User user = userCreationConvert.convertDTOToEntity(userdto);
		user = userRepository.save(user);
		return userCreationConvert.convertEntityToDTO(user);
	}

	@Override
	public Boolean checkUserAlreadyExistById(Long id) {
		var userOptional = userRepository.findById(id);
		return userOptional.isPresent();
	}
}

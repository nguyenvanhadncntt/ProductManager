package com.product.manager.convert.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.product.manager.convert.EntityConvert;
import com.product.manager.dto.UserCreationDTO;
import com.product.manager.entity.Role;
import com.product.manager.entity.User;

@Component
public class UserCreationConvert implements EntityConvert<User, UserCreationDTO> {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User convertDTOToEntity(UserCreationDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setActive(true);
		user.setEmail(dto.getEmail());
		user.setLastName(dto.getLastName());
		user.setFirstName(dto.getFirstName());
		if (dto.getId() == null) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		user.setRole(new Role(dto.getRoleId()));
		return user;
	}

	@Override
	public List<User> convertDTOsToEntities(List<UserCreationDTO> dtos) {
		if (!CollectionUtils.isEmpty(dtos)) {
			return dtos.stream().map(this::convertDTOToEntity).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public UserCreationDTO convertEntityToDTO(User entity) {
		UserCreationDTO userDTO = new UserCreationDTO();
        userDTO.setEmail(entity.getEmail());
        userDTO.setId(entity.getId());
        userDTO.setRoleId(entity.getRole().getId());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setId(entity.getId());
        return userDTO;
	}

	@Override
	public List<UserCreationDTO> convertEntitiesToDTOs(List<User> entities) {
		if (!CollectionUtils.isEmpty(entities)) {
			return entities.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}

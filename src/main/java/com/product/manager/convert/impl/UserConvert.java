package com.product.manager.convert.impl;

import com.product.manager.convert.EntityConvert;
import com.product.manager.dto.UserDTO;
import com.product.manager.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvert implements EntityConvert<User, UserDTO> {

    @Override
    public User convertDTOToEntity(UserDTO dto) {
        return null;
    }

    @Override
    public List<User> convertDTOsToEntities(List<UserDTO> dtos) {
        return null;
    }

    @Override
    public UserDTO convertEntityToDTO(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setActive(entity.getActive());
        userDTO.setEmail(entity.getEmail());
        userDTO.setId(entity.getId());
        userDTO.setCreatedDate(entity.getCreatedDate());
        userDTO.setRole(entity.getRole().getName());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        return userDTO;
    }

    @Override
    public List<UserDTO> convertEntitiesToDTOs(List<User> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }
}

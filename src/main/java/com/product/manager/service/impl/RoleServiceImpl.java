package com.product.manager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.manager.dto.RoleDTO;
import com.product.manager.repository.RoleRepository;
import com.product.manager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RoleDTO> getAllRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll()
				.stream().map(role -> new RoleDTO(role.getId(), role.getName())).collect(Collectors.toList());
	}
	
	
}

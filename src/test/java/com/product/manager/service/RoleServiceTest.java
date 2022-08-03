package com.product.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.product.manager.BaseTest;
import com.product.manager.dto.RoleDTO;

public class RoleServiceTest extends BaseTest {

	@Autowired
	private RoleService roleService;
	
	@Test
	void listRolesTest() {
		List<RoleDTO> roles = roleService.getAllRoles();
		assertEquals(2, roles.size());
		assertEquals("ADMIN", roles.get(0).getName());
		assertEquals("USER", roles.get(1).getName());
	}

}

package com.product.manager.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.manager.constant.AuthoritiesConstants;
import com.product.manager.dto.RoleDTO;
import com.product.manager.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
	
	private final Logger LOGGER = LogManager.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<List<RoleDTO>> getAllRoles() {
		LOGGER.info("get all Roles");
		return ResponseEntity.ok(roleService.getAllRoles());
	}
}

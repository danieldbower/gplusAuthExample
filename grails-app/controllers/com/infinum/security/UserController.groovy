package com.infinum.security

import grails.plugins.springsecurity.Secured

import org.springframework.dao.DataIntegrityViolationException
@Secured('permitAll')

class UserController {

	def create() {
		String email = params.email
		if(!email){
			flash.message("Email is required")
			redirect(uri:'/')
			return
		}
		
		String password = UUID.randomUUID().toString()
		User user = new User(
				username:email,
				password: password, enabled: true,
				accountExpired:false, accountLocked:false,
				passwordExpired: false)
		
		if(!user.save()){
			flash.message = "Failed to create user account for $email"
			redirect(uri:'/')
			return
		}
		
		log.info("User: ${user.username} Password: $password")
		
		Role userRole = Role.findByAuthority(Role.USER)
		UserRole userUserRole = new UserRole(role:userRole, user:user)
		if(! (userUserRole.save())){
			flash.message = "Failed to assign User Role to $email"
			redirect(uri:'/')
			return
		}

		flash.message = "Created user account for ${user.username}"
		redirect(uri:'/')
		return
	}
}

package com.infinum.security

class Role {

	String authority
	
	private static final String ADMIN = 'ROLE_ADMIN'
	private static final String USER = 'ROLE_USER'

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
	
	String toString(){
		return authority
	}
}

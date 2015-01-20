import com.infinum.security.Role
import com.infinum.security.User
import com.infinum.security.UserRole

class BootStrap {

    def init = { servletContext ->
		if(Role.count() == 0){
			addRoles()
		}
		if(User.count() == 0){
			addAdminPerson()
		}
    }
    def destroy = {
    }
	
	private void addRoles(){
		Role adminRole = new Role(authority:Role.ADMIN)
		if(!adminRole.save()){
			log.error("Failed to create Admin Role")
		}
		Role userRole = new Role(authority:Role.USER)
		if(!userRole.save()){
			log.error("Failed to create User Role")
		}
	}
	
	private void addAdminPerson(){
		String password = UUID.randomUUID().toString()
		User admin = new User(
				username:'daniel.bower@infinum.com',
				password: password, enabled: true, 
				accountExpired:false, accountLocked:false, 
				passwordExpired: false)
		
		if(!admin.save()){
			log.error("Failed to create Administrator")
			return
		}
		log.info("Admin: ${admin.username} Password: $password")
		
		Role adminRole = Role.findByAuthority(Role.ADMIN)
		UserRole adminAdminRole = new UserRole(role:adminRole, user:admin)
		if(! (adminAdminRole.save())){
			log.error("Failed to assign Admin Role to Administrator")
		}
	}
}

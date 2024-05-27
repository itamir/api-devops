package br.ufrn.imd.restservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.domain.Users;
import br.ufrn.imd.repositories.UserRepository;

/**
 * User´s rest controller. Learn more in
 * https://spring.io/guides/gs/rest-service/ and
 * https://spring.io/guides/tutorials/rest/
 * 
 * @author itamir
 *
 */
@RestController
public class UserRestService {

	private final UserRepository repository;
	
	public UserRestService(UserRepository repository) {
		this.repository = repository;
	}
	

	@RequestMapping("/users/{email}")
	public Users getUsersByEmail(@PathVariable String email) {
		List<Users> usuarios = repository.findByEmail(email);
		return usuarios != null && !usuarios.isEmpty() ? usuarios.get(0) : null;
	}

	@RequestMapping("/users")
	public List<Users> getUsers() {
		return (List<Users>) repository.findAll();
	}

	@PostMapping("/users")
	public Users newUser(@RequestBody Users usr) {
		return repository.save(usr);
	}

	@PutMapping("/users/{id}")
	public Users updateUser(@RequestBody Users newUser, @PathVariable Long id) {
		return repository.findById(id).map(user -> {
			user.setName(newUser.getName());
			user.setEmail(newUser.getEmail());
			user.setPassword(newUser.getPassword());
			return repository.save(user);
		}).orElseGet(() -> {
			newUser.setId(id);
			return repository.save(newUser);
		});
	}

	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable Long id) {
	    repository.delete(new Users(id));
	}
}

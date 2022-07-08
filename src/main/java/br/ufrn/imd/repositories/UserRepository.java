package br.ufrn.imd.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufrn.imd.domain.Users;
/**
 * User's repository based on CrudRepository
 * Learn more in https://spring.io/guides/gs/accessing-data-jpa/
 * @author itamir
 *
 */
public interface UserRepository extends CrudRepository<Users, Long> {

	public List<Users> findByEmail(String email);

	Users findByEmailAndPassword(String username, String password);
}

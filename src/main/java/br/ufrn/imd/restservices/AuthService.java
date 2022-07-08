package br.ufrn.imd.restservices;

import br.ufrn.imd.repositories.UserRepository;
import br.ufrn.imd.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> auth(String email, String password) {
        var user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password invalid");
        }
        return ResponseEntity.ok(SecurityUtils.buildJwt(user.getEmail()));
    }
}

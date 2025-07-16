package br.tulio.projetospring.services;

import br.tulio.projetospring.data.dto.security.AccountCredentialsDTO;
import br.tulio.projetospring.data.dto.security.TokenDTO;
import br.tulio.projetospring.exception.RequiredObjectIsNullException;
import br.tulio.projetospring.models.User;
import br.tulio.projetospring.repository.UserRepository;
import br.tulio.projetospring.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static br.tulio.projetospring.mapper.ObjectMapper.parseObject;

@Service
public class AuthServices {

    Logger logger = LoggerFactory.getLogger(AuthServices.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUserName(), credentials.getPassword())
        );

        var user = repository.findByUsername(credentials.getUserName());
        if (user == null) throw new UsernameNotFoundException("Username " + credentials.getUserName() + " Not Found");

        var tokenResponse = tokenProvider.createAccessToken(credentials.getUserName(), user.getRoles());

        return ResponseEntity.ok(tokenResponse);
    }

    public ResponseEntity<TokenDTO> refreshToken(String userName, String refreshToken) {
        TokenDTO token;
        var user = repository.findByUsername(userName);

        if (user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + userName + " Not Found");
        }

        return ResponseEntity.ok(token);
    }


    public ResponseEntity<AccountCredentialsDTO> create(AccountCredentialsDTO user) {
        if (user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new User...");
        var entity = new User();
        entity.setUserName(user.getUserName());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setFullName(user.getFullName());
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        return ResponseEntity.ok().body(parseObject(repository.save(entity), AccountCredentialsDTO.class));
    }

    private String generateHashedPassword(String password){
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("pbkdf2"));

        return passwordEncoder.encode(password);
    }
}

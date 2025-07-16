package br.tulio.projetospring.security.jwt;

import br.tulio.projetospring.data.dto.security.TokenDTO;
import br.tulio.projetospring.exception.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;


//Usado para gerar e validar os tokens de seguraça
@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}") // apos os ":" é valor padrão se não houver definição
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-lenght:3600000}")
    private Long validityInSeconds = 3600000L; // 1 hora

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct //Ocorre após a inicialização do spring, mas antes das ações do usuario
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes()); //cria o algoritmo de assinatura usando a nossa chave secreta
    }

    public TokenDTO createAccessToken(String userName, List<String> roles) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds);
        String accessToken = getAccessToken(userName, roles, now, validity);
        String refreshToken = getRefreshToken(userName, roles, now);

        return new TokenDTO(userName, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        if(StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ")){
            refreshToken = refreshToken.substring("Bearer ".length()); //pega só a chave
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        String userName = decodedJWT.getSubject();
        List<String> roles =  decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(refreshToken, roles);
    }

    private String getRefreshToken(String userName, List<String> roles, Date now) {
        Date refreshTokenValidity = new Date(now.getTime() + validityInSeconds);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withSubject(userName)
                .withExpiresAt(refreshTokenValidity)
                .sign(algorithm); // assina o token
    }

    private String getAccessToken(String userName, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); //pega (monta) a url atual do nosso contexto

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(userName)
                .withIssuer(issuerUrl)
                .sign(algorithm); // assina o token
    }


    public Authentication getAuthentication(String token) {
        DecodedJWT jwt = decodeToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwt.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build(); //instancia o verificador
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); //Sempre vem no Header Authorization

        //Bearer  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsIml
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length()); //pega só a chave
        }

        return null;

    }

    public Boolean validateToken(String token) {
        DecodedJWT decodeJwt = decodeToken(token);
        try {
            if (decodeJwt.getExpiresAt().before(new Date())) { //se já expirou
                return false;
            }
            return true;
            
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token!");
        }
    }

}

package mieker.back_recoleto.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //    pega a chave secreta que está na application.yaml
    @Value("${spring.security.jwt.secret.key}")
    private String secretKey;

    //    pega o tempo de expiração do token da application.yaml
    @Value("${spring.security.jwt.expiration-time}")
    private long jwtExpiration;

    //    extrai o e-mail do token JWT. Ele chama o método extractClaim, passando uma função que extrai o assunto (subject) do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //    Este método é um método genérico para extrair informações específicas do token JWT, como o assunto, o tempo de expiração, etc. É usado para extrair qualquer reivindicação (claim) do token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //    Este método é usado para gerar um token JWT com base nas informações do usuário.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //    Este método é usado para gerar um token JWT com base nas informações do usuário e em reivindicações (claims) adicionais.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    //    Este método retorna o tempo de expiração do token JWT configurado para o serviço.
    public long getExpirationTime() {
        return jwtExpiration;
    }

    //    Este método constrói um token JWT, configurando suas reivindicações (claims), como o assunto (subject), a data de emissão (issuedAt) e a data de expiração (expiration), e assinando o token com a chave secreta configurada.
    public String buildToken(Map<String, Object> extraClaims,
                             UserDetails userDetails,
                             long expiration) {
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    //    Este método verifica se o token JWT é válido. '
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //    Este método verifica se o token JWT está expirado.
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //    Este método extrai a data de expiração do token JWT.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //    Este método extrai todas as reivindicações (claims) do token JWT.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //    Este método retorna a chave de assinatura (signing key) usada para assinar e verificar a autenticidade do token JWT. Ele decodifica a chave secreta configurada do formato Base64 e a converte em uma chave HMAC.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
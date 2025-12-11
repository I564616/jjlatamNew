package com.jnj.service.jwt;

import com.jnj.b2b.la.jnjlaawstokenservice.data.JwtTokenBean;
import com.jnj.jwt.service.JwtServiceConstant;
import com.jnj.jwt.service.exception.ErrorCode;
import com.jnj.jwt.service.exception.JwtServiceException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service

public class JwtService {
    private String jwtSecret;
    private String issuer;
    private long hoursToLive;
    protected ConfigurationService configurationService;
    private static final Logger LOG = Logger.getLogger(com.jnj.jwt.service.JwtService.class);

    public String createJWT(final String subject, final String token) {
        final Map<String, String> claims = new HashMap<>();
        claims.put("token", token);
        readJwtConfig();
        return createJWT(issuer, subject, hoursToLive, jwtSecret, claims);
    }

    private String createJWT(final String issuer, final String subject, final long hoursToLive, final String secret, final Map<String, String> claims) {

        final ZonedDateTime now = ZonedDateTime.now();

        final ZonedDateTime expiresAt = now.plusHours(hoursToLive);

        final JwtBuilder builder = Jwts.builder()
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expiresAt.toInstant()))
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(
                        SignatureAlgorithm.HS256,
                        secret.getBytes()
                );

        for(final String key : claims.keySet()) {
            builder.claim(key, claims.get(key));
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public JwtTokenBean validateJwt(final String token) {
        readJwtConfig();
        final JwtTokenBean tokenBean =new JwtTokenBean();
        try {
            final Jws<Claims> jws = Jwts
                    .parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token);
            final Claims claim = jws.getBody();
            //tokenBean.setEmailId(claim.getSubject());
            //tokenBean.setToken(claim.get("token").toString());
            return tokenBean;
        } catch (final JwtException e) {
            LOG.error(e);
            throw new JwtServiceException(ErrorCode.PROCESSING_ERROR, "Invalid token");
        } catch (final Exception e) {
            LOG.error(e);
            throw new JwtServiceException(ErrorCode.PROCESSING_ERROR, "Invalid token");
        }
    }

    private void readJwtConfig(){
        this.issuer = getConfigurationService().getConfiguration().getString(JwtServiceConstant.JWT_ISSUER);
        this.jwtSecret = getConfigurationService().getConfiguration().getString(JwtServiceConstant.JWT_SECRET_CODE);
        this.hoursToLive = Long
                .parseLong(getConfigurationService().getConfiguration().getString(JwtServiceConstant.JWT_HOURS_TO_LIVE));

    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}

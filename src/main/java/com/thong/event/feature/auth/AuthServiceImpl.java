package com.thong.event.feature.auth;
import com.thong.event.domain.Role;
import com.thong.event.domain.User;
import com.thong.event.feature.auth.dto.JwtResponse;
import com.thong.event.feature.auth.dto.LoginRequest;
import com.thong.event.feature.auth.dto.RefreshTokenRequest;
import com.thong.event.feature.auth.dto.RegisterRequest;
import com.thong.event.feature.role.RoleRepository;
import com.thong.event.feature.user.UserMapper;
import com.thong.event.feature.user.UserRespository;
import com.thong.event.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRespository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DaoAuthenticationProvider authProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtEncoder jwtEncoder;
    private final String TOKEN_TYPE = "Bearer";

    private JwtEncoder refreshJwtEncoder;
    @Qualifier("refreshJwtEncoder")
    @Autowired
    public void setRefreshJwtEncoder(JwtEncoder refreshJwtEncoder) {
        this.refreshJwtEncoder = refreshJwtEncoder ;
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        try {
            Authentication authentication =
                    new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken());

            Authentication authenticated =
                    jwtAuthenticationProvider.authenticate(authentication);

            Jwt jwt = (Jwt) authenticated.getPrincipal();

            String scope = jwt.getClaimAsString("scope");
            Instant now = Instant.now();

            JwtClaimsSet newAccessTokenClaims = JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .claim("scope", scope)
                    .issuedAt(now)
                    .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                    .issuer("BackEnd Core Banking")
                    .audience(List.of("Mobile", "Android", "ReactJS"))
                    .build();

            JwtClaimsSet newRefreshTokenClaims = JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .claim("scope", scope)
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .issuer("BackEnd Core Banking")
                    .audience(List.of("Mobile", "Android", "ReactJS"))
                    .build();

            String newAccessToken =
                    jwtEncoder.encode(JwtEncoderParameters.from(newAccessTokenClaims))
                            .getTokenValue();

            String newRefreshToken =
                    refreshJwtEncoder.encode(JwtEncoderParameters.from(newRefreshTokenClaims))
                            .getTokenValue();

            return JwtResponse.builder()
                    .tokenType(TOKEN_TYPE)
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

        } catch (Exception ex) {
            log.error("Refresh token error", ex);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired refresh token"
            );
        }
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password()
        );
        authentication = authProvider.authenticate(authentication);

        // user details
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        // add scope to payload of jwt token
        assert userDetails != null;
        String scope =  userDetails.getUser().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));
        log.info( "Auth scope " + scope);
        Instant now = Instant.now();
        // data custom data to payload
        JwtClaimsSet acessTokenClams = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .claim("scope", scope)
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .audience(List.of("Mobile", "Android","ReactJS"))
                .issuer("BackEnd Core Banking")
                .build();

        JwtClaimsSet refreshTokenClams = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .claim("scope", scope)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .audience(List.of("Mobile", "Android","ReactJS"))
                .issuer("BackEnd Core Banking")
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(acessTokenClams)).getTokenValue();
        String refreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClams)).getTokenValue();

        log.info("Access token: {}", accessToken);

        log.info("Refresh token: {}", refreshToken);


        return JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(RegisterRequest registerRequest)  {

        // Validate email
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        // Validate UserName
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        // Validate password and confirmed password
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Transfer data from DTO to Domain Model
        User user = userMapper.fromRegisterRequest(registerRequest);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsBlocked(false);
        user.setIsDeleted(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setProfileImage("user-avatar.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());
        user.setRoles(roles);

        userRepository.save(user);

    }

}

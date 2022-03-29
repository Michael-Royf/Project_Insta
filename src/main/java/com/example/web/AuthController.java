package com.example.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.entity.enums.ERole;
import com.example.exceptions.GlobalExceptionHandler;
import com.example.exceptions.domain.UserNotFoundException;
import com.example.exceptions.domain.UsernameExistException;
import com.example.payload.request.LoginRequest;
import com.example.payload.request.SignupRequest;
import com.example.payload.response.MessageResponse;
import com.example.constant.SecurityConstant;
import com.example.service.UserServiceImpl;
import com.example.utility.JwtTokenProvider;
import com.example.validations.ResponseErrorValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static com.example.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
@CrossOrigin

public class AuthController extends GlobalExceptionHandler {

    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserEntity userEntity= userService.getUserByUsername(loginRequest.getUsername());
        HttpHeaders jwtHeader = getJwtHeaders(userEntity);
        return new ResponseEntity<>(userEntity, jwtHeader, HttpStatus.OK);

    }
    private HttpHeaders getJwtHeaders(UserEntity user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }



    //регистрация нового пользователя
    @PostMapping("/signup")
      public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) throws UsernameExistException {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }


//    @PostMapping("/token/refresh")
//    public void refreshToken(HttpServletRequest request,
//                             HttpServletResponse response) throws IOException {
//
//        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refreshToken = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.SECRET.getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refreshToken);
//                String username = decodedJWT.getSubject();
//                UserEntity user = userService.getUserByUsername(username);//email
//
//
//                List<String> list = new ArrayList<>();
//                for (ERole role : user.getRole()) {
//                    String name = role.name();
//                    list.add(name);
//                }
//                String access_token = JWT.create()
//                        .withSubject(user.getUsername())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                        .withIssuer(request.getRequestURI().toString())
//                        .withClaim("roles", list)
//                        .sign(algorithm);
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refreshToken);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//              //  response.setHeader("access_token", access_token);
//             //   response.setHeader("refresh_token", refresh_token);
//
//            } catch (Exception e) {
//                response.setHeader("error", e.getMessage());
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                Map<String, String> errors = new HashMap<>();
//                errors.put("error_message", e.getMessage());
//
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), errors);
//            }
//        } else {
//            throw new RuntimeException("Refresh token is missing");
//        }
//
//    }

@GetMapping("/home")
public String hello(){
    return "Hello michael!";
}

}

package com.example.springboot.auth;


import com.example.springboot.config.JwtService;
import com.example.springboot.user.Role;
import com.example.springboot.user.UserRepository;
import com.example.springboot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


//The @Service annotation tells spring that the denoted class will contain business logic of the application
//it will automatically  get injected in other beans inside the spring application that requires it
@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //if the password is incorrect or the user does not exist the authentication manager will throw an exception
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        //add User to database
        userRepository.save(newUser);

        //generate the jwt Token
        String jwt = jwtService.generateToken(newUser);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}

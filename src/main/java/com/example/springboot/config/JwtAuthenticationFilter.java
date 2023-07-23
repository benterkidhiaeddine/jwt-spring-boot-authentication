package com.example.springboot.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;





@Component
//required args constructor will create a constructor for all final fields
@RequiredArgsConstructor
//we extend the once per request filter to make sure that our jwt authentication filter fires only once pre request
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final int BEGINNING_OF_JET_INDEX = 7;

    //We need to implement a JWT service that will extract the username ( in our case the email ) from the jwt token
    // this will be a class and inside will be a method that will do the work

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        //we will be extracting from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;


        //Make sure the authorization header is present
        if ( authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt  = authHeader.substring(BEGINNING_OF_JET_INDEX);
        userEmail = jwtService.extractUsername(jwt);

        //Check if there is a username inside the jwt and that the user is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //the userDetailService will fetch the user from the database via the user repository
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //when we update the Security context holder and set authentication with the authToken that we generated the user will be authenticated

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //always make sure to call the doFilter so that the next filter will be chained
        filterChain.doFilter(request,response);
        //here what happens if the user is already authenticated
    }

}

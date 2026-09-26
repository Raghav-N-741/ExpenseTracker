package com.raghav.expense.filter;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raghav.expense.service.JwtService;
import com.raghav.expense.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    public JwtFilter(JwtService jwtService,UserDetailsServiceImpl userDetailsServiceImpl)
    {
        this.jwtService=jwtService;
        this.userDetailsServiceImpl=userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        String auth=request.getHeader("Authorization");
        if(auth==null || !auth.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt=auth.substring(7);
        String  username=jwtService.extractEmail(jwt);
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=userDetailsServiceImpl.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt,userDetails))
            {
                UsernamePasswordAuthenticationToken upa=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                upa.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upa);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}

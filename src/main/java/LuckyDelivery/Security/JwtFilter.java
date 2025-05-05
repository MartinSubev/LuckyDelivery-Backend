package LuckyDelivery.Security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("JwtFilter: Processing request for URI: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String userId = null; // Extract userId from token

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            userId = jwtUtil.extractUserId(jwt); // Extract userId
            logger.info("JwtFilter: JWT found: {}", jwt);
            logger.info("JwtFilter: Extracted username: {}", username);
            logger.info("JwtFilter: Extracted userId: {}", userId);
        } else {
            logger.info("JwtFilter: Authorization header not found or does not start with Bearer.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("JwtFilter: UserDetails loaded for user: {}", username);

            if (jwtUtil.validateToken(jwt)) {
                // Store both UserDetails and userId in the authentication principal
                Map<String, Object> principal = new HashMap<>();
                principal.put("userDetails", userDetails);
                principal.put("userId", userId);

                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(principal, null, userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                logger.info("JwtFilter: Authentication set for user: {}", username);
            } else {
                logger.warn("JwtFilter: JWT validation failed for user: {}", username);
            }
        } else if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.info("JwtFilter: Authentication already present in SecurityContextHolder.");
        } else {
            logger.info("JwtFilter: Username is null or no existing authentication.");
        }

        filterChain.doFilter(request, response);
    }
}
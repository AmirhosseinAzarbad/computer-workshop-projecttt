package ir.speedy.computerworkshopproject.security.jwt;

import ir.speedy.computerworkshopproject.Repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ir.speedy.computerworkshopproject.models.User;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepo;
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(UserRepository userRepo, JwtTokenProvider tokenProvider) {
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;

    }

    private static final List<String> PUBLIC_PATH = List.of(
            "/auth/login",
            "/auth/register",
            "/api/books/show",
            "/auth/send-otp"
    );
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if(tokenProvider.validateToken(token)){
                String username = tokenProvider.getUsernameFromToken(token);
                        User user = userRepo.findByUsername(username)
                                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));

                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return PUBLIC_PATH.stream().anyMatch(path::startsWith);
    }
}

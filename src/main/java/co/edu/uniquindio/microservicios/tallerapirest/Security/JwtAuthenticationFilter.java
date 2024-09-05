package co.edu.uniquindio.microservicios.tallerapirest.Security;

import co.edu.uniquindio.microservicios.tallerapirest.Entities.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.getTokenFromRequest(request);
        String username = request.getParameter("username");
        try {
            if(request.getServletPath().equals("/login") || request.getServletPath().equals("/signup") || request.getServletPath().equals("/generateChangePasswordToken") || request.getServletPath().equals("/users")) {
                filterChain.doFilter(request, response);
            } else if (request.getServletPath().equals("/changePassword")) {
                if (jwtService.isChangePasswordTokenValid(token)) {
                    filterChain.doFilter(request, response);
                } else {
                    // Error de token inválido para cambiar contraseña
                    setUnauthorizedResponse(response, "Error: Usuario no autorizado.", request);
                }
            } else {
                if (jwtService.isTokenValid(token, username)) {
                    filterChain.doFilter(request, response);
                } else {
                    // Error de token inválido
                    setUnauthorizedResponse(response, "Error: Usuario no autorizado.", request);
                }
            }
        } catch (ExpiredJwtException e) {
            // Token expirado
            setUnauthorizedResponse(response, "Error: El token ha expirado.", request);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message, HttpServletRequest request) throws IOException {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));
        response.getWriter().flush();
    }

}
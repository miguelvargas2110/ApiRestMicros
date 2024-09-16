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
            String servletPath = request.getServletPath();

            // Permitir acceso sin autenticación a rutas específicas
            if (servletPath.equals("/login") ||
                    servletPath.equals("/signup") ||
                    servletPath.equals("/generateChangePasswordToken") ||
                    servletPath.equals("/users") ||
                    servletPath.startsWith("/swagger-ui") ||
                    servletPath.startsWith("/v3/api-docs")) { // Ampliar condición para incluir todas las rutas de Swagger UI

                filterChain.doFilter(request, response);
                return;
            }

            // Manejo especial para la ruta de cambio de contraseña
            if (servletPath.equals("/changePassword")) {
                if (token != null && jwtService.isChangePasswordTokenValid(token)) {
                    filterChain.doFilter(request, response);
                } else {
                    setUnauthorizedResponse(response, "Error: Usuario no autorizado.", request);
                }
                return;
            }

            // Validación para otras rutas
            if (token != null && jwtService.isTokenValid(token, username)) {
                filterChain.doFilter(request, response);
            } else {
                setUnauthorizedResponse(response, "Error: Usuario no autorizado.", request);
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
package co.edu.uniquindio.microservicios.tallerapirest.Services;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.AuthenticationResponse;
import co.edu.uniquindio.microservicios.tallerapirest.DTO.LoginDTO;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioServiceImpl userRepository;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse login(LoginDTO authRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        authenticationManager.authenticate(authToken);

        User user = userRepository.searchByUserName(authRequest.getUsername()).get();

        String jwt = jwtService.getToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt);
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("permissions", user.getAuthorities());
        return extraClaims;
    }


}


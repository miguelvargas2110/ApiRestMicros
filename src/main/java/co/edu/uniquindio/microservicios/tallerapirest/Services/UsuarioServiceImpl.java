package co.edu.uniquindio.microservicios.tallerapirest.Services;

import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl {
    private final UserRepository userDao;

    @Transactional(readOnly = true)
    public Optional<User> searchByUserName(String userName) {
        return userDao.findByUsername(userName);
    }
}

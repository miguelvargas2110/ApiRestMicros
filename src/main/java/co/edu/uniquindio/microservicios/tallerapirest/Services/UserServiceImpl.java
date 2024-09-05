package co.edu.uniquindio.microservicios.tallerapirest.Services;

import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl {
    private final UserRepository userDao;

    @Transactional(readOnly = true)
    public Optional<User> searchByUserName(String userName) {
        return userDao.findByUsername(userName);
    }

    @Transactional
    public User save(User user) {return userDao.save(user);}

    @Transactional
    public void delete(User user) {userDao.delete(user);}

    @Transactional
    public void update(User user) {userDao.save(user);}

    @Transactional
    public Page<User> obtenerEntidadesPaginadas(int numeroPagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamanoPagina);
        return userDao.findAll(pageable);
    }
}

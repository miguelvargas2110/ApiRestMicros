package co.edu.uniquindio.microservicios.tallerapirest.Repositories;

import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
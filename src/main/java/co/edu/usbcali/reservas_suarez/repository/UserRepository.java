package co.edu.usbcali.reservas_suarez.repository;

import co.edu.usbcali.reservas_suarez.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository


public interface UserRepository extends JpaRepository <User, Integer > {
    Optional<User> findByUsername(String username);
}

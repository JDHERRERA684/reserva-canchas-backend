package co.edu.usbcali.reservas_suarez.repository;

import co.edu.usbcali.reservas_suarez.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Integer> {
}

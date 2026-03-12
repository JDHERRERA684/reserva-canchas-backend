package co.edu.usbcali.reservas_suarez.repository;

import co.edu.usbcali.reservas_suarez.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository <Court, Integer>{
}

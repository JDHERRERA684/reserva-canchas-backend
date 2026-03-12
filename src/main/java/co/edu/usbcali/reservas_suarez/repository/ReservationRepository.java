package co.edu.usbcali.reservas_suarez.repository;

import co.edu.usbcali.reservas_suarez.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReservationRepository extends JpaRepository <Reservation, Integer> {
}

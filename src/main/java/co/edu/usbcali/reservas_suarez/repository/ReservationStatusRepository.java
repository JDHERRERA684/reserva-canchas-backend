package co.edu.usbcali.reservas_suarez.repository;

import co.edu.usbcali.reservas_suarez.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReservationStatusRepository extends JpaRepository <ReservationStatus, Integer> {
}

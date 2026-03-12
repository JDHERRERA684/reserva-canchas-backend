package co.edu.usbcali.reservas_suarez.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, referencedColumnName = "id" )
    private Client client;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false,referencedColumnName = "id")
    private Court court;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false, referencedColumnName = "id")
    private ReservationStatus status;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

package co.edu.usbcali.reservas_suarez.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_statu")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStatu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;
}

package co.edu.usbcali.reservas_suarez.model;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
@Entity
@Table(name = "clients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "created_at")
    private Timestamp createdAt;
}

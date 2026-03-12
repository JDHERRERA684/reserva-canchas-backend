package co.edu.usbcali.reservas_suarez.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "courts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Court { @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    private String description;
}

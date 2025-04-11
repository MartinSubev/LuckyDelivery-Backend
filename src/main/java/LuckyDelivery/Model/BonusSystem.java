package LuckyDelivery.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "bonus_system")
public class BonusSystem {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`key`", nullable = false, unique = true, length = 100)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;

}
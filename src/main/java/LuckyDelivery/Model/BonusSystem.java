package LuckyDelivery.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "bonus_system")
public class BonusSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`key`", nullable = false, unique = true, length = 100)
    private String key;

    @Column(name = "value", nullable = false)
    private String value;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
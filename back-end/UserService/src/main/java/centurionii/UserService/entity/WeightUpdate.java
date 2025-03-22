package centurionii.UserService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WeightUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Long userId;

    private int month;
    private int day;
    private int year;

    private float newWeightValue;

    public WeightUpdate() {}

    public WeightUpdate(Long userId, int month, int day, int year, float newWeightValue) {
        this.userId = userId;
        this.month = month;
        this.day = day;
        this.year = year;
        this.newWeightValue = newWeightValue;
    }
}

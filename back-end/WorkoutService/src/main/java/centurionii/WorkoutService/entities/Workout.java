package centurionii.WorkoutService.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;

@Data
@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;

    private String muscleGroup;
    private String exerciseName;

    private int month;
    private int day;
    private int year;

    private String sets;

    public Workout() {

    }
    public Workout(long userId, String muscleGroup, String exerciseName, int day, int month, int year, String sets) {
        this.userId = userId;
        this.muscleGroup = muscleGroup;
        this.exerciseName = exerciseName;
        this.day = day;
        this.month = month;
        this.year = year;
        this.sets = sets;
    }

    public void setDate(String date) {
        int[] dates = Arrays.stream(date.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();

        this.day = dates[0];
        this.month = dates[1];
        this.year = dates[2];
    }
}

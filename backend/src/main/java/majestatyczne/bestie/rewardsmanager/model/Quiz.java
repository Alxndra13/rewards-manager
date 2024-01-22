package majestatyczne.bestie.rewardsmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quiz")
@Data
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxScore;

    @Column(nullable = false)
    private Date date;

    @Column
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Preference> preferences;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("score DESC, endDate ASC")
    private List<Result> results;

    @OneToOne(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private RewardStrategy rewardStrategy;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questions;
}

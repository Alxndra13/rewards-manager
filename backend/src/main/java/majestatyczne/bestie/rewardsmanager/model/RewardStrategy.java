package majestatyczne.bestie.rewardsmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import majestatyczne.bestie.rewardsmanager.reward_selection_strategy.RewardStrategyType;

import java.util.List;

@Entity
@Table(name = "reward_strategy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_strategy_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardStrategyType rewardStrategyType;

    @OneToOne
    @JoinColumn(name="quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "rewardStrategy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("priority ASC")
    private List<RewardStrategyParameter> parameters;
}

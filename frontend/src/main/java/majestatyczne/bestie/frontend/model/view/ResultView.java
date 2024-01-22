package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.*;
import lombok.Getter;
import majestatyczne.bestie.frontend.model.Result;
import majestatyczne.bestie.frontend.model.Reward;

import java.util.Date;

public class ResultView {

    @Getter
    private int id;

    private StringProperty personName;

    private ObjectProperty<Date> endDate;

    private ObjectProperty<Integer> score;
    
    private StringProperty reward;

    public ResultView(int id, String personName, Date endDate, int score, Reward reward) {
        this.id = id;
        this.personName = new SimpleStringProperty(personName);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.score = new SimpleObjectProperty<>(score);

        if (reward == null) {
            this.reward = new SimpleStringProperty("placeholder");
        } else {
            this.reward = new SimpleStringProperty(reward.getName());
        }
    }

    public ResultView(Result result) {
        this(result.getId(), result.getPerson().getName(), result.getEndDate(), result.getScore(), result.getReward());
    }

    public String getPersonName() {
        return personName.get();
    }

    public StringProperty getPersonNameProperty() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName.set(personName);
    }

    public Date getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<Date> getEndDateProperty() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate.set(endDate);
    }

    public int getScore() {
        return score.get();
    }

    public ObjectProperty<Integer> getScoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getReward() {
        return reward.get();
    }

    public StringProperty getRewardProperty() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward.set(reward);
    }


}
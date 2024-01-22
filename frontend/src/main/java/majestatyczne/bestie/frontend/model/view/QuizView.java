package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.*;
import lombok.Getter;

import java.util.Date;
public class QuizView {

    @Getter
    private int id;

    private StringProperty name;

    private IntegerProperty maxScore;

    private ObjectProperty<Date> date;

    public QuizView(int id, String name, int maxScore, Date date) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.maxScore = new SimpleIntegerProperty(maxScore);
        this.date = new SimpleObjectProperty<>(date);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getMaxScore() {
        return maxScore.get();
    }

    public IntegerProperty getMaxScoreProperty() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore.set(maxScore);
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> getDateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

}

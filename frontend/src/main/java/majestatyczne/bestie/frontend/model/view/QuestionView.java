package majestatyczne.bestie.frontend.model.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import majestatyczne.bestie.frontend.AnswerCalculator;
import majestatyczne.bestie.frontend.model.Question;

public class QuestionView {

    private int id;

    private StringProperty content;

    private StringProperty score;

    public QuestionView(int id, String content, String score) {
        this.id = id;
        this.content = new SimpleStringProperty(content);
        this.score = new SimpleStringProperty(score);
    }

    public QuestionView(Question question) {
        this(question.getId(), question.getContent(), AnswerCalculator.getScore(question.getAnswers()) + "%");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty getContentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getScore() {
        return score.get();
    }

    public StringProperty getScoreProperty() {
        return score;
    }

    public void setScore(String score) {
        this.score.set(score);
    }
}

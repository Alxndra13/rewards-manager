package majestatyczne.bestie.frontend.model;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private int id;
    private String content;
    private List<Answer> answers;
}

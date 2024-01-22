package majestatyczne.bestie.frontend.model;

import lombok.Data;

@Data
public class Answer {
    private int id;
    private String content;
    private int selectionCount;
    private boolean correct;
}

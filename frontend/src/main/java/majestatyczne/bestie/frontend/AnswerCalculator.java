package majestatyczne.bestie.frontend;

import majestatyczne.bestie.frontend.model.Answer;

import java.util.List;

public class AnswerCalculator {
    public static int getScore(List<Answer> answers){
        long allAnswersCount = answers.stream()
                .mapToInt(Answer::getSelectionCount)
                .sum();
        long correctAnswersCount = answers.stream()
                .filter(Answer::isCorrect)
                .mapToInt(Answer::getSelectionCount)
                .sum();

        return (int) ( correctAnswersCount / (double) allAnswersCount * 100);
    }
}

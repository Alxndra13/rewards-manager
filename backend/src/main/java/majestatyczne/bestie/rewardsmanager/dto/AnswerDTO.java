package majestatyczne.bestie.rewardsmanager.dto;

import majestatyczne.bestie.rewardsmanager.model.Answer;

import java.util.List;

public record AnswerDTO(int id, String content, int selectionCount, boolean correct) {

    public static AnswerDTO convertToDTO(Answer answer) {
        return new AnswerDTO(answer.getId(), answer.getContent(), answer.getSelectionCount(), answer.isCorrect());
    }

    public static List<AnswerDTO> convertToDTO(List<Answer> answer) {
        return answer.stream().map(a -> new AnswerDTO(a.getId(), a.getContent(), a.getSelectionCount(), a.isCorrect())).toList();
    }
}

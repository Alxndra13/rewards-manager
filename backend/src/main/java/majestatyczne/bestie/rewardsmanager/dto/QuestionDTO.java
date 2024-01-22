package majestatyczne.bestie.rewardsmanager.dto;

import majestatyczne.bestie.rewardsmanager.model.Question;

import java.util.List;

public record QuestionDTO(int id, String content, List<AnswerDTO> answers) {

    public static QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO(question.getId(), question.getContent(), AnswerDTO.convertToDTO(question.getAnswers()));
    }
}

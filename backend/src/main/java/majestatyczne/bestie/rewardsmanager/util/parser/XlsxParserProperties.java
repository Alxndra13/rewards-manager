package majestatyczne.bestie.rewardsmanager.util.parser;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xlsx-parser")
@Setter
public class XlsxParserProperties {

    private int startDateIndex;

    private int endDateIndex;

    private int scoreIndex;

    private int firstEvaluatedQuestionIndex;

    private int personNameIndex;

    private int rewardsIndex;

    private int notEvaluatedQuestions;

    @Getter
    private int columnsPerEvaluatedQuestion;

    @Getter
    private int emptyTerminalColumns;

    private int applyIndexCorrection(int index) {
        return index > 0 ? index : index + emptyTerminalColumns;
    }

    public int getStartDateIndex() {
        return applyIndexCorrection(startDateIndex);
    }

    public int getEndDateIndex() {
        return applyIndexCorrection(endDateIndex);
    }

    public int getScoreIndex() {
        return applyIndexCorrection(scoreIndex);
    }

    public int getFirstEvaluatedQuestionIndex() {
        return applyIndexCorrection(firstEvaluatedQuestionIndex);
    }

    public int getPersonNameIndex() {
        return applyIndexCorrection(personNameIndex);
    }

    public int getRewardsIndex() {
        return applyIndexCorrection(rewardsIndex);
    }

    public int getNotEvaluatedQuestions() {
        return notEvaluatedQuestions - columnsPerEvaluatedQuestion;
    }
}

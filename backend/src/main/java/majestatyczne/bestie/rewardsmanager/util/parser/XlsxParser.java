package majestatyczne.bestie.rewardsmanager.util.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import majestatyczne.bestie.rewardsmanager.model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Getter
public class XlsxParser {

    private final XlsxParserProperties properties;

    private final Logger logger = LoggerFactory.getLogger(XlsxParser.class);

    public ParsedData parseSheet(Sheet sheet) {
        logger.info("parsing data...");

        ParsedData parsedData = new ParsedData();

        // parse questions and answers
        parseQuestionsAndAnswers(sheet, parsedData);

        // remove header row
        sheet.shiftRows(1, sheet.getLastRowNum(), -1); // remove the header row

        // parse rewards
        parseRewards(parsedData, sheet);

        // parse rows
        parseRows(sheet, parsedData);

        // parse quiz
        parseQuiz(parsedData, sheet);

        logger.info("data parsing complete");

        return parsedData;
    }

    private void parseRows(Sheet sheet, ParsedData parsedData) {
        // requires sheet without header row

        int rowLength = sheet.getRow(0).getLastCellNum();

        for (Row row : sheet) {
            Date startDate = row.getCell(properties.getStartDateIndex()).getDateCellValue();
            Date endDate = row.getCell(properties.getEndDateIndex()).getDateCellValue();
            int score = (int) row.getCell(properties.getScoreIndex()).getNumericCellValue();
            String personName = row.getCell(rowLength + properties.getPersonNameIndex()).getStringCellValue();
            String allRewardsWithDescriptions = row.getCell(rowLength + properties.getRewardsIndex())
                    .getStringCellValue();

            // parse person
            Person person = parseAndGetPerson(parsedData, personName);

            // parse result
            parseResult(parsedData, person, score, startDate, endDate);

            // parse preferences
            parsePreferences(parsedData, person, allRewardsWithDescriptions);
        }
    }

    private void parseQuestionsAndAnswers(Sheet sheet, ParsedData parsedData) {
        // requires sheet with header row
        // length of the standard row = length of the header row - 2

        Row header = sheet.getRow(0);
        int lastQuestionCol = sheet.getRow(1).getLastCellNum() + properties.getPersonNameIndex() -
                properties.getColumnsPerEvaluatedQuestion();

        for (int colInd = properties.getFirstEvaluatedQuestionIndex(); colInd <= lastQuestionCol; colInd +=
                properties.getColumnsPerEvaluatedQuestion()) {
            // create question
            Question question = new Question();
            question.setQuiz(parsedData.getQuiz());
            question.setContent(header.getCell(colInd).getStringCellValue());
            parsedData.getQuestions().add(question);

            // get answers data
            Map<String, Integer> answersContentCount = new HashMap<>();
            Map<String, Boolean> answerIsCorrect = new HashMap<>();
            for (int rowInd = 1; rowInd < sheet.getLastRowNum(); rowInd++) {
                String answerContent = sheet.getRow(rowInd).getCell(colInd).getStringCellValue();
                int answerPoints = (int) sheet.getRow(rowInd).getCell(colInd + 1).getNumericCellValue();

                if (answersContentCount.containsKey(answerContent)) {
                    answersContentCount.put(answerContent, answersContentCount.get(answerContent) + 1);
                } else {
                    answersContentCount.put(answerContent, 1);
                }

                answerIsCorrect.put(answerContent, answerPoints > 0);
            }

            // create answers
            List<Answer> answers = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : answersContentCount.entrySet()) {
                Answer answer = new Answer();
                answer.setContent(entry.getKey());
                answer.setSelectionCount(entry.getValue());
                answer.setCorrect(answerIsCorrect.get(entry.getKey()));
                answer.setQuestion(question);

                answers.add(answer);
                parsedData.getAnswers().add(answer);
            }

            question.setAnswers(answers);
        }
    }

    private void parseRewards(ParsedData parsedData, Sheet sheet) {
        // requires sheet without header row

        Row firstRow = sheet.getRow(0);
        int rowLength = firstRow.getLastCellNum();
        String allRewardsWithDescriptions = firstRow.getCell(rowLength + properties.getRewardsIndex())
                .getStringCellValue();
        List<String[]> convertedRewardsWithDescriptions =
                convertRewardsWithDescription(allRewardsWithDescriptions);

        for (String[] convertedReward : convertedRewardsWithDescriptions) {
            Reward reward = new Reward();
            reward.setName(convertedReward[0]);
            reward.setDescription(convertedReward[1]);
            parsedData.getRewards().add(reward);
        }
    }

    private Person parseAndGetPerson(ParsedData parsedData, String personName) {
        Person person = new Person();
        person.setName(personName);
        parsedData.getPeople().add(person);

        return person;
    }

    private void parseResult(ParsedData parsedData, Person person, int score, Date startDate, Date endDate) {
        Result result = new Result();
        result.setQuiz(parsedData.getQuiz());
        result.setScore(score);
        result.setPerson(person);
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        parsedData.getResults().add(result);
    }

    private void parsePreferences(ParsedData parsedData, Person person, String allRewardsWithDescriptions) {
        List<String[]> convertedRewardsWithDescriptions =
                convertRewardsWithDescription(allRewardsWithDescriptions);

        for (String[] convertedReward : convertedRewardsWithDescriptions) {
            String rewardName = convertedReward[0];
            Reward reward = parsedData.getRewards()
                    .stream()
                    .filter(r -> r.getName().equals(rewardName))
                    .findFirst()
                    .orElse(new Reward());

            Preference preference = new Preference();
            preference.setQuiz(parsedData.getQuiz());
            preference.setPerson(person);
            preference.setPriority(convertedRewardsWithDescriptions.indexOf(convertedReward));
            preference.setReward(reward);
            parsedData.getPreferences().add(preference);
        }
    }

    private void parseQuiz(ParsedData parsedData, Sheet sheet) {
        // requires sheet without header

        Row row = sheet.getRow(0);
        Date date = row.getCell(properties.getStartDateIndex()).getDateCellValue();
        int maxScore = (row.getLastCellNum() - properties.getNotEvaluatedQuestions())
                / properties.getColumnsPerEvaluatedQuestion();

        parsedData.getQuiz().setDate(date);
        parsedData.getQuiz().setMaxScore(maxScore);
        parsedData.getQuiz().setResults(parsedData.getResults()); // results must be parsed first!
        parsedData.getQuiz().setName(sheet.getSheetName());
    }

    private List<String[]> convertRewardsWithDescription(String rewardsWithDescription) {
        List<String[]> convertedRewards = new ArrayList<>();
        List<String> split1 = new ArrayList<>(List.of(rewardsWithDescription.split(";")));

        for (String rewardWithDescription : split1) {
            List<String> split2 = new ArrayList<>(List.of(rewardWithDescription.split("[()]")));
            String reward = split2.get(0).substring(0, split2.get(0).length() - 1); // cut last char - whitespace
            String description = split2.get(1);

            convertedRewards.add(new String[]{reward, description});
        }

        return convertedRewards;
    }
}

package majestatyczne.bestie.rewardsmanager.util.file_loader;

import lombok.RequiredArgsConstructor;
import majestatyczne.bestie.rewardsmanager.service.*;
import majestatyczne.bestie.rewardsmanager.util.parser.ParsedData;
import majestatyczne.bestie.rewardsmanager.util.parser.XlsxParser;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
@RequiredArgsConstructor
public class XlsxDataLoader implements FileDataLoader {

    private final XlsxParser xlsxParser;

    private final PersonService personService;

    private final PreferenceService preferenceService;

    private final QuizService quizService;

    private final ResultService resultService;

    private final RewardService rewardService;

    private final QuestionService questionService;

    private final AnswerService answerService;

    private ParsedData parsedData;

    private final Logger logger = LoggerFactory.getLogger(XlsxDataLoader.class);

    @Override
    public void loadData(MultipartFile multipartFile) throws IOException {

        logger.info("loading data...");

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream());
             Workbook workbook = new XSSFWorkbook(bufferedInputStream)) {

            if (workbook.getNumberOfSheets() < 1) {
                // workbook has no sheets
                throw new EmptyFileException();
            }

            Sheet sheet = workbook.getSheetAt(0);
            workbook.setSheetName(0, multipartFile.getOriginalFilename());

            if (sheet.getLastRowNum() < 1) {
                // sheet is empty
                throw new EmptyFileException();
            }

            parsedData = xlsxParser.parseSheet(sheet);

            loadQuiz();
            loadPeople();
            loadRewards();
            loadPreferences();
            loadResults();
            loadQuestions();
            loadAnswers();

            logger.info("data loading complete");

        } catch (NotOfficeXmlFileException | EmptyFileException | IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void loadQuiz() {
        quizService.add(parsedData.getQuiz());
    }

    private void loadPeople() {
        personService.addAllWithoutDuplicates(parsedData.getPeople());
    }

    private void loadRewards() {
        rewardService.addAllWithoutDuplicates(parsedData.getRewards());
    }

    private void loadPreferences() {
        preferenceService.addAll(parsedData.getPreferences());
    }

    private void loadResults() {
        resultService.addAll(parsedData.getResults());
    }

    private void loadQuestions() {
        questionService.addAll(parsedData.getQuestions());
    }

    private void loadAnswers() {
        answerService.addAll(parsedData.getAnswers());
    }
}

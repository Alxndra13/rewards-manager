package majestatyczne.bestie.rewardsmanager.util;

import majestatyczne.bestie.rewardsmanager.RewardsManagerTestsConfiguration;
import majestatyczne.bestie.rewardsmanager.model.*;
import majestatyczne.bestie.rewardsmanager.util.parser.ParsedData;
import majestatyczne.bestie.rewardsmanager.util.parser.XlsxParser;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class XlsxParserTests {

    @Autowired
    private RewardsManagerTestsConfiguration rewardsManagerTestsConfiguration;

    @Autowired
    private XlsxParser xlsxParser;

    private Sheet prepareSheet(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            Workbook workbook = new XSSFWorkbook(bufferedInputStream);

            return workbook.getSheetAt(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parsingShortValidFile1() throws ParseException {
        // given
        Sheet sheet = prepareSheet(rewardsManagerTestsConfiguration.getShortValidFile1Path());

        // when
        ParsedData parsedData = xlsxParser.parseSheet(sheet);

        // then
        parsingPeopleShortValidFile1(parsedData);
        parsingRewardsShortValidFile1(parsedData);
        parsingQuizShortValidFile1(parsedData);
        parsingPreferencesShortValidFile1(parsedData);
        parsingResultsShortValidFile1(parsedData);
    }

    @Test
    public void parsingShortValidFile2() throws ParseException {
        // given
        Sheet sheet = prepareSheet(rewardsManagerTestsConfiguration.getShortValidFile2Path());

        // when
        ParsedData parsedData = xlsxParser.parseSheet(sheet);

        // then
        parsingPeopleShortValidFile2(parsedData);
        parsingRewardsShortValidFile2(parsedData);
        parsingQuizShortValidFile2(parsedData);
        parsingPreferencesShortValidFile2(parsedData);
        parsingResultsShortValidFile2(parsedData);
    }

    private void parsingPeopleShortValidFile1(ParsedData parsedData) {
        // given
        List<Person> people = parsedData.getPeople();

        List<Person> expectedPeople = Arrays.asList(
                new Person(0, "Baweł"),
                new Person(1, "Daredevilq"),
                new Person(2, "Schmetterling")
        );

        // then
        assertEquals(3, people.size());

        expectedPeople.forEach(expectedPerson ->
                assertTrue(people.stream().anyMatch(p -> p.getName().equals(expectedPerson.getName())))
        );
    }

    private void parsingRewardsShortValidFile1(ParsedData parsedData) {
        // given
        List<Reward> rewards = parsedData.getRewards();

        List<Reward> expectedRewards = Arrays.asList(
                new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                new Reward(1, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                new Reward(2, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                new Reward(3, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"));

        // then
        assertEquals(4, rewards.size());

        expectedRewards.forEach(expectedReward ->
                assertTrue(rewards.stream().anyMatch(r -> r.getName().equals(expectedReward.getName()) &&
                        r.getDescription().equals(expectedReward.getDescription()))));
    }

    private void parsingQuizShortValidFile1(ParsedData parsedData) {
        // given
        Quiz quiz = parsedData.getQuiz();

        // then
        assertEquals(2, quiz.getMaxScore());
    }

    private void parsingPreferencesShortValidFile1(ParsedData parsedData) {
        // given
        List<Preference> preferences = parsedData.getPreferences();
        List<Reward> rewards = parsedData.getRewards();
        List<Person> people = parsedData.getPeople();
        Quiz quiz = parsedData.getQuiz();

        List<Preference> expectedPreferences = Arrays.asList(
                new Preference(
                        0,
                        quiz,
                        new Person(0, "Baweł"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "Baweł"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "Baweł"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "Baweł"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        3),
                new Preference(
                        0,
                        quiz,
                        new Person(0, "Daredevilq"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "Daredevilq"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "Daredevilq"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "Daredevilq"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        3),
                new Preference(
                        0,
                        quiz,
                        new Person(0, "Schmetterling"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "Schmetterling"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "Schmetterling"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "Schmetterling"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        3)
        );

        // then
        assertEquals(people.size() * rewards.size(), preferences.size());

        expectedPreferences.forEach(expectedPreference ->
                assertTrue(preferences.stream().anyMatch(p -> p.getPriority() == expectedPreference.getPriority() &&
                        p.getPerson().getName().equals(expectedPreference.getPerson().getName()) &&
                        p.getReward().getName().equals(expectedPreference.getReward().getName()) &&
                        p.getReward().getDescription().equals(expectedPreference.getReward().getDescription())))
        );
    }

    private void parsingResultsShortValidFile1(ParsedData parsedData) throws ParseException {
        // given
        List<Result> results = parsedData.getResults();
        Quiz quiz = parsedData.getQuiz();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        List<Result> expectedResults = Arrays.asList(
                new Result(0,
                        quiz,
                        new Person(0, "Baweł"),
                        dateFormat.parse("09.11.2023  19:34:55"),
                        dateFormat.parse("09.11.2023  19:40:15"),
                        2,
                        new Reward()),
                new Result(1,
                        quiz,
                        new Person(1, "Daredevilq"),
                        dateFormat.parse("09.11.2023  19:35:03"),
                        dateFormat.parse("09.11.2023  19:41:21"),
                        2,
                        new Reward()),
                new Result(2,
                        quiz,
                        new Person(2, "Schmetterling"),
                        dateFormat.parse("09.11.2023  19:34:58"),
                        dateFormat.parse("09.11.2023  19:38:41"),
                        2,
                        new Reward())
        );

        // then
        assertEquals(3, results.size());

        expectedResults.forEach(expectedResult ->
                assertTrue(results.stream().anyMatch(r ->
                        r.getPerson().getName().equals(expectedResult.getPerson().getName()) &&
                                r.getStartDate().equals(expectedResult.getStartDate()) &&
                                r.getEndDate().equals(expectedResult.getEndDate()) &&
                                r.getScore() == expectedResult.getScore()))
        );
    }

    private void parsingPeopleShortValidFile2(ParsedData parsedData) {
        // given
        List<Person> people = parsedData.getPeople();

        List<Person> expectedPeople = Arrays.asList(
                new Person(0, "white hawk"),
                new Person(1, "Wściekły Kiwis"),
                new Person(2, "Aligator z Szafy")
        );

        // then
        assertEquals(3, people.size());

        expectedPeople.forEach(expectedPerson ->
                assertTrue(people.stream().anyMatch(p -> p.getName().equals(expectedPerson.getName())))
        );
    }

    private void parsingRewardsShortValidFile2(ParsedData parsedData) {
        // given
        List<Reward> rewards = parsedData.getRewards();

        List<Reward> expectedRewards = Arrays.asList(
                new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                new Reward(1, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                new Reward(2, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                new Reward(3, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"));

        // then
        assertEquals(4, rewards.size());

        expectedRewards.forEach(expectedReward ->
                assertTrue(rewards.stream().anyMatch(r -> r.getName().equals(expectedReward.getName()) &&
                        r.getDescription().equals(expectedReward.getDescription()))));
    }

    private void parsingQuizShortValidFile2(ParsedData parsedData) {
        // given
        Quiz quiz = parsedData.getQuiz();

        // then
        assertEquals(2, quiz.getMaxScore());
    }

    private void parsingPreferencesShortValidFile2(ParsedData parsedData) {
        // given
        List<Preference> preferences = parsedData.getPreferences();
        List<Reward> rewards = parsedData.getRewards();
        List<Person> people = parsedData.getPeople();
        Quiz quiz = parsedData.getQuiz();

        List<Preference> expectedPreferences = Arrays.asList(
                new Preference(
                        0,
                        quiz,
                        new Person(0, "white hawk"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "white hawk"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "white hawk"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "white hawk"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        3),
                new Preference(
                        0,
                        quiz,
                        new Person(0, "Wściekły Kiwis"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "Wściekły Kiwis"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "Wściekły Kiwis"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "Wściekły Kiwis"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        3),
                new Preference(
                        0,
                        quiz,
                        new Person(0, "Aligator z Szafy"),
                        new Reward(0, new RewardCategory(), "Rabat na sianko", "darmowa kartkówka"),
                        0),
                new Preference(
                        1,
                        quiz,
                        new Person(0, "Aligator z Szafy"),
                        new Reward(0, new RewardCategory(), "Marchewka lab", "+10% do lab"),
                        1),
                new Preference(
                        2,
                        quiz,
                        new Person(0, "Aligator z Szafy"),
                        new Reward(0, new RewardCategory(), "Weterynarz", "odrobienie 1 kartkówki"),
                        2),
                new Preference(
                        3,
                        quiz,
                        new Person(0, "Aligator z Szafy"),
                        new Reward(0, new RewardCategory(), "Lekarstwo", "odrobienie 2pkt lab"),
                        3)
        );

        // then
        assertEquals(people.size() * rewards.size(), preferences.size());

        expectedPreferences.forEach(expectedPreference ->
                assertTrue(preferences.stream().anyMatch(p -> p.getPriority() == expectedPreference.getPriority() &&
                        p.getPerson().getName().equals(expectedPreference.getPerson().getName()) &&
                        p.getReward().getName().equals(expectedPreference.getReward().getName()) &&
                        p.getReward().getDescription().equals(expectedPreference.getReward().getDescription())))
        );
    }

    private void parsingResultsShortValidFile2(ParsedData parsedData) throws ParseException {
        // given
        List<Result> results = parsedData.getResults();
        Quiz quiz = parsedData.getQuiz();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        List<Result> expectedResults = Arrays.asList(
                new Result(0,
                        quiz,
                        new Person(0, "white hawk"),
                        dateFormat.parse("09.11.2023  19:34:54"),
                        dateFormat.parse("09.11.2023  19:39:38"),
                        1,
                        new Reward()),
                new Result(1,
                        quiz,
                        new Person(1, "Wściekły Kiwis"),
                        dateFormat.parse("09.11.2023  19:35:08"),
                        dateFormat.parse("09.11.2023  19:39:42"),
                        1,
                        new Reward()),
                new Result(2,
                        quiz,
                        new Person(2, "Aligator z Szafy"),
                        dateFormat.parse("09.11.2023  19:34:58"),
                        dateFormat.parse("09.11.2023  19:37:47"),
                        0,
                        new Reward())
        );

        // then
        assertEquals(3, results.size());

        expectedResults.forEach(expectedResult ->
                assertTrue(results.stream().anyMatch(r ->
                        r.getPerson().getName().equals(expectedResult.getPerson().getName()) &&
                                r.getStartDate().equals(expectedResult.getStartDate()) &&
                                r.getEndDate().equals(expectedResult.getEndDate()) &&
                                r.getScore() == expectedResult.getScore()))
        );
    }
}

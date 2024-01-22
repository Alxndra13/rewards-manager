package majestatyczne.bestie.rewardsmanager.util;

import majestatyczne.bestie.rewardsmanager.RewardsManagerTestsConfiguration;
import majestatyczne.bestie.rewardsmanager.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FileServiceTests {

    @Autowired
    private RewardsManagerTestsConfiguration rewardsManagerTestsConfiguration;

    @Autowired
    private FileService fileService;

    @Test
    public void testLoadValidNotEmptyFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(rewardsManagerTestsConfiguration.getFullValidFilePath());
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "xlsx file",
                "test_file.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileInputStream
        );

        assertDoesNotThrow(() -> fileService.loadFile(mockMultipartFile));
    }

    @Test
    public void testLoadNotValidNotEmptyFile() {
        MultipartFile mockMultipartFile1 = new MockMultipartFile(
                "data",
                "test_file1.txt",
                "text/plain",
                "some data".getBytes()
        );

        assertThrows(IOException.class, () -> fileService.loadFile(mockMultipartFile1));

        MultipartFile mockMultipartFile2 = new MockMultipartFile(
                "data",
                "test_file2.7z",
                "application/x-7z-compressed",
                "dwedwedwefweifjwofhwrhgwehgurwehguiehrgurhguiwerihwruegherwuhig".getBytes()
        );

        assertThrows(IOException.class, () -> fileService.loadFile(mockMultipartFile2));
    }

    @Test
    public void testLoadValidEmptyFile() {
        byte[] emptyByteArray = {};

        MultipartFile mockMultipartFile1 = new MockMultipartFile(
                "xlsx file",
                "test_file.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                emptyByteArray
        );

        assertThrows(IOException.class, () -> fileService.loadFile(mockMultipartFile1));
    }

    @Test
    public void testLoadNotValidEmptyFile() {
        byte[] emptyByteArray = {};

        MultipartFile mockMultipartFile1 = new MockMultipartFile(
                "data",
                "test_file1.txt",
                "text/plain",
                emptyByteArray
        );

        assertThrows(IOException.class, () -> fileService.loadFile(mockMultipartFile1));
    }
}
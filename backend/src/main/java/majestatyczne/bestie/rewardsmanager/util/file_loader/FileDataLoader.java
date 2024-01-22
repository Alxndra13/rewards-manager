package majestatyczne.bestie.rewardsmanager.util.file_loader;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataLoader {

    void loadData(MultipartFile multipartFile) throws IOException;
}

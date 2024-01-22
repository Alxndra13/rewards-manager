package majestatyczne.bestie.rewardsmanager.util.file_creator;

import java.io.IOException;
import java.util.List;

public interface FileCreator {

    byte[] createFileWithTable(List<List<String>> rows, List<Integer> rowsToHighlight) throws IOException;
}

package majestatyczne.bestie.frontend.service;

import majestatyczne.bestie.frontend.Constants;
import majestatyczne.bestie.frontend.util.SaveFileException;
import okhttp3.ResponseBody;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.*;
import java.util.Objects;

public class FileService {

    public int uploadFile(File file) {
        HttpClient httpClient = HttpClients.createDefault();
        String url = Constants.BASE_SERVER_URL + "files";
        HttpPost httpPost = new HttpPost(url);

        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        httpPost.setEntity(getEntity(fileBody));

        try {
            HttpResponse response;
            response = httpClient.execute(httpPost);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return HttpStatus.SC_BAD_REQUEST;
        }

    }

    private HttpEntity getEntity(FileBody fileBody) {
        return MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .build();
    }

    public int exportResultsFile(int quizId, String format) throws SaveFileException {
        APIService service = getAPIService();

        Call<ResponseBody> call = service.exportResultsFile(quizId, format);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String fileName = getFileName(response);
                writeFileToDisk(response.body(), fileName);
            }
            return response.code();
        } catch (IOException e) {
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

    private String getFileName(Response<ResponseBody> response) {
        return Objects.requireNonNull(response.headers().get("Content-Disposition"))
                .replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
    }

    private void writeFileToDisk(ResponseBody body, String fileName) throws SaveFileException {
        File file = new File(System.getProperty("user.home") + "/Downloads/" + fileName);
        try {
            byte[] fileReader = new byte[4096];
            InputStream inputStream = body.byteStream();
            OutputStream outputStream = new FileOutputStream(file);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new SaveFileException(Constants.SAVE_EXPORT_FILE_EXCEPTION);
        }
    }

    private APIService getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_SERVER_URL)
                .build();
        return retrofit.create(APIService.class);
    }
}

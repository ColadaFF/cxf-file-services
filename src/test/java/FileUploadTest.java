import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class FileUploadTest {
    private static final String BASE_URL = "http://localhost:8080/Files/single/";
    private static CloseableHttpClient client;

    @BeforeClass
    public static void createClient() {
        client = HttpClients.createDefault();
    }

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Test
    public void whenUploadingFile_thenReceiveOkResponse() throws IOException {
        final HttpPost httpPost = new HttpPost(BASE_URL);
        File fileTest = new File(getClass().getClassLoader().getResource("test_image.jpg").getFile());
        FileBody fileBody = new FileBody(fileTest, ContentType.DEFAULT_BINARY, "test_image.jpg");
        StringBody idBody = new StringBody("123", ContentType.MULTIPART_FORM_DATA);


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("upfile", fileBody);
        builder.addPart("id", idBody);
        HttpEntity entity = builder.build();

        httpPost.setHeader("Content-Type", "multipart/form-data");
        httpPost.setEntity(entity);

        final HttpResponse response = client.execute(httpPost);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("{\"status\": \"Copied!\", \"type\": \"Imagen\"}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void whenUploadingInvalidFile_thenReceiveInvalidRequestResponse() throws IOException {
        final HttpPost httpPost = new HttpPost(BASE_URL);
        File fileTest = new File(getClass().getClassLoader().getResource("test.txt").getFile());
        FileBody fileBody = new FileBody(fileTest, ContentType.DEFAULT_BINARY, "test.txt");
        StringBody idBody = new StringBody("456", ContentType.MULTIPART_FORM_DATA);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("upfile", fileBody);
        builder.addPart("id", idBody);
        HttpEntity entity = builder.build();

        httpPost.setHeader("Content-Type", "multipart/form-data");
        httpPost.setEntity(entity);

        final HttpResponse response = client.execute(httpPost);
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertEquals("{\"reason\": \"Extensión inválida!\"}", EntityUtils.toString(response.getEntity()));
    }

}

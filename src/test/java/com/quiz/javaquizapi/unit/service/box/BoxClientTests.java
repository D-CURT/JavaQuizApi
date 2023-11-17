package com.quiz.javaquizapi.unit.service.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIRequest;
import com.box.sdk.BoxJSONRequest;
import com.box.sdk.BoxJSONResponse;
import com.box.sdk.RequestInterceptor;
import com.box.sdk.http.HttpMethod;
import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.service.box.BoxClient;
import com.quiz.javaquizapi.service.box.QuizBoxClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Box service tests")
@ExtendWith(MockitoExtension.class)
public class BoxClientTests extends ApiTests {
    public static final String TEST_JSON = "{\"id\": \"1\", \"entries\": [{\"id\": \"1\", \"type\": \"file\"}]}";
    public static final String UPLOAD_URL = "https://upload.box.com/api/2.0/";
    public static final String TEST_URL = "https://box-api-test/v2/";
    public static final String ACCESS_TOKEN = "test";
    public static final String TEST_ID = "123";
    @Mock
    private RequestInterceptor interceptor;
    @Mock
    private BoxJSONResponse response;
    @Spy
    private BoxAPIConnection api = new BoxAPIConnection(ACCESS_TOKEN);
    private BoxClient client;

    @BeforeEach
    void setUp() {
        initLog();
        var requestCaptor = ArgumentCaptor.forClass(BoxJSONRequest.class);
        when(api.getBaseURL()).thenReturn(TEST_URL);
        when(api.getMaxRetryAttempts()).thenReturn(1);
        when(api.getRequestInterceptor()).thenReturn(interceptor);
        when(interceptor.onRequest(any(BoxJSONRequest.class))).thenReturn(response);
        when(response.getJSON()).thenReturn(TEST_JSON);
        client = QuizBoxClient.of(api).init();
        verify(interceptor, times(2)).onRequest(requestCaptor.capture());
        var request = requestCaptor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.getUrl().toString())
                .isEqualTo(UriComponentsBuilder.fromHttpUrl(TEST_URL)
                        .pathSegment("folders")
                        .build()
                        .toString());
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST.toString());
        assertThat(captureLogs()).contains("Box client initialized.");
    }

    @Test
    @DisplayName("Download box file")
    public void testDownloadingBoxFileGivenValidData() {
        when(response.getBody()).thenReturn(new ByteArrayInputStream(new byte[]{}));
        when(interceptor.onRequest(any(BoxAPIRequest.class))).thenReturn(response);
        client.download(TEST_ID);
        var requestCaptor = ArgumentCaptor.forClass(BoxAPIRequest.class);
        verify(interceptor, times(3)).onRequest(requestCaptor.capture());
        var request = requestCaptor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.getUrl().toString())
                .isEqualTo(UriComponentsBuilder.fromHttpUrl(TEST_URL)
                        .pathSegment("files", TEST_ID, "content")
                        .build()
                        .toString());
        assertThat(captureLogs()).contains(
                "Downloading a Box file with ID '123'...",
                "File download succeeded.");
    }

    @Test
    @DisplayName("Upload box file")
    public void testUploadingBoxFileGivenValidData() {
        var file = new MockMultipartFile("test.pdf", "content".getBytes());
        when(response.getJSON()).thenReturn(TEST_JSON);
        when(interceptor.onRequest(any(BoxAPIRequest.class))).thenReturn(response);
        client.upload(file);
        var requestCaptor = ArgumentCaptor.forClass(BoxAPIRequest.class);
        verify(interceptor, times(3)).onRequest(requestCaptor.capture());
        var request = requestCaptor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.getUrl().toString())
                .isEqualTo(UriComponentsBuilder.fromHttpUrl(UPLOAD_URL)
                        .pathSegment("files", "content")
                        .build()
                        .toString());
        assertThat(captureLogs()).contains("Uploading a Box file...");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}

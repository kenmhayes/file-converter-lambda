package converter;

import static org.mockito.Mockito.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import converter.accessor.S3Accessor;
import converter.accessor.SQSAccessor;
import converter.dagger.HandlerComponent;

import java.io.File;

import converter.filehandler.FileHandler;
import converter.filehandler.FileHandlerProvider;
import converter.model.ConversionStatus;
import converter.utils.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;

public class LambdaHandlerTest {
    private static String TEST_BUCKET = "my-bucket";
    private static String TEST_OBJECT_KEY = "original/png/myfile.jpg";
    private static S3Event TEST_EVENT = new S3Event(Arrays.asList(
            new S3EventNotification.S3EventNotificationRecord(
                    "us-east-1",
                    "Put",
                    "S3",
                    "2023-06-13T12:55:15.240-04:00",
                    "1",
                    null,
                    null,
                    new S3EventNotification.S3Entity(
                            "",
                            new S3EventNotification.S3BucketEntity(TEST_BUCKET, null, ""),
                            new S3EventNotification.S3ObjectEntity(TEST_OBJECT_KEY, 1024L, "tag", "1.0", ""),
                            ""
                    ),
                    null
            )
    ));
    ;
    private LambdaHandler lambdaHandler;
    private MockedStatic mockHandlerComponent;
    private S3Accessor mockS3Accessor;
    private SQSAccessor mockSQSAccessor;
    private Context mockContext;
    private LambdaLogger mockLambdaLogger;
    private FileHandlerProvider mockFileHandlerProvider;
    private FileHandler mockFileHandler;

    @BeforeEach
    public void setupTests() {
        this.mockHandlerComponent = mockStatic(HandlerComponent.class);
        HandlerComponent mockDaggerHandlerComponent = mock(HandlerComponent.class);
        this.mockS3Accessor = mock(S3Accessor.class);
        this.mockSQSAccessor = mock(SQSAccessor.class);
        this.mockContext = mock(Context.class);
        this.mockLambdaLogger = mock(LambdaLogger.class);
        this.mockFileHandlerProvider = mock(FileHandlerProvider.class);
        this.mockFileHandler = mock(FileHandler.class);

        when(mockDaggerHandlerComponent.s3Accessor()).thenReturn(mockS3Accessor);
        when(mockDaggerHandlerComponent.sqsAccessor()).thenReturn(mockSQSAccessor);
        when(mockDaggerHandlerComponent.fileHandlerProvider()).thenReturn(mockFileHandlerProvider);
        when(HandlerComponent.create()).thenReturn(mockDaggerHandlerComponent);
        when(mockContext.getLogger()).thenReturn(mockLambdaLogger);
        when(mockFileHandlerProvider.getFileHandler(anyString(), anyString())).thenReturn(mockFileHandler);

        this.lambdaHandler = new LambdaHandler();
    }

    @AfterEach
    public void teardownTests() {
        // Need to close static mocks before setup is run again
        this.mockHandlerComponent.close();
    }

    @Test
    public void handleRequest_DefaultConstructor_InjectsWithDagger() {
        mockHandlerComponent.verify(() -> HandlerComponent.create());
    }

    @Test
    public void handleRequest_S3Event_PutsObject() throws IOException {
        File downloadedFile = FileUtils.createTempFile("myfile.jpg");
        File convertedFile = FileUtils.createTempFile("myfile.png");

        this.lambdaHandler.handleRequest(TEST_EVENT, mockContext);

        verify(mockS3Accessor).getObject(TEST_BUCKET, TEST_OBJECT_KEY, downloadedFile);
        verify(mockFileHandler).convert(downloadedFile, convertedFile);
        verify(mockS3Accessor).putObject(TEST_BUCKET, "converted/myfile.png", convertedFile);
        verify(mockSQSAccessor).sendStatusUpdateMessage("myfile", ConversionStatus.STARTED);
        verify(mockSQSAccessor).sendStatusUpdateMessage("myfile", ConversionStatus.COMPLETED);
    }

    @Test
    public void handleRequest_ExceptionOccurs_LogsError() throws IOException {
        String errorMessage = "This is an error";
        doThrow(new IOException(errorMessage)).when(mockS3Accessor).getObject(anyString(), anyString(), any(File.class));
        this.lambdaHandler.handleRequest(TEST_EVENT, mockContext);

        verify(mockLambdaLogger).log(errorMessage);
        verify(mockSQSAccessor).sendStatusUpdateMessage("myfile", ConversionStatus.FAILED_NO_RETRY);
    }
}

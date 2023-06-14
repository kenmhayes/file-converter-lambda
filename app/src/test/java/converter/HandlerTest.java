package converter;

import static org.mockito.Mockito.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import converter.accessor.S3Accessor;
import converter.dagger.HandlerComponent;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;

public class HandlerTest {
    private static String TEST_BUCKET = "my-bucket";
    private static String TEST_OBJECT_KEY = "original/myfile.txt";
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
    private Handler handler;
    private MockedStatic mockHandlerComponent;
    private S3Accessor mockS3Accessor;
    private Context mockContext;
    private LambdaLogger mockLambdaLogger;

    @BeforeEach
    public void setupTests() {
        this.mockHandlerComponent = mockStatic(HandlerComponent.class);
        HandlerComponent mockDaggerHandlerComponent = mock(HandlerComponent.class);
        this.mockS3Accessor = mock(S3Accessor.class);
        this.mockContext = mock(Context.class);
        this.mockLambdaLogger = mock(LambdaLogger.class);

        when(mockDaggerHandlerComponent.s3Accessor()).thenReturn(mockS3Accessor);
        when(HandlerComponent.create()).thenReturn(mockDaggerHandlerComponent);
        when(mockContext.getLogger()).thenReturn(mockLambdaLogger);

        this.handler = new Handler();
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
        File tempFile = new File(TEST_BUCKET, TEST_OBJECT_KEY);

        when(mockS3Accessor.getObject(TEST_BUCKET, TEST_OBJECT_KEY)).thenReturn(tempFile);
        this.handler.handleRequest(TEST_EVENT, mockContext);

        verify(mockS3Accessor).putObject(TEST_BUCKET, "converted/" + tempFile.getName(), tempFile);

        tempFile.delete();
    }

    @Test
    public void handleRequest_ExceptionOccurs_LogsError() throws IOException {
        String errorMessage = "This is an error";
        when(mockS3Accessor.getObject(anyString(), anyString())).thenThrow(new IOException(errorMessage));
        this.handler.handleRequest(TEST_EVENT, mockContext);

        verify(mockLambdaLogger).log(errorMessage);
    }
}

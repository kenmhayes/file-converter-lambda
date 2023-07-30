package converter.accessor;

import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import converter.model.ConversionStatus;
import org.junit.jupiter.api.BeforeEach;

import com.amazonaws.services.sqs.AmazonSQS;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SQSAccessorImplTest {
    private static String QUEUE_NAME = "QueueName";
    private static String QUEUE_URL = "test.url";
    private SQSAccessorImpl impl;
    private AmazonSQS mockAmazonSQS;
    private GetQueueUrlResult mockQueueUrlResult;

    @BeforeEach
    public void setupTests() {
        this.mockAmazonSQS = mock(AmazonSQS.class);
        this.impl = new SQSAccessorImpl(this.mockAmazonSQS, QUEUE_NAME);
        this.mockQueueUrlResult = mock(GetQueueUrlResult.class);
        when(this.mockQueueUrlResult.getQueueUrl()).thenReturn(QUEUE_URL);
        when(this.mockAmazonSQS.getQueueUrl(anyString())).thenReturn(this.mockQueueUrlResult);
    }

    @Test
    public void sendStatusUpdateMessage_withValidInput_callsAPI() {
        String conversionId = "12345";
        ConversionStatus conversionStatus = ConversionStatus.STARTED;
        this.impl.sendStatusUpdateMessage(conversionId, conversionStatus);

        verify(this.mockAmazonSQS).sendMessage(
                new SendMessageRequest()
                        .withQueueUrl(QUEUE_URL)
                        .withMessageBody(conversionId + "|" + conversionStatus));
    }
}

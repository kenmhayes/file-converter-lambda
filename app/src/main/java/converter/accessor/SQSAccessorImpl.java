package converter.accessor;

import converter.model.ConversionStatus;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import javax.inject.Inject;
import javax.inject.Named;

public class SQSAccessorImpl implements SQSAccessor {
    private AmazonSQS sqsClient;
    private String queueName;

    @Inject
    public SQSAccessorImpl(AmazonSQS sqsClient, @Named("QueueName") String queueName) {
        this.sqsClient = sqsClient;
        this.queueName = queueName;
    }

    @Override
    public void sendStatusUpdateMessage(String conversionId, ConversionStatus status) {
        String url = sqsClient.getQueueUrl(this.queueName).getQueueUrl();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(url)
                .withMessageBody(conversionId + "|" + status.name());

        sqsClient.sendMessage(send_msg_request);
    }
}

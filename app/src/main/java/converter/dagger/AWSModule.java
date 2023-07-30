package converter.dagger;

import converter.accessor.S3Accessor;
import converter.accessor.S3AccessorImpl;
import converter.accessor.SQSAccessor;
import converter.accessor.SQSAccessorImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.inject.Named;

/**
 * Dagger module that provides classes needed for AWS S3 functionality
 */
@Module
abstract class AWSModule {
    @Provides
    public static S3TransferManager s3TransferManager() {
        return S3TransferManager.create();
    }

    @Binds
    public abstract S3Accessor s3Accessor(S3AccessorImpl impl);

    @Provides
    public static AmazonSQS amazonSQS() { return AmazonSQSClientBuilder.defaultClient(); }

    @Provides
    @Named("QueueName")
    public static String queueName() { return System.getenv().get("QueueName"); }

    @Binds
    public abstract SQSAccessor sqsAccessor(SQSAccessorImpl impl);
}

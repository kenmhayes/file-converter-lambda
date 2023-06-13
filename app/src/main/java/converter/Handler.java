package converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import converter.accessor.S3Accessor;
import converter.accessor.S3AccessorImpl;

import java.io.File;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String> {
  @Override
  public String handleRequest(S3Event s3event, Context context) {
      LambdaLogger logger = context.getLogger();
      S3Accessor s3Accessor = new S3AccessorImpl(S3TransferManager.create());
      S3EventNotificationRecord record = s3event.getRecords().get(0);
      String srcBucket = record.getS3().getBucket().getName();
      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();

      try {
          File downloadedFile = s3Accessor.getObject(srcBucket, srcKey);

          s3Accessor.putObject(srcBucket, "converted/" + downloadedFile.getName(), downloadedFile);
      } catch (Exception e) {
        logger.log(e.getMessage());
      }

      return "Ok";
  }
}
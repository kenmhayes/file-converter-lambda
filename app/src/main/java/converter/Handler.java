package converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import converter.accessor.S3Accessor;
import converter.dagger.HandlerComponent;

import java.io.File;

/**
 * Able to process AWS S3 Lambda requests
 * Converts a file uploaded to S3 to another file type
 * Places the converted file in the 'converted/' folder with the same name
 */
public class Handler implements RequestHandler<S3Event, String> {
    private S3Accessor s3Accessor;

    public Handler() {
        HandlerComponent handlerComponent = HandlerComponent.create();
        this.s3Accessor = handlerComponent.s3Accessor();
    }

    @Override
    public String handleRequest(S3Event s3event, Context context) {
      LambdaLogger logger = context.getLogger();
      S3EventNotificationRecord record = s3event.getRecords().get(0);
      String srcBucket = record.getS3().getBucket().getName();
      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();

      try {
          File downloadedFile = this.s3Accessor.getObject(srcBucket, srcKey);

          String convertedObjectKey = "converted/" + downloadedFile.getName();
          this.s3Accessor.putObject(srcBucket, convertedObjectKey, downloadedFile);
      } catch (Exception e) {
        logger.log(e.getMessage());
      }

      return "Ok";
    }
}
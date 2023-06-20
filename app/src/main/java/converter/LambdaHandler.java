package converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.google.common.io.Files;
import converter.accessor.S3Accessor;
import converter.dagger.HandlerComponent;
import converter.filehandler.FileHandler;
import converter.filehandler.FileHandlerProvider;
import converter.utils.FileUtils;

import java.io.File;

/**
 * Able to process AWS S3 Lambda requests
 * Converts a file uploaded to S3 to another file type
 * Places the converted file in the 'converted/' folder with the same name
 */
public class LambdaHandler implements RequestHandler<S3Event, String> {
    private S3Accessor s3Accessor;
    private FileHandlerProvider fileHandlerProvider;

    public LambdaHandler() {
        HandlerComponent handlerComponent = HandlerComponent.create();
        this.s3Accessor = handlerComponent.s3Accessor();
        this.fileHandlerProvider = handlerComponent.fileHandlerProvider();
    }

    @Override
    public String handleRequest(S3Event s3event, Context context) {
      LambdaLogger logger = context.getLogger();
      S3EventNotificationRecord record = s3event.getRecords().get(0);

      String srcBucket = record.getS3().getBucket().getName();
      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();

      // Objects will be in the format of 'original/[CONVERTED_FILE_TYPE]/filename.ext'
      String[] splitByFolders = srcKey.split("/");
      String fileName = splitByFolders[2];
      String originalFileFormat = Files.getFileExtension(splitByFolders[2]);
      String convertFileFormat = splitByFolders[1];
      String convertedFileName = Files.getNameWithoutExtension(fileName) + "." + convertFileFormat;
      String convertedObjectKey = "converted/" + convertedFileName;

      try {
          FileHandler fileHandler = this.fileHandlerProvider.getFileHandler(originalFileFormat, convertFileFormat);

          File downloadedFile = FileUtils.createTempFile(fileName);
          File convertedFile = FileUtils.createTempFile(convertedFileName);

          this.s3Accessor.getObject(srcBucket, srcKey, downloadedFile);

          fileHandler.convert(downloadedFile, convertedFile);

          this.s3Accessor.putObject(srcBucket, convertedObjectKey, convertedFile);
      } catch (Exception e) {
        logger.log(e.getMessage());
      }

      return "Ok";
    }
}
package converter.accessor;

import java.io.File;
import java.io.IOException;

/**
 * Provides access to AWS S3 APIs
 */
public interface S3Accessor {
    /**
     * Gets an object from a S3 bucket and stores it in a local File
     * @param bucketName S3 bucket name
     * @param objectKey S3 object key
     * @param outputFile File to save data to
     * @throws IOException
     */
    public void getObject(String bucketName, String objectKey, File outputFile) throws IOException;

    /**
     * Puts an object from a local File into S3
     * @param bucketName S3 bucket name
     * @param objectKey S3 object key
     * @param file File with data to upload
     * @throws IOException
     */
    public void putObject(String bucketName, String objectKey, File file) throws IOException;
}

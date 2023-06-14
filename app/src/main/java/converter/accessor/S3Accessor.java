package converter.accessor;

import java.io.File;
import java.io.IOException;

/**
 * Provides access to AWS S3 APIs
 */
public interface S3Accessor {
    /**
     * Gets an object from a S3 bucket and stores it in a local File
     * File is stored in the temporary directory
     * @param bucketName S3 bucket name
     * @param objectKey S3 object key
     * @return File with data downloaded from S3
     * @throws IOException
     */
    public File getObject(String bucketName, String objectKey) throws IOException;

    /**
     * Puts an object from a local File into S3
     * @param bucketName S3 bucket name
     * @param objectKey S3 object key
     * @param file File with data to upload
     * @throws IOException
     */
    public void putObject(String bucketName, String objectKey, File file) throws IOException;
}

package converter.accessor;

import java.io.File;
import java.io.IOException;

public interface S3Accessor {
    public File getObject(String bucketName, String objectKey) throws IOException;

    public void putObject(String bucketName, String objectKey, File file) throws IOException;
}

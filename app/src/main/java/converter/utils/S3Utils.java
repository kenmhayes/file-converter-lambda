package converter.utils;

/**
 * Utilities useful when working with S3 objects
 */
public class S3Utils {
    /**
     * Given a S3 object key, extracts the filename by removing any prefixes
     * i.e. 'prefix1/myfile.txt' becomes 'myfile.txt'
     * @param s3ObjectKey a key that may contain prefixes
     * @return filename including any file extension
     */
    public static String getFileNameFromKey(String s3ObjectKey) {
        String[] splitByFolders = s3ObjectKey.split("/");
        return splitByFolders[splitByFolders.length - 1];
    }
}

package converter.utils;

public class S3Utils {
    public static String getFileNameFromKey(String s3ObjectKey) {
        String[] splitByFolders = s3ObjectKey.split("/");
        return splitByFolders[splitByFolders.length - 1];
    }
}

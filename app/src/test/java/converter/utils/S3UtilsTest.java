package converter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class S3UtilsTest {
    @Test
    public void getFileNameFromKey_emptyString_expectEmptyString() {
        assertEquals("", S3Utils.getFileNameFromKey(""));
    }

    @Test
    public void getFileNameFromKey_noFolders_expectNameToEqualKey() {
        String key = "myfile.txt";
        assertEquals(key, key);
    }

    @Test
    public void getFileNameFromKey_oneFolder_expectNameAfterFolder() {
        assertEquals("myfile.txt", S3Utils.getFileNameFromKey("folder1/myfile.txt"));
    }

    @Test
    public void getFileNameFromKey_multipleFolder_expectNameAfterAllFolders() {
        assertEquals("myfile.txt", S3Utils.getFileNameFromKey("folder1/folder2/folder3/myfile.txt"));
    }
}

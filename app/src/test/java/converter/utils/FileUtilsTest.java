package converter.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsTest {

    @Test
    public void createTempFile_NonemptyName_CreatesFile() throws IOException {
        String fileName = "test.txt";
        File output = FileUtils.createTempFile(fileName);
        assertEquals(System.getProperty("java.io.tmpdir") + "/" + fileName, output.getPath());
        assertEquals(true, output.exists());
    }
}

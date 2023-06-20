package converter.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static File createTempFile(String fileName) throws IOException {
        String temporaryDirectory = System.getProperty("java.io.tmpdir");
        File output = new File(temporaryDirectory, fileName);
        output.createNewFile();
        output.deleteOnExit();
        return output;
    }
}

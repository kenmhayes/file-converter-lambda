package converter.filehandler;

import java.io.File;
import java.io.IOException;

public interface FileHandler {

    public boolean canConvert(String inputFileFormat, String outputFileFormat);

    public void convert(File inputFile, File outputFile)
            throws IOException;
}

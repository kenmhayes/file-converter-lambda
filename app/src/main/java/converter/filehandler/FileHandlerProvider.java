package converter.filehandler;

import javax.inject.Inject;
import java.util.Set;

public class FileHandlerProvider {
    Set<FileHandler> fileHandlers;

    @Inject
    public FileHandlerProvider(Set<FileHandler> fileHandlers) {
        this.fileHandlers = fileHandlers;
    }

    public FileHandler getFileHandler(String inputFileFormat, String outputFileFormat) {
        for (FileHandler fileHandler : this.fileHandlers) {
            if (fileHandler.canConvert(inputFileFormat, outputFileFormat)) {
                return fileHandler;
            }
        }
        return null;
    }
}

package converter.filehandler;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileHandlerProviderTest {
    private static String INPUT_FORMAT = "jpg";
    private static String OUTPUT_FORMAT = "png";
    private FileHandlerProvider fileHandlerProvider;
    private FileHandler mockFileHandler1;
    private FileHandler mockFileHandler2;

    @BeforeEach
    public void setupTests() {
        mockFileHandler1 = mock(FileHandler.class);
        mockFileHandler2 = mock(FileHandler.class);
    }

    @Test
    public void getFileHandler_SingleHandlerCanConvert_GetsThatHandler() {
        fileHandlerProvider = new FileHandlerProvider(Sets.newHashSet(mockFileHandler1, mockFileHandler2));

        when(mockFileHandler1.canConvert(INPUT_FORMAT, OUTPUT_FORMAT)).thenReturn(true);
        when(mockFileHandler2.canConvert(INPUT_FORMAT, OUTPUT_FORMAT)).thenReturn(false);

        assertEquals(mockFileHandler1, fileHandlerProvider.getFileHandler(INPUT_FORMAT, OUTPUT_FORMAT));
    }

    @Test
    public void getFileHandler_NoHandlerCanConvert_ReturnsNull() {
        fileHandlerProvider = new FileHandlerProvider(Sets.newHashSet(mockFileHandler1, mockFileHandler2));

        when(mockFileHandler1.canConvert(INPUT_FORMAT, OUTPUT_FORMAT)).thenReturn(false);
        when(mockFileHandler2.canConvert(INPUT_FORMAT, OUTPUT_FORMAT)).thenReturn(false);

        assertEquals(null, fileHandlerProvider.getFileHandler(INPUT_FORMAT, OUTPUT_FORMAT));
    }

    @Test
    public void getFileHandler_EmptySet_ReturnsNull() {
        fileHandlerProvider = new FileHandlerProvider(Sets.newHashSet());

        assertEquals(null, fileHandlerProvider.getFileHandler(INPUT_FORMAT, OUTPUT_FORMAT));
    }
}

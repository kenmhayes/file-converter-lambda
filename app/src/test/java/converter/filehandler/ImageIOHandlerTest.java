package converter.filehandler;

import converter.utils.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ImageIOHandlerTest {
    private ImageIOHandler imageIOHandler;
    private MockedStatic mockImageIO;
    private BufferedImage mockBufferedImage;

    @BeforeEach
    public void setupTests() {

        imageIOHandler = new ImageIOHandler();
        mockImageIO = mockStatic(ImageIO.class);
        mockBufferedImage = mock(BufferedImage.class);
    }

    @AfterEach
    public void teardownTests() {
        // Need to close static mocks before setup is run again
        this.mockImageIO.close();
    }

    @Test
    public void canConvert_ValidTypes_ReturnsTrue() {
        assertEquals(true, imageIOHandler.canConvert("jpg", "png"));
    }

    @Test
    public void canConvert_InvalidTypes_ReturnsFalse() {
        assertEquals(false, imageIOHandler.canConvert("foo", "bar"));
    }

    @Test
    public void canConvert_InvalidInputType_ReturnsFalse() {
        assertEquals(false, imageIOHandler.canConvert("foo", "png"));
    }

    @Test
    public void canConvert_InvalidOutputType_ReturnsFalse() {
        assertEquals(false, imageIOHandler.canConvert("jpg", "bar"));
    }

    @Test
    public void convert_JpgToPng_ConvertsSuccessfully() throws IOException {
        File inputFile = FileUtils.createTempFile("myfile.jpg");
        File outputFile = FileUtils.createTempFile("myfile.png");

        when(ImageIO.read(any(InputStream.class))).thenReturn(mockBufferedImage);

        imageIOHandler.convert(inputFile, outputFile);
        mockImageIO.verify(
                () -> ImageIO.write(any(BufferedImage.class), argThat(s -> s.equals("png")), any(OutputStream.class)),
                times(1)
        );
    }
}

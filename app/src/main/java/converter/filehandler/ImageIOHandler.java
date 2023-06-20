package converter.filehandler;

import com.google.common.collect.Sets;
import com.google.common.io.Files;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.inject.Inject;

public class ImageIOHandler implements FileHandler {
    private Set<String> supportedInputFormats;
    private Set<String> supportedOutputFormats;

    @Inject
    public ImageIOHandler() {
        supportedInputFormats = Sets.newHashSet(ImageIO.getReaderFormatNames());
        supportedOutputFormats = Sets.newHashSet(ImageIO.getWriterFormatNames());
    }

    public boolean canConvert(String inputFileFormat, String outputFileFormat) {
        return supportedInputFormats.contains(inputFileFormat.toLowerCase())
                && supportedOutputFormats.contains(outputFileFormat.toLowerCase());
    }

    public void convert(File inputFile, File outputFile)
            throws IOException {
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);

        // writes to the output image in specified format
        ImageIO.write(inputImage, Files.getFileExtension(outputFile.getName()), outputStream);

        // needs to close the streams
        outputStream.close();
        inputStream.close();
    }
}

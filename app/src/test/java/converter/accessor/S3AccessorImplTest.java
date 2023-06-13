package converter.accessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class S3AccessorImplTest {
    private S3AccessorImpl s3Accessor;

    private S3TransferManager mockTransferManager;
    private FileDownload mockFileDownload;
    private FileUpload mockFileUpload;

    @BeforeEach
    public void setupTest() {
        mockTransferManager = mock(S3TransferManager.class);
        mockFileDownload = mock(FileDownload.class);
        mockFileUpload = mock(FileUpload.class);

        when(mockTransferManager.downloadFile(any(DownloadFileRequest.class))).thenReturn(mockFileDownload);
        when(mockTransferManager.uploadFile(any(UploadFileRequest.class))).thenReturn(mockFileUpload);
        when(mockFileDownload.completionFuture()).thenReturn(mock(CompletableFuture.class));
        when(mockFileUpload.completionFuture()).thenReturn(mock(CompletableFuture.class));

        this.s3Accessor = new S3AccessorImpl(mockTransferManager);
    }

    @Test
    public void getObject_ReturnsFile() throws IOException {
        String bucketName = "bucket";
        String objectKey = "original/key.txt";

        File output = s3Accessor.getObject(bucketName, objectKey);

        assertEquals("key.txt", output.getName());

        output.delete();
    }

    @Test
    public void getObject_CallsDownloadAPI() throws IOException {
        String bucketName = "bucket";
        String objectKey = "key.txt";

        File output = s3Accessor.getObject(bucketName, objectKey);

        verify(mockTransferManager).downloadFile(
                DownloadFileRequest.builder()
                .getObjectRequest(builder -> builder.bucket(bucketName).key(objectKey))
                .destination(output)
                .build());

        output.delete();
    }

    @Test
    public void putObject_CallsUploadAPI() throws IOException {
        String bucketName = "bucket";
        String objectKey = "key.txt";
        File tempFile = File.createTempFile(bucketName, objectKey);

        s3Accessor.putObject(bucketName, objectKey, tempFile);

        verify(mockTransferManager).uploadFile(
                UploadFileRequest.builder()
                        .putObjectRequest(builder -> builder.bucket(bucketName).key(objectKey))
                        .source(tempFile)
                        .build());

        tempFile.delete();
    }
}

package converter.accessor;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileUpload;

public class S3AccessorImpl implements S3Accessor {
    private S3TransferManager transferManager;
    @Inject
    public S3AccessorImpl(S3TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    public void getObject(String bucketName, String objectKey, File outputFile) throws IOException {
        DownloadFileRequest downloadFileRequest =
                DownloadFileRequest.builder()
                        .getObjectRequest(builder -> builder.bucket(bucketName).key(objectKey))
                        .destination(outputFile)
                        .build();

        FileDownload download = transferManager.downloadFile(downloadFileRequest);

        // Wait for the transfer to complete
        download.completionFuture().join();
    }

    public void putObject(String bucketName, String objectKey, File file) throws IOException {
        UploadFileRequest uploadFileRequest =
                UploadFileRequest.builder()
                        .putObjectRequest(builder -> builder.bucket(bucketName).key(objectKey))
                        .source(file)
                        .build();

        FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

        // Wait for the transfer to complete
        fileUpload.completionFuture().join();
    }
}

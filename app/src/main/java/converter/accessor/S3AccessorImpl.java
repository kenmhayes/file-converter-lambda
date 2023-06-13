package converter.accessor;

import converter.utils.S3Utils;

import java.io.File;
import java.io.IOException;

import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileUpload;

public class S3AccessorImpl implements S3Accessor {
    private S3TransferManager transferManager;
    public S3AccessorImpl(S3TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    public File getObject(String bucketName, String objectKey) throws IOException {
        String temporaryDirectory = System.getProperty("java.io.tmpdir");
        File tempFile = new File(temporaryDirectory, S3Utils.getFileNameFromKey(objectKey));

        DownloadFileRequest downloadFileRequest =
                DownloadFileRequest.builder()
                        .getObjectRequest(builder -> builder.bucket(bucketName).key(objectKey))
                        .destination(tempFile)
                        .build();

        FileDownload download = transferManager.downloadFile(downloadFileRequest);

        // Wait for the transfer to complete
        download.completionFuture().join();

        return tempFile;
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

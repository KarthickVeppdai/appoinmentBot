package com.ChatBot.ChatBot.miniIO_util;


import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class PdfUploaderService {

    @Autowired
    public MinioClient minioClient;

    public void uploadPdfFromUrl(String fileUrl, String bucketName, String objectName) throws Exception {
        // Open connection to the PDF URL
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(fileUrl);
        CloseableHttpResponse response = httpClient.execute(getRequest);
        InputStream inputStream = response.getEntity().getContent();
        // Upload to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, response.getEntity().getContentLength(), -1)
                        .contentType("application/pdf")
                        .build()
        );
    }

    public InputStream getPdfFromMinio (String bucketName, String objectName) throws Exception {

        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public boolean checkIfObjectExists(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true; // Object exists
        } catch (MinioException e) {
            return false; // Object doesn't exist or other error
        } catch (Exception e) {
            return false;
        }
    }
}

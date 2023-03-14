package io.github.gafarrell.shared;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

public class S3Driver {
    private final S3Client s3Client = DependencyHandler.S3Client();

    public List<S3Object> listObjects(String bucket){
        ListObjectsRequest request = ListObjectsRequest.builder().bucket(bucket).build();
        ListObjectsResponse response = s3Client.listObjects(request);
        return response.contents();
    }
}

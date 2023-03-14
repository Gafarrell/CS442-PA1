package io.github.gafarrell;

import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.ArrayList;
import java.util.List;

public class RekognitionDriver {
    private final RekognitionClient rekognitionClient;
    private final S3Client s3Client;
    private final SqsClient sqsClient;

    public RekognitionDriver(RekognitionClient rekognitionClient, S3Client s3Client, SqsClient sqsClient){
        this.rekognitionClient = rekognitionClient;
        this.s3Client = s3Client;
        this.sqsClient = sqsClient;
    }

    public List<DetectLabelsResponse> DetectLabelsWithinBucket(String bucketName){
        ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();
        ListObjectsResponse objectList = s3Client.listObjects(listObjects);

        List<DetectLabelsResponse> detectedLabels = new ArrayList<>();

        for (software.amazon.awssdk.services.s3.model.S3Object object : objectList.contents()){
            S3Object rekogS3Object = S3Object.builder().bucket(bucketName).name(object.key()).build();
            Image rekogImage = Image.builder().s3Object(rekogS3Object).build();

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                    .image(rekogImage)
                    .maxLabels(10)
                    .build();

            detectedLabels.add(rekognitionClient.detectLabels(detectLabelsRequest));
        }

        return detectedLabels;
    }
}

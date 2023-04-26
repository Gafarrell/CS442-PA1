package io.github.gafarrell.shared;

import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.util.List;

public class RekognitionDriver {
    private final RekognitionClient rekognitionClient;

    public RekognitionDriver(){
        rekognitionClient = DependencyHandler.RekognitionClient();
    }

    public RekognitionDriver(boolean lambda){
        rekognitionClient = DependencyHandler.RekognitionClient(lambda);
    }


    public List<Label> detectLabelsWithinImage(String bucket, String key){
        S3Object rekogS3Object = S3Object.builder().bucket(bucket).name(key).build();
        Image rekogImage = Image.builder().s3Object(rekogS3Object).build();

        DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                .image(rekogImage)
                .maxLabels(10)
                .build();

        return rekognitionClient.detectLabels(detectLabelsRequest).labels();
    }

    public DetectTextResponse detectTextWithinImage(String bucket, String key){
        S3Object rekogS3Obect = S3Object.builder().bucket(bucket).name(key).build();
        Image rekogImage = Image.builder().s3Object(rekogS3Obect).build();

        DetectTextRequest request = DetectTextRequest.builder().image(rekogImage).build();

        return rekognitionClient.detectText(request);
    }
}

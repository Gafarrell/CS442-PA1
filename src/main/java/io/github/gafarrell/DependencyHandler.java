package io.github.gafarrell;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

public class DependencyHandler {
    private static Region region = Region.US_EAST_1;

    public static SqsClient SqsClient(){
        return SqsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }

    public static RekognitionClient RekognitionClient(){
        return RekognitionClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }

    public static S3Client S3Client(){
        return S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create())
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(region)
                .build();
    }


}

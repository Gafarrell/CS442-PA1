package io.github.gafarrell.shared;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

public class DependencyHandler {
    private static Region region = Region.US_EAST_1;

    private static SqsClient sqsClient;
    private static RekognitionClient rekognitionClient;
    private static S3Client s3Client;

    public static SqsClient SqsClient(){

        return sqsClient == null ?
                sqsClient = SqsClient.builder()
                        .credentialsProvider(ProfileCredentialsProvider.create())
                        .region(region)
                        .httpClient(UrlConnectionHttpClient.builder().build())
                        .build()
                :
                sqsClient;
    }

    public static SqsClient SqsClient(boolean lambda){

        return sqsClient == null ?
                sqsClient = SqsClient.builder()
                        .region(region)
                        .httpClient(UrlConnectionHttpClient.builder().build())
                        .build()
                :
                sqsClient;
    }

    public static RekognitionClient RekognitionClient(){
        return rekognitionClient == null ?
                rekognitionClient = RekognitionClient.builder()
                        .region(region)
                        .credentialsProvider(ProfileCredentialsProvider.create())
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .build()
                :
                rekognitionClient;
    }

    public static RekognitionClient RekognitionClient(boolean lambda){
        return rekognitionClient == null ?
                rekognitionClient = RekognitionClient.builder()
                        .region(region)
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .build()
                :
                rekognitionClient;
    }

    public static S3Client S3Client(){
        return s3Client == null ?
                s3Client = S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create())
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .region(region)
                        .build()
                :
                s3Client;
    }

    public static S3Client S3Client(boolean lambda){
        return s3Client == null ?
                s3Client = S3Client.builder()
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .region(region)
                        .build()
                :
                s3Client;
    }


}

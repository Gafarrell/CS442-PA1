package io.github.gafarrell;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class App{
    public static void main(String[] args) {

        SqsClient client = SqsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();

        GetQueueUrlResponse response = client.getQueueUrl(GetQueueUrlRequest.builder().queueName("ImageQueue").build());

        String queueUrl = response.queueUrl();

        SendMessageRequest messageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("Hello world")
                .delaySeconds(5)
                .build();

        client.sendMessage(messageRequest);
    }

}

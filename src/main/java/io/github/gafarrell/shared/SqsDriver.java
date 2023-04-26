package io.github.gafarrell.shared;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

public class SqsDriver {
    private final SqsClient sqsClient;

    public SqsDriver(){
        sqsClient = DependencyHandler.SqsClient();
        try{
            getQueueUrl("ImageQueue");
        } catch (QueueDoesNotExistException e){
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName("ImageQueue").build();
            sqsClient.createQueue(createQueueRequest);
        }
    }

    public SqsDriver(boolean lambda){
        sqsClient = DependencyHandler.SqsClient(lambda);
        try{
            getQueueUrl("LambdaQueue");
        } catch (QueueDoesNotExistException e){
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName("ImageQueue").build();
            sqsClient.createQueue(createQueueRequest);
        }
    }

    public void sendLambdaMessage(String message) {
        try{
            getQueueUrl("LambdaQueue");
        } catch (QueueDoesNotExistException e){
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName("LambdaQueue").build();
            sqsClient.createQueue(createQueueRequest);
        }

        SendMessageRequest messageRequest = SendMessageRequest.builder().messageBody(message).queueUrl(getQueueUrl("LambdaQueue")).build();
        sqsClient.sendMessage(messageRequest);
    }

    public void sendBasicMessage(String message){
        SendMessageRequest messageRequest = SendMessageRequest.builder().messageBody(message).queueUrl(getQueueUrl("ImageQueue")).build();
        sqsClient.sendMessage(messageRequest);
    }

    public ReceiveMessageResponse pollMessages(String queueName){
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .maxNumberOfMessages(10)
                .queueUrl(getQueueUrl(queueName))
                .waitTimeSeconds(5)
                .build();

        return sqsClient.receiveMessage(receiveRequest);
    }

    public ReceiveMessageResponse pollMessagesWait(String queueName, int waitTime){
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .maxNumberOfMessages(10)
                .queueUrl(getQueueUrl(queueName))
                .waitTimeSeconds(waitTime)
                .build();

        return sqsClient.receiveMessage(receiveRequest);
    }

    public String getQueueUrl(String queueName){
        GetQueueUrlRequest getUrlrequest = GetQueueUrlRequest.builder().queueName(queueName).build();
        return sqsClient.getQueueUrl(getUrlrequest).queueUrl();
    }
}

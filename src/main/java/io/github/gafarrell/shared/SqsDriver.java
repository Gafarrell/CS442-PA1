package io.github.gafarrell.shared;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

public class SqsDriver {
    private final SqsClient sqsClient = DependencyHandler.SqsClient();

    public SqsDriver(){
        try{
            getQueueUrl("ImageQueue");
        } catch (QueueDoesNotExistException e){
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName("ImageQueue").build();
            sqsClient.createQueue(createQueueRequest);
        }
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

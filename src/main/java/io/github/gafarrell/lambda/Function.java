package io.github.gafarrell.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.github.gafarrell.shared.RekognitionDriver;
import io.github.gafarrell.shared.SqsDriver;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.ArrayList;
import java.util.List;

public class Function implements RequestHandler<SQSEvent, List<String>> {
    private final RekognitionDriver rekognitionDriver = new RekognitionDriver(true);
    private final SqsDriver sqsDriver = new SqsDriver(true);

    @Override
    public List<String> handleRequest(SQSEvent e, Context context) {
        List<SQSEvent.SQSMessage> messages = e.getRecords();
        List<String> results = new ArrayList<>();

        for (SQSEvent.SQSMessage message : messages) {
            if (message.getBody().equalsIgnoreCase("-1")) {
                sqsDriver.sendLambdaMessage("-1");
                return results;
            }

            List<Label> labels = rekognitionDriver.detectLabelsWithinImage("cs442-unr", message.getBody());
            for (Label l : labels){
                if (l.name().equalsIgnoreCase("Person") && l.confidence() >= 90F){
                    sqsDriver.sendLambdaMessage(message.getBody());
                }
            }
        }

        return results;
    }
}
